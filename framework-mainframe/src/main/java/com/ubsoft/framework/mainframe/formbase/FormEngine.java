package com.ubsoft.framework.mainframe.formbase;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import com.ubsoft.framework.core.exception.ComException;
import com.ubsoft.framework.mainframe.widgets.component.tree.XCheckBoxTree;
import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.MetaRegistry;
import com.ubsoft.framework.mainframe.widgets.util.MessageBox;
import com.ubsoft.framework.metadata.model.form.FormMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;
import com.ubsoft.framework.metadata.model.widget.grid.GridCellMeta;
import com.ubsoft.framework.metadata.model.widget.grid.GridMeta;
import com.ubsoft.framework.metadata.model.widget.grid.GridRowMeta;
import com.ubsoft.framework.metadata.model.widget.model.Align;
import com.ubsoft.framework.metadata.model.widget.model.Layout;
import com.ubsoft.framework.metadata.model.widget.tree.TreeMeta;

public class FormEngine {
	private static Map<String, FormMeta> formMeta = new HashMap<String, FormMeta>();
	private Map<String, Component> idmap = new HashMap<String, Component>();

	/**
	 * 根据配置对象生成界面
	 * 
	 * @param meta
	 * @param cmp
	 * @throws Exception
	 */
	public <T extends FormMeta> void rendererForm(FormMeta meta, Container cmp) throws Exception {
		if (cmp instanceof JInternalFrame) {
			JInternalFrame jif = (JInternalFrame) cmp;
			cmp = jif.getContentPane();
		}
		WidgetMeta wgMeta = (WidgetMeta) meta;
		for (WidgetMeta child : wgMeta.getChildren()) {
			renderer(child, cmp);
		}
	}

	/**
	 * 递归算法，绘制所有子控件。
	 * 
	 * @param meta
	 * @param cmp
	 * @param idMaps
	 * @throws
	 * @throws Exception
	 */
	private void renderer(WidgetMeta meta, Container cmp) throws Exception {
		Class<IRenderer> rendererClass = MetaRegistry.getRenderer(meta.getClass());
		if (rendererClass == null) {
			throw new ComException(ComException.MIN_ERROR_CODE_FDM, meta.toString() + " 组件没有注册！");
		}
		IRenderer cmpRenderer = null;
		// tree有两个控件，checkboxtree和jTree 通过checkbox属性来判断
		if (meta instanceof TreeMeta) {
			TreeMeta treeMeta = (TreeMeta) meta;
			if (treeMeta.isCheckBox()) {
				cmpRenderer = new XCheckBoxTree();
			} else {
				cmpRenderer = rendererClass.newInstance();
			}
		} else {
			cmpRenderer = rendererClass.newInstance();
		}
		Map<String, Object> params = new HashMap<String, Object>();
		cmpRenderer.render(meta, cmp, params);

		if (cmpRenderer instanceof Container) {
			Container ctn = (Container) cmpRenderer;
			if (meta.getChildren() != null) {
				// swing 没有像html table行和列布局的组件，交给Panel内部处理；
				if (meta.getLayout() != null && meta.getLayout().equals(Layout.FORMLAYOUT)) {
					if (meta.getChildren() != null && meta.getChildren().size() == 1 && meta.getChildren().get(0) instanceof GridMeta) {
						GridMeta gridMeta = (GridMeta) meta.getChildren().get(0);
						rendererFormLayout(ctn, gridMeta);
					} else {
						throw new ComException(ComException.MIN_ERROR_CODE_FDM, meta.toString() + " FormLayout 必须只有一个GridMeta的子控件！");
					}

				} else {
					for (WidgetMeta child : meta.getChildren()) {
						renderer(child, ctn);
					}
				}
			}
		}

		if (meta.getId() != null) {
			if (cmpRenderer instanceof Component) {
				if (!idmap.containsKey(meta.getId())) {
					idmap.put(meta.getId(), (Component) cmpRenderer);
				} else {
					MessageBox.showInfo(meta.getId()+"不能重复.");
				}
			}
		}

	}

