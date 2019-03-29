package androidForensic;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;





import java.sql.*;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class HandleEvent {
	static String Databasepath= "D:\\�û�Ŀ¼\\�ҵ��ĵ�";
	//�����½�һ�������У�Ĭ��Ŀ¼��C: users Administrator������ֱ����cd nc���빤��Ŀ¼��Ҫʹ�þ���Ŀ¼
	static String SprojectPath=System.getProperty("user.dir");
	public  static  String getDevice(){
		String Sresult="";
		String Ssum="";
		int i=0;
		try {
			Process ps1 = Runtime.getRuntime().exec("����\\getDevices.bat", null);
			// ��������Process���ṩ�ķ�����������ӻ������ó����DOS���д��ڵı�׼���
			InputStreamReader inputStr = new InputStreamReader(ps1.getInputStream());
			BufferedReader br = new BufferedReader(inputStr);
			while ((Sresult = br.readLine()) != null) {
				i++;
				if(i==7){
					//split("\\s+")�ǰ��տո�ָ��ַ�����\s��ʾƥ���κοհ��ַ�����+��ʾƥ��һ�λ���,Ssum+Sresult.split("\\s+")���ص���һ��������
					Ssum="�豸��IMEI��"+'\t'+'\t'+Ssum+Sresult.split("\\s+")[4]+"\n"+"�û����ݷ�����Ϣ"+'\t';
					}
				if(i>7){
						Ssum+=Sresult;		
				}
			}
			// ps1.waitFor();//�������󣬽��̱������ˣ���û�о�������֮ǰ�����ܹر����н���
		} catch (Exception ec) {
			ec.printStackTrace();
		}
		System.out.println(Ssum);
		return Ssum;
	}
	
	public  static  void makeImageWrite(){
		long lPort=52336;
		//ѡ����洢·����
		String Imagepath = null;
		JFileChooser Jchooser =new JFileChooser();
		Jchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		Component parent=null;
		int returnVal=Jchooser.showOpenDialog(parent);
		if(returnVal==JFileChooser.APPROVE_OPTION){
			Imagepath=Jchooser.getSelectedFile().getAbsolutePath();
		}	
		//д����ű�
		StringBuffer SBwrite=new StringBuffer();
		
		File file=new File(Imagepath+"\\AndroidImage");
		file.mkdirs();
		SBwrite.append("@echo off"+"\r\n");
		
		SBwrite.append("cd "+SprojectPath+"\\����\\adb-1.0.31-windows"+"\r\n");
		SBwrite.append("adb kill-server"+"\r\n");
		SBwrite.append("adb start-server"+"\r\n");
		SBwrite.append("adb devices"+"\r\n");
		SBwrite.append("adb forward tcp:"+lPort+" tcp:"+lPort+"\r\n");
		
		SBwrite.append("start cmd /k "+"\"echo �˴���ʹ��dd����Ӱ�׿�豸����������&&cd "+SprojectPath+"\\����\\adb-1.0.31-windows&&adb shell busybox nc -l -p "+lPort+" -e dd if=/dev/block/platform/msm_sdcc.1/by-name/userdata\""+"\r\n");
		SBwrite.append("start cmd /k "+"\"echo �˴���ʹ��nc���ߴӶ˿ڶ�ȡ����"+"&&cd "+SprojectPath+"\\����\\nc&&nc 127.0.0.1 "+lPort+" > "+Imagepath+"\\AndroidImage\\data.img\""+"\r\n");
		//SBwrite.append("pause");
		try {
			FileWriter FWwrite=new FileWriter("����\\makeImage.bat");
			FWwrite.write(SBwrite.toString());
			System.out.print(SBwrite.toString());
			FWwrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static boolean makeImageExec(){
		try {
			Runtime Running = Runtime.getRuntime();
			Process ps1=Running.exec("cmd /c ����\\makeImage.bat");
			if(ps1.waitFor()!=0){
				return false;	
			}
		} catch (Exception ec) {
			ec.printStackTrace();
		}
		return true;
		
	}
	
	public static void pullDataBAT(){
		
		//ѡ����ȡ�����ݿ�洢·����
				
				JFileChooser Jchooser =new JFileChooser();
				Jchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				Component parent=null;
				int returnVal=Jchooser.showOpenDialog(parent);
				if(returnVal==JFileChooser.APPROVE_OPTION){
					Databasepath=Jchooser.getSelectedFile().getAbsolutePath();
				}
			//����д�ű�
				StringBuffer SBwrite=new StringBuffer();
				File file=new File(Databasepath+"\\���ݿ��ļ�");
				file.mkdirs();
				SBwrite.append("@echo off"+"\r\n");
				
				SBwrite.append("cd "+SprojectPath+"\\����\\adb-1.0.31-windows"+"\r\n");
				SBwrite.append("adb kill-server"+"\r\n");
				SBwrite.append("adb start-server"+"\r\n");
				SBwrite.append("adb devices"+"\r\n");
				SBwrite.append("adb pull /data/data/com.android.providers.telephony/databases/mmssms.db "+Databasepath+"\\���ݿ��ļ�"+"\r\n");
				SBwrite.append("echo �������ݿ���ȡ���"+"\r\n");
				SBwrite.append("adb pull /data/data/com.android.providers.contacts/databases/contacts2.db "+Databasepath+"\\���ݿ��ļ�"+"\r\n");
				SBwrite.append("echo ��ϵ�˺�ͨ����¼���ݿ���ȡ���"+"\r\n");
				SBwrite.append("adb pull /data/data/com.UCMobile/databases/bookmark.db "+Databasepath+"\\���ݿ��ļ�"+"\r\n");
				SBwrite.append("echo �������ǩ���ݿ���ȡ���"+"\r\n");
				SBwrite.append("adb pull /data/data/com.android.providers.media/databases/external.db "+Databasepath+"\\���ݿ��ļ�"+"\r\n");
				SBwrite.append("echo ��ý�����ݿ���ȡ���"+"\r\n");
				//SBwrite.append("pause");
				try {
					FileWriter FWwrite=new FileWriter("����\\pullData.bat");
					FWwrite.write(SBwrite.toString());
					FWwrite.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	public static boolean pullDataEXE(){
		try {
			Process ps1 = Runtime.getRuntime().exec("cmd /c ����\\pullData.bat");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void backupData(){
		// ����ȡ�����ݿ��ļ��н��б���
		File dir1 = new File(Databasepath+"\\���ݿ��ļ�");
		File dir2 = new File(Databasepath+"\\���ݿ��ļ�����");
		if (!dir2.exists()) {
			dir2.mkdir();
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		File[] file = dir1.listFiles();
		for (int i = 0; i < file.length; i++) {
			String path = file[i].getAbsolutePath();
			String filename = path.substring(
					path.lastIndexOf("\\") + 1,
					path.length());
			File AfterFile = new File(
					Databasepath+"\\���ݿ��ļ�����\\"
							+ filename);
			if (!AfterFile.exists()) {
				try {
					AfterFile.createNewFile();// �������Ŀ¼�ﴴ���ļ�����Ҫ�ȴ����ļ�
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			try {
				fis = new FileInputStream(file[i]);
				fos = new FileOutputStream(AfterFile);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			byte[] buffer = new byte[1024];
			int iLength = 0;
			try {
				while ((iLength = fis.read(buffer)) != -1) {
					fos.write(buffer, 0, iLength);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
			fis.close();
			fos.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void integrityCheck(){
		File dir1 = new File(Databasepath+"\\���ݿ��ļ�");
		File dir2 = new File(Databasepath+"\\���ݿ��ļ�MD5����");
		if (!dir2.exists()) {
			dir2.mkdir();
		}
		File[] file = dir1.listFiles();
		for (int i = 0; i < file.length; i++) {
			String path = file[i].getAbsolutePath();
			String filename = path.substring(path.lastIndexOf("\\") + 1,path.length());
			File AfterFile = new File(Databasepath+"\\���ݿ��ļ�MD5����\\"+ filename+"У��ֵ.txt");
			String MD5Result=IntegrityCheck.fileMD5(path);
				try {
					FileWriter FWwrite=new FileWriter(AfterFile);
					FWwrite.write(MD5Result.toString());
					FWwrite.close();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
		}
		
	}
	
	
	
	public static boolean isMD5OK(String path,int flag){
		String encoding ="GBK";
		File file=null;
		String MD5Result2=IntegrityCheck.fileMD5(path);
		System.out.println("��һ��MD5ֵΪ"+MD5Result2);
		String MD5Result=null;
		switch(flag){
		case 1:
			file =new File(Databasepath+"\\���ݿ��ļ�MD5����\\"+"mmssms.dbУ��ֵ.txt"); 
			break;
		case 2:
		case 3:
			file =new File(Databasepath+"\\���ݿ��ļ�MD5����\\"+"contacts2.dbУ��ֵ.txt"); 
			break;
		case 4:
			file =new File(Databasepath+"\\���ݿ��ļ�MD5����\\"+"bookmark.dbУ��ֵ.txt"); 
			break;
		case 5:
			file =new File(Databasepath+"\\���ݿ��ļ�MD5����\\"+"external.dbУ��ֵ.txt"); 
			break;
		}
		//��ȡ��������ݿ�����MD5ɢ��ֵ
		if(file.isFile()&&file.exists()){
			try {
				InputStreamReader reader=new InputStreamReader(new FileInputStream(file),encoding);
				BufferedReader bufferReader=new BufferedReader(reader);
				MD5Result=bufferReader.readLine();
				System.out.println("������MD5ֵΪ"+MD5Result);
				reader.close();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(MD5Result2.equals(MD5Result)){
			return true;
		}
		return false;
		
	}
	public static int saveRecData(ArrayList<String> recData,String dataName){
		int iGarbledCount=0;
		Connection con=null;
		Statement stmt=null;
		String dataString = "";
		String sql = "";
		StringBuffer SBwrite=new StringBuffer();//���ָ������������зǷ��ַ�����д�����ݿ��ʱ��д��TXT
		SBwrite.append(dataName+"����д�����ݿ�����ݣ�\r\n");
		//ArrayList <String> failInsert=new ArrayList<String>();
		try {
			Class.forName("org.sqlite.JDBC");
			con=DriverManager.getConnection("jdbc:sqlite:RecData.db");
			con.setAutoCommit(false);
			System.out.println("���ݿ����ӳɹ�"+dataName);
			stmt=con.createStatement();
			//��������ǰ����ձ����ҵ��Ե�ʱ���õ�
			String sqldel="delete from "+dataName+";";
			stmt.executeUpdate(sqldel);
			for(int i=0;i<recData.size();i++){
				dataString=recData.get(i);
				dataString=dataString.substring(0,dataString.lastIndexOf(","));//ȥ�����һ������
				//��д�����ݿ�֮ǰ�жϻָ�����ַ������Ƿ��������ַ�,(,),#���еĻ���д��TXT��
				if(!dataString.contains("")){
					sql="INSERT INTO  "+dataName+"  "+" VALUES ("+dataString+");";
				}
				else{
					iGarbledCount++;
					SBwrite.append(dataString+"\r\n"); 
				}
				//System.out.println("д�����ݿ⺯���е�sql���"+sql);
				if(!sql.equals("")){
					stmt.executeUpdate(sql);
				}
				sql="";
			}
			con.commit();
			stmt.close();
			con.close();

		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		//����д�����ݿ��д��TXT
		writeTXT(SBwrite.toString());
		SBwrite=null;
		return iGarbledCount;
	}
	private static void writeTXT(String str){
		try {
			//new����ʱ���ڶ�������Ϊtrue��ʾ���ļ�����׷��������
			FileWriter FWwrite=new FileWriter("����д�����ݿ�����.txt",true);
			FWwrite.write(str);
			FWwrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
