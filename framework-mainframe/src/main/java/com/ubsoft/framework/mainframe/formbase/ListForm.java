package com.ubsoft.framework.mainframe.formbase;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.borland.dbswing.JdbTable;
import com.borland.dbswing.TableScrollPane;
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataChangeAdapter;
import com.borland.dx.dataset.DataChangeEvent;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.EditAdapter;
import com.borland.dx.dataset.StorageDataSet;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.ConditionNode;
import com.ubsoft.framework.core.dal.model.ConditionTree;
import com.ubsoft.framework.core.dal.model.QueryModel;
import com.ubsoft.framework.core.dal.util.TypeUtil;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.mainframe.conf.Config;
import com.ubsoft.framework.mainframe.widgets.component.BusyPanel;
import com.ubsoft.framework.mainframe.widgets.component.XButton;
import com.ubsoft.framework.mainframe.widgets.component.XToolBar;
import com.ubsoft.framework.mainframe.widgets.component.table.XColumn;
import com.ubsoft.framework.mainframe.widgets.component.table.XTable;
import com.ubsoft.framework.mainframe.widgets.util.DataSetUtil;
import com.ubsoft.framework.mainframe.widgets.util.FormUtil;
import com.ubsoft.framework.mainframe.widgets.util.IconFactory;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;
import com.ubsoft.framework.metadata.model.form.FormMeta;
import com.ubsoft.framework.metadata.model.form.ListFormMeta;
import com.ubsoft.framework.metadata.model.form.fdm.MasterMeta;
import com.ubsoft.framework.metadata.model.widget.ButtonMeta;
import com.ubsoft.framework.metadata.model.widget.ToolBarMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;
import com.ubsoft.framework.metadata.model.widget.model.ColumnType;
import com.ubsoft.framework.metadata.model.widget.table.TableColumnMeta;

public abstract class ListForm extends Form {
	/**
	 * 最大行数
	 **/
	private int MAX_ROW_COUNT = 2000;
	/**
	 * 默认排序
	 **/
	private String DEFAULT_ORDER_BY = "T.createdDate";
	private boolean modified = false;
	// 列表界面工具栏
	protected XToolBar mainToolBar;
	// 列表界面主table
	protected XTable mainTable;
	// 列表界面主查询
	protected Container mainQuery;

	protected QueryModel queryModel;
	/**
	 * 主表key
	 */
	protected String masterKey = "MASTER";
	// 列表数据集
	public StorageDataSet mainDataSet;
	/**
	 * Hot key stroke map.
	 */
	protected HashMap<String, KeyStroke> hotKeyStrokeMap = new HashMap<String, KeyStroke>();
	/**
	 * Hot key action map.
	 */
	protected HashMap<String, Action> hotKeyActionMap = new HashMap<String, Action>();
	// 是否显示新增按钮
	protected boolean showNewBtn = true;
	// 是否显示保存按钮
	protected boolean showSaveBtn = true;
	// 是否显示删除按钮
	protected boolean showDelBtn = true;
	// 是否显示重置按钮
	protected boolean showCancelBtn = true;
	// 保存按钮
	private JButton btnSave;
	// 删除按钮
	private JButton btnDel;
	// 新增按钮
	private JButton btnNew;
	// 打印按钮
	private JButton btnCancel;
	// 刷新按钮
	private JButton btnRefresh;

	protected boolean loading = true;

	protected ListFormMeta meta;

	public ListFormMeta getMeta() {
		return meta;
	}

	public ListForm() {
		EmptyBorder emptyButtonBorder = new EmptyBorder(new Insets(0, 0, 0, 0));

		setBorder(emptyButtonBorder);
		// 去掉标题栏
		((BasicInternalFrameUI) getUI()).setNorthPane(null);
		busyPanel = new BusyPanel();
		this.setGlassPane(busyPanel);
		this.setLayout(new BorderLayout());
		initForm();

	}

