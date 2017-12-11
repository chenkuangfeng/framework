package com.ubsoft.framework.metadata.model.widget.tree;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.ubsoft.framework.metadata.model.widget.WidgetMeta;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement (name="tree")
public class TreeMeta extends WidgetMeta{

	@XmlAttribute
	private Boolean checkbox;

	public boolean isCheckBox() {
		if(checkbox==null){
			return false;
		}
		return checkbox;
	}
	public void setCheckBox(boolean showCheckBox) {
		this.checkbox = showCheckBox;
	}
	
	
	
}
