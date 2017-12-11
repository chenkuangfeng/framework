package com.ubsoft.framework.web.tag;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

/**
 * 列表列标签,查询条件需要放在第一样，所有生成在父类里面处理。
 * 
 * @author chenkf
 * 
 */
public class DataGridColumnTag extends InputTag implements Cloneable {
	protected String sortable;// 是否允许排序
	protected String filter;// 是否添加过滤框
	protected String format;
	protected String align;
	protected String editor;
	protected String displayField;

	public String getDisplayField() {
		return displayField;
	}

	public void setDisplayField(String displayField) {
		this.displayField = displayField;
	}

	public String getSortable() {
		return sortable;
	}

	public void setSortable(String sortable) {
		this.sortable = sortable;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public int doStartTag() throws JspTagException {
		return Tag.EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		Tag t = this.getParent();
		DataGridTag dataGrid = (DataGridTag) t;
		DataGridColumnTag colum = null;
		try {
			colum = (DataGridColumnTag) clone();
		} catch (CloneNotSupportedException e) {

		}
		dataGrid.addColumn(colum);

		return EVAL_PAGE;
	}

	public void release() {
		super.release();

	}

	/**
	 * 列属性不能与inputtag一致,需要单独处理,过滤条件可以一致
	 */
	public void addDefatultProperty(StringBuffer body) {

		addStringProperty(body, "id", id);
		addStringProperty(body, "width", width);
		addStringProperty(body, "height", height);
		// addStringProperty(body, "enable", enable);
		addStringProperty(body, "hidden", visible);
		this.addStringProperty(body, "field", field);
		if (displayField != null) {
			this.addStringProperty(body, "data-options", "displayField:'" + displayField + "'");
		}

		this.addStringProperty(body, "align", align);
		this.addStringProperty(body, "validType", vtype);

		/**
		 * 格式化,js函数 formatter: function(value,row,index){ if (row.user){ return
		 * row.user.name; } else { return value; } }
		 */
		this.addStringProperty(body, "formatter", format);
		/**
		 * 样式格式化,js函数 styler: function(value,row,index){ if (value < 20){ return
		 * 'background-color:#ffee00;color:red;'; // return
		 * {class:'c1',style:'color:red'}
		 * 
		 */
		this.addStringProperty(body, "styler", style);
		this.addStringProperty(body, "required", required);

		this.addStringProperty(body, "editor", editor);
		if (sortable == null) {
			this.addStringProperty(body, "sortable", "true");
		} else {
			this.addStringProperty(body, "sortable", sortable);
		}
		if (type != null && type.equals("checked")) {
			this.addStringProperty(body, "checkbox", "true");
		}

	}

	/**
	 * 添加input所有属性,datagridtag里面的过滤框调用
	 * 
	 * @param body
	 */
	public void addInputProperty(StringBuffer body) {
		addStringProperty(body, "id", id);
		addStringProperty(body, "style", style);
		addStringProperty(body, "class", css);
		this.addStringProperty(body, "field", filterKey == null ? field : filterKey);
		this.addStringProperty(body, "as", as);
		this.addStringProperty(body, "dataType", dataType);
		this.addStringProperty(body, "operater", operater);
		this.addStringProperty(body, "required", required);
		this.addStringProperty(body, "vtype", vtype);
		this.addStringProperty(body, "url", url);
		this.addStringProperty(body, "fromFields", fromFields);
		this.addStringProperty(body, "toFields", fromFields);
		this.addStringProperty(body, "code", code);
		this.addStringProperty(body, "valueField", valueField);
		this.addStringProperty(body, "textField", textField);
		this.addStringProperty(body, "placeholder", placeholder);
		this.addStringProperty(body, "label", label);
		this.addStringProperty(body, "rows", rows);
		// String midWidth=this.width;
		// width=null;
		// super.addDefatultProperty(body);
		// width=midWidth;

	}

	public void clearPropertyValue() {
		super.clearPropertyValue();
		filter = null;
		align = null;
		format = null;
		sortable = null;
		editor = null;
		filterKey = null;
		displayField = null;
	}

}