	/**
	 * 根据formMeta 生成界面，默认按钮不需要再meta里面配置，会自动生成。
	 * 
	 * @param meta
	 * @throws Exception
	 */
	protected void renderer(FormMeta meta) throws Exception {
		this.meta = (ListFormMeta) meta;
		formEngine.rendererForm(meta, this);
		initPrivateControl(this.getClass(), this);
		if (meta.getFdm() == null) {
			return;
		}
		mainToolBar = (XToolBar) getComponent("mainToolBar");
		mainTable = (XTable) getComponent("mainTable");
		mainQuery = (Container) getComponent("mainQuery");
		if (mainToolBar == null) {
			MessageBox.showError("未设置：mainToolBar。");
		}
		if (mainTable == null) {
			MessageBox.showError("未设置：mainTable。");
		}
		if (mainQuery == null) {
			// MessageBox.showError("未设置：mainQuery。");
		}
		if (mainToolBar != null) {
			rendererDefaultToolButton();
			// 添加自定义按钮事件
			for (Object cMeta : meta.getChildren()) {
				if (cMeta instanceof ToolBarMeta) {
					ToolBarMeta toolBarMeta = (ToolBarMeta) cMeta;
					if (toolBarMeta.getChildren() == null)
						break;
					for (WidgetMeta wMeta : toolBarMeta.getChildren()) {
						if (wMeta instanceof ButtonMeta) {
							ButtonMeta btnMeta = (ButtonMeta) wMeta;
							if (btnMeta.getId() != null && btnMeta.getAction() != null) {
								XButton btn = (XButton) getComponent(btnMeta.getId());
								final String action = btnMeta.getAction();
								btn.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										try {
											doExecute(action);
										} catch (Exception ex) {
											MessageBox.showException(ex);
										}
									}
								});
							}
						}
					}
					break;
				}
			}
		}

		if (this.mainTable != null && mainTable.getDataSet() != null) {
			mainDataSet = (StorageDataSet) mainTable.getDataSet();
			if (meta.getEditable() != null && meta.getEditable() == false) {
				mainTable.setEditable(false);
			}
		}
		if (meta.getFdmMeta().getMaster().getBioMeta() != null) {
			DataSetUtil.loadDataSetMetaFromBioMeta(mainDataSet, meta.getFdmMeta().getMaster().getBioMeta());
		}
		addDataSetListener();
		setHotKey();// 设置快捷键
		setToolbarStatus();
		afterRenderer();
	}

	public void updateRowData(DataSet dataSet) {

		boolean bFind = false;
		int j = 0;
		for (int i = 0; i < mainDataSet.getRowCount(); i++) {
			//可以用这种方式循环
//			Variant var= new Variant();
//			mainDataSet.getVariant("id", i, var);
//			var.getString();
			
			mainDataSet.goToRow(i);
			if (dataSet.getString("id").equals(mainDataSet.getString("id"))) {
				bFind = true;
				j = i;
				break;
			}
		}

		if (!bFind) {
			mainDataSet.insertRow(false);
		}
		DataSetUtil.copyDataRow(dataSet, mainDataSet);
		DataSetUtil.acceptChanges(mainDataSet);
		if (!bFind) {
			mainDataSet.goToRow(mainDataSet.getRowCount() - 1);
		} else {
			mainDataSet.goToRow(j);
		}

	}

	@SuppressWarnings("unused")
	public void renderer(final String formId) {
		try {
			this.setBusy(true);
			FormMeta formMeta = formEngine.getFormMeta(formId);
			if (Config.getProperty("debug") != null && Config.getProperty("debug").equals("true")) {
				formMeta = null;
			}
			if (formMeta == null) {
				SwingWorker worker = new SwingWorker() {
					protected Object doInBackground() throws Exception {
						FormMeta formMeta = (FormMeta) formService.getFormMeta(formId);
						formEngine.putFormMeta(formId, formMeta);
						return formMeta;

					}

					protected void done() {
						try {
							Object obj = get();
							if (obj != null) {
								FormMeta meta = (FormMeta) obj;
								renderer(meta);
							}

						} catch (Exception e) {
							MessageBox.showException(e);
						} finally {
							setBusy(false);
						}
					}

				};
				worker.execute();
			} else {
				ListFormMeta fmeta = (ListFormMeta) formEngine.getFormMeta(formId);
				renderer(fmeta);
				setBusy(false);
			}
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

	/**
	 * 新建前执行
	 * 
	 * @return
	 */
	protected boolean beforeNew() {
		return true;
	}

	/**
	 * 执行新增
	 */
	protected void doNew() {
		if (!beforeNew())
			return;

		// 如果设置编辑界面,在tab也打开编辑界面.
		if (meta.getEditForm() != null) {
			getMainFrame().openEditFormTab(this, meta.getEditFormTitle(), meta.getEditForm(), null);
		} else {
			if (!mainDataSet.atLast())
				mainDataSet.last();
			mainDataSet.insertRow(false);
			mainTable.requestFocusInWindow();
		}
		afterNew();
	}

	protected void afterNew() {

	}

	protected boolean beforeSave() {
		return true;
	}

	
	/**
	 * 执行保存
	 */
	protected void doSave() {
		if (!this.isModified())
			return;
		if (!beforeSave()) {
			return;
		}
		this.setBusy(true);
		setStatusMessage("正在保存......");
		SwingWorker worker = new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {
				try {
					List<Bio> listBio = DataSetUtil.getChanges(mainDataSet);
					return formService.saveForm(meta.getFdm(), listBio);
				} catch (Exception e) {
					MessageBox.showException(e);
					return null;
				}
			}

			@Override
			public void done() {
				try {
					Object result = get();
					if (result != null) {
						List<Bio> data = (List<Bio>) result;
						// 实体列表
						// Object entityData = data.get(masterKey);
						loading = true;
						DataSetUtil.fillbackFromBio(data, mainDataSet);
						DataSetUtil.acceptChanges(mainDataSet);
						afterSave(data);
						setModified(false);
						setStatusMessage("就绪");
						loading = false;
					}
					setBusy(false);
				} catch (Exception e) {
					setBusy(false);
					loading = false;
					setStatusMessage("就绪");
					MessageBox.showException(e);
				}
			}

		};
		worker.execute();
	}

	 
	protected void afterSave(List<Bio> result) {

	}

	protected boolean beforeDelete() {
		return true;
	}

	/**
	 * 执行删除
	 */
	protected void doDelete() {
		if (!beforeDelete()) {
			return;
		}
		setStatusMessage("正在删除......");
		if (mainTable.getSelectedRowCount() > 0) {
			int[] rowIndex = mainTable.getSelectedRows();
			final List<Serializable> ids = new ArrayList<Serializable>();
			final long[] deleteIndex = new long[rowIndex.length];
			int n = JOptionPane.showConfirmDialog(null, "是否确认删除" + mainTable.getSelectedRowCount() + "条记录?", "确认删除", JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				MasterMeta masterMeta = meta.getFdmMeta().getMaster();
				String propertyKey = masterMeta.getBioMeta().getPrimaryProperty().getPropertyKey();
				String propertyType = masterMeta.getBioMeta().getPrimaryProperty().getDataType();
				for (int i = 0; i < rowIndex.length; i++) {
					mainDataSet.goToRow(rowIndex[i]);
					if (propertyType.equals(TypeUtil.STRING)) {
						String id = mainDataSet.getString(propertyKey);
						if (StringUtil.isNotEmpty(id)) {
							ids.add(id);
						}
					} else {
						int id = mainDataSet.getInt(propertyKey);
						if (id != 0) {
							ids.add(id);
						}
					}
					deleteIndex[i] = mainDataSet.getInternalRow();
				}

				SwingWorker worker = new SwingWorker() {
					@Override
					protected Object doInBackground() throws Exception {
						try {
							Serializable[] deleteIds = new Serializable[ids.size()];
							int i = 0;
							for (Serializable id : ids) {
								deleteIds[i] = id;
								i++;
							}
							formService.deleteForm(meta.getFdm(), deleteIds);
							return 1;
						} catch (Exception e) {
							MessageBox.showException(e);
							return 0;
						}
					}

					@Override
					public void done() {
						try {
							Object result = get();
							if (result != null) {
								int flag = (Integer) result;
								if (flag == 1) {
									loading = true;
									for (long index : deleteIndex) {
										mainDataSet.goToInternalRow(index);
										mainDataSet.deleteRow();
									}
									setStatusMessage("就绪");
									afterDelete();
									loading = false;

								}
							}
							setBusy(false);

						} catch (Exception e) {
							setBusy(false);
							loading = false;
							MessageBox.showException(e);
						}
					}

				};
				worker.execute();

			}
		} else {
			MessageBox.showInfo("请选择要删除的记录.");
		}
	}

	protected void afterDelete() {

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
		buildQueryModel();
		final QueryModel qm = queryModel;
		if (!beforeRefresh()) {
			return;
		}
		setStatusMessage("正在加载......");

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
						loading = true;
						DataSetUtil.loadFromBio(set, mainDataSet);
						loading = false;
						afterRefresh();
						setModified(false);
						setStatusMessage("就绪");
					}
					setBusy(false);
				} catch (Exception e) {
					loading = false;
					setBusy(false);
					MessageBox.showException(e);
					setStatusMessage("就绪");

				}
			}

		};
		worker.execute();

	}

	protected void doCancel() {
		loading = true;// 设置成加载中
		mainDataSet.cancel();
		mainDataSet.reset();
		this.setModified(false);
		mainTable.requestFocusInWindow();
		loading = false;//
	}

	protected boolean beforeClose() {
		return true;
	}

	// 关闭窗体
	public void doClose() throws Exception {
		if (!beforeClose())
			return;
		this.mainToolBar = null;
		this.mainQuery = null;
		this.mainTable = null;
		this.mainDataSet.empty();
		this.mainDataSet.close();
		this.mainDataSet = null;
		this.setClosed(true);
		this.dispose();

	}

	/**
	 * 自定义按钮事件统一入口
	 * 
	 * @param action
	 */
	public void doExecute(String action) {

	}

	/**
	 * 添加dataset监听事件
	 */
	protected void addDataSetListener() {
		this.mainDataSet.addEditListener(new EditAdapter() {
			public void modifying(DataSet dataSet) {
				if (mainDataSet.isEditing()) {
					mainDataSet.post();
				}
				if (!loading) {
					setModified(true);
				}
			}

		});

		this.mainDataSet.addDataChangeListener(new DataChangeAdapter() {
			public void dataChanged(DataChangeEvent event) {
				if (!loading) {
					setModified(true);
				}

			}
		});
		mainTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = ((JdbTable) e.getSource()).rowAtPoint(e.getPoint()); // 获得行位置
				int col = ((JdbTable) e.getSource()).columnAtPoint(e.getPoint());
				if (e.getButton() == MouseEvent.BUTTON1) {
					doLinkClick(mainDataSet.getColumn(col).getColumnName());

					if (e.getClickCount() == 2) {
						// if (!loading) {
						// int[] selectRows = mainTable.getSelectedRows();
						// if (selectRows.length != 1)
						// return;
						// // selectedRowIndex = selectRows[0];
						// mainDataSet.goToRow(selectRows[0]);
						// String id = mainDataSet.getString("id");
						// loading = true;
						// // doLoad(id);
						//
						// }
					}
				}
			}
		});
	}

	/**
	 * 是否修改
	 * 
	 * @return
	 */
	protected boolean isModified() {
		return modified;
	}

	/**
	 * 设置form修改状态
	 * 
	 * @param modified
	 * @return
	 */
	protected void setModified(boolean modified) {
		if (this.modified == modified) {
			return;
		} else {
			this.modified = modified;
			this.setToolbarStatus();
		}
	}

	/**
	 * 添加固定默认查询条件，子类重写
	 * 
	 * @param conditionNode
	 */
	protected void addDefaultQueryCondition(List<ConditionNode> conditionNode) {

	}

	protected void buildQueryModel() {
		queryModel = new QueryModel();
		queryModel.setFdmId(meta.getFdm());
		if (meta.getMaxRowCount() != null) {
			MAX_ROW_COUNT = meta.getMaxRowCount();
		}
		queryModel.setLimit(MAX_ROW_COUNT);
		if (meta.getOrderBy() != null) {
			DEFAULT_ORDER_BY = meta.getOrderBy();
		}
		queryModel.setOrderBy(DEFAULT_ORDER_BY);
		ConditionTree conditionTree = new ConditionTree();
		List<ConditionNode> conditionNode = new ArrayList<ConditionNode>();
		if (this.mainQuery != null) {
			FormUtil.getQueryCondition(this.mainQuery, conditionNode);
		}
		addDefaultQueryCondition(conditionNode);
		conditionTree.setNodes(conditionNode);
		queryModel.setConditionTree(conditionTree);

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
		setToolbarStatus();

	}

	protected void rendererDefaultToolButton() {
		// 默认根据全局变量的值设置固定的按钮：保存、删除等
		EmptyBorder emptyButtonBorder = new EmptyBorder(new Insets(3, 3, 3, 3));
		mainToolBar.setFloatable(false);
		int i = 0;
		if (showNewBtn) {
			btnNew = new XButton();
			btnNew.setText("新建");
			btnNew.setToolTipText("新建");

			btnNew.setBorder(emptyButtonBorder);
			btnNew.setIcon(IconFactory.getImageIcon("icon/new.gif"));
			mainToolBar.add(btnNew, i);
			i++;
			btnNew.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					doNew();
				}
			});
		}
		if (showSaveBtn) {
			btnSave = new XButton();
			btnSave.setBorder(emptyButtonBorder);
			btnSave.setText("保存");
			btnSave.setToolTipText("保存");
			btnSave.setIcon(IconFactory.getImageIcon("icon/save.gif"));
			mainToolBar.add(btnSave, i);
			i++;
			btnSave.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					doSave();
				}
			});
		}
		if (showDelBtn) {
			btnDel = new XButton();
			btnDel.setBorder(emptyButtonBorder);
			btnDel.setText("删除");
			btnDel.setToolTipText("删除");
			btnDel.setIcon(IconFactory.getImageIcon("icon/remove.gif"));
			mainToolBar.add(btnDel, i);
			i++;
			btnDel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					doDelete();
				}
			});
		}
		if (showCancelBtn) {
			btnCancel = new XButton();
			btnCancel.setBorder(emptyButtonBorder);
			btnCancel.setText("撤销");
			btnCancel.setToolTipText("撤销");
			btnCancel.setIcon(IconFactory.getImageIcon("icon/redo.gif"));
			mainToolBar.add(btnCancel, i);
			i++;
			btnCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					doCancel();
				}
			});
		}
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

	}

	/**
	 * 设置快捷键
	 */
	protected void setHotKey() {
		KeyStroke newKey = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK);// ctrl+N
		KeyStroke saveKey = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK);// ctrl+S;
		KeyStroke delKey = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK);// ctrl+D;
		KeyStroke refreshKey = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK);// ctrl+D;
		KeyStroke cancelKey = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK);// ctrl+D;
		KeyStroke enterKey = KeyStroke.getKeyStroke("ENTER");// ctrl+D;

		if (showNewBtn) {
			hotKeyStrokeMap.put("NEW", newKey);
			hotKeyActionMap.put("NEW", new HotKeyAction("NEW"));
		}
		if (showSaveBtn) {
			hotKeyStrokeMap.put("SAVE", saveKey);
			hotKeyActionMap.put("SAVE", new HotKeyAction("SAVE"));
		}
		if (showDelBtn) {
			hotKeyStrokeMap.put("DELETE", delKey);
			hotKeyActionMap.put("DELETE", new HotKeyAction("DELETE"));
		}
		if (showCancelBtn) {
			hotKeyStrokeMap.put("CANCEL", cancelKey);
			hotKeyActionMap.put("CANCEL", new HotKeyAction("CANCEL"));

		}

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

	protected void setToolbarStatus() {
		if (!SwingUtilities.isEventDispatchThread()) {
			return;
		}
		if (this.modified) {
			if (btnSave != null) {
				this.btnSave.setEnabled(true);
			}
			if (btnNew != null) {
				btnNew.setEnabled(true);
			}
			if (btnDel != null) {
				btnDel.setEnabled(true);
			}
			if (btnCancel != null) {
				btnCancel.setEnabled(true);
				btnRefresh.setEnabled(false);
			}
		} else {
			if (btnSave != null) {
				this.btnSave.setEnabled(false);
			}
			if (btnNew != null) {
				btnNew.setEnabled(true);
			}
			if (btnDel != null) {
				btnDel.setEnabled(true);
			}
			if (btnCancel != null) {
				btnCancel.setEnabled(false);
				btnRefresh.setEnabled(true);
			}
		}
	}

	protected void setFormEditable(boolean enable) {
		for (Column column : mainDataSet.getColumns()) {
			column.setEditable(enable);
		}
	}

	protected void doLinkClick(String colName) {
		XColumn column = (XColumn) mainDataSet.getColumn(colName);
		if (column.getMeta() != null) {
			TableColumnMeta colMeta = column.getMeta();
			String type = colMeta.getType();
			if (type != null && type.equals(ColumnType.LINK)) {
				String module = colMeta.getLinkModule();
				String key = colMeta.getLinkKey();
				// 默认编辑界面,取主键id的值传到编辑界面
				if (key == null && module == null) {
					getMainFrame().openEditFormTab(this, meta.getEditFormTitle(), meta.getEditForm(), mainDataSet.getString("id"));
				} else {
					// TODO 其他界面用弹出框
				}

			}
		}

	}

	/**
	 * 快捷键action
	 * 
	 * @author chenkf
	 */
	protected class HotKeyAction extends AbstractAction {
		private String key = null;

		public HotKeyAction(String key) {

			this.key = key;
		}

		public void actionPerformed(ActionEvent e) {
			if (key != null) {
				if (key.equals("SAVE")) {
					doSave();
				} else if (key.equals("NEW")) {
					doNew();
				} else if (key.equals("DELETE")) {
					doDelete();
				} else if (key.equals("REFRESH")) {
					doRefresh();
				} else if (key.equals("ENTER")) {
					doRefresh();
				} else if (key.equals("CANCEL")) {
					doCancel();
				} else {
					doExecute(key);
				}
			}

		}
	}

}
