package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.metadata.model.widget.PanelMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@SuppressWarnings("serial")
public class XPanel extends JPanel implements IRenderer {

	@Override
	public Component render(WidgetMeta meta, Container parent, Map<String, Object> params) {
		PanelMeta compMeta = (PanelMeta) meta;
		this.meta = compMeta;		
		// 默认不显示border
		if (compMeta.getBorder() != null && compMeta.getBorder()==true) {			
			Border titleBorder = BorderFactory.createTitledBorder(compMeta.getLabel());			
			this.setBorder(titleBorder);
		}
		RendererUtil.addComponent(compMeta, this, parent, params);
		return this;
	}

	private PanelMeta meta = null;

	@Override
	public PanelMeta getMeta() {
		return meta;
	}
}
