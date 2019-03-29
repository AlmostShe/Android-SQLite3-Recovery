package androidForensic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import androidForensic.Sql_master;

public class DBHeader {
	
	char cIdetify[] = new char[16]; // 头字符串，SQLite format 3 0-15
	short sPageSize;// 页大小 16-17
	byte cFormatVesionWrite;// 文件格式版本写 18
	byte cFormatVesionRead;// 文件格式版本读 19
	byte cReserve1;// 每页尾部保留空间大小默认为0 20
	byte cMaxPayloadFraction;// btree内部页中一个单元最多能够使用的空间0x40是25% 21
	byte cMinPayloadFraction;// btree内部页中一个单元使用空间的最小值 0x20 是12.4% 22
	byte cLeafPayloadFraction;// btree叶子页中一个单元使用空间的额最小值 0x20 12.5% 23
	int iFileChangeCount;// 文件修改次数，由事务增加他的值 24-27
	int iDatabasePageSize;// 数据库占据的总页数 28-31
	int iFreeBlockHead;// 空闲页链表头指针 32-35
	int iFreePageNumber;// 空闲页数量 36-40
	int iSchemaVersion;// schema版本号，每次创建删除表索引或试图触发器等导致sql_master表变化就加1 40-43
	int iFileFormatSchema;// 值为1-4 44-47
	int iCacheSize;// 默认的页缓存大小 48-51
	int iLargestRootPage;// 启用auto-vacuum模式时，表示b-tree最大的根页号 52-55
	int iCharacterCode;// 编码方式，1-utf-8,2-utf-16le，3-utf-16be 56-59
	int iUserVersion;// 由用户pragma读取或设置值 60-63
	int iIncrementVacuumMode;// 对于auto-vacuum数据库，当为incremental-vacuum模式时，为1否则为0 64-67
	int iUserAppID;// 由PRAGMA application_id设置的应用ID 68-71
	byte cReserver2[];// 为扩展空间所做的保留 72-91
	int iVersionValid;// 有效版本 92-95
	int iSqlVersionNumber;// SQLite版本号 96-99
    
    String SDBPath;//将创建数据库头对象时声明的文件路径设置为public类型，这样sql_master类可以不继承而直接使用该变量
    RandomAccessFile raFile;
    Sql_master [] sql_master;
    
    /*
     * 初始化数据库头
     * String SDatapath：是数据库文件的路径
     */
	DBHeader(String SDatapath) {
		this.SDBPath = SDatapath;
		try {
			raFile = new RandomAccessFile(SDBPath, "r");
			try {
				raFile.skipBytes(16);
				this.sPageSize = raFile.readShort();	
				raFile.skipBytes(6);// 要注意读完后指针发生了改变，因此跳过的字节数是相对的
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
