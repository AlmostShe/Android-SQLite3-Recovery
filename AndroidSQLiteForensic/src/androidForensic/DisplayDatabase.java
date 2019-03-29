package androidForensic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;//该类是ResultSet的元数据集合说明


import javax.swing.JTable;
import javax.swing.table.JTableHeader;

public class DisplayDatabase {

	JTableHeader JTheader;
	JTable JtabDemo;
	String SDataName;
	String SSql;
	static int iCountRow = 0;
	public static int iCountColumn = 0;
	public static Object[] SColumnName;
	ResultSet RS;
	ResultSet RSQuery=null;
	ResultSetMetaData rSMdataData;
	// 连接数据库变量定义
	Connection connection = null;
	PreparedStatement pstmt = null;
		
	/*
	 * 初始化函数
	 * String SDataName：展示表所在数据库路径
	 * String SSql：展示数据表相关的sql语句
	 */
	public DisplayDatabase(String SDataName, String SSql) {
		this.SDataName = SDataName;
		this.SSql = SSql;
		try {
			// 连接数据库
			Class.forName("org.sqlite.JDBC");//要求JVM查找并加载指定的类，JVM会执行该类的静态代码段
			connection = DriverManager.getConnection("jdbc:sqlite:" + SDataName);
			connection.setAutoCommit(false);
			System.out.println("展示函数中的"+SDataName + "数据库连接成功");
			pstmt = connection.prepareStatement(SSql);
			RS = pstmt.executeQuery();
			// 得到列数
			iCountColumn = RS.getMetaData().getColumnCount();		
			// 得到表头信息, 由于列数从1开始，因此SColumnName[]数组大小必须是iCountColumn+1,另外要加上checkbox的一列，因此是加2
			SColumnName = new Object[iCountColumn + 1]; 
			for (int i = 1; i <= iCountColumn; i++) {
				SColumnName[i] = RS.getMetaData().getColumnName(i);
			}
			// 得到行数
			while (RS.next()) {
				iCountRow++;
			}
		} catch (Exception ee) {
			// TODO Auto-generated catch block
			System.err.println(ee.getClass().getName() + ":" + ee.getMessage());
			System.exit(0);
		}
	}

	/*
	 * 展示表格函数 
	 * return：ResultSet，将表的内容ResultSet对象的形式返回
	 */
	public ResultSet displayTable() {		
		try {
			if(RSQuery!=null){
				RSQuery.close();
			}
			pstmt=connection.prepareStatement(SSql);
			RSQuery=pstmt.executeQuery();
			//pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return RSQuery;
	}
	
	public void closeDatabase(){
		try{
			pstmt.close();
			connection.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/*
	 * 返回字段名函数
	 */
	public Object[] getTableTitle() {
		return SColumnName;
	}
	
	/*
	 * 返回列数函数
	 */
	public int getcolumncount() {
		return iCountColumn;
	}
	
	/*
	 * 返回行数函数
	 */
	public int getrowcount() {
		return iCountRow;
	}
}
