package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import com.borland.dbswing.JdbTextField;
import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.metadata.model.widget.TextFieldMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@SuppressWarnings("serial")
public class XTextField extends JdbTextField implements IRenderer {
	
	
	@Override
	public Component render(WidgetMeta meta, Container parent,Map<String,Object> params) {
		TextFieldMeta compMeta = (TextFieldMeta) meta;
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
	
	TextFieldMeta meta =null;
	@Override
	public TextFieldMeta getMeta() {
		return meta;
	}
}
