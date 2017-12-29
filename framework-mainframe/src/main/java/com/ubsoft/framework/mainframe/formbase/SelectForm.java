package com.ubsoft.framework.mainframe.formbase;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.borland.dbswing.TableScrollPane;
import com.borland.dx.dataset.StorageDataSet;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.ConditionNode;
import com.ubsoft.framework.core.dal.model.ConditionTree;
import com.ubsoft.framework.core.dal.model.QueryModel;
import com.ubsoft.framework.core.service.IFormService;
import com.ubsoft.framework.mainframe.widgets.component.BusyPanel;
import com.ubsoft.framework.mainframe.widgets.component.XButton;
import com.ubsoft.framework.mainframe.widgets.component.XToolBar;
import com.ubsoft.framework.mainframe.widgets.component.table.XTable;
import com.ubsoft.framework.mainframe.widgets.util.DataSetUtil;
import com.ubsoft.framework.mainframe.widgets.util.FormUtil;
import com.ubsoft.framework.mainframe.widgets.util.IconFactory;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;
import com.ubsoft.framework.metadata.model.form.FormMeta;
import com.ubsoft.framework.rpc.proxy.RpcProxy;
/**
 * 选择界面基类
 * @author chenkf
 *
 */
public abstract class SelectForm extends JDialog {
	private int DEFAULT_ROW_COUNT = 20;
	// 列表界面工具栏
	protected XToolBar mainToolBar;
	// 列表界面主table
	protected XTable mainTable;
	// 列表界面主查询
	protected Container mainQuery;
	protected List<Bio> result;
	/**
	 * 主表key
	 */
	protected String masterKey = "MASTER";
	// 列表数据集
	public StorageDataSet mainDataSet;
	/** Hot key stroke map. */
	protected HashMap<String, KeyStroke> hotKeyStrokeMap = new HashMap<String, KeyStroke>();
	/** Hot key action map. */
	protected HashMap<String, Action> hotKeyActionMap = new HashMap<String, Action>();

	// 确认按钮
	private JButton btnConfirm;
	// 取消按钮
	private JButton btnCancel;

	// 刷新按钮
	private JButton btnRefresh;

	protected BusyPanel busyPanel;

	protected IFormService formService = RpcProxy.getProxy(IFormService.class);
	protected FormEngine formEngine = new FormEngine();

	public SelectForm(String formId, Frame owner) {
		super(owner);

		busyPanel = new BusyPanel();
		this.setGlassPane(busyPanel);
		this.setLayout(new BorderLayout());
		this.renderer(formId);

	}

	public SelectForm(String formId) {
		super();
		busyPanel = new BusyPanel();
		this.setGlassPane(busyPanel);
		this.setLayout(new BorderLayout());
		this.renderer(formId);

	}

	public SelectForm(String formId, Dialog owner) {
		super(owner);
		busyPanel = new BusyPanel();
		this.setGlassPane(busyPanel);
		this.setLayout(new BorderLayout());
		this.renderer(formId);
	}

	protected Component getComponent(String id) {
		return formEngine.getComponent(id);
	}

	protected FormMeta meta;

	public FormMeta getMeta() {
		return meta;
	}

	protected void setBusy(boolean busy) {
		if (busy) {
			busyPanel.stop();
			busyPanel.start();
			busyPanel.setVisible(true);
		} else {
			busyPanel.stop();
			busyPanel.setVisible(false);
		}
	}

	/**
	 * 根据formMeta 生成界面，默认按钮不需要再meta里面配置，会自动生成。
	 * 
	 * @param meta
	 * @throws Exception
	 */
	protected void renderer(FormMeta meta) throws Exception {
		this.meta = meta;
		formEngine.rendererForm(meta, this);
		mainToolBar = (XToolBar) getComponent("mainToolBar");
		mainTable = (XTable) getComponent("mainTable");
		mainQuery = (Container) getComponent("mainQuery");
		if (mainToolBar == null) {
			MessageBox.showError("未设置：mainToolBar。");
		}
		if (mainTable == null) {
			MessageBox.showError("未设置：mainTable。");
		}
		mainTable.setEditable(false);
		if (mainQuery == null) {
			MessageBox.showError("未设置：mainQuery。");
		}
		if (mainToolBar != null) {
			rendererDefaultToolButton();
		}
		if (this.mainTable != null && mainTable.getDataSet() != null) {
			mainDataSet = (StorageDataSet) mainTable.getDataSet();
		}
		if (meta.getFdmMeta().getMaster().getBioMeta() != null) {
			DataSetUtil.loadDataSetMetaFromBioMeta(mainDataSet, meta.getFdmMeta().getMaster().getBioMeta());
		}

		this.setTitle(meta.getLabel());
		// MessageBox.showInfo(mainTable.getRowHeight()+"..."+mainTable.getColumnCount());
		this.setSize(new Dimension(mainTable.getRowHeight() * 40, mainTable.getRowHeight() * 25));
		
		addDataSetListener();
		setHotKey();// 设置快捷键
		afterRenderer();
		this.doRefresh();
	}

