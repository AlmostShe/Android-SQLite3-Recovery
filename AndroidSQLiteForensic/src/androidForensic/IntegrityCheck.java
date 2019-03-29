package androidForensic;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IntegrityCheck {
	static int bufferSize=256*1024;
	
	public static String fileMD5(String filePath){
		FileInputStream fileInputStream=null;
		DigestInputStream digestInputStream =null;
		
		try {
			//���һ��MD5ת����
			MessageDigest messageDigest = null;
			try {
				messageDigest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			fileInputStream =new FileInputStream(filePath);
			digestInputStream=new DigestInputStream(fileInputStream,messageDigest);
			//��ȡ�ļ������ڶ��Ĺ����н���MD5����
			byte [] buffer=new byte[bufferSize];
			while(digestInputStream.read(buffer)>0);
			//���MessageDigest
			messageDigest=digestInputStream.getMessageDigest();
			//�õ���ϣ�Ľ����������16���ֽڵ��ֽ�������
			byte [] resultByteArray= messageDigest.digest();
			//���ֽ�תΪ�ַ���
			return byteArraytoHex(resultByteArray);
		} catch (IOException  e) {
			return null;
		}
		finally{
			try {
				digestInputStream.close();
				fileInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private static String byteArraytoHex(byte [] byteArray){
		char [] hexDigits={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		//������ַ���������һ���ַ�������
		char [] resultCharArray=new char[byteArray.length*2];
		//�����ֽ����飬ͨ��λ����ת�����ַ��ŵ��ַ�������
		int index=0;
		for(byte b :byteArray){
			resultCharArray[index++]=hexDigits[b>>4&0xf];
			resultCharArray[index++]=hexDigits[b&0xf];
		}
		return new String(resultCharArray);
		
	}
}
