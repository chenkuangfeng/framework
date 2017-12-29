package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import javax.swing.JScrollPane;

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
		this.setLineWrap(true); 
		JScrollPane scr = new JScrollPane(this);
		//this.setRows(200);
		if(meta.getLabel()!=null){
		
			RendererUtil.addLabelField(meta, scr, parent, params);
		}else{
			RendererUtil.addComponent(compMeta, scr, parent, params);
		}		
		//this.setColumns(8);
		//RendererUtil.addComponent(compMeta, this, parent);
		return this;
	}
	
	TextAreaMeta meta =null;
	@Override
	public TextAreaMeta getMeta() {
		return meta;
	}
}