	/**
	 * formLayout 布局实现
	 * 
	 * @param cmp
	 * @param gridMeta
	 * @throws Exception
	 */
	public void rendererFormLayout(Container cmp, GridMeta gridMeta) throws Exception {
		int rowIndex = 1;
		List<WidgetMeta> rows = gridMeta.getChildren();
		for (int r = 0; r < rows.size(); r++) {
			GridRowMeta rowMeta = (GridRowMeta) rows.get(r);
			List<WidgetMeta> cells = rowMeta.getChildren();
			int colIndex = 1;
			for (int c = 0; c < cells.size(); c++) {
				GridCellMeta cellMeta = (GridCellMeta) cells.get(c);
				int colSpan = 1;
				if (cellMeta.getColSpan() != null) {
					colSpan = cellMeta.getColSpan();
				}

				int rowSpan = 1;
				if (cellMeta.getRowSpan() != null) {
					rowSpan = cellMeta.getRowSpan();
				}

				if (cellMeta.getChildren() != null && cellMeta.getChildren().size() == 1) {
					WidgetMeta cmpMeta = cellMeta.getChildren().get(0);
					String width = cellMeta.getWidth();
					String height = cellMeta.getHeight();
					String align = cellMeta.getAlign();
					Class<IRenderer> rendererClass = MetaRegistry.getRenderer(cmpMeta.getClass());
					if (rendererClass == null) {
						throw new ComException(ComException.MIN_ERROR_CODE_FDM, cmpMeta.toString() + " 组件没有注册！");
					}
					IRenderer cmpRenderer = rendererClass.newInstance();
					GridBagConstraints ctr = new GridBagConstraints();
					Map<String, Object> params = new HashMap<String, Object>();
					ctr.gridx = colIndex;
					ctr.gridy = rowIndex;
					ctr.gridwidth = colSpan;
					ctr.gridheight = rowSpan;
					ctr.insets = new Insets(2, 2, 2, 2);

					if (width == null) {
						ctr.weightx = 0;
					} else {
						if (width.indexOf("%") != -1) {
							width = width.replaceAll("%", "");
							ctr.weightx = Integer.parseInt(width);
						}
					}
					if (height == null) {
						ctr.weighty = 0;
					} else {
						if (height.indexOf("%") != -1) {
							height = height.replaceAll("%", "");
							ctr.weighty = Integer.parseInt(height);
						}
					}
					if (align != null) {
						if (align.equals(Align.LEFT)) {
							ctr.anchor = GridBagConstraints.WEST;
						} else if (align.equals(Align.RIGHT)) {
							ctr.anchor = GridBagConstraints.EAST;
						} else if (align.equals(Align.TOP)) {
							ctr.anchor = GridBagConstraints.NORTH;
						} else if (align.equals(Align.TOPLEFT)) {
							ctr.anchor = GridBagConstraints.NORTHWEST;
						} else if (align.equals(Align.TOPRIGHT)) {
							ctr.anchor = GridBagConstraints.NORTHEAST;
						} else if (align.equals(Align.BOTTOM)) {
							ctr.anchor = GridBagConstraints.SOUTH;
						} else if (align.equals(Align.BOTTOMRIGHT)) {
							ctr.anchor = GridBagConstraints.SOUTHEAST;
						} else if (align.equals(Align.BOTTOMLEFT)) {
							ctr.anchor = GridBagConstraints.SOUTHWEST;
						} else {
							ctr.anchor = GridBagConstraints.WEST;
						}

					}
					ctr.fill = GridBagConstraints.HORIZONTAL;
					params.put("ctr", ctr);
					cmpRenderer.render(cmpMeta, cmp, params);
					if (cmpMeta.getId() != null) {
						if (cmpRenderer instanceof Component) {
							idmap.put(cmpMeta.getId(), (Component) cmpRenderer);
						}
					}
					// cmp.add((Component) cmpRenderer, ctr);
					if (cmpMeta.getChildren() != null) {
						for (WidgetMeta child : cmpMeta.getChildren()) {
							if (cmpRenderer instanceof Container) {
								renderer(child, (Container) cmpRenderer);
							}
						}
					}

				} else {
					throw new ComException(ComException.MIN_ERROR_CODE_FDM, cellMeta.toString() + " GridCellMeta 必须切只有一个子控件！");

				}
				colIndex += colSpan;
			}
			// 增加一行
			rowIndex++;

		}
		GridBagConstraints ctr = new GridBagConstraints();

		ctr.gridx = 1;
		ctr.gridy = rowIndex;
		// ctr.gridwidth = 10;
		// ctr.gridheight = rowSpan;
		// ctr.insets = new Insets(2, 2, 2, 2);
		ctr.weighty = 1;
		JLabel footerPanel = new JLabel();
		cmp.add(footerPanel, ctr);

	}

	/**
	 * 根据Id获取控件
	 * 
	 * @param id
	 * @return
	 */
	public Component getComponent(String id) {
		return idmap.get(id);
	}

	public FormMeta getFormMeta(String id) {
		return formMeta.get(id);
	}

	public FormMeta putFormMeta(String id, FormMeta meta) {
		return formMeta.put(id, meta);
	}
}
