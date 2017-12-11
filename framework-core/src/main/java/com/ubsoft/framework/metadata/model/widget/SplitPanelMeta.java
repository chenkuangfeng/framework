package com.ubsoft.framework.metadata.model.widget;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "splitpanel")
public class SplitPanelMeta extends WidgetMeta {

	@XmlAttribute
	private Integer dividerSize;
	
	@XmlAttribute
	private Double position;
	
	@XmlAttribute
	private String direction ;//Vertical,Horizontal

	public Integer getDividerSize() {
		return dividerSize;
	}

	public void setDividerSize(Integer dividerSize) {
		this.dividerSize = dividerSize;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Double getPosition() {
		return position;
	}

	public void setPosition(Double position) {
		this.position = position;
	}
	
	
	
	

}
