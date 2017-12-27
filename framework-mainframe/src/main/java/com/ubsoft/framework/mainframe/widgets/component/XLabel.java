package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import javax.swing.JLabel;

import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.metadata.model.widget.LabelMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@SuppressWarnings("serial")
public class XLabel extends JLabel implements IRenderer {
	
	@Override
	public Component render(WidgetMeta meta, Container parent,Map<String,Object> params) {
		LabelMeta compMeta = (LabelMeta) meta;		
		this.meta=compMeta;
		if(meta.getLabel()!=null){			
			this.setText(meta.getLabel());
		}
		RendererUtil.addComponent(compMeta, this, parent, params);

		return this;
	}
	
	private LabelMeta meta =null;
	@Override
	public LabelMeta getMeta() {
		return meta;
	}
}
