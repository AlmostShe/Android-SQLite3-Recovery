package androidForensic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import androidForensic.FreeBlock;
import androidForensic.DBHeader;
import androidForensic.PageHeader;
import androidForensic.Sql_master;

public class RecoveryDatabase {
		static String SRecoverDataPath;
		static String SSQL_master;
		static int[] iAllLeafPage;
		static int [] iAllInterPage;
		static int  FreeBlockCount;
		static int  RecFreeblockCount;
		static int iTypeNumber;
		static int iTraverse = 0;
		static int iInterCount=0;
		static int iCharacterCode=0;
		static String SRecResult="";
		static ArrayList<String> SRecResultList;
	/*
	 * String SRecoverDataPath：表示需要恢复的数据库的路径
	 * String SSQL_master：表示获得系统表相关信息的sql语句
	 */
	public RecoveryDatabase(String SRecoverDataPath,String SSQL_master){
		RecoveryDatabase.SRecoverDataPath=SRecoverDataPath;
		RecoveryDatabase.SSQL_master=SSQL_master;
	
	}
	 /*
	  * 该函数取得某个字段对应的数据的长度
	  * long type：某个字段的长度,配合数据库的变长整数，因此将长度定为long类型
	  */
	 private  static long sizeOfData(long type){
		 if(type<0){
			 return -1;
		 }
		 else if(type<=4){
			 return type;
		 }
		 else if(type==5){
			 return(type+1);
		 }
		 else if(type==6||type==7){
			 return 8;
		 }
		 else if(type>12&&type%2==0){
			 return((type-12)/2);
		 }
		 else if(type>13&&type%2==1){
			 return ((type-13)/2);
		 }
		 else {
			 //type的值为8-11或0的时候没有使用，因此返回的长度为0
			 return 0;
		 }
	}
	 
	 /*
	  * 该函数将变长整数转为定长整数
	  * short[] changeInteger：存储变长整数的字节数组
	  * int iLength：字节数组的大小
	  * return：long类型的定长整数，因为sqlite3的变长整数是用1-9个字节表示，除去符号位的话，也就是说sqlite的变长整数全部使用时就相当于long类型
	  */
	 private static long changeVariableToImutable(short[] changeInteger,int iLength) {			 
		long lSum = 0;
		for (int i = 0; i < iLength - 1; i++) {
			if (changeInteger[i] > 128) {
				lSum += (changeInteger[i] - 128)
						* Math.pow(128, (iLength - i - 1));
			} else {
				lSum += changeInteger[i] * Math.pow(128, (iLength - i - 1));
			}
		}
		// 加上最后一个最高位为0的字节
		lSum += changeInteger[iLength - 1];
		return lSum;
	}
	 
