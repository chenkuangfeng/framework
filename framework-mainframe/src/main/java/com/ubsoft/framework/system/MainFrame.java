package com.ubsoft.framework.system;

import java.awt.BorderLayout;
import java.awt.Component;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jidesoft.swing.JideTabbedPane;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.mainframe.formbase.EditForm;
import com.ubsoft.framework.mainframe.formbase.Form;
import com.ubsoft.framework.mainframe.formbase.ListForm;
import com.ubsoft.framework.mainframe.widgets.component.tree.TreeNodeModel;
import com.ubsoft.framework.mainframe.widgets.component.tree.XTree;
import com.ubsoft.framework.mainframe.widgets.util.FormUtil;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;
import com.ubsoft.framework.rpc.model.SessionContext;
import com.ubsoft.framework.rpc.proxy.RpcProxy;
import com.ubsoft.framework.system.entity.Permission;
import com.ubsoft.framework.system.service.IUserService;

public class MainFrame extends JPanel {

	private JFrame app = null;

	private MainApplet applet = null;

	private static MainFrame mainFrame;
	private XTree mainTree;
	private MainStatusBar statusBar = new MainStatusBar();
	private JideTabbedPane mainTabbedPane;

	public static MainFrame getMainFrame() {
		return mainFrame;
	}

	public MainStatusBar getStatusBar() {

		return statusBar;
	}

	public void setStatusBar(MainStatusBar statusBar) {
		this.statusBar = statusBar;
	}

	public MainFrame(MainApplet applet) throws Exception {
		initComponents();
		//JInternalFrame mm = new JInternalFrame();

		this.applet = applet;
		mainFrame = this;

	}

	public MainFrame(MainApp app) throws Exception {
		initComponents();
		this.app = app;
		mainFrame = this;
	}

	private void initComponents() throws Exception {
		setLookAndFeel();
		setLayout(new BorderLayout());
		genMainMenu();
		genMainStatusBar();
		genMainCenter();

	}

	private void setLookAndFeel() throws Exception {
		// 设置皮肤方案

		// 默认皮肤
		// UIManager.setLookAndFeel(com.jgoodies.looks.plastic.PlasticLookAndFeel.class.getName());
		UIManager.setLookAndFeel(WindowsLookAndFeel.class.getName());// 还可以，只能在windows系统中用
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		// LookAndFeelFactory.installJideExtension(LookAndFeelFactory.OFFICE2003_STYLE);
		// LookAndFeelFactory.installDefaultLookAndFeel();
		// UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
		// UIManager.setLookAndFeel(MotifLookAndFeel.class.getName());
		// UIManager.setLookAndFeel(GTKLookAndFeel.class.getName());//还可以

	}

	private void genMainMenu() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.getAccessibleContext().setAccessibleName("工具栏");
		this.add(menuBar, BorderLayout.NORTH);
		JMenu sysMenu = (JMenu) menuBar.add(new JMenu("系统(S)"));
		sysMenu.setMnemonic('S');
		JMenuItem reLogin = new JMenuItem("注销");

		sysMenu.add(reLogin);

		JMenuItem changPwd = new JMenuItem("修改密码");
		sysMenu.add(changPwd);
		// MenuItem reLogin= new
		JMenu toolMenu = (JMenu) menuBar.add(new JMenu("工具(T)"));
		toolMenu.setMnemonic('T');

		JMenu windowMenu = (JMenu) menuBar.add(new JMenu("窗口(V)"));
		windowMenu.setMnemonic('V');
		JMenuItem closeMenu = new JMenuItem("关闭(C)");
		closeMenu.setMnemonic('C');
		JMenuItem closeAllMenu = new JMenuItem("关闭所有(A)");// (JMenu)
		// menuBar.add(new
		// JMenu("关闭其他(A)"));
		closeAllMenu.setMnemonic('A');
		JMenuItem closeOtherMenu = new JMenuItem("关闭其他(O)");// (JMenu)
		// menuBar.add(new
		// JMenu("关闭其他(A)"));
		closeOtherMenu.setMnemonic('O');
		windowMenu.add(closeMenu);
		windowMenu.add(closeOtherMenu);
		windowMenu.add(closeAllMenu);

