package androidForensic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;//������ResultSet��Ԫ���ݼ���˵��


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
	// �������ݿ��������
	Connection connection = null;
	PreparedStatement pstmt = null;
		
	/*
	 * ��ʼ������
	 * String SDataName��չʾ���������ݿ�·��
	 * String SSql��չʾ���ݱ���ص�sql���
	 */
	public DisplayDatabase(String SDataName, String SSql) {
		this.SDataName = SDataName;
		this.SSql = SSql;
		try {
			// �������ݿ�
			Class.forName("org.sqlite.JDBC");//Ҫ��JVM���Ҳ�����ָ�����࣬JVM��ִ�и���ľ�̬�����
			connection = DriverManager.getConnection("jdbc:sqlite:" + SDataName);
			connection.setAutoCommit(false);
			System.out.println("չʾ�����е�"+SDataName + "���ݿ����ӳɹ�");
			pstmt = connection.prepareStatement(SSql);
			RS = pstmt.executeQuery();
			// �õ�����
			iCountColumn = RS.getMetaData().getColumnCount();		
			// �õ���ͷ��Ϣ, ����������1��ʼ�����SColumnName[]�����С������iCountColumn+1,����Ҫ����checkbox��һ�У�����Ǽ�2
			SColumnName = new Object[iCountColumn + 1]; 
			for (int i = 1; i <= iCountColumn; i++) {
				SColumnName[i] = RS.getMetaData().getColumnName(i);
			}
			// �õ�����
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
	 * չʾ����� 
	 * return��ResultSet�����������ResultSet�������ʽ����
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
	 * �����ֶ�������
	 */
	public Object[] getTableTitle() {
		return SColumnName;
	}
	
	/*
	 * ������������
	 */
	public int getcolumncount() {
		return iCountColumn;
	}
	
	/*
	 * ������������
	 */
	public int getrowcount() {
		return iCountRow;
	}
}
