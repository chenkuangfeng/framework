package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import javax.swing.JTabbedPane;

import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.metadata.model.widget.TabPanelMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@SuppressWarnings("serial")
public class XTabPanel extends JTabbedPane implements IRenderer {	
	public XTabPanel(){
	
	}
	@Override
	public Component render(WidgetMeta meta, Container parent,Map<String,Object> params) {
		TabPanelMeta compMeta = (TabPanelMeta) meta;		
		this.meta=compMeta;
		RendererUtil.addComponent(compMeta, this, parent, params);
		return this;
	}	
	private TabPanelMeta meta =null;
	@Override
	public TabPanelMeta getMeta() {
		return meta;
	}
	public int findTab(String title){
		for(int i=0;i<this.getTabCount();i++){					
            String header = this.getTitleAt(i); //获得选项卡标签
            if(header.equals(title)){
            	return i;
            }          
		}
		return -1;
	}
}
