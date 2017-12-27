package com.ubsoft.framework.system;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.ubsoft.framework.mainframe.conf.Config;
import com.ubsoft.framework.mainframe.widgets.component.XImagePanel;
import com.ubsoft.framework.mainframe.widgets.util.IconFactory;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;
import com.ubsoft.framework.rpc.model.SessionContext;
import com.ubsoft.framework.rpc.proxy.RpcProxy;
import com.ubsoft.framework.system.entity.Session;
import com.ubsoft.framework.system.service.IUserService;

public class LoginFrame extends JFrame {
	private MainApp app = null;
	private MainApplet applet = null;
	private JButton btnOk, btnCancel;
	private JTextField txtUser;
	private JPasswordField txtPwd;

	public LoginFrame(MainApp app) {
		this.app = app;
		try {
			this.initComponents();
		} catch (Exception e) {
			MessageBox.showError(e.getMessage());
			e.printStackTrace();
		}
	}

	public LoginFrame(MainApplet applet) {
		this.applet = applet;
	}

	private void initComponents() throws Exception {
		JComponent content = (JComponent) getContentPane();
		setTitle(Config.getProperty("APPNAME"));
		setIconImage(IconFactory.getImage("icon/home.png"));
		content.setPreferredSize(new Dimension(334, 190));
		content.setLayout(new BorderLayout(0,2));
		content.setBorder(new EmptyBorder(0, 0, 0, 0));
		content.setBackground(Color.WHITE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
	
		UIManager.setLookAndFeel(WindowsLookAndFeel.class.getName());// 还可以，只能在windows系统中用
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		initUI(content);
	

		getRootPane().setDefaultButton(btnOk);
		pack();
		setLocationRelativeTo(null);
		this.setVisible(true);
	


	}

	private void initUI(JComponent content) {
		// logo
		XImagePanel bannerPane = new XImagePanel();

		bannerPane.setImage(IconFactory.getImage("icon/logo.png"));
		bannerPane.setPreferredSize(new Dimension(334, 70));
		bannerPane.setMode(XImagePanel.SCALED);
		content.add(bannerPane, BorderLayout.NORTH);

		// 用户名和密码
		JPanel centerPane = new JPanel();
		GridLayout gridLayout = new GridLayout(2, 1, 10, 5);
		centerPane.setLayout(gridLayout);

		content.add(centerPane, BorderLayout.CENTER);
		JLabel lbUser = new JLabel("用户名：");

		// lbUser.setPreferredSize(new Dimension(74, 24));
		txtUser = new JTextField(25);
		txtUser.setPreferredSize(new Dimension(30, 27));
		JPanel panelUser = new JPanel(new FlowLayout(FlowLayout.CENTER));

		panelUser.add(lbUser);
		panelUser.add(txtUser);
		centerPane.add(panelUser);

		JPanel panelPwd = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel lbPwd = new JLabel("密  码：");
		// lbPwd.setPreferredSize(new Dimension(74, 24));
		txtPwd = new JPasswordField(25);
		txtPwd.setPreferredSize(new Dimension(30, 27));
		panelPwd.add(lbPwd);
		panelPwd.add(txtPwd);
		centerPane.add(panelPwd);
		// 登录按钮
		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
		btnOk = new JButton("登录");
		btnOk.setPreferredSize(new Dimension(69, 25));
		btnCancel = new JButton("取消");
		btnCancel.setPreferredSize(btnOk.getPreferredSize());
		ActionHandler actionListener = new ActionHandler();
		btnOk.addActionListener(actionListener);
		btnCancel.addActionListener(actionListener);
		bottomPane.add(btnOk);
		bottomPane.add(btnCancel);
		content.add(bottomPane, BorderLayout.SOUTH);

	}

	private class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			try{
				RpcProxy.getRemote();
				} catch(Exception ex){
					MessageBox.showError("连接服务器失败!");
				}
			if (source == btnOk) {
				if (txtUser.getText().equals("")) {
					MessageBox.showInfo("请输入用户名!");
					return;
				}
				if (txtPwd.getText().equals("")) {
					MessageBox.showInfo("请输入密码!");
					return;
				}
				if (app != null) {
					IUserService userService = RpcProxy.getProxy(IUserService.class);
					try {
						Session session = userService.login(txtUser.getText(), txtPwd.getText());
						SessionContext context= new SessionContext();
						context.setSessionId(session.getSessionId());
						context.setUserKey(session.getUserKey());
						context.setUserName(session.getUserName());
						context.setOrgName(session.getOrgName());
						SessionContext.setContext(context);
						app.initApp();
					} catch (Exception ex) {
						MessageBox.showException(ex);
					}
					// userService.login(userKey, pwd)

				}

			} else if (source == btnCancel) {
				System.exit(0);
			}
		}
	}
}