	@SuppressWarnings("unused")
	public void renderer(final String formId) {
		try {
			this.setBusy(true);
			FormMeta formMeta = formEngine.getFormMeta(formId);
			if (formMeta == null) {
				formMeta = (FormMeta) formService.getFormMeta(formId);
				formEngine.putFormMeta(formId, formMeta);
			}
			renderer(formMeta);
			setBusy(false);

		} catch (Exception e) {
			setBusy(false);
			MessageBox.showException(e);
		}
	}

	/**
	 * 生成界面后执行
	 */
	protected void afterRenderer() {

	}

	protected boolean beforeRefresh() {
		return true;
	}

	protected void afterRefresh() {

	}

	/**
	 * 执行加载和刷新
	 * 
	 * @param <T>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void doRefresh() {
		if (!beforeRefresh()) {
			return;
		}
		final QueryModel qm = this.getQueryModel();
		this.setBusy(true);
		SwingWorker worker = new SwingWorker() {
			@Override
			protected Object doInBackground() throws Exception {
				try {
					return formService.query(qm);
				} catch (Exception e) {
					MessageBox.showException(e);
					return null;
				}
			}

			protected void done() {
				try {
					List<Bio> set = (List<Bio>) get();
					if (set != null) {
						DataSetUtil.loadFromBio(set, mainDataSet);
						afterRefresh();
					}

					setBusy(false);
				} catch (Exception e) {

					setBusy(false);
					MessageBox.showException(e);

				}
			}

		};
		worker.execute();

	}

	protected void doCancel() {
		result = null;
		dispose();
	}

	protected void doConfirm() {
		if (mainTable.getSelectedRowCount() == 0) {
			return;
		}
		int[] rows = mainTable.getSelectedRows();
		result = new ArrayList<Bio>();
		//DataRow row = new DataRow(mainDataSet);

		for (int i = 0; i < rows.length; i++) {
			mainDataSet.goToRow(rows[i]);
			
			Bio bio = new Bio();
			DataSetUtil.dataRow2Bio(mainDataSet, bio);
			result.add(bio);
		}

		dispose();
	}

	/**
	 * 添加dataset监听事件
	 */
	protected void addDataSetListener() {

		mainTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 2&&e.getButton() == MouseEvent.BUTTON1) {

					doConfirm();

				}
			}
		});
	}

	/**
	 * 添加固定默认查询条件，子类重写
	 * 
	 * @param conditionNode
	 */
	protected void addDefaultQueryCondition(List<ConditionNode> conditionNode) {

	}

	protected QueryModel getQueryModel() {
		QueryModel model = new QueryModel();
		model.setFdmId(meta.getFdm());
		model.setLimit(DEFAULT_ROW_COUNT);
		// MessageBox.showInfo(this.dataSet.getColumnNames(0).toString());
		model.setOrderBy("T.createdDate desc");
		ConditionTree conditionTree = new ConditionTree();
		List<ConditionNode> conditionNode = new ArrayList<ConditionNode>();
		FormUtil.getQueryCondition(this.mainQuery, conditionNode);
		addDefaultQueryCondition(conditionNode);
		conditionTree.setNodes(conditionNode);
		model.setConditionTree(conditionTree);
		return model;
	}

	/**
	 * 不通过配置文件继承，需要调用次默认布局。
	 * 
	 * @throws Exception
	 */
	protected void setDefaultLayout() throws Exception {
		this.setLayout(new BorderLayout());
		// 设置工具栏
		add(mainToolBar, BorderLayout.NORTH);
		// 设置查询form和table
		TableScrollPane lstTablePane = new TableScrollPane();
		lstTablePane.setViewportView(mainTable);
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerSize(5);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(0.5);// 设置分割线位于中央;
		splitPane.setTopComponent(mainQuery);
		splitPane.setBottomComponent(lstTablePane);
		add(splitPane, BorderLayout.CENTER);
		rendererDefaultToolButton();
		// 添加事件
		if (this.mainTable != null && mainTable.getDataSet() != null) {
			mainDataSet = (StorageDataSet) mainTable.getDataSet();
		}
		addDataSetListener();
		setHotKey();// 设置快捷键

	}

	protected void rendererDefaultToolButton() {
		// 默认根据全局变量的值设置固定的按钮：保存、删除等
		EmptyBorder emptyButtonBorder = new EmptyBorder(new Insets(3, 3, 3, 3));
		mainToolBar.setFloatable(false);
		int i = 0;
		btnRefresh = new JButton();
		btnRefresh.setBorder(emptyButtonBorder);
		btnRefresh.setText("刷新");
		btnRefresh.setToolTipText("刷新");
		btnRefresh.setIcon(IconFactory.getImageIcon("icon/reload.png"));
		mainToolBar.add(btnRefresh, i);
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					doRefresh();
				} catch (Exception ex) {
					MessageBox.showException(ex);
				}
			}
		});
		i++;
		mainToolBar.addSeparator();
		i++;
		btnConfirm = new XButton();
		btnConfirm.setBorder(emptyButtonBorder);
		btnConfirm.setText("确认");
		btnConfirm.setToolTipText("确认");
		btnConfirm.setIcon(IconFactory.getImageIcon("icon/confirm.png"));
		mainToolBar.add(btnConfirm, i);
		i++;
		btnConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doConfirm();
			}
		});
		mainToolBar.addSeparator();
		i++;
		btnCancel = new XButton();
		btnCancel.setBorder(emptyButtonBorder);
		btnCancel.setText("关闭");
		btnCancel.setToolTipText("关闭");
		btnCancel.setIcon(IconFactory.getImageIcon("icon/close.png"));
		mainToolBar.add(btnCancel, i);
		i++;
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});
		

		
	}

	/**
	 * 设置快捷键
	 */
	protected void setHotKey() {

		KeyStroke refreshKey = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK);
		KeyStroke cancelKey = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK);
		KeyStroke enterKey = KeyStroke.getKeyStroke("ENTER");
		hotKeyStrokeMap.put("CANCEL", cancelKey);
		hotKeyActionMap.put("CANCEL", new HotKeyAction("CANCEL"));
		hotKeyStrokeMap.put("REFRESH", refreshKey);
		hotKeyActionMap.put("REFRESH", new HotKeyAction("REFRESH"));
		hotKeyStrokeMap.put("ENTER", enterKey);
		hotKeyActionMap.put("ENTER", new HotKeyAction("ENTER"));

		// 设置界面hotkey
		InputMap inputMap = ((JComponent) getContentPane()).getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap actionMap = ((JComponent) getContentPane()).getActionMap();
		for (String key : hotKeyActionMap.keySet()) {
			inputMap.put(hotKeyStrokeMap.get(key), key);
			actionMap.put(key, hotKeyActionMap.get(key));
		}
		// 设置table hotKey
		actionMap = mainTable.getActionMap();
		inputMap = mainTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		for (String key : hotKeyActionMap.keySet()) {
			inputMap.put(hotKeyStrokeMap.get(key), key);
			actionMap.put(key, hotKeyActionMap.get(key));
		}
	}

	/**
	 * 快捷键action
	 * 
	 * @author chenkf
	 * 
	 */
	protected class HotKeyAction extends AbstractAction {
		private String key = null;

		public HotKeyAction(String key) {

			this.key = key;
		}

		public void actionPerformed(ActionEvent e) {
			if (key != null) {
				if (key.equals("REFRESH")) {
					doRefresh();
				} else if (key.equals("ENTER")) {
					doRefresh();
				} else if (key.equals("CANCEL")) {
					doCancel();
				}
			}

		}
	}

}