	/*
	 * 该函数判断单个自由块是否正确
	 * int n：表示判断的n值，n值在4-6之间，最多可以是4-28之间
	 * return：FreeBlock类的对象，如果找到正确的自由块返回自由块的相关信息，否则返回null
	 */
	 private static boolean isFreeBlock(int n,FreeBlock freeblock,int iStart ) {
		 System.out.println("单个完整的自由块起始地址是"+(freeblock.iPageStartAddr + freeblock.getFreeBlockAddr()));
		//该数组存储每个数据的长度数组的维度由sql_master表中读出的字段个数决定
		freeblock.lDataSize = new long[iTypeNumber];
		RandomAccessFile raFile = null;
		freeblock.iSumTypeSize=0;
		freeblock.iSumDataSize=0;
		//由于Java里面没有无符号byte类型，因此这里设置short类型接收每个字节的值
		short[] sChangeInteger = new short[9];
		try {
			raFile = new RandomAccessFile(SRecoverDataPath, "r");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		byte bReadByte = 0;
		int lStartAddr = freeblock.iPageStartAddr + freeblock.getFreeBlockAddr() + n;
		try {
			raFile.skipBytes(lStartAddr);
		} catch (IOException e) {
			e.printStackTrace();
		}	

		for (int i = iStart; i <= iTypeNumber; i++) {
			try {
				bReadByte = raFile.readByte();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (((bReadByte >> 7) & 1) == 0) {// 判断读入的字节是否是变长整数
				
				freeblock.iSumTypeSize++;
				// 由于i是从字段数组的第二个开始判断的，第一个字段值始终为0，跳过了，为了防止数组的溢出，这里存储每个数据大小的数组从1开始			
				freeblock.lDataSize[i - 1] = bReadByte;
				// System.out.println("lDataSize["+(i-1)+"]"+"的值为"+lDataSize[i-1]);
				freeblock.iSumDataSize += sizeOfData(freeblock.lDataSize[i - 1]);
			} else {
				int k = 0;
				while (((bReadByte >> 7) & 1) != 0) {
					// 由于readByte（）是以有符号byte类型读入的字节，因此要转化为无符号类型
					sChangeInteger[k] = (short) (bReadByte & 0xff);
					freeblock.iSumTypeSize++;
					k++;
					try {
						bReadByte = raFile.readByte();
						sChangeInteger[k] = (short) (bReadByte & 0xff);
						freeblock.iSumTypeSize++;
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(k>=9){//如果变长整数长度大于 9，找错了
						return false;
					}
				}
				// 要注意在调用变长整数转定长整数函数的时候传入的值应该为数组的大小，因此这里的k要加1
				freeblock.lDataSize[i - 1] = changeVariableToImutable(
						sChangeInteger, k + 1);
				freeblock.iSumDataSize += sizeOfData(freeblock.lDataSize[i - 1]);
			}
		}
		if(iStart==2){
			freeblock.lDataSize[0]=0;
		}
		try {
			raFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if ((n + freeblock.iSumDataSize + freeblock.iSumTypeSize) == freeblock.getFreeBlockSize()) {
			freeblock.iDataStartAddr = (int) (freeblock.iPageStartAddr+ freeblock.getFreeBlockAddr() + (n) + freeblock.iSumTypeSize);
			
			
			return true;
		} else
			return false;
	}
	 
	 private void similarIsFreeBlock(FreeBlock freeblock ,int [] previewType ) {
		 // System.out.println("相似函数中自由块的起始地址是"+(freeblock.getFreeBlockAddr()+freeblock.iPageStartAddr));
		 int n=4;//从第四个开始读
		 int flag=0;
		 byte[] block = new byte[(int) freeblock.getFreeBlockSize()];
		 FreeBlock tempBlock=new FreeBlock(SRecoverDataPath,freeblock.iPageStartAddr,freeblock.getFreeBlockAddr());
		 tempBlock.lDataSize = new long[iTypeNumber];
		 RandomAccessFile raFile;
		 byte tempbyte;
		 int nStart=-1;
		 try {
			 raFile = new RandomAccessFile(SRecoverDataPath, "r");
			 raFile.skipBytes(freeblock.iPageStartAddr + freeblock.getFreeBlockAddr());
			 System.out.println("相似函数中的自由块起始地址是"+(freeblock.iPageStartAddr + freeblock.getFreeBlockAddr()));
			 raFile.readFully(block);//将整个自由块读入
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 for(;n<block.length;n++){
			 tempbyte=block[n];			 
			 if(tempbyte==0){//当读入的字节为0时，默认类型匹配
				 tempBlock.lDataSize[flag] = tempbyte;
				 tempBlock.iSumTypeSize++;
				 flag++;
			 }
			 else if (((tempbyte >> 7) & 1) == 0) {// 判断读入的字节是否是变长整数
				 if(getTypeKind(tempbyte)!=previewType[flag]){
					 flag=0;
					 tempBlock.iSumTypeSize=tempBlock.iSumDataSize=0;
					 continue;
				 }
				 tempBlock.lDataSize[flag] = tempbyte;
				 tempBlock.iSumDataSize += sizeOfData(tempBlock.lDataSize[flag]);
				 tempBlock.iSumTypeSize++;
				 flag++;
			 } 
			 else {
				 short [] sChangeInteger=new short[9];
				 int k = 0;
				 while ((((tempbyte >> 7) & 1) != 0)){
					 sChangeInteger[k] = (short) (tempbyte & 0xff);
					 tempBlock.iSumTypeSize++;
					 k++;
					 if(k>=9){//如果变长整数的值大于9个字节，表名这个自由块查找错乱，放弃，提高恢复数量可在考虑这里
						 return;
					 }
					 n++;
					 if(n>=block.length){
						 return;
					 }
					 tempbyte = block[n];
					 if(tempbyte!=0){//如果下一个字节不等于0就属于变长整数，等于0的话就不是了
						 sChangeInteger[k] = (short) (tempbyte & 0xff);
						 tempBlock.iSumTypeSize++;
					 }
					 else{
						 n--;
					 }
				 }
				 int tempchange=(int)changeVariableToImutable(sChangeInteger, k + 1);
				 if(getTypeKind(tempchange)!=previewType[flag]){
					 flag=0;	
					 tempBlock.iSumTypeSize=tempBlock.iSumDataSize=0;
					 continue;
				 }
				 tempBlock.lDataSize[flag] =tempchange;	
				 tempBlock.iSumDataSize += sizeOfData(tempBlock.lDataSize[flag]);
				 flag++;
			 }
			 if(flag==iTypeNumber){
				 tempBlock.setDataStartAddr(tempBlock.iPageStartAddr + tempBlock.getFreeBlockAddr()+n-nStart);
				 // System.out.println("数据的起始地址是"+ tempBlock.getDataStartAddr());
				 if(tempBlock.getFreeBlockSize()>block.length){//如果读入的下一个自由块大小比总的自由块大小还大，表名错误
					 return;
				 }
				 if((n+tempBlock.iSumTypeSize+tempBlock.iSumDataSize)>block.length){//和大于自由块的长度表示存在被部分覆盖情况
					 tempBlock.iSumDataSize=block.length-n-1;
				 }
				 System.out.println("相似函数中的数据长度是"+  tempBlock.iSumDataSize);
				 insertFreeBlock(tempBlock);
				 int nextFreeBlockAddr=tempBlock.getDataStartAddr()-tempBlock.iPageStartAddr+tempBlock.iSumDataSize;
				 //	 System.out.println("下一个自由块起始地址是"+ nextFreeBlockAddr);
				 n+=tempBlock.iSumDataSize;
				 nStart=n--;
				 n+=4;
				 tempBlock=new FreeBlock(SRecoverDataPath,freeblock.iPageStartAddr,nextFreeBlockAddr);
				 tempBlock.lDataSize = new long[iTypeNumber];
				 flag=0;
			 }
		 }
	 }

	 private static int getTypeKind(int type){
		if(type>0&&(type<=7||type==8||type==9)){
			return 1;
		}
		else if(type>=13&&(type%2==1)){
			return 3;
		}
		else if(type==0){
			return 0;
		}
		else if(type==7){
			return 2;
		}
		else if(type>=12&&(type%2==0)){
			return 4;
		}
		else return -1;
	 }
	
	 
	 /*
		 * 该函数将一个找到的自由块组织成功字符串 
		 * String SDataPath：要恢复数据库的路径
		 * FreeBlockfreeblock：要恢复的自由块类对象 
		 * int iCharacterCode：数据库编码方式，从数据库头中读出来
		 * return：返回恢复好的字符串
		 */
		 private static void insertFreeBlock( FreeBlock freeblock) {
				SRecResult=" ";
				byte[] payloadData = new byte[freeblock.iSumDataSize];
				try {
					RandomAccessFile raFile = new RandomAccessFile(SRecoverDataPath, "r");
					raFile.skipBytes(freeblock.iDataStartAddr);
					System.out.println("相似函数中的数据起始地址是"+freeblock.iDataStartAddr);
					raFile.readFully(payloadData);
					raFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				int iPayload = 0;
				int i = 0;
				for (; i <iTypeNumber; i++) {
					 if (freeblock.lDataSize[i] < 12) {
						
						switch ((int) freeblock.lDataSize[i]) {
						case 0:
							
							 SRecResult+= null + ",";
							 break;
						case 1:
							if(iPayload<freeblock.iSumDataSize){
								SRecResult+= "'"+Byte.toString(payloadData[iPayload++])+"'" + ",";
							}
							else{
								SRecResult+=null+ ",";
							}
							break;
						case 2:
							if((iPayload+1)<freeblock.iSumDataSize){
								byte[] b = { payloadData[iPayload++],payloadData[iPayload++] };
								int iTemp = byteToInt(b, 2);
								SRecResult +="'"+ Integer.toString(iTemp)+"'" + ",";
							}		
							else{
								SRecResult+=null+ ",";
							}
							break;
						case 3:
							if((iPayload+1)<freeblock.iSumDataSize&&(iPayload+2)<freeblock.iSumDataSize){
								byte[] b2 = { payloadData[iPayload++],
										payloadData[iPayload++], payloadData[iPayload++] };
								SRecResult += "'"+Integer.toString(byteToInt(b2, 3)) +"'"+ ",";	
							}
							else{
								SRecResult+=null+ ",";
							}
							break;
						case 4:
							byte[] b4 = new byte[4];
							int tempi=0;
							int k = 0;
							for (; k < 4&&(tempi=iPayload++)<freeblock.iSumDataSize; k++) {
									b4[k] = payloadData[tempi];	
							}
							if(k>=4){
								SRecResult += "'"+Integer.toString(byteToInt(b4, 4))+"'" + ",";	
							}
							else{
								SRecResult +=null+ ",";
							}
							
							break;
						case 5:
							byte[] b5 = new byte[6];
							int temp2=0;
							for (k = 0; k < 6&&(temp2=iPayload++)<freeblock.iSumDataSize; k++) {		
									b5[k] = payloadData[temp2];
							}
							if(k>=6){
							SRecResult +="'"+ Long.toString(byteToLong(b5, 6)) +"'"+ ",";		
							}
							else{
								SRecResult +=null+ ",";
							}
							break;
						case 6:
						case 7:
							byte[] b7 = new byte[8];
							int temp3=0;
							for (k = 0; k < 8&&(temp3=iPayload++)<freeblock.iSumDataSize; k++) {
								b7[k] = payloadData[temp3];								
							}
							if(k>=8){
							SRecResult += "'"+Long.toString(byteToLong(b7, 8)) +"'"+ ",";					
							}
							else{
								SRecResult +=null+ ",";
							}
							break;
						case 8:
						case 9:
							 SRecResult+= null + ",";
							 break;
						}
					} else if (freeblock.lDataSize[i] >= 13&& freeblock.lDataSize[i] % 2 == 1) {
						
						if (iCharacterCode == 1) {
							byte[] b8 = new byte[(int) sizeOfData(freeblock.lDataSize[i])];
							int tempi=0;
							for (int k = 0; k < (int) sizeOfData(freeblock.lDataSize[i]); k++) {
								tempi=iPayload++;
								if(tempi<freeblock.iSumDataSize){
									b8[k] = payloadData[tempi];
								}
								else{
									break;
								}
								
							}
							String ss = null;
							try {
								ss = new String(b8, 0,(int)sizeOfData(freeblock.lDataSize[i]),"UTF-8");
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							if(freeblock.lDataSize[i] > 13){
								SRecResult += "'"+ss +"'"+",";
							}
							else{
								SRecResult += null+",";
							}
						}
					}
					else {
						continue;
					}	
					 
				}
				System.out.println("找到的数据是"+SRecResult);
				SRecResultList.add(SRecResult);	
				RecFreeblockCount++;
			}

	/*
	 * 该函数从表的根页开始递归遍历找到所有的叶子页,保存所有叶子页的起始位置 PageHeader
	 * page：将页头类的对象传入，以便每次递归能判断页的类型 将找到的所有叶子页的起始位置保存在全局变量iAllLeafPage中
	 * 叶子页的个数就是全局数组的大小iTraverse
	 */
	 private void traverseLeafPage(PageHeader page) {
		if (page.cPageType == 13) {
			iAllLeafPage[iTraverse++] = PageHeader.iPageStartAddr;	
		} else if (page.cPageType == 5) {
			int iChildNumber = page.sPayloadNumber + 1;
			for (int i = 0; i < iChildNumber; i++) {
				traverseLeafPage(new PageHeader(page.SDataPath, page.iLeafPage[i]));
			}
		} else {
			return;
		}
	}
	 //遍历所有内部页
	 private void traverseInterPage(PageHeader page) {
		  if (page.cPageType == 5) {
			  
				iAllInterPage[iInterCount++] = PageHeader.iPageStartAddr;
				System.out.println("内部页起始地址"+PageHeader.iPageStartAddr);
				int iChildNumber = page.sPayloadNumber + 1;
				for (int i = 0; i < iChildNumber; i++) {
					traverseInterPage(new PageHeader(page.SDataPath, page.iLeafPage[i]));
				}
			} else {
				return;
			}
		}
	 
	 
	 
	/*
	 * 该函数将一个四字节一下的字节数组转成整数类型
	 * byte [] bbyte：字节数组，int n数组长度
	 * return：int 返回转换完成的整数
	 */
	 private static int byteToInt(byte[] bbyte, int n) {
		int[] iB = new int[n];
		for (int i = 0; i < n; i++) {
			iB[i] = bbyte[i] & 0xff;
		}
		int itemp = iB[n - 1];
		for (int i = 0; i < n - 1; i++) {
			itemp |= (iB[i] << (8 * (n - i - 1)));
		}
		return itemp;
	}
	 
	/*
	 * 该函数将一个字节数组转为长整型
	 */
	 private static long byteToLong(byte[] bbyte, int n) {
		long[] lB = new long[n];
		for (int i = 0; i < n; i++) {
			lB[i] = bbyte[i] & 0xff;
		}
		long ltemp = lB[n - 1];
		for (int i = 0; i < n - 1; i++) {
			ltemp |= (lB[i] << (8 * (n - i - 1)));
		}
		return ltemp;
	}
	 
	
	 
	/*
	 * 该函数作为恢复函数对外的接口，执行恢复动作
	 *  return：String，返回恢复出来的字符串
	 */
	@SuppressWarnings("resource")
	public ArrayList<String> recover() {
		//无论自由块查找成功或失败都需要将叶子页计数器置零，以便下一个数据库的搜索
		iTraverse=0;
		iInterCount=0;
		FreeBlockCount=0;
		RecFreeblockCount=0;
		SRecResult="";
		SRecResultList=new ArrayList<String>();
		DBHeader dbHeader = new DBHeader(SRecoverDataPath);
		Sql_master sql_master = new Sql_master(SRecoverDataPath, SSQL_master);
		PageHeader pageHeader = new PageHeader(SRecoverDataPath,sql_master.iRootpageNumber);
		iTypeNumber = sql_master.getTypeNumber();
		int [] previewType=sql_master.getType();


		iCharacterCode = dbHeader.iCharacterCode;
		iAllLeafPage = new int[dbHeader.iDatabasePageSize];	// 设置叶子页的大小是该数据库占据的页数
		iAllInterPage=new int[dbHeader.iDatabasePageSize];
		traverseLeafPage(pageHeader);
		//PageHeader pageInterHeader = new PageHeader(SRecoverDataPath,sql_master.iRootpageNumber);
		//traverseInterPage(pageInterHeader);

		
		ArrayList<FreeBlock> freeblock=new ArrayList<FreeBlock>();
		//将内部页的自由块加上
		//freeblock=getAllBlock(iAllInterPage,iInterCount);
		// 对所有的叶子页，找到自由块起始地址，判断该自由块是否寻找正确,这里只能是通过递归函数之后得到所有叶子页的个数，因为如果是根页就是叶子页的话，那么单元数不再是孩子页
		for (int i = 0; i < iTraverse; i++) {
			int iFirstAddr=0;
			int addr=0;
			try {
				RandomAccessFile raFile=new RandomAccessFile(SRecoverDataPath,"r");
				raFile.skipBytes(iAllLeafPage[i]+1);
				iFirstAddr=raFile.readShort();


				} catch (Exception e) {
				e.printStackTrace();
			}
			if(iFirstAddr!=0){
				FreeBlock temp=new FreeBlock(SRecoverDataPath,iAllLeafPage[i],iFirstAddr);
				freeblock.add(temp);
				while((addr=temp.getNextFreeBlockAddr())!=0){
					 temp=new FreeBlock(SRecoverDataPath,iAllLeafPage[i],addr);
					 freeblock.add(temp);
				}
			}	
			}
		FreeBlockCount=freeblock.size();
		
		for(int n=0;n<freeblock.size();n++){
			int iTryn = 4;				 
	//		System.out.println("得到的自由块的大小是" + freeblock.get(n).getFreeBlockSize());
			boolean bresult=isFreeBlock(iTryn,freeblock.get(n),2);
			 
			while (iTryn <=6 && bresult==false) {
				bresult = isFreeBlock(iTryn++,freeblock.get(n),1);
			}
			if (iTryn <=6) {
				insertFreeBlock(freeblock.get(n));
		}
			else{
				similarIsFreeBlock(freeblock.get(n),previewType);
			}
	}
		if(!SRecResult.equals("")){
			return SRecResultList;
		}	
		return null;
	}
	
	
	/*
	@SuppressWarnings("null")
	private ArrayList<FreeBlock> getAllBlock(int [] pageStartAddr ,int pageCount){
		ArrayList<FreeBlock> myfreeBlock = new ArrayList<FreeBlock>();
		int index=0;
		byte pageType;
		 RandomAccessFile raFile;
		for(int i=0;i<pageCount;i++){
		 try {
			raFile= new RandomAccessFile(SRecoverDataPath, "r");		
			raFile.skipBytes(pageStartAddr[i]);	
			pageType=raFile.readByte();
			raFile.skipBytes(2);
			//这里读取正常记录单元起始地址后的未覆盖位置偏移
			if(pageType==13){	
				raFile.skipBytes(3+raFile.readShort()*2);
			}
			else if(pageType==5){
				raFile.skipBytes(7+raFile.readShort()*2);
			}
			int addrTemp;
			while((addrTemp=raFile.readShort())<4096&&addrTemp>0){
				FreeBlock tempFreeBlock=new FreeBlock(SRecoverDataPath,pageStartAddr[i],addrTemp);
				myfreeBlock.add(tempFreeBlock);
				System.out.println("内部页中自由块起始起始是"+(pageStartAddr[i]+addrTemp));
			}
			raFile.close();
		 } catch (IOException e) {			
			e.printStackTrace();
		}
		}
		return myfreeBlock;
	}
	*/

}
