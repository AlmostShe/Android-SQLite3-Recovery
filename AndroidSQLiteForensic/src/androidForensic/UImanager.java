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
	
	private static  String  disDataPath = "D:\\用户目录\\我的文档\\数据库文件\\";
	private static  String  recDataPath = "D:\\用户目录\\我的文档\\数据库文件备份\\";
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
	// 物理镜像界面组件
	JButton jbMakeImge;
	JButton jbDisplayImge;
	//JTextArea jtaImage;
	JButton jbPullData;
	JButton jbBackupData;
	JButton jbConnect;
	JButton jbIntegrity;

	// 查看证据界面组件
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
	
	

	// 恢复证据界面组件
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
		ComponentStyle.loadStyle();//将组建的个性化设置加载进来
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
			this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// 退出应用程序默认窗口关闭操作
			getContentPane().setLayout(null);// 初始化容器
			this.setResizable(false);// 窗体不可自由改变大小
			this.setBounds(200, 150, 600, 500);// 左边上边距离，宽度，高度，窗体的大小
			this.setBackground(new Color(235,245,224));
			this.setTitle("安卓智能终端综合分析层取证系统");
			{
				{
					jlPhoneInfo=new JLabel("连接的设备信息");
					jlPhoneInfo.setBounds(1000, 5, 160, 20);
					getContentPane().add(jlPhoneInfo);	
					jlPhoneInfo.setVisible(false);
				}
				//添加设备信息面板
				{				
					JPDevices=new JPanel();
					getContentPane().add(JPDevices);
					JPDevices.setBounds(0, 600, 950, 100);	
				}
				
				jtab = new JTabbedPane();
				getContentPane().add(jtab);
				jtab.setBounds(0, 0, 950, 600);// 设置三个标签页的大小							
				// 在tab上添加物理镜像界面
				{
					JPImage = new JPanel();
					jtab.addTab("物理镜像", null, JPImage, null);
					//JPImage.setBounds(200, 150, 500, 300);// 镜像面板的设置
					JPImage.setLayout(null);
					{
						{
						JPButton =new JPanel();
						JPImage.add(JPButton);
						JPButton.setBounds(120,70,700,400);
						//GridLayout的参数：行，列，水平间距，垂直间距
						JPButton.setLayout(new GridLayout(2,3,5,5));
						}
						jbConnect=new JButton("连接设备");
						JPButton.add(jbConnect);
						jbConnect.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								jlPhoneInfo.setVisible(true);
								jtDevices=new JTextArea(3,115);	
								
								devicesInfo=HandleEvent.getDevice();
								System.out.println(devicesInfo);
								jtDevices.append(devicesInfo);	
								jtDevices.setLineWrap(true);//设置自动换行功能
								jtDevices.setWrapStyleWord(true);//激活断行不断字功能
								//jtDevices.setBackground(new Color(248,253,255));
								JPDevices.add(jtDevices);
							}
						});
						jbConnect.setBorderPainted(false);
						jbConnect.setFocusable(false);//取消文字框
					}
				
					// 在物理镜像标签上添加镜像按钮，并添加事件处理函数	
					{
						jbMakeImge = new JButton("制作物理镜像");
						JPButton.add(jbMakeImge);
						jbMakeImge.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								//添加显示过程的多行文本框
								{
									JOptionPane.showMessageDialog(null,"请选择镜像存储路径");		
									HandleEvent.makeImageWrite();
									if(HandleEvent.makeImageExec()){
										JOptionPane.showMessageDialog(null,"镜像正在制作......");
									}
									else{
										JOptionPane.showMessageDialog(null,"镜像制作失败，您可以尝试双击makeImage.bat制作");
									}
								}							
							}	
						});										
						jbMakeImge.setBorderPainted(false);
						jbMakeImge.setFocusable(false);//取消文字框
						
					}
					// 在物理镜像标签上添加展示镜像按钮
					{
						jbDisplayImge = new JButton("查看物理镜像");
						jbDisplayImge.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								try {
									Process ps = Runtime.getRuntime().exec("工具\\ext2explore.exe");
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						});
						JPButton.add(jbDisplayImge);					
						jbDisplayImge.setBorderPainted(false);
						jbDisplayImge.setFocusable(false);//取消文字框
					}
					// 在物理镜像界面上添加拉取数据库按钮
					{
						jbPullData = new JButton("提取数据库文件");
						jbPullData.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								{
									JOptionPane.showMessageDialog(null,"请选择数据库存储路径");
									HandleEvent.pullDataBAT();
									if(HandleEvent.pullDataEXE()){
										JOptionPane.showMessageDialog(null,"数据库提取完成");				
									}
									else{
										JOptionPane.showMessageDialog(null,"数据库提取失败，您可以尝试双击pullData.bat提取数据库文件");
									}
						
								}
	
							}
						});
		
						JPButton.add(jbPullData);	
						jbPullData.setBorderPainted(false);
						jbPullData.setFocusable(false);//取消文字框
					}
					//完整性校验按钮
					{
						jbIntegrity=new JButton("MD5散列");
						jbIntegrity.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								HandleEvent.integrityCheck();
								JOptionPane.showMessageDialog(null,"数据库MD5散列完成，请在数据库文件目录下查看");
							}
							
						});
						JPButton.add(jbIntegrity);		
						jbIntegrity.setBorderPainted(false);
						jbIntegrity.setFocusable(false);//取消文字框
					}
					// 添加备份数据库文件按钮
					{
						jbBackupData = new JButton();
						jbBackupData.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								JOptionPane.showMessageDialog(null,"备份的数据库目录与数据库文件在同一目录");
								HandleEvent.backupData();
								JOptionPane.showMessageDialog(null,"备份完成！");
							}
						});
						JPButton.add(jbBackupData);
						jbBackupData.setText("备份数据库文件");
						jbBackupData.setBounds(25, 340, 160, 35);				
						jbBackupData.setBorderPainted(false);
						jbBackupData.setFocusable(false);//取消文字框
					}
				}
				// 在tab上添加展示界面
				{
					JPDisplay = new JPanel();
					jtab.addTab("查看证据", null, JPDisplay, null);
					JPDisplay.setBounds(200, 150, 800, 600);
					JPDisplay.setLayout(null);
					// 在查看证据界面中添加组件
					// 查看短信按钮
					{			
						{
							jlDisplaydata = new JLabel();
							JPDisplay.add(jlDisplaydata);
							jlDisplaydata.setText("选择要查看的数据");
							jlDisplaydata.setBounds(25, 20, 160, 35);											
						}
						jbDisplaySMS = new JButton("短信");
						jbDisplaySMS.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								
								// 连接短信SQLite数据库
								SDisplayDataName = disDataPath+"mmssms.db";
								SSql = "select address,person,date,date_sent,body,read,status,type from sms;";
								displaydata = new DisplayDatabase(SDisplayDataName, SSql);
								iClick=1;
								
								// 添加滚动面板
								{	
									if(HandleEvent.isMD5OK(SDisplayDataName,iClick)){
										JOptionPane.showMessageDialog(null,"数据库完整性校验通过!");	
									}
									else{
										JOptionPane.showMessageDialog(null,"数据库完整性校验未通过，数据库可能被更改!");
									}
									JPDisplay.add(getJScrollPane(),null);
									sViewResultCount[0]="短信表查看条数:"+Integer.toString(JTabDispaly.getRowCount());
									JPDisplay.add(getJCheckBox(iClick),null);
									
								}			
							}
						});
						jbDisplaySMS.setBounds(25, 65, 120, 35);
						jbDisplaySMS.setBorderPainted(false);//去掉按钮边框	
						JPDisplay.add(jbDisplaySMS);
						jbDisplaySMS.setFocusable(false);//取消文字框
						
					}
					// 查看联系人按钮
					{
						jbDisplayContacts = new JButton("联系人");
						jbDisplayContacts.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										SDisplayDataName =disDataPath+"contacts2.db";
										SSql = "select _id,data_version,data1,data4,data12 from data;";
										displaydata = new DisplayDatabase(SDisplayDataName, SSql);
										iClick=2;
										
										// 添加滚动面板
										{
											if(HandleEvent.isMD5OK(SDisplayDataName,iClick)){
												JOptionPane.showMessageDialog(null,"数据库完整性校验通过!");	
											}
											else{
												JOptionPane.showMessageDialog(null,"数据库完整性校验未通过，数据库可能被更改!");
											}
											JPDisplay.add(getJScrollPane(),null);
											sViewResultCount[1]="联系人表查看条数："+Integer.toString(JTabDispaly.getRowCount());
										}
										//添加查询复选框
											JPDisplay.add(getJCheckBox(iClick),null);
									}
								});
						JPDisplay.add(jbDisplayContacts);						
						jbDisplayContacts.setBounds(25, 125, 120, 35);				
						jbDisplayContacts.setBorderPainted(false);//去掉按钮边框
						jbDisplayContacts.setFocusable(false);//取消文字框
					}
					// 查看通话记录按钮
					{
						jbDisplayCallLog = new JButton("通话记录");
						jbDisplayCallLog.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {										
										SDisplayDataName = disDataPath+"contacts2.db";
										SSql = "select _id,number,presentation,date,duration,type,name,numbertype,numberlabel,geocoded_location,lookup_uri,matched_number,photo_id,formatted_number from calls;";
										displaydata = new DisplayDatabase(SDisplayDataName, SSql);
										iClick=3;
										
										// 添加滚动面板
										{
											if(HandleEvent.isMD5OK(SDisplayDataName,iClick)){
												JOptionPane.showMessageDialog(null,"数据库完整性校验通过!");	
											}
											else{
												JOptionPane.showMessageDialog(null,"数据库完整性校验未通过，数据库可能被更改!");
											}
											JPDisplay.add(getJScrollPane(),null);
											sViewResultCount[2]="通话记录表查看条数："+Integer.toString(JTabDispaly.getRowCount());
										}
										//添加查询复选框
										{
											JPDisplay.add(getJCheckBox(iClick),null);
										}
									}
								});
						JPDisplay.add(jbDisplayCallLog);						
						jbDisplayCallLog.setBounds(25, 185, 120, 35);
						jbDisplayCallLog.setBorderPainted(false);//去掉按钮边框
						jbDisplayCallLog.setFocusable(false);//取消文字框
					}
					// 查看浏览记录按钮
					{
						jbDisplayBroswer = new JButton("浏览器书签");
						jbDisplayBroswer.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {										
										SDisplayDataName = disDataPath+"bookmark.db";
										SSql = "select * from bookmark;";
										
										displaydata = new DisplayDatabase(SDisplayDataName, SSql);
										iClick=4;

										// 添加滚动面板
										{
											if(HandleEvent.isMD5OK(SDisplayDataName,iClick)){
												JOptionPane.showMessageDialog(null,"数据库完整性校验通过!");	
											}
											else{
												JOptionPane.showMessageDialog(null,"数据库完整性校验未通过，数据库可能被更改!");
											}
											JPDisplay.add(getJScrollPane(),null);
											sViewResultCount[3]="浏览器书签表查看条数："+Integer.toString(JTabDispaly.getRowCount());
											JPDisplay.add(getJCheckBox(iClick),null);
										}
									}
								});
						JPDisplay.add(jbDisplayBroswer);						
						jbDisplayBroswer.setBounds(25, 245, 120, 35);
						jbDisplayBroswer.setBorderPainted(false);//去掉按钮边框
						jbDisplayBroswer.setFocusable(false);//取消文字框
					}
					// 查看多媒体信息
					{
						jbDisplayExternal = new JButton("多媒体信息");
						jbDisplayExternal.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {										
										SDisplayDataName = disDataPath+"external.db";
										SSql = "select * from thumbnails;";
										displaydata = new DisplayDatabase(
												SDisplayDataName, SSql);
										iClick=5;
										
										// 添加滚动面板
										{
											if(HandleEvent.isMD5OK(SDisplayDataName,iClick)){
												JOptionPane.showMessageDialog(null,"数据库完整性校验通过!");	
											}
											else{
												JOptionPane.showMessageDialog(null,"数据库完整性校验未通过，数据库可能被更改!");
											}
											JPDisplay.add(getJScrollPane(),null);
											sViewResultCount[4]="多媒体表查看条数："+Integer.toString(JTabDispaly.getRowCount());
											JPDisplay.add(getJCheckBox(iClick),null);
										}
									}
								});
						JPDisplay.add(jbDisplayExternal);
						jbDisplayExternal.setBounds(25, 315, 120, 35);					
						jbDisplayExternal.setBorderPainted(false);//去掉按钮边框
						jbDisplayExternal.setFocusable(false);//取消文字框
						
					}
					//保存查看导出结果
					{
						jbOutView=new JButton("写入报告");
						jbOutView.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e) {
								
								JOptionPane.showMessageDialog(null,"导出数据保存在ForensicReport.docx文档中");	
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
						jbOutView.setFocusable(false);//取消文字框
					}
					// 搜索条件标签jlFind
					{
						jlFindTime = new JLabel();
						JPDisplay.add(jlFindTime);
						jlFindTime.setText("时间搜索");
						jlFindTime.setBounds(25, 400, 120, 35);													
					}
					{
						jlSTime = new JLabel();
						JPDisplay.add(jlSTime);
						jlSTime.setText("起始时间");
						jlSTime.setBounds(130, 410, 120, 35);													
					}
					{
						jlETime = new JLabel();
						JPDisplay.add(jlETime);
						jlETime.setText("结束时间");
						jlETime.setBounds(130, 440, 120, 35);													
					}
					
					// 按时间搜索证据
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
						jbOkTime=new JButton("确认搜索");
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
								System.out.println("选中的时间是"+findStartTime+"\n结束时间是"+findEndTime);
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
									// 添加滚动面板
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
									// 添加滚动面板
									{
										JPDisplay.add(getJScrollPane(),null);	
									}
									break;
								case 4:
									SDisplayDataName = disDataPath+"browser2.db";
									SSql = "select * from search_history where date between "+millionstart+" and "+millionend;
									displaydata = new DisplayDatabase(SDisplayDataName, SSql);									
									// 添加滚动面板
									{
										JPDisplay.add(getJScrollPane(),null);	
									}
									break;																		
								}
							}
						});
						jbOkTime.setBounds(670, 425, 120, 35);
						jbOkTime.setBorderPainted(false);
						jbOkTime.setFocusable(false);//取消文字框
					}
					// 按关键字搜索记录
					{
						jlFindKey = new JLabel();
						JPDisplay.add(jlFindKey);
						jlFindKey.setText("敏感字搜索");
						jlFindKey.setBounds(25, 497, 120, 35);											
					}
					{
						jtfKey = new JTextField();
						JPDisplay.add(jtfKey);
						jtfKey.setText(null);
						jtfKey.setBounds(410, 500, 200, 35);
					}
					// 确认按钮
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
										 JOptionPane.showMessageDialog(null,"请选择要搜索的列");
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
									// 添加滚动面板
									{										
										JPDisplay.add(getJScrollPane(),null);										
									}
									break;
								case 2:
									SDisplayDataName = disDataPath+"contacts2.db";
									SSql = "select _id,data_version,data1,data4,data12 from data where ";
									
									if(!jcbox1.isSelected()&&!jcbox2.isSelected()&&!jcbox3.isSelected()){
										JOptionPane.showMessageDialog(null,"请选择要搜索的列");
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
									// 添加滚动面板
									{
										JPDisplay.add(getJScrollPane(),null);	
									}
									break;
								case 3:
									SDisplayDataName = disDataPath+"contacts2.db";
									SSql = "select _id,number,presentation,date,duration,type,name,numbertype,numberlabel,geocoded_location,lookup_uri,matched_number,photo_id,formatted_number from calls where ";
									if(!jcbox1.isSelected()&&!jcbox2.isSelected()){	
										 JOptionPane.showMessageDialog(null,"请选择要搜索的列");
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
									// 添加滚动面板
									{
										JPDisplay.add(getJScrollPane(),null);	
									}
									break;
								case 4:
									SDisplayDataName = disDataPath+"bookmark.db";
									SSql = "select * from bookmark where ";	
									if(!jcbox1.isSelected()&&!jcbox2.isSelected()){
										JOptionPane.showMessageDialog(null,"请选择要搜索的列");
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
									// 添加滚动面板
									{										
										JPDisplay.add(getJScrollPane(),null);										
									}
									break;
								case 5:
									SDisplayDataName = disDataPath+"external.db";
									SSql = "select * from thumbnails where ";	
									if(!jcbox1.isSelected()){
										JOptionPane joption=new JOptionPane();
										joption.showMessageDialog(null,"请选择要搜索的列");
										SSql="select * from thumbnails";
									}
									else if(jcbox1.isSelected()){
										SSql+="_data like'%"+fingKey+"%';";
									}											
									displaydata = new DisplayDatabase(SDisplayDataName, SSql);
									// 添加滚动面板
									{										
										JPDisplay.add(getJScrollPane(),null);										
									}
									break;
																					
								}
							}
						});	
						jbOKKey.setText("确认搜索");
						jbOKKey.setBounds(670, 500, 120, 35);
						jbOKKey.setBorderPainted(false);
						jbOKKey.setFocusable(false);//取消文字框
					}
				}
				// 证据恢复界面
				{
					JPRecovery = new JPanel();
					jtab.addTab("数据库恢复", null, JPRecovery, null);
					JPRecovery.setBounds(200, 150, 800, 500);
					JPRecovery.setLayout(null);
					
					// 在恢复证据界面中添加组件
					{
						jlRecoverdata1 = new JLabel();
						JPRecovery.add(jlRecoverdata1);
						jlRecoverdata1.setText("选择恢复的数据");
						jlRecoverdata1.setBounds(25, 20, 160, 35);											
					}
					// 恢复短信表按钮
					{
						jbRecoverSMS1 = new JButton();
						jbRecoverSMS1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								
								iRecClick=1;
								RecoveryDatabase recoverdata = new RecoveryDatabase(recDataPath+"mmssms.db",
										"select * from sqlite_master where name='sms' and tbl_name='sms';");
								
								if(HandleEvent.isMD5OK(recDataPath+"mmssms.db",iRecClick)){
									JOptionPane.showMessageDialog(null,"短信数据库完整性校验通过,现在尝试恢复短信表!");	
								}
								else{
									JOptionPane.showMessageDialog(null,"短信数据库完整性校验未通过,数据有可能被篡改，现在尝试恢复短信表!!");	
								}
								
								SResultList=recoverdata.recover();
								iGarbledCount=HandleEvent.saveRecData(SResultList,"RecSMS");
								JOptionPane.showMessageDialog(null, "恢复数据存入数据库完成");
								SDisplayDataName = "RecData.db";
								SSql = "select address,person,date,date_sent,body,read,status,type from RecSMS;";
								displaydata = new DisplayDatabase(SDisplayDataName, SSql);
								// 添加多行文本框
								{
									JPRecovery.add(getJScrollRecPanel(),null);	
									JPRecovery.add(getRecCount(),null);	
								}
							}
						});
						JPRecovery.add(jbRecoverSMS1);
						jbRecoverSMS1.setText("短信表");
						jbRecoverSMS1.setBounds(25, 100, 120, 35);			
						jbRecoverSMS1.setBorderPainted(false);//去掉按钮边框	
						jbRecoverSMS1.setFocusable(false);//取消文字框
					}
					// 恢复联系人表按钮
					{
						jbRecoverContacts1 = new JButton();
						jbRecoverContacts1.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										iRecClick=2;
										RecoveryDatabase recoverdata = new RecoveryDatabase(
												recDataPath+"contacts2.db",
												"select * from sqlite_master where name='data' and tbl_name='data';");	
										if(HandleEvent.isMD5OK(recDataPath+"contacts2.db",iRecClick)){
											JOptionPane.showMessageDialog(null,"联系人数据库完整性校验通过,现在尝试恢复联系人表!");	
										}
										else{
											JOptionPane.showMessageDialog(null,"联系人数据库完整性校验未通过,数据有可能被篡改，现在尝试恢复联系人表!!");	
										}
										SResultList=	recoverdata.recover();	
										iGarbledCount=HandleEvent.saveRecData(SResultList,"RecContacts");
										JOptionPane.showMessageDialog(null, "恢复数据存入数据库完成");
										SDisplayDataName = "RecData.db";
										SSql = "select _id,data_version,data1,data4,data12 from RecContacts;";
										displaydata = new DisplayDatabase(SDisplayDataName, SSql);
										// 添加多行文本框
										
										{
											JPRecovery.add(getJScrollRecPanel(),null);																							
											JPRecovery.add(getRecCount(),null);	
										}
									}
								});
						JPRecovery.add(jbRecoverContacts1);
						jbRecoverContacts1.setText("联系人表");
						jbRecoverContacts1.setBounds(25, 160, 120, 35);				
						jbRecoverContacts1.setBorderPainted(false);//去掉按钮边框
						jbRecoverContacts1.setFocusable(false);//取消文字框
					}
					// 恢复通话记录表按钮
					{
						jbRecoverCalls1 = new JButton();
						jbRecoverCalls1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								iRecClick=3;
								RecoveryDatabase recoverdata = new RecoveryDatabase(
										recDataPath+"contacts2.db",
										"select * from sqlite_master where name='calls' and tbl_name='calls';");
								if(HandleEvent.isMD5OK(recDataPath+"contacts2.db",iRecClick)){
									JOptionPane.showMessageDialog(null,"通话记录数据库完整性校验通过,现在尝试恢复通话记录表!");	
								}
								else{
									JOptionPane.showMessageDialog(null,"通话记录数据库完整性校验未通过,数据有可能被篡改，现在尝试恢复通话记录表!!");	
								}
								SResultList=recoverdata.recover();
								iGarbledCount=HandleEvent.saveRecData(SResultList,"RecCalls");
								JOptionPane.showMessageDialog(null, "恢复数据存入数据库完成");
								SDisplayDataName = "RecData.db";
								SSql = "select _id,number,presentation,date,duration,type,name,numbertype,numberlabel,geocoded_location,lookup_uri,matched_number,photo_id,formatted_number from RecCalls;";
								displaydata = new DisplayDatabase(SDisplayDataName, SSql);	
								// 添加多行文本框
								{
									JPRecovery.add(getJScrollRecPanel(),null);																							
									JPRecovery.add(getRecCount(),null);	
								}
							}
						});
						JPRecovery.add(jbRecoverCalls1);
						jbRecoverCalls1.setText("通话记录表");
						jbRecoverCalls1.setBounds(25, 220, 120, 35);
						jbRecoverCalls1.setBorderPainted(false);//去掉按钮边框	
						jbRecoverCalls1.setFocusable(false);//取消文字框
					}
					// 恢复浏览记录表按钮
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
											JOptionPane.showMessageDialog(null,"浏览器书签数据库完整性校验通过,现在尝试恢复通话书签表!");	
										}
										else{
											JOptionPane.showMessageDialog(null,"浏览器书签数据库完整性校验未通过,数据有可能被篡改，现在尝试恢复通话书签表!!");	
										}
										SResultList=	recoverdata.recover();	
										iGarbledCount=HandleEvent.saveRecData(SResultList,"RecBookMarks");
										JOptionPane.showMessageDialog(null, "恢复数据存入数据库完成");
										SDisplayDataName = "RecData.db";
										SSql = "select * from RecBookMarks;";
										displaydata = new DisplayDatabase(SDisplayDataName, SSql);	
										// 添加多行文本框
										
										{
											JPRecovery.add(getJScrollRecPanel(),null);																							
											JPRecovery.add(getRecCount(),null);	
										}
									}
								});
						JPRecovery.add(jbRecoverBrowsers1);
						jbRecoverBrowsers1.setText("浏览器书签");						
						jbRecoverBrowsers1.setBounds(25, 280, 120, 35);
						jbRecoverBrowsers1.setBorderPainted(false);//去掉按钮边框
						jbRecoverBrowsers1.setFocusable(false);//取消文字框
						
					}
					// 恢复浏多媒体记录表按钮
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
											JOptionPane.showMessageDialog(null,"多媒体数据库完整性校验通过,现在尝试恢复通话图片表!");	
										}
										else{
											JOptionPane.showMessageDialog(null,"多媒体数据库完整性校验未通过,数据有可能被篡改，现在尝试恢复通话图片表!!");	
										}
										SResultList=recoverdata.recover();	
										iGarbledCount=HandleEvent.saveRecData(SResultList,"RecThumbnails");
										JOptionPane.showMessageDialog(null, "恢复数据存入数据库完成");
										SDisplayDataName = "RecData.db";
										SSql = "select * from RecThumbnails;";
										displaydata = new DisplayDatabase(SDisplayDataName, SSql);	
										// 添加多行文本框
										
										{
											JPRecovery.add(getJScrollRecPanel(),null);																							
											JPRecovery.add(getRecCount(),null);	
										}
									}
								});
						JPRecovery.add(jbRecoverExternals1);
						jbRecoverExternals1.setText("多媒体表");
						jbRecoverExternals1.setBounds(25, 340, 120, 35);
						jbRecoverExternals1.setBorderPainted(false);//去掉按钮边框
						jbRecoverExternals1.setFocusable(false);//取消文字框
					}
					
					//保存恢复导出结果
					{
						jbOutRec=new JButton("写入报告");
						jbOutRec.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e) {
								
								JOptionPane.showMessageDialog(null,"导出数据保存在ForensicReport.docx文档中");	
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
						jbOutRec.setFocusable(false);//取消文字框
					}
					
					//导出报告按钮
					{
						jbOutputDataButton1=new JButton("导出报告");
						jbOutputDataButton1.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								WriteDocx writeReport=new WriteDocx(devicesInfo,outDisDataArray,outRecDataArray,sViewResultCount,sRecResultCount);
								writeReport.WriteData();
								JOptionPane.showMessageDialog(null,"导出报告完成！");
							}
						});
						JPRecovery.add(jbOutputDataButton1);
						jbOutputDataButton1.setBounds(650,500,120,35);
						jbOutputDataButton1.setBorderPainted(false);//去掉按钮边框
						jbOutputDataButton1.setFocusable(false);//取消文字框
					}
			}
		}			
			pack();// 依据设定组件设置窗口大小
			this.setSize(950, 700);// 窗口的大小
			this.setBackground(new Color(235,245,223));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 该函数设置查看数据界面的搜索时间中的天数
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
	 * 该函数设置时间
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
	 * 该函数设置查看数据界面显示表的内容
	 * ResultSet RSQuery：查询结果对象
	 * Object[] title：表头也就是字段名
	 * int iColumn：表的列数
	 */
	private void getTableModel(ResultSet RSQuery, Object[] title, int iColumn,int iRow) {
		//将数据库中查出来的毫秒数转为普通时间格式
		SimpleDateFormat sdateformate =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DTablemodel.addColumn("是否导出");
		for (int i = 1; i <= iColumn; i++) {
			DTablemodel.addColumn(title[i]);
		}
			try {
				while (RSQuery.next()) {
					Vector<Object> VInfo = new Vector<Object>();
					//将每一行的第一列设置为布尔类型，用于判断是否导出该行数据
					VInfo.addElement(new Boolean(false));
					for (int i = 2; i <= iColumn+1; i++) {
						// getObject()函数得到某行某列的某个数据
						if(title[i-1].equals("date")||title[i-1].equals("date_sent")||title[i-1].equals("create_time")){
							Object tempEle= RSQuery.getObject(i-1);
							if(tempEle!= null){
								VInfo.addElement(sdateformate.format(new Date((Long)tempEle)));
							}
							else{
								VInfo.addElement(null);//处理恢复出来的数据时间为0的情况
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
		case 1:outDisDataArray[0].tableName="3.1短信表可疑数据:";
				break;
		case 2:outDisDataArray[1].tableName="3.2联系人表可疑数据:";
		break;
		case 3:outDisDataArray[2].tableName="3.3通话记录表可疑数据:";
		break;
		case 4:outDisDataArray[3].tableName="3.4浏览记录表可疑数据:";
		break;
		case 5:outDisDataArray[4].tableName="3.5多媒体表可疑数据:";
		break;		
		}
		//每getpanel一次就是新打开了一个表，因此重新新建一次outDataArray
		outDisDataArray[iClick-1].tableBody=new Object[displaydata.getrowcount()][displaydata.getcolumncount()+1];
		outDisDataArray[iClick-1].rowCount[iClick-1]=0;
		if (JscpDisplay != null) {
			JscpDisplay.setVisible(false);
		}
		JscpDisplay = new JScrollPane();
		JsBar=new JScrollBar();
		JsBar=JscpDisplay.getHorizontalScrollBar();
		// 添加表格
		{
			DTablemodel = new DefaultTableModel();
			JTabDispaly = new JTable();
			JscpDisplay.setViewportView(JTabDispaly);
			
			getTableModel(displaydata.displayTable(),displaydata.getTableTitle(),displaydata.getcolumncount(),displaydata.getrowcount());
			displaydata.closeDatabase();//查询完数据库后关闭连接
			JTabDispaly.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 设置列宽不可变，自动显示水平滚动条
			JTabDispaly.setModel(DTablemodel);	
			//设置单元格渲染，实现表格中添加checkbox
			TableColumnModel tcm=JTabDispaly.getColumnModel();
			tcm.getColumn(0).setCellRenderer(new TableCellRenderer() {	
				public Component getTableCellRendererComponent(JTable table, Object value,
						boolean isSelected, boolean hasFocus, int row, int column) {
					JCheckBox Jck=new JCheckBox();
					Jck.setSelected(isSelected);//使具有焦点的行对应的复选框选中
					Jck.setHorizontalAlignment((int)0.5f);//设置复选框居中显示
					Jck.setBackground(new Color(208,232,245));
					return Jck;
				}
			});
			
			//表格排序功能实现
			RowSorter <javax.swing.table.TableModel> sorter =new TableRowSorter <javax.swing.table.TableModel> (JTabDispaly.getModel());
			JTabDispaly.setRowSorter(sorter);
		}		
		JscpDisplay.setBounds(165, 60, 700, 320);
		return JscpDisplay;	
	}
	
	
	
	private JScrollPane getJScrollRecPanel(){
		switch(iRecClick){
		case 1:
			outRecDataArray[0].tableName="5.1恢复短信表可疑数据:";
			sRecResultCount[0]="短信表找到自由块："+RecoveryDatabase.FreeBlockCount+"\n"+"短信表最终恢复记录数："+RecoveryDatabase.RecFreeblockCount+"含有乱码的记录数："+iGarbledCount;
			
			break;
		case 2:
			outRecDataArray[1].tableName="5.2恢复联系人表可疑数据:";
			sRecResultCount[1]="联系人找到自由块："+RecoveryDatabase.FreeBlockCount+"\n"+"联系人表最终恢复记录数："+RecoveryDatabase.RecFreeblockCount+"含有乱码的记录数："+iGarbledCount;
			break;
		case 3:
			outRecDataArray[2].tableName="5.3恢复通话记录表可疑数据:";
			sRecResultCount[2]="通话记录表找到自由块："+RecoveryDatabase.FreeBlockCount+"\n"+"通话记录表最终恢复记录数："+RecoveryDatabase.RecFreeblockCount+"含有乱码的记录数："+iGarbledCount;
			break;
		case 4:
			outRecDataArray[3].tableName="5.4恢复浏览器书签表可疑数据:";
			sRecResultCount[3]="浏览器书签表找到自由块："+RecoveryDatabase.FreeBlockCount+"\n"+"浏览器书签表最终恢复记录数："+RecoveryDatabase.RecFreeblockCount+"含有乱码的记录数："+iGarbledCount;
			break;
		case 5:
			outRecDataArray[4].tableName="5.5恢复多媒体表可疑数据:";
			sRecResultCount[4]="多媒体表找到自由块："+RecoveryDatabase.FreeBlockCount+"\n"+"多媒体表最终恢复记录数："+RecoveryDatabase.RecFreeblockCount+"含有乱码的记录数："+iGarbledCount;
			break;		
		}
		//每getpanel一次就是新打开了一个表，因此重新新建一次outDataArray
		System.out.println("恢复按钮点击后click的值是"+iRecClick);
		outRecDataArray[iRecClick-1].tableBody=new Object[displaydata.getrowcount()][displaydata.getcolumncount()+1];
		outRecDataArray[iRecClick-1].rowCount[iRecClick-1]=0;
		if (JscpDisplay != null) {
			JscpDisplay.setVisible(false);
		}
		JscpDisplay = new JScrollPane();
		JsBar=new JScrollBar();
		JsBar=JscpDisplay.getHorizontalScrollBar();
		// 添加表格
		{
			DTablemodel = new DefaultTableModel();
			JTabDispaly = new JTable();
			JscpDisplay.setViewportView(JTabDispaly);
			
			getTableModel(displaydata.displayTable(),displaydata.getTableTitle(),displaydata.getcolumncount(),displaydata.getrowcount());
			
			JTabDispaly.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 设置列宽不可变，自动显示水平滚动条
			
			JTabDispaly.setModel(DTablemodel);	
			TableColumnModel tcm=JTabDispaly.getColumnModel();
			tcm.getColumn(0).setCellRenderer(new TableCellRenderer() {	
				public Component getTableCellRendererComponent(JTable table, Object value,
						boolean isSelected, boolean hasFocus, int row, int column) {
					JCheckBox Jck=new JCheckBox();
					Jck.setSelected(isSelected);//使具有焦点的行对应的复选框选中
					Jck.setHorizontalAlignment((int)0.5f);//设置复选框居中显示
					Jck.setBackground(new Color(208,232,245));
					return Jck;
				}
			});
			//表格排序功能实现
			RowSorter <javax.swing.table.TableModel> sorter =new TableRowSorter <javax.swing.table.TableModel> (JTabDispaly.getModel());
			JTabDispaly.setRowSorter(sorter);
			displaydata.closeDatabase();//查询完数据库后关闭连接
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
				jcbox1.setFocusable(false);//取消文字框
				jcbox2.setFocusable(false);//取消文字框
				jpcbox.add(jcbox1);
				jpcbox.add(jcbox2);		
			}		
				break;
			case 2:
			{
				jcbox1=new JCheckBox("data1");
				jcbox2=new JCheckBox("data4");
				jcbox3=new JCheckBox("data12");
				jcbox1.setFocusable(false);//取消文字框
				jcbox2.setFocusable(false);//取消文字框
				jcbox3.setFocusable(false);//取消文字框
				jpcbox.add(jcbox1);
				jpcbox.add(jcbox2);	
				jpcbox.add(jcbox3);	
				
			}
				break;
			case 3:
			{
				jcbox1=new JCheckBox("number");
				jcbox2=new JCheckBox("name");
				jcbox1.setFocusable(false);//取消文字框
				jcbox2.setFocusable(false);//取消文字框
				jpcbox.add(jcbox1);
				jpcbox.add(jcbox2);	
				
			}
				break;	
			case 4:
			{
				jcbox1=new JCheckBox("title");
				jcbox2=new JCheckBox("url");
				jcbox1.setFocusable(false);//取消文字框
				jcbox2.setFocusable(false);//取消文字框
				jpcbox.add(jcbox1);
				jpcbox.add(jcbox2);	
				
			}
				break;	
			case 5:
			{
				jcbox1=new JCheckBox("_data");
				jcbox1.setFocusable(false);//取消文字框			
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
				jlCountAll=new JLabel("短信表中找到自由块个数："+RecoveryDatabase.FreeBlockCount+" ");
				break;
			case 2:
				jlCountAll=new JLabel("联系人表中找到自由块个数："+RecoveryDatabase.FreeBlockCount+" ");
				break;
			case 3:
				jlCountAll=new JLabel("通话记录表中找到自由块个数："+RecoveryDatabase.FreeBlockCount+" ");
				break;
			case 4:
				jlCountAll=new JLabel("浏览器书签表中找到自由块个数："+RecoveryDatabase.FreeBlockCount+" ");
				break;
			case 5:
				jlCountAll=new JLabel("多媒体表中找到自由块个数："+RecoveryDatabase.FreeBlockCount+" ");
				break;
		}
			JPCountlabel.add(jlCountAll);
			jlCountAll.setBounds(0,0,700,35);
			jlCountAll.setFocusable(false);//取消文字框
		}
		{
			jlCountRec=new JLabel("恢复的记录数："+RecoveryDatabase.RecFreeblockCount+"  含有乱码的记录数："+iGarbledCount);
			JPCountlabel.add(jlCountRec);
			jlCountRec.setBounds(0,40,700,35);
			jlCountRec.setFocusable(false);//取消文字框
		}
		JPCountlabel.setBounds(270,400,700,80);
		return JPCountlabel;
	}
}
