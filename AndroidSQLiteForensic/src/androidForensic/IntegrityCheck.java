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
			//获得一个MD5转换器
			MessageDigest messageDigest = null;
			try {
				messageDigest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			fileInputStream =new FileInputStream(filePath);
			digestInputStream=new DigestInputStream(fileInputStream,messageDigest);
			//读取文件，并在读的过程中进行MD5处理
			byte [] buffer=new byte[bufferSize];
			while(digestInputStream.read(buffer)>0);
			//获得MessageDigest
			messageDigest=digestInputStream.getMessageDigest();
			//得到哈希的结果，保存在16个字节的字节数组中
			byte [] resultByteArray= messageDigest.digest();
			//将字节转为字符串
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
		//将结果字符串保存在一个字符数组中
		char [] resultCharArray=new char[byteArray.length*2];
		//遍历字节数组，通过位运算转换成字符放到字符数组总
		int index=0;
		for(byte b :byteArray){
			resultCharArray[index++]=hexDigits[b>>4&0xf];
			resultCharArray[index++]=hexDigits[b&0xf];
		}
		return new String(resultCharArray);
		
	}
}
