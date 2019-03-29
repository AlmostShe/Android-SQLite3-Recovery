package androidForensic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import androidForensic.Sql_master;

public class DBHeader {
	
	char cIdetify[] = new char[16]; // ͷ�ַ�����SQLite format 3 0-15
	short sPageSize;// ҳ��С 16-17
	byte cFormatVesionWrite;// �ļ���ʽ�汾д 18
	byte cFormatVesionRead;// �ļ���ʽ�汾�� 19
	byte cReserve1;// ÿҳβ�������ռ��СĬ��Ϊ0 20
	byte cMaxPayloadFraction;// btree�ڲ�ҳ��һ����Ԫ����ܹ�ʹ�õĿռ�0x40��25% 21
	byte cMinPayloadFraction;// btree�ڲ�ҳ��һ����Ԫʹ�ÿռ����Сֵ 0x20 ��12.4% 22
	byte cLeafPayloadFraction;// btreeҶ��ҳ��һ����Ԫʹ�ÿռ�Ķ���Сֵ 0x20 12.5% 23
	int iFileChangeCount;// �ļ��޸Ĵ�������������������ֵ 24-27
	int iDatabasePageSize;// ���ݿ�ռ�ݵ���ҳ�� 28-31
	int iFreeBlockHead;// ����ҳ����ͷָ�� 32-35
	int iFreePageNumber;// ����ҳ���� 36-40
	int iSchemaVersion;// schema�汾�ţ�ÿ�δ���ɾ������������ͼ�������ȵ���sql_master��仯�ͼ�1 40-43
	int iFileFormatSchema;// ֵΪ1-4 44-47
	int iCacheSize;// Ĭ�ϵ�ҳ�����С 48-51
	int iLargestRootPage;// ����auto-vacuumģʽʱ����ʾb-tree���ĸ�ҳ�� 52-55
	int iCharacterCode;// ���뷽ʽ��1-utf-8,2-utf-16le��3-utf-16be 56-59
	int iUserVersion;// ���û�pragma��ȡ������ֵ 60-63
	int iIncrementVacuumMode;// ����auto-vacuum���ݿ⣬��Ϊincremental-vacuumģʽʱ��Ϊ1����Ϊ0 64-67
	int iUserAppID;// ��PRAGMA application_id���õ�Ӧ��ID 68-71
	byte cReserver2[];// Ϊ��չ�ռ������ı��� 72-91
	int iVersionValid;// ��Ч�汾 92-95
	int iSqlVersionNumber;// SQLite�汾�� 96-99
    
    String SDBPath;//���������ݿ�ͷ����ʱ�������ļ�·������Ϊpublic���ͣ�����sql_master����Բ��̳ж�ֱ��ʹ�øñ���
    RandomAccessFile raFile;
    Sql_master [] sql_master;
    
    /*
     * ��ʼ�����ݿ�ͷ
     * String SDatapath�������ݿ��ļ���·��
     */
	DBHeader(String SDatapath) {
		this.SDBPath = SDatapath;
		try {
			raFile = new RandomAccessFile(SDBPath, "r");
			try {
				raFile.skipBytes(16);
				this.sPageSize = raFile.readShort();	
				raFile.skipBytes(6);// Ҫע������ָ�뷢���˸ı䣬����������ֽ�������Ե�
				this.iFileChangeCount = raFile.readInt();
				this.iDatabasePageSize = raFile.readInt();
				this.iFreeBlockHead = raFile.readInt();
				this.iFreePageNumber = raFile.readInt();
				this.iSchemaVersion = raFile.readInt();
				this.iFileFormatSchema = raFile.readInt();
				this.iCacheSize = raFile.readInt();
				this.iLargestRootPage = raFile.readInt();
				this.iCharacterCode = raFile.readInt();
				this.iUserVersion = raFile.readInt();
				this.iIncrementVacuumMode = raFile.readInt();
				this.iUserAppID = raFile.readInt();
				raFile.skipBytes(20);
				this.iVersionValid = raFile.readInt();
				this.iSqlVersionNumber = raFile.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
