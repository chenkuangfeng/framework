package com.ubsoft.framework.system;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.alibaba.dubbo.container.Main;
import com.ubsoft.framework.core.conf.AppConfig;
import com.ubsoft.framework.mainframe.conf.Config;
import com.ubsoft.framework.mainframe.widgets.util.IconFactory;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;

public class MainApplet extends JApplet {
	private static LoginFrame loginFrame;
	//private static MainApplet mainApp;

	public MainApplet() {
		

	}
	public void init() {
		final MainApplet applet=this;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					// 加载日志					
					String path = Main.class.getClassLoader().getResource("conf/log4j.properties").getPath();				
					AppConfig.initLog4j(path);
					Config.load(ClassLoader.getSystemResource("conf/config.properties").getPath());
					//mainApp = new MainApplet();					
					//mainApp.initConfig();
					//mainApp.setVisible(false);	
					setVisible(false);
					loginFrame = new LoginFrame(applet);
									} catch (Exception ex) {
					MessageBox.showException(ex);
				}

			}
		});
//		this.setJMenuBar(null);
//		getContentPane().setLayout(new BorderLayout());
//		try {
//			getContentPane().add(new MainFrame(this), BorderLayout.CENTER);
//		} catch (Exception e) {
//			MessageBox.showException(e);
//		}
	}
	public void initApp() {
		loginFrame.setVisible(false);
		// 关闭窗体后退出程序
		//mainApp.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// 设置图标
		// setIconImage(new
		// ImageIcon("resources\\icon\\bb.png").getImage());
		// 自动适配所有控件大小
		//setSize(getPreferredSize().width, 200);
		//this.setsetLocationRelativeTo(null);
		//setMinimumSize(new Dimension(400, 300));
		
		//mainApp.setTitle(Config.getProperty("APPNAME"));
		//mainApp.setIconImage(IconFactory.getImage("icon/home.png"));
		//mainApp.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		// 设置布局，并加入主Panel
		getContentPane().setLayout(new BorderLayout());
		try {
			getContentPane().add(new MainFrame(this), BorderLayout.CENTER);
		} catch (Exception e) {
			MessageBox.showException(e);
		}
		setVisible(true);
	}

	public void initConfig() {
		Config.load(ClassLoader.getSystemResource("conf/config.properties").getPath());
	}
	/**
	 * Gets the uRL.
	 * 
	 * @param filename
	 *            the filename
	 * @return the uRL
	 */
	public URL getURL(String filename) {
		URL codeBase = this.getCodeBase();
		URL url = null;

		try {
			url = new URL(codeBase, filename);
			System.out.println(url);
		} catch (java.net.MalformedURLException e) {
			System.out.println("Error: badly specified URL");
			return null;
		}

		return url;
	}

}
