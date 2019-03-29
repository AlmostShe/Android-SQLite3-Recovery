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
	
	//boolean bRightFreeBlock=false;//�ñ�����עĳ�����ɿ��Ƿ�����ȷ�����ɿ�
	//���ɿ����ʼ��ַ��������һ�����ɿ��Ҷ��ҳ���е����ɿ�
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
		 * �ú���ȡ�����ɿ���ܴ�С������ֵ����ȫ�ֱ���lFreeBlockSizeAll
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
