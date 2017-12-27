package com.ubsoft.framework.mainframe.widgets.renderer;

import java.awt.Component;
import java.awt.Container;
import java.util.Map;

import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

public interface IRenderer {
	public  Component render(WidgetMeta meta, Container parent, Map<String, Object> params);
	
	public WidgetMeta getMeta();
}
