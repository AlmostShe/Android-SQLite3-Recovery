package androidForensic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FreeBlock {
	static long lFreeBlockSizeAll=0;
	int iSumTypeSize=0;
	int iSumDataSize=0;
	long [] lDataSize;
	int iDataStartAddr;
	
	//boolean bRightFreeBlock=false;//该变量标注某个自由块是否是正确的自由块
	//自由块的起始地址，包括第一个自由块和叶子页所有的自由块
	 int iFreeBlockAddr = 0;
	
	 String SRecoverDataPath;
	 int iPageStartAddr;

	public FreeBlock(  String SRecoverDataPath,int iPageStartAddr,int iFreeBlockAddr ){
		this.SRecoverDataPath=SRecoverDataPath;
		this.iPageStartAddr=iPageStartAddr;	
		this.iFreeBlockAddr=iFreeBlockAddr;
	}
	
	
 
 public int getFreeBlockAddr(){
	 return iFreeBlockAddr;
 }
 public void setFreeBlockAddr(int iFreeBlockAddr){
	  this.iFreeBlockAddr=iFreeBlockAddr;
 }
 public void setDataStartAddr(int temp){
	  this.iDataStartAddr=temp;
}
 public int getDataStartAddr(){
	 return iDataStartAddr;
 }
	 
	 /*
		 * 该函数取得自由块的总大小，将该值赋给全局变量lFreeBlockSizeAll
		 */
		 public  long getFreeBlockSize(){	
			RandomAccessFile raFile;
			 try {
				raFile= new RandomAccessFile(SRecoverDataPath, "r");		
				raFile.skipBytes(iPageStartAddr+iFreeBlockAddr+2);			
				lFreeBlockSizeAll=raFile.readShort();
				raFile.close();
			 } catch (IOException e) {			
				e.printStackTrace();
			}	
			 return lFreeBlockSizeAll;
		 }
		 
		 @SuppressWarnings("resource")
		public int getNextFreeBlockAddr(){
			 int nextAddr=0;
		
				RandomAccessFile raFile = null;
				try {
					raFile = new RandomAccessFile(SRecoverDataPath,"r");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					raFile.skipBytes(iPageStartAddr+iFreeBlockAddr);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					nextAddr = raFile.readShort();
					} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
			 return nextAddr;
		 }
		 
		
		 
		 
	 
}
