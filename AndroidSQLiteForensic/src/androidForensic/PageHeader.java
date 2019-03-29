package androidForensic;

import java.io.IOException;
import java.io.RandomAccessFile;

public class PageHeader extends DBHeader{
	byte cPageType;// 0x0d是b+tree叶子页，0x05是b+tree内部页，0x0A是b-tree叶子页，0x02b-tree内部页  0
	short sFisrtFreeBlockAddr;// 第一个自由块的偏移量 1-2
	short sPayloadNumber;// 本页单元数 3-4
	short sPayloadStartAddr;// 单元内容起始地址 5-6
	byte cFragmentBytes;// 碎片字节数 7
	int iRightChildPage;// 最右孩子页号，只有内部页有这一部分 8-11
	short[] sPerPayloadStartNumber;// 这一页中每个单元的起始偏移地址，用两个字节表示,这是一个相对地址，要找到绝对偏移，得加上这一页的开始偏移位置

	static int iPageStartAddr;
	int[] iLeafPage = { 0 };// 只有在内部页的时候才使用，存储内部页的所有孩子页，包括最右孩子页数,因此给数组的大小是单元个数加1
	// ILeafKey存储每个叶子页的key，是一个变长整数，所以数组维度为9
	int[] iLeafKey = { 0 };
	String SDataPath;
	int iPageNumber;
	
	/*
	 * 初始化表的页头
	 * String SDataPath：该表所在数据库的路径
	 * int pageNumber：该页的页号
	 */
	public PageHeader(String SDataPath, int pageNumber) {
		super(SDataPath);
		this.SDataPath = SDataPath;
		this.iPageNumber=pageNumber;
		try {
			// 必须重新new一次raFile，不然在继承DBHeader的时候让File初始不是在数据库头的位置
			raFile = new RandomAccessFile(SDataPath, "r");
			PageHeader.iPageStartAddr = sPageSize * (iPageNumber - 1);
			raFile.skipBytes(iPageStartAddr);
			this.cPageType = raFile.readByte();
			this.sFisrtFreeBlockAddr = raFile.readShort();
			this.sPayloadNumber = raFile.readShort();
			this.sPayloadStartAddr = raFile.readShort();
			this.cFragmentBytes = raFile.readByte();
			if ( cPageType == 5) {
				this.iRightChildPage = raFile.readInt();
			}
			sPerPayloadStartNumber = new short[sPayloadNumber];
			for (int i = 0; i < sPayloadNumber; i++) {
				this.sPerPayloadStartNumber[i] = raFile.readShort();
			}
			if (cPageType == 5) {
				iLeafPage = new int[sPayloadNumber + 1];
				iLeafKey = new int[9];
				// 跳到第一个单元的位置
				raFile.skipBytes(sPayloadStartAddr - 12 - 2 * sPayloadNumber);
				// 这里读入内部页每个叶子页的页号，跳过key值
				int i = 0;
				for (; i < sPayloadNumber; i++) {
					iLeafPage[i] = raFile.readInt();
					int sKey;
					int j = 0;
					// 跳过key值
					while ((((sKey = raFile.readUnsignedByte()) >> 7) & 1) != 0) {
						iLeafKey[j++] = sKey;
					}
				}
				iLeafPage[i] = iRightChildPage;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
