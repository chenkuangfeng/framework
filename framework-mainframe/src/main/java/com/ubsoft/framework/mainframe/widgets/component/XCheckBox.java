package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import com.borland.dbswing.JdbCheckBox;
import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.metadata.model.widget.CheckBoxMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@SuppressWarnings("serial")
public class XCheckBox extends JdbCheckBox implements IRenderer {

	@Override
	public Component render(WidgetMeta meta, Container parent, Map<String, Object> params) {
		CheckBoxMeta compMeta = (CheckBoxMeta) meta;
		this.meta = compMeta;
		if (compMeta.getLabel() != null) {
			this.setText(compMeta.getLabel());
		}
		RendererUtil.addComponent(compMeta, this, parent, params);
		return this;
	}

	CheckBoxMeta meta = null;

	@Override
	public CheckBoxMeta getMeta() {
		return meta;
	}
}
