package com.ubsoft.framework.mainframe.widgets.component.combobox;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import com.borland.dbswing.JdbComboBox;
import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.mainframe.widgets.util.LookupManager;
import com.ubsoft.framework.metadata.model.widget.ComboBoxMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@SuppressWarnings("serial")
public class XComboBox extends JdbComboBox implements IRenderer {
	
	
	@Override
	public Component render(WidgetMeta meta, Container parent,Map<String,Object> params) {
		ComboBoxMeta compMeta = (ComboBoxMeta) meta;
		this.meta=compMeta;	
		//RendererUtil.addComponent(compMeta, this, parent);
		Boolean bind=compMeta.getBind()==null?true:compMeta.getBind();

		if(compMeta.getCode()!=null&&bind==false){
			LookupManager.bindComboxBox(this, compMeta.getCode());
		}
		if(meta.getLabel()!=null){
			RendererUtil.addLabelField(meta, this, parent, params);

		}else{
			RendererUtil.addComponent(compMeta, this, parent, params);
		}		
		
		return this;
	}
	
	ComboBoxMeta meta =null;
	@Override
	public ComboBoxMeta getMeta() {
		return meta;
	}
}
