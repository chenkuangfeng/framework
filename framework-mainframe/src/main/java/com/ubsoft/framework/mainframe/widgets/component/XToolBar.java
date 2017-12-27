package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;
import javax.swing.border.Border;

import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.metadata.model.widget.ToolBarMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@SuppressWarnings("serial")
public class XToolBar extends JToolBar implements IRenderer {

	@Override
	public Component render(WidgetMeta meta, Container parent, Map params) {
		ToolBarMeta compMeta = (ToolBarMeta) meta;
		this.setFloatable(false);
		this.meta = compMeta;

		//if (compMeta.getBorder() != null && compMeta.getBorder()==true) {
			//Border titleBorder = BorderFactory.createLineBorder(Color.black);
		Border titleBorder = BorderFactory.createRaisedBevelBorder();
			this.setBorder(titleBorder);
		//}

		RendererUtil.addComponent(compMeta, this, parent, params);
		return this;
	}

	private ToolBarMeta meta = null;

	@Override
	public ToolBarMeta getMeta() {
		return meta;
	}
}
