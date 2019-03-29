package androidForensic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Sql_master  {
	 
	char[] cType;// ��ʾϵͳ���������ͣ���table��index��trigger��view����ֵ
	char[] cName;// ��������������������ͼ������
	char[] cTbl_name;// �Ա����ͼ����cName��һ���ģ��������ʹ�������˵�ǽ������ĸ����ϵ�����
	int iRootpageNumber;// �Ա��������˵���Ǹ�ҳ��ҳ�ţ��ڼ������п�����ҳ�Ŷ���һ���ֽڱ�ʾ��
	String SSQL;// ������������������������ͼ��ʹ�õ����
	
	String SdataPath;
	String Sql;
	int iTypeNumber;
	
	/*
	 * ��ʼ��ϵͳ��
	 * String SdataPath��ϵͳ�����ڵ����ݿ�·��
	 * String Sql������ϵͳ����ʹ�õ�SQL���
	 */
	public Sql_master(String SdataPath, String Sql) {
		this.SdataPath = SdataPath;
		this.Sql = Sql;
		Connection connection;
		PreparedStatement pstmt;
		ResultSet RS;
		try {
			// �������ݿ�
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
	 * ͨ������ϵͳ��sql����ж��ŵĸ����õ��ֶεĸ���
	 * return������Ŀ����ֶθ���
	 */
	int getTypeNumber() {
		int count = 0;
		int offset = 0;
		while ((offset = SSQL.indexOf(",", offset)) != -1) {
			offset++;// ���ҳɹ�֮��ƫ����������ռ�õ��ַ�
			count++;
		}
		return (count+1);
	}
	int [] getType(){
		int[] typeKind=new int[getTypeNumber()];
		String  splitSql1=SSQL.substring(SSQL.indexOf("(")+1,SSQL.lastIndexOf(")"));
		System.out.println("SQL�����"+splitSql1);
		String [] splitSql2=splitSql1.split(",");
		String temp;
		String splitSql3;
		for(int i=0;i<splitSql2.length;i++){
			splitSql3=splitSql2[i].split("\\s+")[1];//���տո��ڲ�һ��,������ʱǰ�滹�пո�
			temp=splitSql3;
			
			if(temp.equals(temp.toLowerCase())){//����õ�����Сд��������˳��һ��
				splitSql3=splitSql2[i].split("\\s+")[2];}
			if(splitSql3.equals("NULL")){
				typeKind[i]=0;//0��null����
			}
			else if(splitSql3.equals("INTEGER")){
				typeKind[i]=1;//1������
			}
			else if(splitSql3.equals("REAL")){
				typeKind[i]=2;//2�Ǹ�����
			}
			else if(splitSql3.equals("TEXT")){
				typeKind[i]=3;//3���ı�����
			}
			else {
				typeKind[i]=4;//4��BLOB����
			}
		}
		return typeKind;
	}
}

