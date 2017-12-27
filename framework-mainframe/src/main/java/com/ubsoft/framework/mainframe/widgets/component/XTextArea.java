package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import com.borland.dbswing.JdbTextArea;
import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.metadata.model.widget.TextAreaMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@SuppressWarnings("serial")
public class XTextArea extends JdbTextArea implements IRenderer {
	
	
	@Override
	public Component render(WidgetMeta meta, Container parent,Map<String,Object> params) {
		TextAreaMeta compMeta = (TextAreaMeta) meta;
		this.meta=compMeta;
		if(meta.getLabel()!=null){
			RendererUtil.addLabelField(meta, this, parent, params);

		}else{
			RendererUtil.addComponent(compMeta, this, parent, params);
		}		
		this.setColumns(8);
		//RendererUtil.addComponent(compMeta, this, parent);
		return this;
	}
	
	TextAreaMeta meta =null;
	@Override
	public TextAreaMeta getMeta() {
		return meta;
	}
}
