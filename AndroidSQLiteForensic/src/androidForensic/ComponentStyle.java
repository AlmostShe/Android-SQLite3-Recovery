package androidForensic;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.UIManager; 

public class ComponentStyle {
	public static void loadStyle(){
		
		UIManager.put("Frame.font", new Font("宋体",0,16));
		UIManager.put("Frame.background", new Color(248,253,255));		
		UIManager.put("Frame.foreground", new Color(45,45,45));
		UIManager.put("Frame.select", new Color(208,232,245));
		
		UIManager.put("CheckBox.font", new Font("宋体",0,16));
		UIManager.put("CheckBox.background", new Color(248,253,255));		
		UIManager.put("CheckBox.foreground", new Color(45,45,45));
		UIManager.put("CheckBox.select", new Color(208,232,245));
		
		UIManager.put("OptionPane.font", new Font("黑体",0,16));
		UIManager.put("OptionPane.background", new Color(248,253,255));		
		UIManager.put("OptionPane.foreground", new Color(45,45,45));
		UIManager.put("OptionPane.select", new Color(208,232,245));
		
		UIManager.put("Button.font", new Font("宋体",0,16));
		UIManager.put("Button.background", new Color(142,193,246));		
		UIManager.put("Button.foreground", new Color(45,45,45));
		UIManager.put("Button.preferredSize", new Dimension(160, 35));
		UIManager.put("Button.select", new Color(208,232,245));
		
		UIManager.put("TabbedPane.font", new Font("宋体",0,16));
		UIManager.put("TabbedPane.background", new Color(208,232,245));
		UIManager.put("TabbedPane.foreground", new Color(45,45,45));
		UIManager.put("TabbedPane.selected", new Color(142,193,246));//设置标签的选中颜色
		
		UIManager.put("Panel.font", new Font("宋体",0,20));
		UIManager.put("Panel.background", new Color(248,253,255));
		UIManager.put("Panel.foreground", new Color(45,45,45));
		
		UIManager.put("Label.font",new Font("宋体",0,16));
		UIManager.put("Label.background", new Color(248,253,255));
		UIManager.put("Label.foreground", new Color(45,45,45));		
		
		UIManager.put("TextArea.font",new Font("宋体",0,16));
		UIManager.put("TextArea.background", new Color(248,253,255));
		UIManager.put("TextArea.foreground", new Color(45,45,45));		
		
		UIManager.put("TitledBorder.font",new Font("宋体",0,16));
		
		UIManager.put("ComboBox.font",new Font("宋体",0,16));
		UIManager.put("ComboBox.background", new Color(248,253,255));
		UIManager.put("ComboBox.foreground", new Color(45,45,45));		
		
		UIManager.put("Table.font",new Font("宋体",0,12));
		UIManager.put("Table.background", new Color(208,232,245));
		UIManager.put("Table.foreground", new Color(45,45,45));
		//JTabDispaly.setSelectionBackground(new Color(235,245,223));
		UIManager.put("Table.selectionBackground", new Color(142,193,246));
		
		UIManager.put("TextField.font",new Font("宋体",0,14));
		UIManager.put("TextField.background", new Color(248,253,255));
		UIManager.put("TextField.foreground", new Color(45,45,45));
		
		UIManager.put("ScrollBar.background", new Color(248,253,255));//改变滑条的颜色
		
		
		UIManager.put("ScrollPane.background", new Color(248,253,255));//改变滑条的颜色
		UIManager.put("ScrollPane.foreground", new Color(235,245,223));//改变滑条的颜色
		
		
		
		
	}

}
