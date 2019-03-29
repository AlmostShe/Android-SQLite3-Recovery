package androidForensic;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;


public class UImanager extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int start = 1970;
	private static final int end = 2020;
	private static int iClick=0;
	private static int iRecClick=0;
	
	private static  String  disDataPath = "D:\\�û�Ŀ¼\\�ҵ��ĵ�\\���ݿ��ļ�\\";
	private static  String  recDataPath = "D:\\�û�Ŀ¼\\�ҵ��ĵ�\\���ݿ��ļ�����\\";
	private static  String  devicesInfo = "XXXXXXXXX";
	private static ArrayList<String>  SResultList;
	static String SDisplayDataName;
	static String SSql;
	DisplayDatabase displaydata = null;
	DefaultTableModel DTablemodel;
	private static OutPutTable [] outDisDataArray;
	private static OutPutTable [] outRecDataArray;


	//String outDataString="";
	JTabbedPane jtab;
	JLabel jlPhoneInfo;
	JPanel JPImage;
	JPanel JPDisplay;
	JPanel JPRecovery; 
	JPanel JPRecovery2;
	JPanel JPDevices;
	JPanel JPButton;
	JTextArea jtDevices;
	// ������������
	JButton jbMakeImge;
	JButton jbDisplayImge;
	//JTextArea jtaImage;
	JButton jbPullData;
	JButton jbBackupData;
	JButton jbConnect;
	JButton jbIntegrity;

	// �鿴֤�ݽ������
	String [] sViewResultCount=new String [5];
	
	JButton jbDisplaySMS;
	JButton jbDisplayContacts;
	JButton jbDisplayCallLog;
	JButton jbDisplayBroswer;
	JButton jbDisplayExternal;
	JButton jbOKKey;
	JButton jbOkTime;
	JButton jbOutView;
	JLabel jlFindTime;
	JLabel jlFindKey;
	JLabel jlImage;
	JLabel jlDisplaydata;
	JLabel jlSTime;
	JLabel jlETime;
	JLabel jlRow;
	JComboBox jcbYear;
	JComboBox jcbMonth;
	JComboBox jcbDay;
	JComboBox jcbHour;
	JComboBox jcbMinute;
	JComboBox jcbEYear;
	JComboBox jcbEMonth;
	JComboBox jcbEDay;
	JComboBox jcbEHour;
	JComboBox jcbEMinute;
	JTextField jtfKey;
	JScrollPane JscpDisplay;
	JScrollBar JsBar;
	JTable JTabDispaly;
	JPanel jpcbox;
	JCheckBox jcbox1;
	JCheckBox jcbox2;
	JCheckBox jcbox3;
	
	

	// �ָ�֤�ݽ������
	String [] sRecResultCount=new String [5];
	int iGarbledCount;
	JPanel JPCountlabel;
	JLabel jlRecoverdata1;
	JButton jbRecoverSMS1;
	JButton jbRecoverContacts1;
	JButton jbRecoverCalls1;
	JButton jbRecoverBrowsers1;
	JButton jbRecoverExternals1;
	JButton jbOutputDataButton1;
	JButton jbSaveRecover1;
	JButton jbOutRec;
	JLabel  jlCountAll;
	JLabel  jlCountRec;

	
	JLabel jlRecoverdata2;
	JButton jbRecoverSMS2;
	JButton jbRecoverContacts2;
	JButton jbRecoverCalls2;
	JButton jbRecoverBrowsers2;
	JButton jbRecoverExternals2;
	JScrollPane jSPDisplayText2;
	JTextArea jtDisplayRecovery2;
	
	JScrollPane jSPDisplayText;
	JTextArea jtDisplayRecovery;	

	public static void main(String[] args) {
		SResultList=new ArrayList<String>();
		if(outDisDataArray==null){
			outDisDataArray=new OutPutTable[5];
			for(int i=0;i<5;i++){
				outDisDataArray[i]=new OutPutTable();
			}
		}
		if(outRecDataArray==null){
			outRecDataArray=new OutPutTable[5];
			for(int i=0;i<5;i++){
				outRecDataArray[i]=new OutPutTable();
			}
		}
		ComponentStyle.loadStyle();//���齨�ĸ��Ի����ü��ؽ���
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {  
				UImanager ui = new UImanager();
				ui.setLocationRelativeTo(null);
				ui.setVisible(true);			
			}
		});
	}

	public UImanager() {
		super();
		initUI();
	}

	public void initUI() {
		try {
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// �˳�Ӧ�ó���Ĭ�ϴ��ڹرղ���
			getContentPane().setLayout(null);// ��ʼ������
			this.setResizable(false);// ���岻�����ɸı��С
			this.setBounds(200, 150, 600, 500);// ����ϱ߾��룬��ȣ��߶ȣ�����Ĵ�С
			this.setBackground(new Color(235,245,224));
			this.setTitle("��׿�����ն��ۺϷ�����ȡ֤ϵͳ");
			{
				{
					jlPhoneInfo=new JLabel("���ӵ��豸��Ϣ");
					jlPhoneInfo.setBounds(1000, 5, 160, 20);
					getContentPane().add(jlPhoneInfo);	
					jlPhoneInfo.setVisible(false);
				}
				//����豸��Ϣ���
				{				
					JPDevices=new JPanel();
					getContentPane().add(JPDevices);
					JPDevices.setBounds(0, 600, 950, 100);	
				}
				
				jtab = new JTabbedPane();
				getContentPane().add(jtab);
				jtab.setBounds(0, 0, 950, 600);// ����������ǩҳ�Ĵ�С							
				// ��tab��������������
				{
					JPImage = new JPanel();
					jtab.addTab("������", null, JPImage, null);
					//JPImage.setBounds(200, 150, 500, 300);// ������������
					JPImage.setLayout(null);
					{
						{
						JPButton =new JPanel();
						JPImage.add(JPButton);
						JPButton.setBounds(120,70,700,400);
						//GridLayout�Ĳ������У��У�ˮƽ��࣬��ֱ���
						JPButton.setLayout(new GridLayout(2,3,5,5));
						}
						jbConnect=new JButton("�����豸");
						JPButton.add(jbConnect);
						jbConnect.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								jlPhoneInfo.setVisible(true);
								jtDevices=new JTextArea(3,115);	
								
								devicesInfo=HandleEvent.getDevice();
								System.out.println(devicesInfo);
								jtDevices.append(devicesInfo);	
								jtDevices.setLineWrap(true);//�����Զ����й���
								jtDevices.setWrapStyleWord(true);//������в����ֹ���
								//jtDevices.setBackground(new Color(248,253,255));
								JPDevices.add(jtDevices);
							}
						});
						jbConnect.setBorderPainted(false);
						jbConnect.setFocusable(false);//ȡ�����ֿ�
					}
				
					// ���������ǩ����Ӿ���ť��������¼�������	
					{
						jbMakeImge = new JButton("����������");
						JPButton.add(jbMakeImge);
						jbMakeImge.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								//�����ʾ���̵Ķ����ı���
								{
									JOptionPane.showMessageDialog(null,"��ѡ����洢·��");		
									HandleEvent.makeImageWrite();
									if(HandleEvent.makeImageExec()){
										JOptionPane.showMessageDialog(null,"������������......");
									}
									else{
										JOptionPane.showMessageDialog(null,"��������ʧ�ܣ������Գ���˫��makeImage.bat����");
									}
								}							
							}	
						});										
						jbMakeImge.setBorderPainted(false);
						jbMakeImge.setFocusable(false);//ȡ�����ֿ�
						
					}
					// ���������ǩ�����չʾ����ť
					{
						jbDisplayImge = new JButton("�鿴������");
						jbDisplayImge.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								try {
									Process ps = Runtime.getRuntime().exec("����\\ext2explore.exe");
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						});
						JPButton.add(jbDisplayImge);					
						jbDisplayImge.setBorderPainted(false);
						jbDisplayImge.setFocusable(false);//ȡ�����ֿ�
					}
					// ������������������ȡ���ݿⰴť
					{
						jbPullData = new JButton("��ȡ���ݿ��ļ�");
						jbPullData.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								{
									JOptionPane.showMessageDialog(null,"��ѡ�����ݿ�洢·��");
									HandleEvent.pullDataBAT();
									if(HandleEvent.pullDataEXE()){
										JOptionPane.showMessageDialog(null,"���ݿ���ȡ���");				
									}
									else{
										JOptionPane.showMessageDialog(null,"���ݿ���ȡʧ�ܣ������Գ���˫��pullData.bat��ȡ���ݿ��ļ�");
									}
						
								}
	
							}
						});
		
						JPButton.add(jbPullData);	
						jbPullData.setBorderPainted(false);
						jbPullData.setFocusable(false);//ȡ�����ֿ�
					}
					//������У�鰴ť
					{
						jbIntegrity=new JButton("MD5ɢ��");
						jbIntegrity.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								HandleEvent.integrityCheck();
								JOptionPane.showMessageDialog(null,"���ݿ�MD5ɢ����ɣ��������ݿ��ļ�Ŀ¼�²鿴");
							}
							
						});
						JPButton.add(jbIntegrity);		
						jbIntegrity.setBorderPainted(false);
						jbIntegrity.setFocusable(false);//ȡ�����ֿ�
					}
					// ��ӱ������ݿ��ļ���ť
					{
						jbBackupData = new JButton();
						jbBackupData.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								JOptionPane.showMessageDialog(null,"���ݵ����ݿ�Ŀ¼�����ݿ��ļ���ͬһĿ¼");
								HandleEvent.backupData();
								JOptionPane.showMessageDialog(null,"������ɣ�");
							}
						});
						JPButton.add(jbBackupData);
						jbBackupData.setText("�������ݿ��ļ�");
						jbBackupData.setBounds(25, 340, 160, 35);				
						jbBackupData.setBorderPainted(false);
						jbBackupData.setFocusable(false);//ȡ�����ֿ�
					}
				}
				// ��tab�����չʾ����
				{
					JPDisplay = new JPanel();
					jtab.addTab("�鿴֤��", null, JPDisplay, null);
					JPDisplay.setBounds(200, 150, 800, 600);
					JPDisplay.setLayout(null);
					// �ڲ鿴֤�ݽ�����������
					// �鿴���Ű�ť
					{			
						{
							jlDisplaydata = new JLabel();
							JPDisplay.add(jlDisplaydata);
							jlDisplaydata.setText("ѡ��Ҫ�鿴������");
							jlDisplaydata.setBounds(25, 20, 160, 35);											
						}
						jbDisplaySMS = new JButton("����");
						jbDisplaySMS.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								
								// ���Ӷ���SQLite���ݿ�
								SDisplayDataName = disDataPath+"mmssms.db";
								SSql = "select address,person,date,date_sent,body,read,status,type from sms;";
								displaydata = new DisplayDatabase(SDisplayDataName, SSql);
								iClick=1;
								
								// ��ӹ������
								{	
									if(HandleEvent.isMD5OK(SDisplayDataName,iClick)){
										JOptionPane.showMessageDialog(null,"���ݿ�������У��ͨ��!");	
									}
									else{
										JOptionPane.showMessageDialog(null,"���ݿ�������У��δͨ�������ݿ���ܱ�����!");
									}
									JPDisplay.add(getJScrollPane(),null);
									sViewResultCount[0]="���ű�鿴����:"+Integer.toString(JTabDispaly.getRowCount());
									JPDisplay.add(getJCheckBox(iClick),null);
									
								}			
							}
						});
						jbDisplaySMS.setBounds(25, 65, 120, 35);
						jbDisplaySMS.setBorderPainted(false);//ȥ����ť�߿�	
						JPDisplay.add(jbDisplaySMS);
						jbDisplaySMS.setFocusable(false);//ȡ�����ֿ�
						
					}
					// �鿴��ϵ�˰�ť
					{
						jbDisplayContacts = new JButton("��ϵ��");
						jbDisplayContacts.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										SDisplayDataName =disDataPath+"contacts2.db";
										SSql = "select _id,data_version,data1,data4,data12 from data;";
										displaydata = new DisplayDatabase(SDisplayDataName, SSql);
										iClick=2;
										
										// ��ӹ������
										{
											if(HandleEvent.isMD5OK(SDisplayDataName,iClick)){
												JOptionPane.showMessageDialog(null,"���ݿ�������У��ͨ��!");	
											}
											else{
												JOptionPane.showMessageDialog(null,"���ݿ�������У��δͨ�������ݿ���ܱ�����!");
											}
											JPDisplay.add(getJScrollPane(),null);
											sViewResultCount[1]="��ϵ�˱�鿴������"+Integer.toString(JTabDispaly.getRowCount());
										}
										//��Ӳ�ѯ��ѡ��
											JPDisplay.add(getJCheckBox(iClick),null);
									}
								});
						JPDisplay.add(jbDisplayContacts);						
						jbDisplayContacts.setBounds(25, 125, 120, 35);				
						jbDisplayContacts.setBorderPainted(false);//ȥ����ť�߿�
						jbDisplayContacts.setFocusable(false);//ȡ�����ֿ�
					}
					// �鿴ͨ����¼��ť
					{
						jbDisplayCallLog = new JButton("ͨ����¼");
						jbDisplayCallLog.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {										
										SDisplayDataName = disDataPath+"contacts2.db";
										SSql = "select _id,number,presentation,date,duration,type,name,numbertype,numberlabel,geocoded_location,lookup_uri,matched_number,photo_id,formatted_number from calls;";
										displaydata = new DisplayDatabase(SDisplayDataName, SSql);
										iClick=3;
										
										// ��ӹ������
										{
											if(HandleEvent.isMD5OK(SDisplayDataName,iClick)){
												JOptionPane.showMessageDialog(null,"���ݿ�������У��ͨ��!");	
											}
											else{
												JOptionPane.showMessageDialog(null,"���ݿ�������У��δͨ�������ݿ���ܱ�����!");
											}
											JPDisplay.add(getJScrollPane(),null);
											sViewResultCount[2]="ͨ����¼��鿴������"+Integer.toString(JTabDispaly.getRowCount());
										}
										//��Ӳ�ѯ��ѡ��
										{
											JPDisplay.add(getJCheckBox(iClick),null);
										}
									}
								});
						JPDisplay.add(jbDisplayCallLog);						
						jbDisplayCallLog.setBounds(25, 185, 120, 35);
						jbDisplayCallLog.setBorderPainted(false);//ȥ����ť�߿�
						jbDisplayCallLog.setFocusable(false);//ȡ�����ֿ�
					}
					// �鿴�����¼��ť
					{
						jbDisplayBroswer = new JButton("�������ǩ");
						jbDisplayBroswer.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {										
										SDisplayDataName = disDataPath+"bookmark.db";
										SSql = "select * from bookmark;";
										
										displaydata = new DisplayDatabase(SDisplayDataName, SSql);
										iClick=4;

										// ��ӹ������
										{
											if(HandleEvent.isMD5OK(SDisplayDataName,iClick)){
												JOptionPane.showMessageDialog(null,"���ݿ�������У��ͨ��!");	
											}
											else{
												JOptionPane.showMessageDialog(null,"���ݿ�������У��δͨ�������ݿ���ܱ�����!");
											}
											JPDisplay.add(getJScrollPane(),null);
											sViewResultCount[3]="�������ǩ��鿴������"+Integer.toString(JTabDispaly.getRowCount());
											JPDisplay.add(getJCheckBox(iClick),null);
										}
									}
								});
						JPDisplay.add(jbDisplayBroswer);						
						jbDisplayBroswer.setBounds(25, 245, 120, 35);
						jbDisplayBroswer.setBorderPainted(false);//ȥ����ť�߿�
						jbDisplayBroswer.setFocusable(false);//ȡ�����ֿ�
					}
					// �鿴��ý����Ϣ
					{
						jbDisplayExternal = new JButton("��ý����Ϣ");
						jbDisplayExternal.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {										
										SDisplayDataName = disDataPath+"external.db";
										SSql = "select * from thumbnails;";
										displaydata = new DisplayDatabase(
												SDisplayDataName, SSql);
										iClick=5;
										
										// ��ӹ������
										{
											if(HandleEvent.isMD5OK(SDisplayDataName,iClick)){
												JOptionPane.showMessageDialog(null,"���ݿ�������У��ͨ��!");	
											}
											else{
												JOptionPane.showMessageDialog(null,"���ݿ�������У��δͨ�������ݿ���ܱ�����!");
											}
											JPDisplay.add(getJScrollPane(),null);
											sViewResultCount[4]="��ý���鿴������"+Integer.toString(JTabDispaly.getRowCount());
											JPDisplay.add(getJCheckBox(iClick),null);
										}
									}
								});
						JPDisplay.add(jbDisplayExternal);
						jbDisplayExternal.setBounds(25, 315, 120, 35);					
						jbDisplayExternal.setBorderPainted(false);//ȥ����ť�߿�
						jbDisplayExternal.setFocusable(false);//ȡ�����ֿ�
						
					}
					//����鿴�������
					{
						jbOutView=new JButton("д�뱨��");
						jbOutView.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e) {
								
								JOptionPane.showMessageDialog(null,"�������ݱ�����ForensicReport.docx�ĵ���");	
								for(int rowindex:JTabDispaly.getSelectedRows()){						
										outDisDataArray[iClick-1].ColumnCount=displaydata.getcolumncount();
										outDisDataArray[iClick-1].title=displaydata.getTableTitle();
										
										for(int i=1;i<=displaydata.getcolumncount();i++){
											outDisDataArray[iClick-1].tableBody[outDisDataArray[iClick-1].rowCount[iClick-1]][i]=JTabDispaly.getValueAt(rowindex, i);
										}
										outDisDataArray[iClick-1].rowCount[iClick-1]++;
								}
							}
						});
						JPDisplay.add(jbOutView);
						jbOutView.setBounds(665, 10, 120, 35);
						jbOutView.setBorderPainted(false);
						jbOutView.setFocusable(false);//ȡ�����ֿ�
					}
					// ����������ǩjlFind
					{
						jlFindTime = new JLabel();
						JPDisplay.add(jlFindTime);
						jlFindTime.setText("ʱ������");
						jlFindTime.setBounds(25, 400, 120, 35);													
					}
					{
						jlSTime = new JLabel();
						JPDisplay.add(jlSTime);
						jlSTime.setText("��ʼʱ��");
						jlSTime.setBounds(130, 410, 120, 35);													
					}
					{
						jlETime = new JLabel();
						JPDisplay.add(jlETime);
						jlETime.setText("����ʱ��");
						jlETime.setBounds(130, 440, 120, 35);													
					}
					
					// ��ʱ������֤��
					{
						jcbEYear = new JComboBox();
						JPDisplay.add(jcbEYear);
						jcbEYear.setModel(new DefaultComboBoxModel(getModel(start, end)));
						jcbEYear.addItemListener(new ItemListener() {
				            public void itemStateChanged(ItemEvent e) {
				                if (e.getStateChange() == ItemEvent.SELECTED) {
				                    setDay(jcbEYear, jcbEMonth, jcbEDay);
				                }
				            }
				        });
						jcbEYear.setBounds(260, 445, 60, 20);																											
					}
					{
						jcbEMonth = new JComboBox();
						JPDisplay.add(jcbEMonth);
						jcbEMonth.setModel(new DefaultComboBoxModel(getModel(1,12)));
						jcbEMonth.addItemListener(new ItemListener() {
				            public void itemStateChanged(ItemEvent e) {
				                if (e.getStateChange() == ItemEvent.SELECTED) {				               
				                    setDay(jcbEYear, jcbEMonth, jcbEDay);
				                }
				            }
				        });
						jcbEMonth.setBounds(325, 445, 60, 20);											
					}
					{
						jcbEDay = new JComboBox();
						JPDisplay.add(jcbEDay);
						setDay(jcbEYear, jcbEMonth, jcbEDay);
						jcbEDay.setBounds(390, 445, 60, 20);																							
					}
					{
						jcbEHour = new JComboBox();
						JPDisplay.add(jcbEHour);
						jcbEHour.setModel(new DefaultComboBoxModel(getModel(0, 23)));
						jcbEHour.setBounds(455, 445, 60, 20);																							
					}
					{
						jcbEMinute = new JComboBox();
						JPDisplay.add(jcbEMinute);
						jcbEMinute.setModel(new DefaultComboBoxModel(getModel(0, 59)));
						jcbEMinute.setBounds(520, 445, 60, 20);																							
					}
					{
						jcbYear = new JComboBox();
						JPDisplay.add(jcbYear);
						jcbYear.setModel(new DefaultComboBoxModel(getModel(start, end)));
						jcbYear.addItemListener(new ItemListener() {
				            public void itemStateChanged(ItemEvent e) {
				                if (e.getStateChange() == ItemEvent.SELECTED) {
				                    setDay(jcbYear, jcbMonth, jcbDay);
				                }
				            }
				        });
						jcbYear.setBounds(260, 418, 60, 20);																											
					}
					{
						jcbMonth = new JComboBox();
						JPDisplay.add(jcbMonth);
						jcbMonth.setModel(new DefaultComboBoxModel(getModel(1,12)));
						jcbMonth.addItemListener(new ItemListener() {
				            public void itemStateChanged(ItemEvent e) {
				                if (e.getStateChange() == ItemEvent.SELECTED) {
				                    setDay(jcbYear, jcbMonth, jcbDay);
				                }
				            }
				        });
						jcbMonth.setBounds(325, 418, 60, 20);											
					}
					{
						jcbDay = new JComboBox();
						JPDisplay.add(jcbDay);
						setDay(jcbYear, jcbMonth, jcbDay);
						jcbDay.setBounds(390, 418, 60, 20);																							
					}
					{
						jcbHour = new JComboBox();
						JPDisplay.add(jcbHour);
						jcbHour.setModel(new DefaultComboBoxModel(getModel(00, 23)));
						jcbHour.setBounds(455, 418, 60, 20);																							
					}
					{
						jcbMinute = new JComboBox();
						JPDisplay.add(jcbMinute);
						jcbMinute.setModel(new DefaultComboBoxModel(getModel(00, 59)));
						jcbMinute.setBounds(520, 418, 60, 20);																							
					}
					{
						jbOkTime=new JButton("ȷ������");
						JPDisplay.add(jbOkTime);
						jbOkTime.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e) {
								SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
								String findStartTime="";
								String findEndTime="";
								long millionstart = 0;
								long millionend = 0;
								findStartTime+=jcbYear.getSelectedItem()+"-"+jcbMonth.getSelectedItem()+"-"+jcbDay.getSelectedItem()+
										" "+jcbHour.getSelectedItem()+":"+jcbMinute.getSelectedItem();
								findEndTime+=jcbEYear.getSelectedItem()+"-"+jcbEMonth.getSelectedItem()+"-"+jcbEDay.getSelectedItem()+
										" "+jcbEHour.getSelectedItem()+":"+jcbEMinute.getSelectedItem();
								System.out.println("ѡ�е�ʱ����"+findStartTime+"\n����ʱ����"+findEndTime);
								try {
									 millionstart=sdf.parse(findStartTime).getTime();
									 millionend=sdf.parse(findEndTime).getTime();
								} catch (ParseException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								switch(iClick){
								case 1:
									SDisplayDataName = disDataPath+"mmssms.db";
									SSql = "select address,person,date,date_sent,body,read,status,type from sms where date between "+millionstart+" and "+millionend;
									displaydata = new DisplayDatabase(SDisplayDataName, SSql);
									// ��ӹ������
									{										
										JPDisplay.add(getJScrollPane(),null);										
									}
									break;
								case 2:
									break;
								case 3:
									SDisplayDataName = disDataPath+"contacts2.db";
									SSql = "select _id,number,presentation,date,duration,type,name,numbertype,numberlabel,geocoded_location,lookup_uri,matched_number,photo_id,formatted_number from calls where date between "+millionstart+" and "+millionend;
									displaydata = new DisplayDatabase(SDisplayDataName, SSql);									
									// ��ӹ������
									{
										JPDisplay.add(getJScrollPane(),null);	
									}
									break;
								case 4:
									SDisplayDataName = disDataPath+"browser2.db";
									SSql = "select * from search_history where date between "+millionstart+" and "+millionend;
									displaydata = new DisplayDatabase(SDisplayDataName, SSql);									
									// ��ӹ������
									{
										JPDisplay.add(getJScrollPane(),null);	
									}
									break;																		
								}
							}
						});
						jbOkTime.setBounds(670, 425, 120, 35);
						jbOkTime.setBorderPainted(false);
						jbOkTime.setFocusable(false);//ȡ�����ֿ�
					}
					// ���ؼ���������¼
					{
						jlFindKey = new JLabel();
						JPDisplay.add(jlFindKey);
						jlFindKey.setText("����������");
						jlFindKey.setBounds(25, 497, 120, 35);											
					}
					{
						jtfKey = new JTextField();
						JPDisplay.add(jtfKey);
						jtfKey.setText(null);
						jtfKey.setBounds(410, 500, 200, 35);
					}
					// ȷ�ϰ�ť
					{
						jbOKKey = new JButton();
						JPDisplay.add(jbOKKey);
						jbOKKey.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								String fingKey = null;		
								if(jtfKey!=null){
									fingKey=jtfKey.getText();
									jtfKey.setText(null);
								}
								switch(iClick){
								case 1:
									SDisplayDataName = disDataPath+"mmssms.db";
									SSql = "select address,person,date,date_sent,body,read,status,type from sms where ";	
									if(!jcbox1.isSelected()&&!jcbox2.isSelected()){
										 JOptionPane.showMessageDialog(null,"��ѡ��Ҫ��������");
										SSql="select address,person,date,date_sent,body,read,status,type from sms";
									}
									else if(jcbox1.isSelected()){
										SSql+="body like'%"+fingKey+"%';";
									}
									else if(jcbox2.isSelected()){
										SSql+="address like '%"+fingKey+"%';";	
									}
									else{
										SSql+="body like'%"+fingKey+"%' or address like '%"+fingKey+"%';";
									}
																					
									displaydata = new DisplayDatabase(SDisplayDataName, SSql);
									// ��ӹ������
									{										
										JPDisplay.add(getJScrollPane(),null);										
									}
									break;
								case 2:
									SDisplayDataName = disDataPath+"contacts2.db";
									SSql = "select _id,data_version,data1,data4,data12 from data where ";
									
									if(!jcbox1.isSelected()&&!jcbox2.isSelected()&&!jcbox3.isSelected()){
										JOptionPane.showMessageDialog(null,"��ѡ��Ҫ��������");
										SSql="select _id,data_version,data1,data4,data12 from data";
									}
									else if(jcbox1.isSelected()){
										SSql+="data1 like'%"+fingKey+"%';";
									}
									else if(jcbox2.isSelected()){
										SSql+="data4 like '%"+fingKey+"%';";	
									}
									else if(jcbox3.isSelected()){
										SSql+="data12 like '%"+fingKey+"%';";	
									}
									else{
										SSql+="data1 like'%"+fingKey+"%' or data4 like '%"+fingKey+"%' or data12 like '%"+fingKey+"%';";
									}
									
									
									displaydata = new DisplayDatabase(SDisplayDataName, SSql);									
									// ��ӹ������
									{
										JPDisplay.add(getJScrollPane(),null);	
									}
									break;
								case 3:
									SDisplayDataName = disDataPath+"contacts2.db";
									SSql = "select _id,number,presentation,date,duration,type,name,numbertype,numberlabel,geocoded_location,lookup_uri,matched_number,photo_id,formatted_number from calls where ";
									if(!jcbox1.isSelected()&&!jcbox2.isSelected()){	
										 JOptionPane.showMessageDialog(null,"��ѡ��Ҫ��������");
										SSql="select _id,number,presentation,date,duration,type,name,numbertype,numberlabel,geocoded_location,lookup_uri,matched_number,photo_id,formatted_number from calls";
									}
									else if(jcbox1.isSelected()){
										SSql+="number like'%"+fingKey+"%';";
									}
									else if(jcbox2.isSelected()){
										SSql+="name like '%"+fingKey+"%';";	
									}
									else{
										SSql+="number like'%"+fingKey+"%' or name like '%"+fingKey+"%';";
									}
									displaydata = new DisplayDatabase(SDisplayDataName, SSql);									
									// ��ӹ������
									{
										JPDisplay.add(getJScrollPane(),null);	
									}
									break;
								case 4:
									SDisplayDataName = disDataPath+"bookmark.db";
									SSql = "select * from bookmark where ";	
									if(!jcbox1.isSelected()&&!jcbox2.isSelected()){
										JOptionPane.showMessageDialog(null,"��ѡ��Ҫ��������");
										SSql="select * from bookmark";
									}
									else if(jcbox1.isSelected()){
										SSql+="title like'%"+fingKey+"%';";
									}
									else if(jcbox2.isSelected()){
										SSql+="url like '%"+fingKey+"%';";	
									}
									else{
										SSql+="title like'%"+fingKey+"%' or url like '%"+fingKey+"%';";
									}
																					
									displaydata = new DisplayDatabase(SDisplayDataName, SSql);
									// ��ӹ������
									{										
										JPDisplay.add(getJScrollPane(),null);										
									}
									break;
								case 5:
									SDisplayDataName = disDataPath+"external.db";
									SSql = "select * from thumbnails where ";	
									if(!jcbox1.isSelected()){
										JOptionPane joption=new JOptionPane();
										joption.showMessageDialog(null,"��ѡ��Ҫ��������");
										SSql="select * from thumbnails";
									}
									else if(jcbox1.isSelected()){
										SSql+="_data like'%"+fingKey+"%';";
									}											
									displaydata = new DisplayDatabase(SDisplayDataName, SSql);
									// ��ӹ������
									{										
										JPDisplay.add(getJScrollPane(),null);										
									}
									break;
																					
								}
							}
						});	
						jbOKKey.setText("ȷ������");
						jbOKKey.setBounds(670, 500, 120, 35);
						jbOKKey.setBorderPainted(false);
						jbOKKey.setFocusable(false);//ȡ�����ֿ�
					}
				}
				// ֤�ݻָ�����
				{
					JPRecovery = new JPanel();
					jtab.addTab("���ݿ�ָ�", null, JPRecovery, null);
					JPRecovery.setBounds(200, 150, 800, 500);
					JPRecovery.setLayout(null);
					
					// �ڻָ�֤�ݽ�����������
					{
						jlRecoverdata1 = new JLabel();
						JPRecovery.add(jlRecoverdata1);
						jlRecoverdata1.setText("ѡ��ָ�������");
						jlRecoverdata1.setBounds(25, 20, 160, 35);											
					}
					// �ָ����ű�ť
					{
						jbRecoverSMS1 = new JButton();
						jbRecoverSMS1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								
								iRecClick=1;
								RecoveryDatabase recoverdata = new RecoveryDatabase(recDataPath+"mmssms.db",
										"select * from sqlite_master where name='sms' and tbl_name='sms';");
								
								if(HandleEvent.isMD5OK(recDataPath+"mmssms.db",iRecClick)){
									JOptionPane.showMessageDialog(null,"�������ݿ�������У��ͨ��,���ڳ��Իָ����ű�!");	
								}
								else{
									JOptionPane.showMessageDialog(null,"�������ݿ�������У��δͨ��,�����п��ܱ��۸ģ����ڳ��Իָ����ű�!!");	
								}
								
								SResultList=recoverdata.recover();
								iGarbledCount=HandleEvent.saveRecData(SResultList,"RecSMS");
								JOptionPane.showMessageDialog(null, "�ָ����ݴ������ݿ����");
								SDisplayDataName = "RecData.db";
								SSql = "select address,person,date,date_sent,body,read,status,type from RecSMS;";
								displaydata = new DisplayDatabase(SDisplayDataName, SSql);
								// ��Ӷ����ı���
								{
									JPRecovery.add(getJScrollRecPanel(),null);	
									JPRecovery.add(getRecCount(),null);	
								}
							}
						});
						JPRecovery.add(jbRecoverSMS1);
						jbRecoverSMS1.setText("���ű�");
						jbRecoverSMS1.setBounds(25, 100, 120, 35);			
						jbRecoverSMS1.setBorderPainted(false);//ȥ����ť�߿�	
						jbRecoverSMS1.setFocusable(false);//ȡ�����ֿ�
					}
					// �ָ���ϵ�˱�ť
					{
						jbRecoverContacts1 = new JButton();
						jbRecoverContacts1.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										iRecClick=2;
										RecoveryDatabase recoverdata = new RecoveryDatabase(
												recDataPath+"contacts2.db",
												"select * from sqlite_master where name='data' and tbl_name='data';");	
										if(HandleEvent.isMD5OK(recDataPath+"contacts2.db",iRecClick)){
											JOptionPane.showMessageDialog(null,"��ϵ�����ݿ�������У��ͨ��,���ڳ��Իָ���ϵ�˱�!");	
										}
										else{
											JOptionPane.showMessageDialog(null,"��ϵ�����ݿ�������У��δͨ��,�����п��ܱ��۸ģ����ڳ��Իָ���ϵ�˱�!!");	
										}
										SResultList=	recoverdata.recover();	
										iGarbledCount=HandleEvent.saveRecData(SResultList,"RecContacts");
										JOptionPane.showMessageDialog(null, "�ָ����ݴ������ݿ����");
										SDisplayDataName = "RecData.db";
										SSql = "select _id,data_version,data1,data4,data12 from RecContacts;";
										displaydata = new DisplayDatabase(SDisplayDataName, SSql);
										// ��Ӷ����ı���
										
										{
											JPRecovery.add(getJScrollRecPanel(),null);																							
											JPRecovery.add(getRecCount(),null);	
										}
									}
								});
						JPRecovery.add(jbRecoverContacts1);
						jbRecoverContacts1.setText("��ϵ�˱�");
						jbRecoverContacts1.setBounds(25, 160, 120, 35);				
						jbRecoverContacts1.setBorderPainted(false);//ȥ����ť�߿�
						jbRecoverContacts1.setFocusable(false);//ȡ�����ֿ�
					}
					// �ָ�ͨ����¼��ť
					{
						jbRecoverCalls1 = new JButton();
						jbRecoverCalls1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								iRecClick=3;
								RecoveryDatabase recoverdata = new RecoveryDatabase(
										recDataPath+"contacts2.db",
										"select * from sqlite_master where name='calls' and tbl_name='calls';");
								if(HandleEvent.isMD5OK(recDataPath+"contacts2.db",iRecClick)){
									JOptionPane.showMessageDialog(null,"ͨ����¼���ݿ�������У��ͨ��,���ڳ��Իָ�ͨ����¼��!");	
								}
								else{
									JOptionPane.showMessageDialog(null,"ͨ����¼���ݿ�������У��δͨ��,�����п��ܱ��۸ģ����ڳ��Իָ�ͨ����¼��!!");	
								}
								SResultList=recoverdata.recover();
								iGarbledCount=HandleEvent.saveRecData(SResultList,"RecCalls");
								JOptionPane.showMessageDialog(null, "�ָ����ݴ������ݿ����");
								SDisplayDataName = "RecData.db";
								SSql = "select _id,number,presentation,date,duration,type,name,numbertype,numberlabel,geocoded_location,lookup_uri,matched_number,photo_id,formatted_number from RecCalls;";
								displaydata = new DisplayDatabase(SDisplayDataName, SSql);	
								// ��Ӷ����ı���
								{
									JPRecovery.add(getJScrollRecPanel(),null);																							
									JPRecovery.add(getRecCount(),null);	
								}
							}
						});
						JPRecovery.add(jbRecoverCalls1);
						jbRecoverCalls1.setText("ͨ����¼��");
						jbRecoverCalls1.setBounds(25, 220, 120, 35);
						jbRecoverCalls1.setBorderPainted(false);//ȥ����ť�߿�	
						jbRecoverCalls1.setFocusable(false);//ȡ�����ֿ�
					}
					// �ָ������¼��ť
					{
						jbRecoverBrowsers1 = new JButton();
						jbRecoverBrowsers1
								.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										iRecClick=4;
										RecoveryDatabase recoverdata = new RecoveryDatabase(
												recDataPath+"bookmark.db",
												"select * from sqlite_master where name='bookmark' and tbl_name='bookmark';");				
										if(HandleEvent.isMD5OK(recDataPath+"bookmark.db",iRecClick)){
											JOptionPane.showMessageDialog(null,"�������ǩ���ݿ�������У��ͨ��,���ڳ��Իָ�ͨ����ǩ��!");	
										}
										else{
											JOptionPane.showMessageDialog(null,"�������ǩ���ݿ�������У��δͨ��,�����п��ܱ��۸ģ����ڳ��Իָ�ͨ����ǩ��!!");	
										}
										SResultList=	recoverdata.recover();	
										iGarbledCount=HandleEvent.saveRecData(SResultList,"RecBookMarks");
										JOptionPane.showMessageDialog(null, "�ָ����ݴ������ݿ����");
										SDisplayDataName = "RecData.db";
										SSql = "select * from RecBookMarks;";
										displaydata = new DisplayDatabase(SDisplayDataName, SSql);	
										// ��Ӷ����ı���
										
										{
											JPRecovery.add(getJScrollRecPanel(),null);																							
											JPRecovery.add(getRecCount(),null);	
										}
									}
								});
						JPRecovery.add(jbRecoverBrowsers1);
						jbRecoverBrowsers1.setText("�������ǩ");						
						jbRecoverBrowsers1.setBounds(25, 280, 120, 35);
						jbRecoverBrowsers1.setBorderPainted(false);//ȥ����ť�߿�
						jbRecoverBrowsers1.setFocusable(false);//ȡ�����ֿ�
						
					}
					// �ָ�䯶�ý���¼��ť
					{
						jbRecoverExternals1 = new JButton();
						jbRecoverExternals1
								.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										iRecClick=5;
										RecoveryDatabase recoverdata = new RecoveryDatabase(
												recDataPath+"external.db",
												"select * from sqlite_master where name='thumbnails' and tbl_name='thumbnails';");									
										if(HandleEvent.isMD5OK(recDataPath+"external.db",iRecClick)){
											JOptionPane.showMessageDialog(null,"��ý�����ݿ�������У��ͨ��,���ڳ��Իָ�ͨ��ͼƬ��!");	
										}
										else{
											JOptionPane.showMessageDialog(null,"��ý�����ݿ�������У��δͨ��,�����п��ܱ��۸ģ����ڳ��Իָ�ͨ��ͼƬ��!!");	
										}
										SResultList=recoverdata.recover();	
										iGarbledCount=HandleEvent.saveRecData(SResultList,"RecThumbnails");
										JOptionPane.showMessageDialog(null, "�ָ����ݴ������ݿ����");
										SDisplayDataName = "RecData.db";
										SSql = "select * from RecThumbnails;";
										displaydata = new DisplayDatabase(SDisplayDataName, SSql);	
										// ��Ӷ����ı���
										
										{
											JPRecovery.add(getJScrollRecPanel(),null);																							
											JPRecovery.add(getRecCount(),null);	
										}
									}
								});
						JPRecovery.add(jbRecoverExternals1);
						jbRecoverExternals1.setText("��ý���");
						jbRecoverExternals1.setBounds(25, 340, 120, 35);
						jbRecoverExternals1.setBorderPainted(false);//ȥ����ť�߿�
						jbRecoverExternals1.setFocusable(false);//ȡ�����ֿ�
					}
					
					//����ָ��������
					{
						jbOutRec=new JButton("д�뱨��");
						jbOutRec.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e) {
								
								JOptionPane.showMessageDialog(null,"�������ݱ�����ForensicReport.docx�ĵ���");	
								for(int rowindex:JTabDispaly.getSelectedRows()){						
									outRecDataArray[iRecClick-1].ColumnCount=displaydata.getcolumncount();
									outRecDataArray[iRecClick-1].title=displaydata.getTableTitle();
										
										for(int i=1;i<=displaydata.getcolumncount();i++){
											outRecDataArray[iRecClick-1].tableBody[outRecDataArray[iRecClick-1].rowCount[iRecClick-1]][i]=JTabDispaly.getValueAt(rowindex, i);
										}
										outRecDataArray[iRecClick-1].rowCount[iRecClick-1]++;
								}
							}
						});
						JPRecovery.add(jbOutRec);
						jbOutRec.setBounds(665, 10, 120, 35);
						jbOutRec.setBorderPainted(false);
						jbOutRec.setFocusable(false);//ȡ�����ֿ�
					}
					
					//�������水ť
					{
						jbOutputDataButton1=new JButton("��������");
						jbOutputDataButton1.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								WriteDocx writeReport=new WriteDocx(devicesInfo,outDisDataArray,outRecDataArray,sViewResultCount,sRecResultCount);
								writeReport.WriteData();
								JOptionPane.showMessageDialog(null,"����������ɣ�");
							}
						});
						JPRecovery.add(jbOutputDataButton1);
						jbOutputDataButton1.setBounds(650,500,120,35);
						jbOutputDataButton1.setBorderPainted(false);//ȥ����ť�߿�
						jbOutputDataButton1.setFocusable(false);//ȡ�����ֿ�
					}
			}
		}			
			pack();// �����趨������ô��ڴ�С
			this.setSize(950, 700);// ���ڵĴ�С
			this.setBackground(new Color(235,245,223));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * �ú������ò鿴���ݽ��������ʱ���е�����
	 */
	private void setDay(JComboBox jcbYear, JComboBox jcbMonth, JComboBox jcbDay) {
		int iYear = Integer.parseInt((String) jcbYear.getSelectedItem());
		int iMonth = Integer.parseInt((String) jcbMonth.getSelectedItem());
		Calendar Ccal = Calendar.getInstance();
		Ccal.set(Calendar.YEAR,iYear);
		Ccal.set(Calendar.MONTH, iMonth - 1);
		int iDays = Ccal.getActualMaximum(Calendar.DATE);
		jcbDay.setModel(new DefaultComboBoxModel(getModel(1, iDays)));
	}

	/*
	 * �ú�������ʱ��
	 */
	private String[] getModel(int start, int end) {
		String[] Sm = new String[end - start + 1];
		for (int i = 0; i < Sm.length; i++) {
			if((i + start)<10&&start<1970){
				Sm[i] = String.valueOf(i + start);
				Sm[i]="0"+Sm[i];
			}
			else{
				Sm[i] = String.valueOf(i + start);
			}
			
		}
		return Sm;
	}
	/*
	 * �ú������ò鿴���ݽ�����ʾ�������
	 * ResultSet RSQuery����ѯ�������
	 * Object[] title����ͷҲ�����ֶ���
	 * int iColumn���������
	 */
	private void getTableModel(ResultSet RSQuery, Object[] title, int iColumn,int iRow) {
		//�����ݿ��в�����ĺ�����תΪ��ͨʱ���ʽ
		SimpleDateFormat sdateformate =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DTablemodel.addColumn("�Ƿ񵼳�");
		for (int i = 1; i <= iColumn; i++) {
			DTablemodel.addColumn(title[i]);
		}
			try {
				while (RSQuery.next()) {
					Vector<Object> VInfo = new Vector<Object>();
					//��ÿһ�еĵ�һ������Ϊ�������ͣ������ж��Ƿ񵼳���������
					VInfo.addElement(new Boolean(false));
					for (int i = 2; i <= iColumn+1; i++) {
						// getObject()�����õ�ĳ��ĳ�е�ĳ������
						if(title[i-1].equals("date")||title[i-1].equals("date_sent")||title[i-1].equals("create_time")){
							Object tempEle= RSQuery.getObject(i-1);
							if(tempEle!= null){
								VInfo.addElement(sdateformate.format(new Date((Long)tempEle)));
							}
							else{
								VInfo.addElement(null);//����ָ�����������ʱ��Ϊ0�����
							}
										
						}
						else{
						VInfo.addElement(RSQuery.getObject(i-1));
						}	
					}
					DTablemodel.addRow(VInfo);
				}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private JScrollPane getJScrollPane(){
		switch(iClick){
		case 1:outDisDataArray[0].tableName="3.1���ű��������:";
				break;
		case 2:outDisDataArray[1].tableName="3.2��ϵ�˱��������:";
		break;
		case 3:outDisDataArray[2].tableName="3.3ͨ����¼���������:";
		break;
		case 4:outDisDataArray[3].tableName="3.4�����¼���������:";
		break;
		case 5:outDisDataArray[4].tableName="3.5��ý����������:";
		break;		
		}
		//ÿgetpanelһ�ξ����´���һ������������½�һ��outDataArray
		outDisDataArray[iClick-1].tableBody=new Object[displaydata.getrowcount()][displaydata.getcolumncount()+1];
		outDisDataArray[iClick-1].rowCount[iClick-1]=0;
		if (JscpDisplay != null) {
			JscpDisplay.setVisible(false);
		}
		JscpDisplay = new JScrollPane();
		JsBar=new JScrollBar();
		JsBar=JscpDisplay.getHorizontalScrollBar();
		// ��ӱ��
		{
			DTablemodel = new DefaultTableModel();
			JTabDispaly = new JTable();
			JscpDisplay.setViewportView(JTabDispaly);
			
			getTableModel(displaydata.displayTable(),displaydata.getTableTitle(),displaydata.getcolumncount(),displaydata.getrowcount());
			displaydata.closeDatabase();//��ѯ�����ݿ��ر�����
			JTabDispaly.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // �����п��ɱ䣬�Զ���ʾˮƽ������
			JTabDispaly.setModel(DTablemodel);	
			//���õ�Ԫ����Ⱦ��ʵ�ֱ�������checkbox
			TableColumnModel tcm=JTabDispaly.getColumnModel();
			tcm.getColumn(0).setCellRenderer(new TableCellRenderer() {	
				public Component getTableCellRendererComponent(JTable table, Object value,
						boolean isSelected, boolean hasFocus, int row, int column) {
					JCheckBox Jck=new JCheckBox();
					Jck.setSelected(isSelected);//ʹ���н�����ж�Ӧ�ĸ�ѡ��ѡ��
					Jck.setHorizontalAlignment((int)0.5f);//���ø�ѡ�������ʾ
					Jck.setBackground(new Color(208,232,245));
					return Jck;
				}
			});
			
			//���������ʵ��
			RowSorter <javax.swing.table.TableModel> sorter =new TableRowSorter <javax.swing.table.TableModel> (JTabDispaly.getModel());
			JTabDispaly.setRowSorter(sorter);
		}		
		JscpDisplay.setBounds(165, 60, 700, 320);
		return JscpDisplay;	
	}
	
	
	
	private JScrollPane getJScrollRecPanel(){
		switch(iRecClick){
		case 1:
			outRecDataArray[0].tableName="5.1�ָ����ű��������:";
			sRecResultCount[0]="���ű��ҵ����ɿ飺"+RecoveryDatabase.FreeBlockCount+"\n"+"���ű����ջָ���¼����"+RecoveryDatabase.RecFreeblockCount+"��������ļ�¼����"+iGarbledCount;
			
			break;
		case 2:
			outRecDataArray[1].tableName="5.2�ָ���ϵ�˱��������:";
			sRecResultCount[1]="��ϵ���ҵ����ɿ飺"+RecoveryDatabase.FreeBlockCount+"\n"+"��ϵ�˱����ջָ���¼����"+RecoveryDatabase.RecFreeblockCount+"��������ļ�¼����"+iGarbledCount;
			break;
		case 3:
			outRecDataArray[2].tableName="5.3�ָ�ͨ����¼���������:";
			sRecResultCount[2]="ͨ����¼���ҵ����ɿ飺"+RecoveryDatabase.FreeBlockCount+"\n"+"ͨ����¼�����ջָ���¼����"+RecoveryDatabase.RecFreeblockCount+"��������ļ�¼����"+iGarbledCount;
			break;
		case 4:
			outRecDataArray[3].tableName="5.4�ָ��������ǩ���������:";
			sRecResultCount[3]="�������ǩ���ҵ����ɿ飺"+RecoveryDatabase.FreeBlockCount+"\n"+"�������ǩ�����ջָ���¼����"+RecoveryDatabase.RecFreeblockCount+"��������ļ�¼����"+iGarbledCount;
			break;
		case 5:
			outRecDataArray[4].tableName="5.5�ָ���ý����������:";
			sRecResultCount[4]="��ý����ҵ����ɿ飺"+RecoveryDatabase.FreeBlockCount+"\n"+"��ý������ջָ���¼����"+RecoveryDatabase.RecFreeblockCount+"��������ļ�¼����"+iGarbledCount;
			break;		
		}
		//ÿgetpanelһ�ξ����´���һ������������½�һ��outDataArray
		System.out.println("�ָ���ť�����click��ֵ��"+iRecClick);
		outRecDataArray[iRecClick-1].tableBody=new Object[displaydata.getrowcount()][displaydata.getcolumncount()+1];
		outRecDataArray[iRecClick-1].rowCount[iRecClick-1]=0;
		if (JscpDisplay != null) {
			JscpDisplay.setVisible(false);
		}
		JscpDisplay = new JScrollPane();
		JsBar=new JScrollBar();
		JsBar=JscpDisplay.getHorizontalScrollBar();
		// ��ӱ��
		{
			DTablemodel = new DefaultTableModel();
			JTabDispaly = new JTable();
			JscpDisplay.setViewportView(JTabDispaly);
			
			getTableModel(displaydata.displayTable(),displaydata.getTableTitle(),displaydata.getcolumncount(),displaydata.getrowcount());
			
			JTabDispaly.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // �����п��ɱ䣬�Զ���ʾˮƽ������
			
			JTabDispaly.setModel(DTablemodel);	
			TableColumnModel tcm=JTabDispaly.getColumnModel();
			tcm.getColumn(0).setCellRenderer(new TableCellRenderer() {	
				public Component getTableCellRendererComponent(JTable table, Object value,
						boolean isSelected, boolean hasFocus, int row, int column) {
					JCheckBox Jck=new JCheckBox();
					Jck.setSelected(isSelected);//ʹ���н�����ж�Ӧ�ĸ�ѡ��ѡ��
					Jck.setHorizontalAlignment((int)0.5f);//���ø�ѡ�������ʾ
					Jck.setBackground(new Color(208,232,245));
					return Jck;
				}
			});
			//���������ʵ��
			RowSorter <javax.swing.table.TableModel> sorter =new TableRowSorter <javax.swing.table.TableModel> (JTabDispaly.getModel());
			JTabDispaly.setRowSorter(sorter);
			displaydata.closeDatabase();//��ѯ�����ݿ��ر�����
		}		
		JscpDisplay.setBounds(165, 60, 700, 320);
		return JscpDisplay;	
	}

	private  JPanel getJCheckBox(int iClick) {
		if (jpcbox != null) {
			jpcbox.setVisible(false);
		}
		
		jpcbox=new JPanel();
			switch(iClick){
			case 1:
			{
				jcbox1=new JCheckBox("body");
				jcbox2=new JCheckBox("address");
				jcbox1.setFocusable(false);//ȡ�����ֿ�
				jcbox2.setFocusable(false);//ȡ�����ֿ�
				jpcbox.add(jcbox1);
				jpcbox.add(jcbox2);		
			}		
				break;
			case 2:
			{
				jcbox1=new JCheckBox("data1");
				jcbox2=new JCheckBox("data4");
				jcbox3=new JCheckBox("data12");
				jcbox1.setFocusable(false);//ȡ�����ֿ�
				jcbox2.setFocusable(false);//ȡ�����ֿ�
				jcbox3.setFocusable(false);//ȡ�����ֿ�
				jpcbox.add(jcbox1);
				jpcbox.add(jcbox2);	
				jpcbox.add(jcbox3);	
				
			}
				break;
			case 3:
			{
				jcbox1=new JCheckBox("number");
				jcbox2=new JCheckBox("name");
				jcbox1.setFocusable(false);//ȡ�����ֿ�
				jcbox2.setFocusable(false);//ȡ�����ֿ�
				jpcbox.add(jcbox1);
				jpcbox.add(jcbox2);	
				
			}
				break;	
			case 4:
			{
				jcbox1=new JCheckBox("title");
				jcbox2=new JCheckBox("url");
				jcbox1.setFocusable(false);//ȡ�����ֿ�
				jcbox2.setFocusable(false);//ȡ�����ֿ�
				jpcbox.add(jcbox1);
				jpcbox.add(jcbox2);	
				
			}
				break;	
			case 5:
			{
				jcbox1=new JCheckBox("_data");
				jcbox1.setFocusable(false);//ȡ�����ֿ�			
				jpcbox.add(jcbox1);
				
			}
				break;	
		}
		jpcbox.setBounds(110,495,300,35);
		jpcbox.setVisible(true);
		return jpcbox;
	}
	private JPanel getRecCount(){
		if(JPCountlabel!=null){
			JPCountlabel.setVisible(false);
		}
		JPCountlabel=new JPanel();
		{	
			switch(iRecClick){
			case 1:
				jlCountAll=new JLabel("���ű����ҵ����ɿ������"+RecoveryDatabase.FreeBlockCount+" ");
				break;
			case 2:
				jlCountAll=new JLabel("��ϵ�˱����ҵ����ɿ������"+RecoveryDatabase.FreeBlockCount+" ");
				break;
			case 3:
				jlCountAll=new JLabel("ͨ����¼�����ҵ����ɿ������"+RecoveryDatabase.FreeBlockCount+" ");
				break;
			case 4:
				jlCountAll=new JLabel("�������ǩ�����ҵ����ɿ������"+RecoveryDatabase.FreeBlockCount+" ");
				break;
			case 5:
				jlCountAll=new JLabel("��ý������ҵ����ɿ������"+RecoveryDatabase.FreeBlockCount+" ");
				break;
		}
			JPCountlabel.add(jlCountAll);
			jlCountAll.setBounds(0,0,700,35);
			jlCountAll.setFocusable(false);//ȡ�����ֿ�
		}
		{
			jlCountRec=new JLabel("�ָ��ļ�¼����"+RecoveryDatabase.RecFreeblockCount+"  ��������ļ�¼����"+iGarbledCount);
			JPCountlabel.add(jlCountRec);
			jlCountRec.setBounds(0,40,700,35);
			jlCountRec.setFocusable(false);//ȡ�����ֿ�
		}
		JPCountlabel.setBounds(270,400,700,80);
		return JPCountlabel;
	}
}
