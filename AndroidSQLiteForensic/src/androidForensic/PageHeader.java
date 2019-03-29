package androidForensic;

import java.io.IOException;
import java.io.RandomAccessFile;

public class PageHeader extends DBHeader{
	byte cPageType;// 0x0d��b+treeҶ��ҳ��0x05��b+tree�ڲ�ҳ��0x0A��b-treeҶ��ҳ��0x02b-tree�ڲ�ҳ  0
	short sFisrtFreeBlockAddr;// ��һ�����ɿ��ƫ���� 1-2
	short sPayloadNumber;// ��ҳ��Ԫ�� 3-4
	short sPayloadStartAddr;// ��Ԫ������ʼ��ַ 5-6
	byte cFragmentBytes;// ��Ƭ�ֽ��� 7
	int iRightChildPage;// ���Һ���ҳ�ţ�ֻ���ڲ�ҳ����һ���� 8-11
	short[] sPerPayloadStartNumber;// ��һҳ��ÿ����Ԫ����ʼƫ�Ƶ�ַ���������ֽڱ�ʾ,����һ����Ե�ַ��Ҫ�ҵ�����ƫ�ƣ��ü�����һҳ�Ŀ�ʼƫ��λ��

	static int iPageStartAddr;
	int[] iLeafPage = { 0 };// ֻ�����ڲ�ҳ��ʱ���ʹ�ã��洢�ڲ�ҳ�����к���ҳ���������Һ���ҳ��,��˸�����Ĵ�С�ǵ�Ԫ������1
	// ILeafKey�洢ÿ��Ҷ��ҳ��key����һ���䳤��������������ά��Ϊ9
	int[] iLeafKey = { 0 };
	String SDataPath;
	int iPageNumber;
	
	/*
	 * ��ʼ�����ҳͷ
	 * String SDataPath���ñ��������ݿ��·��
	 * int pageNumber����ҳ��ҳ��
	 */
	public PageHeader(String SDataPath, int pageNumber) {
		super(SDataPath);
		this.SDataPath = SDataPath;
		this.iPageNumber=pageNumber;
		try {
			// ��������newһ��raFile����Ȼ�ڼ̳�DBHeader��ʱ����File��ʼ���������ݿ�ͷ��λ��
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
				// ������һ����Ԫ��λ��
				raFile.skipBytes(sPayloadStartAddr - 12 - 2 * sPayloadNumber);
				// ��������ڲ�ҳÿ��Ҷ��ҳ��ҳ�ţ�����keyֵ
				int i = 0;
				for (; i < sPayloadNumber; i++) {
					iLeafPage[i] = raFile.readInt();
					int sKey;
					int j = 0;
					// ����keyֵ
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
