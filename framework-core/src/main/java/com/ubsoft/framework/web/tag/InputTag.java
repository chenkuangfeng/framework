package com.ubsoft.framework.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;

/**
 * 输入框标签
 * 
 * @author chenkf
 * 
 */
public class InputTag extends BaseTag {
	protected String field;
	protected String filterKey;
	protected String type;// 类型:text,date,datetime,checkbox
	protected String as;// 查询条件别名
	protected String dataType;// 数据类型；
	protected String operater;// 查询操作符：
	protected String required;// 是否允许为空
	protected String vtype;
	protected String label;

	protected String placeholder;
	protected String url;// 获取列表数据的地址
	protected String code;
	protected String valueField;
	protected String textField;

	protected String fromFields;
	protected String toFields;

	protected String colSpan;
	protected String rowSpan;

	protected String rows;

	protected String defaultValue;

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getFromFields() {
		return fromFields;
	}

	public void setFromFields(String fromFields) {
		this.fromFields = fromFields;
	}

	public String getToFields() {
		return toFields;
	}

	public void setToFields(String toFields) {
		this.toFields = toFields;
	}

	public String getFilterKey() {
		return filterKey;
	}

	public void setFilterKey(String filterKey) {
		this.filterKey = filterKey;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getColSpan() {
		return colSpan;
	}

	public void setColSpan(String colSpan) {
		this.colSpan = colSpan;
	}

	public String getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(String rowSpan) {
		this.rowSpan = rowSpan;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValueField() {
		return valueField;
	}

	public void setValueField(String valueField) {
		this.valueField = valueField;
	}

	public String getTextField() {
		return textField;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAs() {
		return as;
	}

	public void setAs(String as) {
		this.as = as;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getOperater() {
		return operater;
	}

	public void setOperater(String operater) {
		this.operater = operater;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getVtype() {
		return vtype;
	}

	public void setVtype(String vtype) {
		this.vtype = vtype;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public int doEndTag() throws JspTagException {
		StringBuffer body = new StringBuffer();
		Tag t = this.getParent();
		boolean inGridRow = false;
		boolean inFormRow = false;
		if (t != null && t instanceof GridRowTag) {
			inGridRow = true;
		}
		if (t != null && t instanceof FormRowTag) {
			inFormRow = true;
		}
		// 如果在gridRow里面,添加td
		if (inGridRow) {
			body.append("<td class=\"gridlabel\">").append(label).append("</td>");
			body.append("<td class=\"gridinput\" ");
			this.addStringProperty(body, "rowspan", rowSpan);
			this.addStringProperty(body, "colspan", colSpan);
			body.append(">");
		}
		// 如果在formRow里面添加formgroup
		if (inFormRow) {
			body.append("<div");
			// 默认占满12格
			String classStr = "col-md-12";
			if (colSpan != null) {
				classStr = "col-md-" + colSpan;
			}
			this.addStringProperty(body, "class", classStr);
			body.append(">");
			body.append("<div class=\"form-group\">");
			if (id == null && field != null) {
				id = field;
			}
			body.append("<label for=\"").append(id).append("\">");
			if (label != null) {
				if (this.required != null && required.equals("true")) {
					String labelRequeird = label + "<font color='red'>*</font>";
					body.append(labelRequeird).append("</label>");
				} else {
					body.append(label).append("</label>");
				}
			} else {
				body.append("&nbsp;").append("</label>");
			}

		}
		// 添加公共属性
		if (type == null) {
			type = "text";
		}
		if (type.equals("text")) {
			body.append(" <input ");
			addDefatultProperty(body);
			if (css == null) {
				this.addStringProperty(body, "class", "form-control input-sm");
			}
			if (id == null && field != null) {
				this.addStringProperty(body, "id", field);
			}

			body.append("/>");

		} else if (type.equals("textarea")) {
			body.append(" <textarea ");
			addDefatultProperty(body);
			if (css == null) {
				this.addStringProperty(body, "class", "form-control input-sm");
			}
			if (id == null && field != null) {
				this.addStringProperty(body, "id", field);
			}

			body.append(">");
			body.append("</textarea>");
		} else if (type.equals("datepicker")) {

			body.append(" <input ");
			addDefatultProperty(body);
			this.addStringProperty(body, "type", "datepicker");
			if (id == null && field != null) {
				this.addStringProperty(body, "id", field);
			}
			this.addStringProperty(body, "class", "form-control input-sm");
			body.append("/>");
		} else if (type.equals("datetimepicker")) {
			body.append(" <input ");
			addDefatultProperty(body);
			this.addStringProperty(body, "type", "datetimepicker");
			if (id == null && field != null) {
				this.addStringProperty(body, "id", field);
			}
			this.addStringProperty(body, "class", "form-control input-sm");
			body.append("/>");
		}
		if (inGridRow) {
			body.append("</td>");
		}
		if (inFormRow) {
			body.append("</div>");
			body.append("</div>");

		}
		JspWriter out = this.pageContext.getOut();
		try {
			out.write(body.toString());
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			clearPropertyValue();
		}
		return EVAL_PAGE;
	}

	public void release() {
		super.release();

	}

	public void addDefatultProperty(StringBuffer body) {
		super.addDefatultProperty(body);
		// 实现过滤条件用,列表有别名的情况下影射到物理表
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
		this.addStringProperty(body, "defaultValue", defaultValue);
		if(enable!=null&&enable.equals("false")){
			body.append(" disabled ");
		}
		this.addStringProperty(body, "rows", rows);
		this.addStringProperty(body, "name", field);

	}

	public void clearPropertyValue() {
		id = null;
		width = null;
		height = null;
		as = null;
		vtype = null;
		enable = null;
		visible = null;
		style = null;
		css = null;
		field = null;
		type = null;// 类型:text,date,datetime,select,checkbox
		as = null;// 查询条件别名
		dataType = null;// 数据类型；
		operater = null;// 查询操作符：
		required = null;// 是否允许为空
		vtype = null;
		code = null;
		valueField = null;
		textField = null;
		url = null;
		fromFields = null;
		toFields = null;
		colSpan = null;
		rowSpan = null;
		placeholder = null;
		rows = null;
		defaultValue = null;
		filterKey = null;
	}

}