		JMenu helpMenu = (JMenu) menuBar.add(new JMenu("帮助(H)"));
		JMenuItem aboutMenu = new JMenuItem("关于(B)");
		helpMenu.add(aboutMenu);

	}

	private void genMainStatusBar() {

		this.add(statusBar, BorderLayout.SOUTH);
	}

	private void genMainCenter() {
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerSize(5);
		splitPane.setDividerLocation(180);
		splitPane.setLeftComponent(genMainTree());
		splitPane.setRightComponent(this.genMainTabPanel());
		this.add(splitPane, BorderLayout.CENTER);

	}

	private JideTabbedPane genMainTabPanel() {
		mainTabbedPane = new JideTabbedPane();
		mainTabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_WIN2K);
		mainTabbedPane.addTab("首页", new JPanel());
		mainTabbedPane.setTabClosableAt(0, false);
		mainTabbedPane.setShowCloseButtonOnTab(true);
		mainTabbedPane.setBoldActiveTab(true);
		// mainTabbedPane.setCloseAction(new Action(){
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// MessageBox.showInfo(e.getSource().toString());
		//
		// }
		//
		// @Override
		// public Object getValue(String key) {
		// // TODO Auto-generated method stub
		// return null;
		// }
		//
		// @Override
		// public void putValue(String key, Object value) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void setEnabled(boolean b) {
		// b=true;
		//
		// }
		//
		// @Override
		// public boolean isEnabled() {
		// // TODO Auto-generated method stub
		// return false;
		// }
		//
		// @Override
		// public void addPropertyChangeListener(PropertyChangeListener
		// listener) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void removePropertyChangeListener(PropertyChangeListener
		// listener) {
		// // TODO Auto-generated method stub
		//
		// }});
		// mainTabbedPane.addtaaddTabbedPaneListener
		// mainTabbedPane.setCloseAction(arg0)
		// mainTabbedPane
		return mainTabbedPane;
	}

	/**
	 * 转换tree模型
	 * @param permission
	 * @return
	 */
	private void buildTreeNoeModel(TreeNodeModel parentNode,List<Permission> permission) {

		List<TreeNodeModel> childNode= new ArrayList<TreeNodeModel>();
		for(Permission perm:permission){
			
			if(parentNode.getId().equals(perm.getParentPermKey())){
				TreeNodeModel item=new TreeNodeModel();
				item.setId(perm.getPermKey());
				item.setText(perm.getPermName());
				item.setModule(perm.getPermModule());					
				childNode.add(item);
				buildTreeNoeModel(item,permission);
			}
		}
		parentNode.setChildren(childNode);
		

	}
    private void buildChildTreeNodeModel(){
    	
    }
	private JScrollPane genMainTree() {
		IUserService userService = RpcProxy.getProxy(IUserService.class);
		List<Permission> permissions = userService.getPermission(SessionContext.getContext().getUserKey());		
		TreeNodeModel root=	 FormUtil.loadTreeModel("permKey", "permName", "permModule", "parentPermKey", permissions, "ROOT", "系统菜单");	
		mainTree = new XTree();
		mainTree.setRootVisible(true);
		JScrollPane sbar = new JScrollPane(mainTree);		
		mainTree.setModel(root);
		mainTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent evt) {

				if (mainTree.getLastSelectedPathComponent() == null) {
					return;
				}
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) mainTree.getLastSelectedPathComponent();// 返回最后选定的节点
				if (selectedNode.getUserObject() == null) {
					return;
				}
				TreeNodeModel nodeModel = (TreeNodeModel) selectedNode.getUserObject();
				if (StringUtil.isNotEmpty(nodeModel.getModule())) {
					int index = findTab(nodeModel.getText());
					if (index == -1) {
						try {
							Class cla = Class.forName(nodeModel.getModule());
							Form form = (Form) cla.newInstance();

							mainTabbedPane.add(nodeModel.getText(), form);
							mainTabbedPane.setSelectedIndex(mainTabbedPane.getTabCount() - 1);
						} catch (Exception e) {
							MessageBox.showException(e);

						}

					} else {
						mainTabbedPane.setSelectedIndex(index);
					}
				}

			}

		});
		return sbar;
	}

	public void openEditFormTab(ListForm lstForm, String title, String module, String entityId) {
		int index = findTab(title);
		if (index == -1) {
			try {
				Class cla = Class.forName(module);
				Form form = null;//
				Constructor c = cla.getConstructor(String.class);
				form = (Form) c.newInstance(entityId);
				EditForm cntForm = (EditForm) form;
				cntForm.lstForm = lstForm;
				// if(form instanceof FormCnt){
				// FormCnt cntForm=(FormCnt)form;
				//
				// if(entityId==null){
				// cntForm.doNew();
				// }else{
				// cntForm.entityId=entityId;
				// cntForm.doLoad();
				// }
				//
				// }

				mainTabbedPane.add(title, form);
				mainTabbedPane.setSelectedIndex(mainTabbedPane.getTabCount() - 1);
			} catch (Exception e) {
				MessageBox.showException(e);

			}

		} else {
			mainTabbedPane.setSelectedIndex(index);
			Component obj = mainTabbedPane.getSelectedComponent();
			if (obj != null && obj instanceof EditForm) {
				EditForm cntForm = (EditForm) obj;
				if (entityId != null) {
					cntForm.entityId = entityId;
					cntForm.doLoad();
				} else {
					cntForm.doNew();
				}
			}

		}
	}

	public int findTab(String title) {
		for (int i = 0; i < mainTabbedPane.getTabCount(); i++) {
			// mainTabbedPane.get
			String header = mainTabbedPane.getTitleAt(i); // 获得选项卡标签
			if (header.equals(title)) {
				return i;
			}

		}
		return -1;
	}

	public boolean isApplet() {
		return (applet != null);
	}

	public JFrame getApp() {
		return app;
	}
}
