package com.ubsoft.framework.mainframe.widgets.component;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import javax.swing.JSplitPane;

import com.ubsoft.framework.mainframe.widgets.renderer.IRenderer;
import com.ubsoft.framework.mainframe.widgets.renderer.RendererUtil;
import com.ubsoft.framework.metadata.model.widget.SplitPanelMeta;
import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@SuppressWarnings("serial")
public class XSplitPanel extends JSplitPane implements IRenderer {
	
	@Override
	public Component render(WidgetMeta meta, Container parent,Map<String,Object> params) {
		SplitPanelMeta compMeta = (SplitPanelMeta) meta;		
		this.meta=compMeta;	
		setOneTouchExpandable(true);
		//setDividerSize(5);
		String direction=compMeta.getDirection();
		if(direction!=null){
			if(direction.equals("h")){
				setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			}else if(direction.equals("v")){
				setOrientation(JSplitPane.VERTICAL_SPLIT);
			}else{
				setOrientation(JSplitPane.VERTICAL_SPLIT);
			}
		}else{
			setOrientation(JSplitPane.VERTICAL_SPLIT);
		}
		
		if(compMeta.getPosition()!=null){	
			setDividerLocation(compMeta.getPosition().intValue());
			//setDividerLocation(compMeta.getPosition());
	        //setResizeWeight(compMeta.getPosition());
		}
		setOneTouchExpandable(true);
		this.setAutoscrolls(false);
			//setDividerLocation(0.5);// 设置分割线位置。
		RendererUtil.addComponent(compMeta, this, parent, params);
		return this;
	}
	
	private SplitPanelMeta meta =null;
	@Override
	public SplitPanelMeta getMeta() {
		return meta;
	}
}
