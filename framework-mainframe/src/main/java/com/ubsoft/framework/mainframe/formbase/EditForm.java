package com.ubsoft.framework.mainframe.formbase;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.borland.dx.dataset.DataChangeAdapter;
import com.borland.dx.dataset.DataChangeEvent;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.EditAdapter;
import com.borland.dx.dataset.StorageDataSet;
import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.BioSet;
import com.ubsoft.framework.core.dal.util.TypeUtil;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.mainframe.conf.Config;
import com.ubsoft.framework.mainframe.widgets.component.BusyPanel;
import com.ubsoft.framework.mainframe.widgets.component.XButton;
import com.ubsoft.framework.mainframe.widgets.component.XToolBar;
import com.ubsoft.framework.mainframe.widgets.component.table.XTable;
import com.ubsoft.framework.mainframe.widgets.util.DataSetUtil;
import com.ubsoft.framework.mainframe.widgets.util.FormUtil;
import com.ubsoft.framework.mainframe.widgets.util.IconFactory;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;
import com.ubsoft.framework.metadata.model.form.EditFormMeta;
import com.ubsoft.framework.metadata.model.form.FormMeta;
import com.ubsoft.framework.metadata.model.form.fdm.DetailMeta;
import com.ubsoft.framework.metadata.model.widget.ButtonMeta;
import com.ubsoft.framework.metadata.model.widget.ToolBarMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

public abstract class EditForm extends Form {
	private boolean modified = false;
	// 列表界面工具栏
	protected XToolBar mainToolBar;

	public Serializable entityId;

	protected EditFormMeta meta;
	
	protected Bio masterBio;

	public EditFormMeta getMeta() {
		return meta;
	}

	/**
	 * 主表key
	 */
	// 主数据集
	protected StorageDataSet masterDataSet;
	protected Container mainForm;
	/** 子数据集 **/
	protected Map<String, StorageDataSet> detailDataSets = new HashMap<String, StorageDataSet>();
	protected Map<String, XTable> detailTables = new HashMap<String, XTable>();
	/** Hot key stroke map. */
	protected HashMap<String, KeyStroke> hotKeyStrokeMap = new HashMap<String, KeyStroke>();
	/** Hot key action map. */
	protected HashMap<String, Action> hotKeyActionMap = new HashMap<String, Action>();
	// 是否显示新增按钮
	protected boolean showNewBtn = true;
	// 是否显示保存按钮
	protected boolean showSaveBtn = true;
	// 是否显示重置按钮
	protected boolean showCancelBtn = true;

	// 保存按钮
	private JButton btnSave;
	// 新增按钮
	private JButton btnNew;
	// 打印按钮
	private JButton btnCancel;
	// 刷新按钮
	private JButton btnRefresh;

	// 新增明细按钮
	private JButton btnAddLine;

	// 删除明细按钮
	private JButton btnDelLine;

	protected boolean loading = false;

	public ListForm lstForm;

