package com.ubsoft.framework.mainframe.widgets.renderer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import com.ubsoft.framework.core.exception.ComException;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.mainframe.widgets.component.XSplitPanel;
import com.ubsoft.framework.mainframe.widgets.component.XTabPanel;
import com.ubsoft.framework.mainframe.widgets.component.layout.FormLayout;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;
import com.ubsoft.framework.metadata.model.widget.model.Layout;
import com.ubsoft.framework.metadata.model.widget.model.Region;

public class RendererUtil {
	public static void addLabelField(WidgetMeta meta, Component comp, Container parent, Map params) {
		JLabel label=new JLabel();
		JPanel panel= new JPanel();
		panel.setBorder(null);			
		panel.setLayout(new BorderLayout());
		label.setText(meta.getLabel()+" ");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setPreferredSize(new Dimension(60,10));
		panel.add(label,BorderLayout.WEST);
		panel.add(comp,BorderLayout.CENTER);
		RendererUtil.addComponent(meta, panel, parent, params);
	}

	public static void addComponent(WidgetMeta meta, Component cmp, Container parent, Map params) {
		// 设置共同属性
		setDefaultAttribute(cmp, meta);

		String region = meta.getRegion();
		String id = meta.getId();
		String layout = meta.getLayout(); // 设置layout
		if (layout != null && cmp instanceof Container) {
			Container ctn = (Container) cmp;
			if (layout.equals(Layout.BORDERLAYOUT)) {
				ctn.setLayout(new BorderLayout());
			} else if (layout.equals(Layout.FORMLAYOUT)) {
				ctn.setLayout(new FormLayout());
			}else if (layout.equals(Layout.FLOWLAYOUT)) {
				ctn.setLayout(new FlowLayout(FlowLayout.LEFT));
			}
		}
		// 获取父标签的布局
		LayoutManager parentLayout = parent.getLayout();
		if (parentLayout != null) {
			if (parentLayout instanceof BorderLayout) {
				if (StringUtil.isEmpty(region)) {
					throw new ComException(ComException.MIN_ERROR_CODE_FDM, meta.toString() + " region 不能为空！");
				}
				if (region.equals(Region.NORTH)) {
					parent.add(cmp, BorderLayout.NORTH);
				} else if (region.equals(Region.SOUTH)) {
					parent.add(cmp, BorderLayout.SOUTH);
				} else if (region.equals(Region.CENTER)) {
					parent.add(cmp, BorderLayout.CENTER);
				} else if (region.equals(Region.EAST)) {
					parent.add(cmp, BorderLayout.EAST);
				} else if (region.equals(Region.WEST)) {
					parent.add(cmp, BorderLayout.WEST);
				} else {
					throw new ComException(ComException.MIN_ERROR_CODE_FDM, meta.toString() + " region 设置不正确！");
				}
			} else if (parentLayout instanceof GridBagLayout) {
				if (params.get("ctr") != null) {
					GridBagConstraints ctr = (GridBagConstraints) params.get("ctr");
					parent.add(cmp, ctr);
				}
			} else {
				// JSplitPane layout为null
				if (parent instanceof XSplitPanel) {
					if (StringUtil.isEmpty(region)) {
						throw new ComException(ComException.MIN_ERROR_CODE_FDM, meta.toString() + " region 不能为空！");
					}
					XSplitPanel split = (XSplitPanel) parent;
					split.setBorder(null);
					// JScrollPane scr= new JScrollPane(cmp);
					// scr.setBorder(null);
					if (split.getOrientation() == JSplitPane.VERTICAL_SPLIT) {
						if (region.equals(Region.TOP)) {
							split.setTopComponent(cmp);
						} else if (region.equals(Region.BOTTOM)) {
							split.setBottomComponent(cmp);
						}
					} else {
						if (region.equals(Region.LEFT)) {
							split.setLeftComponent(cmp);
						} else if (region.equals(Region.RIGHT)) {
							split.setRightComponent(cmp);
						} else {
							throw new ComException(ComException.MIN_ERROR_CODE_FDM, meta.toString() + " region 设置不正确！");
						}
					}
				} else if (parent instanceof XTabPanel) {
					XTabPanel tabpanel = (XTabPanel) parent;
					JPanel p = (JPanel) cmp;
					p.setBorder(null);
					tabpanel.add(meta.getLabel(), cmp);
				} else {
					parent.add(cmp);
				}
			}
		}
	}

	private static void setDefaultAttribute(Component cmp, WidgetMeta meta) {
		if (meta.getEnabled() != null) {
			cmp.setEnabled(meta.getEnabled());

		}
		if (meta.getVisible() != null) {
			cmp.setVisible(meta.getVisible());
		}
		if (meta.getId() != null) {
			cmp.setName(meta.getId());
		}
		if (meta.getWidth() != null && meta.getHeight() != null) {
			cmp.setPreferredSize(new Dimension(Integer.parseInt(meta.getWidth()), Integer.parseInt(meta.getHeight())));
		}

	}

}