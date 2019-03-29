package androidForensic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Sql_master  {
	 
	char[] cType;// 表示系统表创建的类型，有table，index，trigger，view四种值
	char[] cName;// 表，索引，触发器或者试图的名字
	char[] cTbl_name;// 对表和视图，和cName是一样的，对索引和触发器来说是建立在哪个表上的名字
	int iRootpageNumber;// 对表或索引来说，是根页的页号，在几个表中看到的页号都是一个字节表示的
	String SSQL;// 创建表、索引、触发器或者视图所使用的语句
	
	String SdataPath;
	String Sql;
	int iTypeNumber;
	
	/*
	 * 初始化系统表
	 * String SdataPath：系统表所在的数据库路径
	 * String Sql：处理系统表是使用的SQL语句
	 */
	public Sql_master(String SdataPath, String Sql) {
		this.SdataPath = SdataPath;
		this.Sql = Sql;
		Connection connection;
		PreparedStatement pstmt;
		ResultSet RS;
		try {
			// 连接数据库
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager
					.getConnection("jdbc:sqlite:" + SdataPath);
			connection.setAutoCommit(false);
			pstmt = connection.prepareStatement(Sql);
			RS = pstmt.executeQuery();
			this.iRootpageNumber = RS.getInt("rootpage");
			this.SSQL = RS.getString("sql");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ":" + e.getMessage());
			System.exit(0);
		}
	}
	
	/*
	 * 通过计算系统表sql语句中逗号的个数得到字段的个数
	 * return：返回目标表字段个数
	 */
	int getTypeNumber() {
		int count = 0;
		int offset = 0;
		while ((offset = SSQL.indexOf(",", offset)) != -1) {
			offset++;// 查找成功之后，偏移跳过逗号占用的字符
			count++;
		}
		return (count+1);
	}
	int [] getType(){
		int[] typeKind=new int[getTypeNumber()];
		String  splitSql1=SSQL.substring(SSQL.indexOf("(")+1,SSQL.lastIndexOf(")"));
		System.out.println("SQL语句是"+splitSql1);
		String [] splitSql2=splitSql1.split(",");
		String temp;
		String splitSql3;
		for(int i=0;i<splitSql2.length;i++){
			splitSql3=splitSql2[i].split("\\s+")[1];//按照空格在拆一次,但是有时前面还有空格
			temp=splitSql3;
			
			if(temp.equals(temp.toLowerCase())){//如果得到的是小写，在往后顺延一个
				splitSql3=splitSql2[i].split("\\s+")[2];}
			if(splitSql3.equals("NULL")){
				typeKind[i]=0;//0是null类型
			}
			else if(splitSql3.equals("INTEGER")){
				typeKind[i]=1;//1是整数
			}
			else if(splitSql3.equals("REAL")){
				typeKind[i]=2;//2是浮点数
			}
			else if(splitSql3.equals("TEXT")){
				typeKind[i]=3;//3是文本类型
			}
			else {
				typeKind[i]=4;//4是BLOB类型
			}
		}
		return typeKind;
	}
}

