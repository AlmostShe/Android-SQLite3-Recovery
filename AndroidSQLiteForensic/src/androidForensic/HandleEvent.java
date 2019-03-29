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
	static String Databasepath= "D:\\用户目录\\我的文档";
	//这里新建一个命令行，默认目录是C: users Administrator，不能直接用cd nc进入工具目录，要使用绝对目录
	static String SprojectPath=System.getProperty("user.dir");
	public  static  String getDevice(){
		String Sresult="";
		String Ssum="";
		int i=0;
		try {
			Process ps1 = Runtime.getRuntime().exec("工具\\getDevices.bat", null);
			// 以下是用Process类提供的方法让虚拟机接货被调用程序的DOS运行窗口的标准输出
			InputStreamReader inputStr = new InputStreamReader(ps1.getInputStream());
			BufferedReader br = new BufferedReader(inputStr);
			while ((Sresult = br.readLine()) != null) {
				i++;
				if(i==7){
					//split("\\s+")是按照空格分隔字符串，\s表示匹配任何空白字符串，+表示匹配一次或多次,Ssum+Sresult.split("\\s+")返回的是一个数组名
					Ssum="设备的IMEI码"+'\t'+'\t'+Ssum+Sresult.split("\\s+")[4]+"\n"+"用户数据分区信息"+'\t';
					}
				if(i>7){
						Ssum+=Sresult;		
				}
			}
			// ps1.waitFor();//加上这句后，进程被阻塞了，在没有镜像制作之前，不能关闭运行界面
		} catch (Exception ec) {
			ec.printStackTrace();
		}
		System.out.println(Ssum);
		return Ssum;
	}
	
	public  static  void makeImageWrite(){
		long lPort=52336;
		//选择镜像存储路径，
		String Imagepath = null;
		JFileChooser Jchooser =new JFileChooser();
		Jchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		Component parent=null;
		int returnVal=Jchooser.showOpenDialog(parent);
		if(returnVal==JFileChooser.APPROVE_OPTION){
			Imagepath=Jchooser.getSelectedFile().getAbsolutePath();
		}	
		//写镜像脚本
		StringBuffer SBwrite=new StringBuffer();
		
		File file=new File(Imagepath+"\\AndroidImage");
		file.mkdirs();
		SBwrite.append("@echo off"+"\r\n");
		
		SBwrite.append("cd "+SprojectPath+"\\工具\\adb-1.0.31-windows"+"\r\n");
		SBwrite.append("adb kill-server"+"\r\n");
		SBwrite.append("adb start-server"+"\r\n");
		SBwrite.append("adb devices"+"\r\n");
		SBwrite.append("adb forward tcp:"+lPort+" tcp:"+lPort+"\r\n");
		
		SBwrite.append("start cmd /k "+"\"echo 此窗口使用dd命令从安卓设备端制作镜像&&cd "+SprojectPath+"\\工具\\adb-1.0.31-windows&&adb shell busybox nc -l -p "+lPort+" -e dd if=/dev/block/platform/msm_sdcc.1/by-name/userdata\""+"\r\n");
		SBwrite.append("start cmd /k "+"\"echo 此窗口使用nc工具从端口读取镜像"+"&&cd "+SprojectPath+"\\工具\\nc&&nc 127.0.0.1 "+lPort+" > "+Imagepath+"\\AndroidImage\\data.img\""+"\r\n");
		//SBwrite.append("pause");
		try {
			FileWriter FWwrite=new FileWriter("工具\\makeImage.bat");
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
			Process ps1=Running.exec("cmd /c 工具\\makeImage.bat");
			if(ps1.waitFor()!=0){
				return false;	
			}
		} catch (Exception ec) {
			ec.printStackTrace();
		}
		return true;
		
	}
	
	public static void pullDataBAT(){
		
		//选择拉取的数据库存储路径，
				
				JFileChooser Jchooser =new JFileChooser();
				Jchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				Component parent=null;
				int returnVal=Jchooser.showOpenDialog(parent);
				if(returnVal==JFileChooser.APPROVE_OPTION){
					Databasepath=Jchooser.getSelectedFile().getAbsolutePath();
				}
			//这里写脚本
				StringBuffer SBwrite=new StringBuffer();
				File file=new File(Databasepath+"\\数据库文件");
				file.mkdirs();
				SBwrite.append("@echo off"+"\r\n");
				
				SBwrite.append("cd "+SprojectPath+"\\工具\\adb-1.0.31-windows"+"\r\n");
				SBwrite.append("adb kill-server"+"\r\n");
				SBwrite.append("adb start-server"+"\r\n");
				SBwrite.append("adb devices"+"\r\n");
				SBwrite.append("adb pull /data/data/com.android.providers.telephony/databases/mmssms.db "+Databasepath+"\\数据库文件"+"\r\n");
				SBwrite.append("echo 短信数据库提取完成"+"\r\n");
				SBwrite.append("adb pull /data/data/com.android.providers.contacts/databases/contacts2.db "+Databasepath+"\\数据库文件"+"\r\n");
				SBwrite.append("echo 联系人和通话记录数据库提取完成"+"\r\n");
				SBwrite.append("adb pull /data/data/com.UCMobile/databases/bookmark.db "+Databasepath+"\\数据库文件"+"\r\n");
				SBwrite.append("echo 浏览器书签数据库提取完成"+"\r\n");
				SBwrite.append("adb pull /data/data/com.android.providers.media/databases/external.db "+Databasepath+"\\数据库文件"+"\r\n");
				SBwrite.append("echo 多媒体数据库提取完成"+"\r\n");
				//SBwrite.append("pause");
				try {
					FileWriter FWwrite=new FileWriter("工具\\pullData.bat");
					FWwrite.write(SBwrite.toString());
					FWwrite.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	public static boolean pullDataEXE(){
		try {
			Process ps1 = Runtime.getRuntime().exec("cmd /c 工具\\pullData.bat");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void backupData(){
		// 将拉取的数据库文件夹进行备份
		File dir1 = new File(Databasepath+"\\数据库文件");
		File dir2 = new File(Databasepath+"\\数据库文件备份");
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
					Databasepath+"\\数据库文件备份\\"
							+ filename);
			if (!AfterFile.exists()) {
				try {
					AfterFile.createNewFile();// 这个是在目录里创建文件，需要先创建文件
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
		File dir1 = new File(Databasepath+"\\数据库文件");
		File dir2 = new File(Databasepath+"\\数据库文件MD5检验");
		if (!dir2.exists()) {
			dir2.mkdir();
		}
		File[] file = dir1.listFiles();
		for (int i = 0; i < file.length; i++) {
			String path = file[i].getAbsolutePath();
			String filename = path.substring(path.lastIndexOf("\\") + 1,path.length());
			File AfterFile = new File(Databasepath+"\\数据库文件MD5检验\\"+ filename+"校验值.txt");
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
		System.out.println("后一步MD5值为"+MD5Result2);
		String MD5Result=null;
		switch(flag){
		case 1:
			file =new File(Databasepath+"\\数据库文件MD5检验\\"+"mmssms.db校验值.txt"); 
			break;
		case 2:
		case 3:
			file =new File(Databasepath+"\\数据库文件MD5检验\\"+"contacts2.db校验值.txt"); 
			break;
		case 4:
			file =new File(Databasepath+"\\数据库文件MD5检验\\"+"bookmark.db校验值.txt"); 
			break;
		case 5:
			file =new File(Databasepath+"\\数据库文件MD5检验\\"+"external.db校验值.txt"); 
			break;
		}
		//读取提出完数据库做的MD5散列值
		if(file.isFile()&&file.exists()){
			try {
				InputStreamReader reader=new InputStreamReader(new FileInputStream(file),encoding);
				BufferedReader bufferReader=new BufferedReader(reader);
				MD5Result=bufferReader.readLine();
				System.out.println("读出来MD5值为"+MD5Result);
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
		StringBuffer SBwrite=new StringBuffer();//当恢复出来的数据有非法字符不能写入数据库的时候写入TXT
		SBwrite.append(dataName+"不能写入数据库的内容：\r\n");
		//ArrayList <String> failInsert=new ArrayList<String>();
		try {
			Class.forName("org.sqlite.JDBC");
			con=DriverManager.getConnection("jdbc:sqlite:RecData.db");
			con.setAutoCommit(false);
			System.out.println("数据库连接成功"+dataName);
			stmt=con.createStatement();
			//插入数据前先清空表，是我调试的时候用的
			String sqldel="delete from "+dataName+";";
			stmt.executeUpdate(sqldel);
			for(int i=0;i<recData.size();i++){
				dataString=recData.get(i);
				dataString=dataString.substring(0,dataString.lastIndexOf(","));//去掉最后一个逗号
				//在写入数据库之前判断恢复结果字符串中是否有乱码字符,(,),#，有的话就写入TXT中
				if(!dataString.contains("")){
					sql="INSERT INTO  "+dataName+"  "+" VALUES ("+dataString+");";
				}
				else{
					iGarbledCount++;
					SBwrite.append(dataString+"\r\n"); 
				}
				//System.out.println("写入数据库函数中的sql语句"+sql);
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
		//不能写入数据库的写入TXT
		writeTXT(SBwrite.toString());
		SBwrite=null;
		return iGarbledCount;
	}
	private static void writeTXT(String str){
		try {
			//new对象时，第二个参数为true表示在文件后面追加新内容
			FileWriter FWwrite=new FileWriter("不能写入数据库内容.txt",true);
			FWwrite.write(str);
			FWwrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
