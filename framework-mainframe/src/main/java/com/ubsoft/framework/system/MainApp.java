/*
 * MainApp.java
 *
 * Created on __DATE__, __TIME__
 */

package com.ubsoft.framework.system;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.alibaba.dubbo.container.Main;
import com.ubsoft.framework.core.conf.AppConfig;
import com.ubsoft.framework.mainframe.conf.Config;
import com.ubsoft.framework.mainframe.widgets.util.IconFactory;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;

public class MainApp extends JFrame {

	private static LoginFrame loginFrame;
	private static MainApp mainApp;

	public MainApp() {
		

	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					// 加载日志					
					String path = Main.class.getClassLoader().getResource("conf/log4j.properties").getPath();				
					AppConfig.initLog4j(path);
					
					mainApp = new MainApp();					
					mainApp.initConfig();
					mainApp.setVisible(false);
					loginFrame = new LoginFrame(mainApp);
									} catch (Exception ex) {
					MessageBox.showException(ex);
				}

			}
		});
	}

	public void loginFrame() {
		
	}

	public void initApp() {
		loginFrame.setVisible(false);

		// 关闭窗体后退出程序
		mainApp.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// 设置图标
		// setIconImage(new
		// ImageIcon("resources\\icon\\bb.png").getImage());
		// 自动适配所有控件大小
		mainApp.setSize(mainApp.getPreferredSize().width, 200);
		mainApp.setLocationRelativeTo(null);
		mainApp.setMinimumSize(new Dimension(400, 300));
		mainApp.setTitle(Config.getProperty("APPNAME"));
		mainApp.setIconImage(IconFactory.getImage("icon/home.png"));
		mainApp.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainApp.setVisible(true);
		// 设置布局，并加入主Panel
		mainApp.getContentPane().setLayout(new BorderLayout());
		try {
			mainApp.getContentPane().add(new MainFrame(mainApp), BorderLayout.CENTER);
		} catch (Exception e) {
			MessageBox.showException(e);
		}
		mainApp.setVisible(true);
	}

	public void initConfig() {
		Config.load(ClassLoader.getSystemResource("conf/config.properties").getPath());
	}

}