package com.ubsoft.framework.metadata.model.widget;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name="panel")
public class PanelMeta extends WidgetMeta{
	
	@XmlAttribute
	private Boolean showTitle;

	public Boolean getShowTitle() {
		return showTitle;
	}

	public void setShowTitle(Boolean showTitle) {
		this.showTitle = showTitle;
	}

	
	
	
	
}
