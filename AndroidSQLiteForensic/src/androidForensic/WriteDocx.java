package androidForensic;

import java.awt.Component;
import java.util.List;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

public class WriteDocx {
	String deviceInfo;
	OutPutTable [] viewOut;
	OutPutTable [] RecOut;
	static String reportPath;
	String [] sViewResult;
	String [] sRecResult;

	 
	public WriteDocx(String deviceInfo,OutPutTable [] viewOut,OutPutTable [] RecOut,String [] sViewResult,String [] sRecResult){
		this.deviceInfo=deviceInfo;
		this.viewOut=viewOut;
		this.RecOut=RecOut;
		this.sViewResult=sViewResult;
		this.sRecResult=sRecResult;
	}
	@SuppressWarnings("static-access")
	public  void WriteData(){
		
		JOptionPane.showMessageDialog(null,"��ѡ�񱨸�����·��");	
		JFileChooser Jchooser =new JFileChooser();
		Jchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		Component parent=null;
		int returnVal=Jchooser.showOpenDialog(parent);
		if(returnVal==Jchooser.APPROVE_OPTION){
			reportPath=Jchooser.getSelectedFile().getAbsolutePath();
		}
		
		//�½��ĵ�
		XWPFDocument docx =new XWPFDocument();
		//�½�����
		XWPFParagraph paragraph=docx.createParagraph();
		//�½�����
		XWPFRun run=paragraph.createRun();
		run.setBold(true);//�Ӵ�
		run.setFontSize(20);
		run.setText("��׿�����ն��ۺϷ�����ȡ֤����");
		paragraph.setAlignment(ParagraphAlignment.CENTER);//���ö�����뷽ʽ
		
		XWPFParagraph parasubtitle=docx.createParagraph();
		run=parasubtitle.createRun();
		run.setFontSize(14);
		SimpleDateFormat smdate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		run.setText("��������ʱ�䣺"+smdate.format(new Date()));
		
		paragraph=docx.createParagraph();
		run=paragraph.createRun();
		run.setFontSize(14);
		run.setText("�������ݣ�");
		
		paragraph=docx.createParagraph();
		run=paragraph.createRun();
		run.setFontSize(14);
		run.setText("1���豸��Ϣ��");
		
	
		//�û��з�����Filesystem�ָ�
		String [] contents=deviceInfo.split("\n|��Ϣ|/dev");
		for(int i=0;i<contents.length;i++){
			if(i==3){
				contents[i]="/dev"+contents[i];
			}
			docx.createParagraph().createRun().setText(contents[i]);
		}
		
		paragraph=docx.createParagraph();
		run=paragraph.createRun();
		run.setFontSize(14);
		run.setText("2���鿴�����");
		
		for(int i=0;i<5;i++){
			docx.createParagraph().createRun().setText(sViewResult[i]);
		}
		
		paragraph=docx.createParagraph();
		run=paragraph.createRun();
		run.setFontSize(14);
		run.setText("3���������ݿ�����Ϣ��");
		
		//д�鿴���Ľ��
		for(int i=0;i<5;i++){
			if(viewOut[i].tableName==null){
				continue;
			}
			else{
				docx.createParagraph().createRun().setText(viewOut[i].tableName);
				WriteTable(docx,viewOut[i],i);
			}
		}
		paragraph=docx.createParagraph();
		run=paragraph.createRun();
		run.setFontSize(14);
		run.setText("4���ָ������");
		
		for(int i=0;i<5;i++){
			docx.createParagraph().createRun().setText(sRecResult[i]);
		}
		
		paragraph=docx.createParagraph();
		run=paragraph.createRun();
		run.setFontSize(14);
		run.setText("5���ָ����ݿ�����Ϣ��");
		//д�ָ����Ľ��
				for(int i=0;i<5;i++){
					if(RecOut[i].tableName==null){
						continue;
					}
					else{
						docx.createParagraph().createRun().setText(RecOut[i].tableName);
						WriteTable(docx,RecOut[i],i);
					}
				}
		 try {
			 OutputStream OS =new FileOutputStream(reportPath+"\\"+"ForensicReport.docx");
			 docx.write(OS);
			 OS.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void WriteTable(XWPFDocument docx,OutPutTable outdata,int index){
		 int indexBody=0;
		//��ÿ����Ŀ�����Ϣд������
				//�½����
				XWPFTable table=docx.createTable(outdata.rowCount[index]+1,outdata.ColumnCount);
				//���ñ����
				CTTbl ttbl =table.getCTTbl();
				CTTblPr tblPr=ttbl.getTblPr()==null?ttbl.addNewTblPr():ttbl.getTblPr();
				CTTblWidth tblWidth=tblPr.isSetTblW()?tblPr.getTblW():tblPr.addNewTblW();
				CTJc cTJc=tblPr.addNewJc();
				cTJc.setVal(STJc.Enum.forString("left"));
				tblWidth.setW(new BigInteger("8000"));
				tblWidth.setType(STTblWidth.DXA);
				List<XWPFTableCell> tableCells=table.getRow(0).getTableCells();
				//д��ͷ
				for(int i=0;i<outdata.ColumnCount;i++){
					tableCells.get(i).setText((String) outdata.title[i]);
				}
				//д�������			
				for(;indexBody<outdata.rowCount[index];indexBody++){
					List<XWPFTableCell> tableCellBody=table.getRow(indexBody+1).getTableCells();					
					for(int j=0;j<outdata.ColumnCount;j++){
						if(outdata.tableBody[indexBody][j] instanceof Number){
							tableCellBody.get(j).setText(String.valueOf(outdata.tableBody[indexBody][j]));
						}
						else{	
							tableCellBody.get(j).setText((String) outdata.tableBody[indexBody][j]);
						}
						
					}
				}
	}

}
