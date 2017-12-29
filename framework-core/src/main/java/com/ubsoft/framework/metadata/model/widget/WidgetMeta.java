package com.ubsoft.framework.metadata.model.widget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.ubsoft.framework.metadata.model.widget.grid.GridCellMeta;
import com.ubsoft.framework.metadata.model.widget.grid.GridMeta;
import com.ubsoft.framework.metadata.model.widget.grid.GridRowMeta;
import com.ubsoft.framework.metadata.model.widget.menu.MenuMeta;
import com.ubsoft.framework.metadata.model.widget.table.TableColumnMeta;
import com.ubsoft.framework.metadata.model.widget.table.TableMeta;
import com.ubsoft.framework.metadata.model.widget.tree.TreeItemMeta;
import com.ubsoft.framework.metadata.model.widget.tree.TreeMeta;

@XmlRootElement (name="widget")
@XmlAccessorType(XmlAccessType.FIELD)
public class WidgetMeta implements Serializable {
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String label;
	@XmlAttribute
	private String width;
	@XmlAttribute
	private String height;
	
	@XmlAttribute
	private Boolean editable;;
	
	@XmlAttribute
	private Boolean enabled;
	
	@XmlAttribute
	private Boolean visible;
	
	@XmlAttribute
	private String align;
	
	@XmlAttribute
	private String tooltip;
	
	@XmlAttribute
	private String layout;
	@XmlAttribute
	private Boolean border;

	
//	@XmlAttribute
//	private Boolean fit;
	
	//layout布局区域:North,Source,West,East,Center, SplitPanel:Left,Right,Top,Bottom;
	@XmlAttribute
	private String region; 	

	@XmlElements({// 表示或的关系，list中内容可以为以下两种类型	
	@XmlElement(name = "label", type = LabelMeta.class), 
	
	@XmlElement(name = "checkbox", type = CheckBoxMeta.class), 
	@XmlElement(name = "combobox", type =ComboBoxMeta.class), 
	@XmlElement(name = "lookup", type = LookupFieldMeta.class), 
	@XmlElement(name = "date", type = DateFieldMeta.class), 
	@XmlElement(name = "datetime", type = DateTimeFieldMeta.class), 
	@XmlElement(name = "hidden", type = HiddenFieldMeta.class), 
	@XmlElement(name = "text", type = TextFieldMeta.class), 	
	@XmlElement(name = "textarea", type = TextAreaMeta.class), 	

	@XmlElement(name = "grid", type = GridMeta.class), 
	@XmlElement(name = "row", type = GridRowMeta.class), 
	@XmlElement(name = "cell", type = GridCellMeta.class), 
	@XmlElement(name = "datagrid", type = TableMeta.class), 
	@XmlElement(name = "column", type = TableColumnMeta.class), 
	@XmlElement(name = "tabpanel", type = TabPanelMeta.class), 	
	@XmlElement(name = "tree", type = TreeMeta.class), 
	@XmlElement(name = "treeitem", type =TreeItemMeta.class), 
	@XmlElement(name = "splitpanel", type = SplitPanelMeta.class), 
	@XmlElement(name = "panel", type = PanelMeta.class), 
	@XmlElement(name = "button", type = ButtonMeta.class), 
	@XmlElement(name = "toobar", type = ToolBarMeta.class),
	@XmlElement(name = "menu", type = MenuMeta.class) })	
	private List<WidgetMeta> children;
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	
	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	
	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	
//	public Boolean getFit() {
//		return fit;
//	}
//
//	public void setFit(Boolean fit) {
//		this.fit = fit;
//	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	
	

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public List<WidgetMeta> getChildren() {
		return children;
	}

	
	
	
	

	public Boolean getBorder() {
		return border;
	}

	public void setBorder(Boolean border) {
		this.border = border;
	}

	public void addChildren(WidgetMeta widgetMeta){
		if(children==null)
			children= new ArrayList<WidgetMeta>();
		children.add(widgetMeta);
	}


	


}
