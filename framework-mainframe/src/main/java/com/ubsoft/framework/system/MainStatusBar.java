package com.ubsoft.framework.system;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.Timer;

import com.ubsoft.framework.core.support.util.IPUtil;
import com.ubsoft.framework.mainframe.widgets.util.IconFactory;
import com.ubsoft.framework.rpc.model.SessionContext;

public class MainStatusBar extends JPanel {
	public static final Font FONT_14_BOLD = new Font("微软雅黑", 1, 14);
	public static final Font FONT_12_BOLD = new Font("微软雅黑", 1, 12);
	public static final Font FONT_14_PLAIN = new Font("微软雅黑", 0, 14);
	public static final Font FONT_12_PLAIN = new Font("微软雅黑", 0, 12);
	private static MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
	private static NumberFormat format = NumberFormat.getInstance();
	private JToolBar leftToolBar;
	private JToolBar rightToolBar;

	private JLabel messageLabel;
	JProgressBar memoryBar = new JProgressBar(0, 0, 100);

	public MainStatusBar() {
		leftToolBar = new JToolBar();
		rightToolBar = new JToolBar();
		leftToolBar.setFloatable(false);
		rightToolBar.setFloatable(false);
		this.setLayout(new BorderLayout());
		add(leftToolBar, BorderLayout.CENTER);
		add(rightToolBar, BorderLayout.EAST);

		addMessageLable();
		addMemoryBar();
		//addGCButton();
		addServerLable();
		addUserLabel();
		//addTimeLable();
	}

	private void addMessageLable() {
		messageLabel = new JLabel();
		//messageLabel.setIcon(IconFactory.getImageIcon("com/framework/widgets/swing/component/icons/message.png"));
		messageLabel.setText("就绪");
		messageLabel.setOpaque(false);
		//messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		//messageLabel.setFont(FONT_12_BOLD);
		messageLabel.setVerticalAlignment(0);
		messageLabel.setVerticalTextPosition(0);
		//messageLabel.setIconTextGap(5);
		leftToolBar.add(messageLabel);
		// addSeparator();
	}

	public void setMessage(String text) {
		this.messageLabel.setText(text);
	}


	public void addMemoryBar() {

		memoryBar.setOrientation(JProgressBar.HORIZONTAL);
		memoryBar.setMinimum(0);
		memoryBar.setMaximum(100);
		memoryBar.setValue(0);
		memoryBar.setStringPainted(true);
		// memoryBar.addChangeListener(this);
		memoryBar.setPreferredSize(new Dimension(100, 20));
		memoryBar.setBorderPainted(true);
		rightToolBar.add(memoryBar);
		rightToolBar.addSeparator();
		int delay = 2000;
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				long usedMemory = memorymbean.getHeapMemoryUsage().getUsed();
				long totalMemory = memorymbean.getHeapMemoryUsage().getMax();
				updateMemoryUsage(usedMemory, totalMemory);
			}

		};
		(new Timer(delay, taskPerformer)).start();
	}

	private void addGCButton() {
		JButton button = new JButton();
		button.setIcon(IconFactory.getImageIcon("icon/gc.png"));
		button.setToolTipText("释放内存");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.gc();
			}
		});
		rightToolBar.add(button);
		rightToolBar.addSeparator();
	}

	private void addServerLable() {
		JLabel serverLabel = new JLabel();
		serverLabel.setIcon(IconFactory.getImageIcon("icon/server.png"));
		serverLabel.setText(IPUtil.getLocalIP());
		serverLabel.setOpaque(false);
		//serverLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		//serverLabel.setFont(FONT_12_BOLD);
		serverLabel.setVerticalAlignment(0);
		serverLabel.setVerticalTextPosition(0);
		serverLabel.setIconTextGap(5);
		rightToolBar.add(serverLabel);
		rightToolBar.addSeparator();
		// addSeparator();
	}
	JLabel userLabel ;
	JLabel orgLabel;
	public void setUserLabel(String userName,String orgName){
		userLabel.setText(userName);
		orgLabel.setText(orgName);
	}
	private void addUserLabel() {
		userLabel = new JLabel();
		userLabel.setIcon(IconFactory.getImageIcon("icon/user.png"));
		userLabel.setText(SessionContext.getContext().getUserKey());
		userLabel.setOpaque(false);
	//	userLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		//userLabel.setFont(FONT_12_BOLD);
		userLabel.setVerticalAlignment(0);
		userLabel.setVerticalTextPosition(0);
		userLabel.setIconTextGap(5);
		rightToolBar.add(userLabel);
		rightToolBar.addSeparator();
		orgLabel = new JLabel();
		orgLabel.setText(SessionContext.getContext().getOrgName());
		rightToolBar.add(orgLabel);
		
	}
	

	
	private void addTimeLable() {
		final JLabel timeLabel = new JLabel();
		//userLabel.setIcon(IconFactory.getImageIcon("com/framework/widgets/swing/component/icons/user.png"));
	
		timeLabel.setOpaque(false);
		//timeLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		//timeLabel.setFont(FONT_12_BOLD);
		timeLabel.setVerticalAlignment(0);
		timeLabel.setVerticalTextPosition(0);	
		rightToolBar.add(timeLabel);
		rightToolBar.addSeparator();
		int delay = 1000;
		
		new Timer(delay,new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String dateString = dateFormat.format(new Date());
				timeLabel.setText(dateString);
			}
			
		}).start();
		
	}

	private void updateMemoryUsage(long usedMemory, long totalMemory) {
		int percent = (int) ((usedMemory * 100L) / totalMemory);
		memoryBar.setValue(percent);
		String usedMega = (new StringBuilder()).append(format.format(usedMemory / 1024L / 1024L)).append("M").toString();
		String totalMega = (new StringBuilder()).append(format.format(totalMemory / 1024L / 1024L)).append("M").toString();
		String message = (new StringBuilder()).append(usedMega).append("/").append(totalMega).toString();
		memoryBar.setString(message);
		memoryBar.setToolTipText((new StringBuilder()).append("Memory used ").append(format.format(usedMemory)).append(" of total ").append(format.format(totalMemory)).toString());
	}

}
