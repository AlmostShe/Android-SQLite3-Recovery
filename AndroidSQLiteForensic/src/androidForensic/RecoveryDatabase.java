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
	 * String SRecoverDataPath����ʾ��Ҫ�ָ������ݿ��·��
	 * String SSQL_master����ʾ���ϵͳ�������Ϣ��sql���
	 */
	public RecoveryDatabase(String SRecoverDataPath,String SSQL_master){
		RecoveryDatabase.SRecoverDataPath=SRecoverDataPath;
		RecoveryDatabase.SSQL_master=SSQL_master;
	
	}
	 /*
	  * �ú���ȡ��ĳ���ֶζ�Ӧ�����ݵĳ���
	  * long type��ĳ���ֶεĳ���,������ݿ�ı䳤��������˽����ȶ�Ϊlong����
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
			 //type��ֵΪ8-11��0��ʱ��û��ʹ�ã���˷��صĳ���Ϊ0
			 return 0;
		 }
	}
	 
	 /*
	  * �ú������䳤����תΪ��������
	  * short[] changeInteger���洢�䳤�������ֽ�����
	  * int iLength���ֽ�����Ĵ�С
	  * return��long���͵Ķ�����������Ϊsqlite3�ı䳤��������1-9���ֽڱ�ʾ����ȥ����λ�Ļ���Ҳ����˵sqlite�ı䳤����ȫ��ʹ��ʱ���൱��long����
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
		// �������һ�����λΪ0���ֽ�
		lSum += changeInteger[iLength - 1];
		return lSum;
	}
	 
	/*
	 * �ú����жϵ������ɿ��Ƿ���ȷ
	 * int n����ʾ�жϵ�nֵ��nֵ��4-6֮�䣬��������4-28֮��
	 * return��FreeBlock��Ķ�������ҵ���ȷ�����ɿ鷵�����ɿ�������Ϣ�����򷵻�null
	 */
	 private static boolean isFreeBlock(int n,FreeBlock freeblock,int iStart ) {
		 System.out.println("�������������ɿ���ʼ��ַ��"+(freeblock.iPageStartAddr + freeblock.getFreeBlockAddr()));
		//������洢ÿ�����ݵĳ��������ά����sql_master���ж������ֶθ�������
		freeblock.lDataSize = new long[iTypeNumber];
		RandomAccessFile raFile = null;
		freeblock.iSumTypeSize=0;
		freeblock.iSumDataSize=0;
		//����Java����û���޷���byte���ͣ������������short���ͽ���ÿ���ֽڵ�ֵ
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
			if (((bReadByte >> 7) & 1) == 0) {// �ж϶�����ֽ��Ƿ��Ǳ䳤����
				
				freeblock.iSumTypeSize++;
				// ����i�Ǵ��ֶ�����ĵڶ�����ʼ�жϵģ���һ���ֶ�ֵʼ��Ϊ0�������ˣ�Ϊ�˷�ֹ��������������洢ÿ�����ݴ�С�������1��ʼ			
				freeblock.lDataSize[i - 1] = bReadByte;
				// System.out.println("lDataSize["+(i-1)+"]"+"��ֵΪ"+lDataSize[i-1]);
				freeblock.iSumDataSize += sizeOfData(freeblock.lDataSize[i - 1]);
			} else {
				int k = 0;
				while (((bReadByte >> 7) & 1) != 0) {
					// ����readByte���������з���byte���Ͷ�����ֽڣ����Ҫת��Ϊ�޷�������
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
					if(k>=9){//����䳤�������ȴ��� 9���Ҵ���
						return false;
					}
				}
				// Ҫע���ڵ��ñ䳤����ת��������������ʱ�����ֵӦ��Ϊ����Ĵ�С����������kҪ��1
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
		 // System.out.println("���ƺ��������ɿ����ʼ��ַ��"+(freeblock.getFreeBlockAddr()+freeblock.iPageStartAddr));
		 int n=4;//�ӵ��ĸ���ʼ��
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
			 System.out.println("���ƺ����е����ɿ���ʼ��ַ��"+(freeblock.iPageStartAddr + freeblock.getFreeBlockAddr()));
			 raFile.readFully(block);//���������ɿ����
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 for(;n<block.length;n++){
			 tempbyte=block[n];			 
			 if(tempbyte==0){//��������ֽ�Ϊ0ʱ��Ĭ������ƥ��
				 tempBlock.lDataSize[flag] = tempbyte;
				 tempBlock.iSumTypeSize++;
				 flag++;
			 }
			 else if (((tempbyte >> 7) & 1) == 0) {// �ж϶�����ֽ��Ƿ��Ǳ䳤����
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
					 if(k>=9){//����䳤������ֵ����9���ֽڣ�����������ɿ���Ҵ��ң���������߻ָ��������ڿ�������
						 return;
					 }
					 n++;
					 if(n>=block.length){
						 return;
					 }
					 tempbyte = block[n];
					 if(tempbyte!=0){//�����һ���ֽڲ�����0�����ڱ䳤����������0�Ļ��Ͳ�����
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
				 // System.out.println("���ݵ���ʼ��ַ��"+ tempBlock.getDataStartAddr());
				 if(tempBlock.getFreeBlockSize()>block.length){//����������һ�����ɿ��С���ܵ����ɿ��С���󣬱�������
					 return;
				 }
				 if((n+tempBlock.iSumTypeSize+tempBlock.iSumDataSize)>block.length){//�ʹ������ɿ�ĳ��ȱ�ʾ���ڱ����ָ������
					 tempBlock.iSumDataSize=block.length-n-1;
				 }
				 System.out.println("���ƺ����е����ݳ�����"+  tempBlock.iSumDataSize);
				 insertFreeBlock(tempBlock);
				 int nextFreeBlockAddr=tempBlock.getDataStartAddr()-tempBlock.iPageStartAddr+tempBlock.iSumDataSize;
				 //	 System.out.println("��һ�����ɿ���ʼ��ַ��"+ nextFreeBlockAddr);
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
		 * �ú�����һ���ҵ������ɿ���֯�ɹ��ַ��� 
		 * String SDataPath��Ҫ�ָ����ݿ��·��
		 * FreeBlockfreeblock��Ҫ�ָ������ɿ������ 
		 * int iCharacterCode�����ݿ���뷽ʽ�������ݿ�ͷ�ж�����
		 * return�����ػָ��õ��ַ���
		 */
		 private static void insertFreeBlock( FreeBlock freeblock) {
				SRecResult=" ";
				byte[] payloadData = new byte[freeblock.iSumDataSize];
				try {
					RandomAccessFile raFile = new RandomAccessFile(SRecoverDataPath, "r");
					raFile.skipBytes(freeblock.iDataStartAddr);
					System.out.println("���ƺ����е�������ʼ��ַ��"+freeblock.iDataStartAddr);
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
				System.out.println("�ҵ���������"+SRecResult);
				SRecResultList.add(SRecResult);	
				RecFreeblockCount++;
			}

	/*
	 * �ú����ӱ�ĸ�ҳ��ʼ�ݹ�����ҵ����е�Ҷ��ҳ,��������Ҷ��ҳ����ʼλ�� PageHeader
	 * page����ҳͷ��Ķ����룬�Ա�ÿ�εݹ����ж�ҳ������ ���ҵ�������Ҷ��ҳ����ʼλ�ñ�����ȫ�ֱ���iAllLeafPage��
	 * Ҷ��ҳ�ĸ�������ȫ������Ĵ�СiTraverse
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
	 //���������ڲ�ҳ
	 private void traverseInterPage(PageHeader page) {
		  if (page.cPageType == 5) {
			  
				iAllInterPage[iInterCount++] = PageHeader.iPageStartAddr;
				System.out.println("�ڲ�ҳ��ʼ��ַ"+PageHeader.iPageStartAddr);
				int iChildNumber = page.sPayloadNumber + 1;
				for (int i = 0; i < iChildNumber; i++) {
					traverseInterPage(new PageHeader(page.SDataPath, page.iLeafPage[i]));
				}
			} else {
				return;
			}
		}
	 
	 
	 
	/*
	 * �ú�����һ�����ֽ�һ�µ��ֽ�����ת����������
	 * byte [] bbyte���ֽ����飬int n���鳤��
	 * return��int ����ת����ɵ�����
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
	 * �ú�����һ���ֽ�����תΪ������
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
	 * �ú�����Ϊ�ָ���������Ľӿڣ�ִ�лָ�����
	 *  return��String�����ػָ��������ַ���
	 */
	@SuppressWarnings("resource")
	public ArrayList<String> recover() {
		//�������ɿ���ҳɹ���ʧ�ܶ���Ҫ��Ҷ��ҳ���������㣬�Ա���һ�����ݿ������
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
		iAllLeafPage = new int[dbHeader.iDatabasePageSize];	// ����Ҷ��ҳ�Ĵ�С�Ǹ����ݿ�ռ�ݵ�ҳ��
		iAllInterPage=new int[dbHeader.iDatabasePageSize];
		traverseLeafPage(pageHeader);
		//PageHeader pageInterHeader = new PageHeader(SRecoverDataPath,sql_master.iRootpageNumber);
		//traverseInterPage(pageInterHeader);

		
		ArrayList<FreeBlock> freeblock=new ArrayList<FreeBlock>();
		//���ڲ�ҳ�����ɿ����
		//freeblock=getAllBlock(iAllInterPage,iInterCount);
		// �����е�Ҷ��ҳ���ҵ����ɿ���ʼ��ַ���жϸ����ɿ��Ƿ�Ѱ����ȷ,����ֻ����ͨ���ݹ麯��֮��õ�����Ҷ��ҳ�ĸ�������Ϊ����Ǹ�ҳ����Ҷ��ҳ�Ļ�����ô��Ԫ�������Ǻ���ҳ
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
	//		System.out.println("�õ������ɿ�Ĵ�С��" + freeblock.get(n).getFreeBlockSize());
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
			//�����ȡ������¼��Ԫ��ʼ��ַ���δ����λ��ƫ��
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
				System.out.println("�ڲ�ҳ�����ɿ���ʼ��ʼ��"+(pageStartAddr[i]+addrTemp));
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