	public EditForm(String entityId) {
		this.entityId = entityId;
		setBorder(null);
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
		this.meta = (EditFormMeta) meta;
		formEngine.rendererForm(meta, this);
		mainToolBar = (XToolBar) getComponent("mainToolBar");

		if (mainToolBar == null) {
			MessageBox.showError("未设置：mainToolBar。");
		}
		mainForm = (Container) getComponent("mainForm");
		if (mainForm == null) {
			MessageBox.showError("未设置：mainForm。");
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
					break;
				}
			}
		}

		if (masterDataSet == null) {
			masterDataSet = new StorageDataSet();
			FormUtil.setFormModel(mainForm, masterDataSet);

		}

		if (meta.getFdmMeta().getMaster().getBioMeta() != null) {
			DataSetUtil.loadDataSetMetaFromBioMeta(masterDataSet, meta.getFdmMeta().getMaster().getBioMeta());
		}
		masterDataSet.open();
		addDataSetListener();
		setHotKey();// 设置快捷键
		this.rendererDetailToolBar();
		setToolbarStatus();
		afterRenderer();
		// //界面生成后加载或者新增记录
		if (entityId != null) {
			doLoad();

		} else {
			doNew();

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
					@Override
					protected Object doInBackground() throws Exception {
						try {
							FormMeta formMeta = (FormMeta) formService.getFormMeta(formId);
							formEngine.putFormMeta(formId, formMeta);
							return formMeta;
						} catch (Exception e) {
							MessageBox.showException(e);
							return null;
						}
					}

					protected void done() {
						try {
							Object obj = get();
							if (obj != null) {
								FormMeta meta = (FormMeta) obj;
								renderer(meta);
							}
							setBusy(false);
						} catch (Exception e) {
							setBusy(false);
							MessageBox.showException(e);
						}
					}

				};
				worker.execute();
			} else {

				EditFormMeta fmeta = (EditFormMeta) formEngine.getFormMeta(formId);
				renderer(fmeta);
				setBusy(false);
			}
		} catch (Exception e) {
			setBusy(false);
			MessageBox.showException(e);
		}
	}

	protected void rendererDetailToolBar() {
		// 添加默认添加行和默认按钮
		for (final Map.Entry<String, StorageDataSet> entry : detailDataSets.entrySet()) {
			XToolBar detailToolBar = (XToolBar) getComponent(entry.getKey() + "_ToolBar");
			if (detailToolBar != null) {
				XButton btnAddLine = new XButton();
				EmptyBorder emptyButtonBorder = new EmptyBorder(new Insets(1, 1, 1, 1));
				btnAddLine.setText("新增行");
				btnAddLine.setToolTipText("新增行");
				btnAddLine.setBorder(emptyButtonBorder);
				btnAddLine.setIcon(IconFactory.getImageIcon("icon/addline.png"));
				btnAddLine.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						doAddLine(entry.getKey() + "");
					}
				});
				detailToolBar.add(btnAddLine);

				btnDelLine = new XButton();
				btnDelLine.setText("删除行");
				btnDelLine.setToolTipText("删除行");
				btnDelLine.setBorder(emptyButtonBorder);
				btnDelLine.setIcon(IconFactory.getImageIcon("icon/delline.png"));
				btnDelLine.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						doDelLine(entry.getKey() + "");
					}
				});
				detailToolBar.add(btnDelLine);
			}

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
	public void doNew() {
		loading = false;
		
		if (!beforeNew())
			return;
		clearMasterDetail();
		entityId = null;
		masterBio=new Bio();
		masterDataSet.insertRow(false);
		this.setFormEditable(true);
		afterNew();
	}

	private void clearMasterDetail() {
		masterDataSet.empty();
		for (Map.Entry<String, StorageDataSet> entry : detailDataSets.entrySet()) {

			StorageDataSet detailDataSet = entry.getValue();
			detailDataSet.empty();
		}
	}

	protected void afterNew() {

	}

	protected boolean beforeSave() {
		return true;
	}

	/**
	 * 执行保存
	 */
	@SuppressWarnings("rawtypes")
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
					return formService.saveForm(meta.getFdm(), getChanges());
				} catch (Exception e) {
					MessageBox.showException(e);
					return null;
				}
			}
			@Override
			public void done() {
				try {
					BioSet result = (BioSet) get();
					if (result != null) {
						try {
							loading = true;							
							masterBio = result.getMaster();
							BioMeta masterBioMeta= meta.getFdmMeta().getMaster().getBioMeta();
							String propertyKey = masterBioMeta.getPrimaryProperty().getPropertyKey();
							String propertyType = masterBioMeta.getPrimaryProperty().getDataType();
							// 更新编辑界面
							DataSetUtil.bio2DataRow(masterBio, masterDataSet);
							if (propertyType.equals(TypeUtil.STRING)) {
								entityId = masterDataSet.getString(propertyKey);
								
							}else{
								entityId = masterDataSet.getInt(propertyKey);
							}
						
							DataSetUtil.acceptChanges(masterDataSet);
							if (detailDataSets.size() > 0) {
								for(Map.Entry<String, StorageDataSet> entry:detailDataSets.entrySet()){
									List<Bio> detailList = result.getDetail(entry.getKey());
									DataSetUtil.fillbackFromBio(detailList, entry.getValue());
									DataSetUtil.acceptChanges(entry.getValue());									
								}
							}
							setModified(false);
							setStatusMessage("就绪");
							lstForm.updateRowData(masterDataSet);
							loading = false;
						} catch (Exception e) {
							MessageBox.showException(e);
							setStatusMessage("就绪");
							loading = false;
							e.printStackTrace();
						}
						loading = false;
					}
					setBusy(false);
				} catch (Exception e) {
					setBusy(false);
					setStatusMessage("就绪");
					loading = false;
					MessageBox.showException(e);
				}
			}

		};
		worker.execute();
	}

	protected void afterSave(Map<String, ?> result) {

	}

	protected boolean beforeLoad() {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public void doLoad() {
		if (entityId == null)
			return;
		if (!beforeLoad())
			return;
		this.setBusy(true);
		setStatusMessage("正在加载......");
		SwingWorker worker = new SwingWorker() {
			@Override
			protected BioSet doInBackground() throws Exception {
				try {
					return formService.load(meta.getFdm(), entityId);
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
						loading = true;
						BioSet data = (BioSet) result;
						// 父表
						masterBio = data.getMaster();
						DataSetUtil.bio2DataRow(masterBio, masterDataSet);
						DataSetUtil.acceptChanges(masterDataSet);
						// mainDataSet.goToRow(selectedRowIndex);
						for (Map.Entry<String, StorageDataSet> entry : detailDataSets.entrySet()) {
							String key = entry.getKey();
							StorageDataSet value = entry.getValue();
							if (data.getDetail(key) != null) {
								DataSetUtil.loadFromBio(data.getDetail(key), value);
							}
						}
						setFormEditable(true);
						setModified(false);
						afterLoad();
						setStatusMessage("就绪");
						loading = false;
					}
					setBusy(false);
				} catch (Exception e) {
					setBusy(false);
					setStatusMessage("异常");
					MessageBox.showException(e);
				}
			}

		};
		worker.execute();
	}

	protected void afterLoad() {

	}

	protected boolean beforeAddLine(String key) {
		return true;
	}

	/**
	 * 新增明细
	 */
	protected void doAddLine(String key) {
		if (!beforeAddLine(key))
			return;
		StorageDataSet detailDataSet = this.detailDataSets.get(key);
		if (!detailDataSet.atLast())
			detailDataSet.last();
		detailDataSet.insertRow(false);
		this.detailTables.get(key).requestFocusInWindow();
		afterAddLine(key);
	}

	protected void afterAddLine(String key) {

	}

	protected boolean beforeDelLine(String key) {
		return true;
	}

	/**
	 * 删除明细
	 */
	@SuppressWarnings("rawtypes")
	protected void doDelLine(final String key) {
		if (!beforeDelLine(key)) {
			return;
		}
		final StorageDataSet detailDataSet = this.detailDataSets.get(key);
		XTable detailTable = detailTables.get(key);
		if (detailTable.getSelectedRowCount() > 0) {
			int[] rowIndex = detailTable.getSelectedRows();
			final List<Serializable> ids = new ArrayList<Serializable>();
			final long[] deleteIndex = new long[rowIndex.length];
			final DetailMeta detailMeta=meta.getFdmMeta().getDetailMeta(key);
			
			String propertyKey = detailMeta.getBioMeta().getPrimaryProperty().getPropertyKey();
			String propertyType = detailMeta.getBioMeta().getPrimaryProperty().getDataType();
			int n = JOptionPane.showConfirmDialog(null, "是否确认删除" + detailTable.getSelectedRowCount() + "条记录?", "确认删除", JOptionPane.YES_NO_OPTION);
			if (n == 0) {
				for (int i = 0; i < rowIndex.length; i++) {
					detailDataSet.goToRow(rowIndex[i]);
					if (propertyType.equals(TypeUtil.STRING)) {
						String id = detailDataSet.getString(propertyKey);
						if(StringUtil.isNotEmpty(id)){
							ids.add(id);
						}
					}else{
						int id = detailDataSet.getInt(propertyKey);
						if(id!=0){
							ids.add(id);
						}
					}
					deleteIndex[i] = detailDataSet.getInternalRow();
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
							formService.deleteBio(detailMeta.getBioMeta().getBioKey(), deleteIds);
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
										detailDataSet.goToInternalRow(index);
										detailDataSet.deleteRow();
									}
									afterDelLine(key);
									loading = false;

								}
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
		} else {
			MessageBox.showInfo("请选择要删除的记录.");
		}
	}

	protected void afterDelLine(String key) {

	}

	protected void doCancel() {
		loading = true;// 设置成加载中
		masterDataSet.cancel();
		masterDataSet.reset();
		this.setModified(false);
		for (Map.Entry<String, StorageDataSet> entry : detailDataSets.entrySet()) {
			String key = entry.getKey() + "";
			StorageDataSet detailDataSet = entry.getValue();
			detailDataSet.cancel();
			detailDataSet.reset();
		}
		loading = false;
	}

	protected boolean beforeClose() {
		return true;
	}

	// 关闭窗体
	public void doClose() throws Exception {
		if (!beforeClose())
			return;
		this.mainToolBar = null;
		this.mainForm = null;
		this.masterDataSet.empty();
		this.masterDataSet.close();
		this.masterDataSet = null;
		for (Map.Entry<String, StorageDataSet> entry : detailDataSets.entrySet()) {
			String key = entry.getKey() + "";
			StorageDataSet detailDataSet = entry.getValue();
			detailDataSet.empty();
			detailDataSet.close();
			detailDataSet = null;
		}
		detailDataSets.clear();
		detailTables.clear();
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
	 * 
	 * @throws Exception
	 */
	protected void addDataSetListener() throws Exception {
		this.masterDataSet.addEditListener(new EditAdapter() {
			public void modifying(DataSet dataSet) {
				if (!loading) {
					setModified(true);

				}
			}

		});

		this.masterDataSet.addDataChangeListener(new DataChangeAdapter() {
			public void dataChanged(DataChangeEvent event) {
				if (!loading) {
					setModified(true);
				}

			}
		});

		List<DetailMeta> listDetailMeta = meta.getFdmMeta().getDetails();
		if (listDetailMeta != null) {
			detailDataSets = new HashMap<String, StorageDataSet>();
			detailTables = new HashMap<String, XTable>();

			for (DetailMeta dtlMeta : meta.getFdmMeta().getDetails()) {
				String dtlId = dtlMeta.getId();

				XTable dtlTable = (XTable) this.getComponent(dtlId);
				if (dtlTable == null) {
					MessageBox.showError("未设置detailTable id:" + dtlId);
				}
				StorageDataSet detailDataSet = (StorageDataSet) dtlTable.getDataSet();
				if (dtlMeta.getBioMeta() != null) {
					DataSetUtil.loadDataSetMetaFromBioMeta(detailDataSet, dtlMeta.getBioMeta());
				}
				detailDataSets.put(dtlId, detailDataSet);
				detailTables.put(dtlId, dtlTable);

			}
			// DataSetUtil.enableComponent(mainForm, false);
			// XSplitPanel mainSplit = (XSplitPanel)
			// this.getComponent("mainSplit");
			// mainSplit.setDividerLocation(200);

		}

		// 添加子表数据集事件
		for (Map.Entry<String, StorageDataSet> entry : detailDataSets.entrySet()) {
			// String key = entry.getKey() + "";
			StorageDataSet detailDataSet = entry.getValue();
			detailDataSet.addEditListener(new EditAdapter() {
				public void modifying(DataSet dataSet) {
					if (!loading) {
						setModified(true);

					}
				}

			});
			detailDataSet.addDataChangeListener(new DataChangeAdapter() {
				public void dataChanged(DataChangeEvent event) {
					if (!loading) {
						setModified(true);
					}

				}
			});
		}
	}

	protected BioSet getChanges() throws Exception {
		BioSet data = new BioSet();
		masterDataSet.post();
		Bio bioMaster = new Bio();
		DataSetUtil.dataRow2Bio(masterDataSet, bioMaster);
		data.setMaster(bioMaster);
		// 明细
		if (detailDataSets.size() > 0) {
			for(Map.Entry<String, StorageDataSet> entry:detailDataSets.entrySet()){
				List<Bio> detailBio = DataSetUtil.getChanges(entry.getValue());
				data.setDetail(entry.getKey(), detailBio);

			}
			
		}
		return data;

	}

	/**
	 * 是否修改
	 * 
	 * @return
	 */
	protected boolean isModified() {
		return modified;
	}

	protected void setFormEditable(boolean enable) {
		// 禁用控件
		FormUtil.setFormEditable(mainForm, enable);
		// 禁用子工具栏
		for (final Map.Entry<String, StorageDataSet> entry : detailDataSets.entrySet()) {
			XToolBar detailToolBar = (XToolBar) getComponent(entry.getKey() + "_ToolBar");
			if (detailToolBar != null) {
				for (Component com : detailToolBar.getComponents()) {
					com.setEnabled(enable);
				}
			}
			detailTables.get(entry.getKey()).setEditable(enable);
		}
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
	 * 不通过配置文件继承，需要调用次默认布局。
	 * 
	 * @throws Exception
	 */
	protected void setDefaultLayout() throws Exception {
		// this.setLayout(new BorderLayout());
		// // 设置工具栏
		// add(mainToolBar, BorderLayout.NORTH);
		// // 设置查询form和table
		// TableScrollPane lstTablePane = new TableScrollPane();
		// lstTablePane.setViewportView(mainTable);
		// JSplitPane splitPane = new JSplitPane();
		// splitPane.setOneTouchExpandable(true);
		// splitPane.setDividerSize(5);
		// splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		// splitPane.setDividerLocation(0.5);// 设置分割线位于中央;
		// splitPane.setTopComponent(mainQuery);
		// splitPane.setBottomComponent(lstTablePane);
		// add(splitPane, BorderLayout.CENTER);
		// rendererDefaultToolButton(();
		// // 添加事件
		// if (this.mainTable != null && mainTable.getDataSet() != null) {
		// mainDataSet = (StorageDataSet) mainTable.getDataSet();
		// }
		//
		// addDataSetListener();
		// setHotKey();// 设置快捷键
		// setToolbarStatus();

	}

	protected void rendererDefaultToolButton() {
		// 默认根据全局变量的值设置固定的按钮：保存、删除等
		EmptyBorder emptyButtonBorder = null;
		mainToolBar.setFloatable(false);
		int i = 0;
		if (showNewBtn) {
			btnNew = new XButton();
			emptyButtonBorder = new EmptyBorder(new Insets(3, 3, 3, 3));
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
		i++;
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					doLoad();
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

		if (showNewBtn) {
			hotKeyStrokeMap.put("NEW", newKey);
			hotKeyActionMap.put("NEW", new HotKeyAction("NEW"));
		}
		if (showSaveBtn) {
			hotKeyStrokeMap.put("SAVE", saveKey);
			hotKeyActionMap.put("SAVE", new HotKeyAction("SAVE"));
		}

		if (showCancelBtn) {
			hotKeyStrokeMap.put("CANCEL", cancelKey);
			hotKeyActionMap.put("CANCEL", new HotKeyAction("CANCEL"));
		}

		hotKeyStrokeMap.put("REFRESH", refreshKey);
		hotKeyActionMap.put("REFRESH", new HotKeyAction("REFRESH"));

		// 设置界面hotkey
		InputMap inputMap = ((JComponent) getContentPane()).getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap actionMap = ((JComponent) getContentPane()).getActionMap();
		for (String key : hotKeyActionMap.keySet()) {
			inputMap.put(hotKeyStrokeMap.get(key), key);
			actionMap.put(key, hotKeyActionMap.get(key));
		}

		// // 设置table hotKey
		// actionMap = mainTable.getActionMap();
		// inputMap =
		// mainTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		// for (String key : hotKeyActionMap.keySet()) {
		// inputMap.put(hotKeyStrokeMap.get(key), key);
		// actionMap.put(key, hotKeyActionMap.get(key));
		// }
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
				btnNew.setEnabled(false);
			}

			btnCancel.setEnabled(true);
			btnRefresh.setEnabled(false);
			if (btnAddLine != null) {
				btnAddLine.setEnabled(true);
			}
		} else {
			if (btnSave != null) {
				this.btnSave.setEnabled(false);
			}
			if (btnNew != null) {
				btnNew.setEnabled(true);
			}

			btnCancel.setEnabled(false);
			btnRefresh.setEnabled(true);
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
				if (key.equals("SAVE")) {
					doSave();
				} else if (key.equals("NEW")) {
					doNew();
				} else if (key.equals("REFRESH")) {
					doLoad();
				} else if (key.equals("CANCEL")) {
					doCancel();
				} else {
					doExecute(key);
				}
			}

		}
	}
}
