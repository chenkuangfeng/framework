package com.ubsoft.framework.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;

/**
 * 下拉框标签
 * 
 * @author chenkf
 * 
 */
public class SelectTag extends InputTag {
	protected String url;// 获取列表数据的地址
	protected String code;
	protected String valueField;
	protected String textField;

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

	public int doStartTag() throws JspTagException {
		Tag t = this.getParent();
		boolean inGridRow = false;
		boolean inFormRow = false;
		StringBuffer body = new StringBuffer();
		if (t != null && t instanceof GridRowTag) {
			inGridRow = true;
		}
		if (t != null && t instanceof FormRowTag) {
			inFormRow = true;
		}
		if (inGridRow) {
			body.append("<td class=\"gridlabel\">").append(label).append("</td>");
			body.append("<td class=\"gridinput\" ");
			this.addStringProperty(body, "rowspan", rowSpan);
			this.addStringProperty(body, "colspan", colSpan);
			body.append(">");
		}
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
			String tempLabel = label;
			if (this.required != null && required.equals("true")) {
				tempLabel += "<font color='red'>*</font>";
			}
			body.append(tempLabel).append("</label>");

		}
		// 添加公共属性
		body.append(" <select ");
		type = "select";
		addDefatultProperty(body);
		this.addStringProperty(body, "class", "form-control input-sm");

		if (id == null && field != null) {
			this.addStringProperty(body, "id", field);
		}
		this.addStringProperty(body, "type", "select");

		body.append(">");
		if (code != null) {
			addSelectOptions(code, body);
		}
		JspWriter out = this.pageContext.getOut();
		try {
			out.write(body.toString());
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Tag.EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		Tag t = this.getParent();
		boolean inGridRow = false;
		StringBuffer body = new StringBuffer();
		if (t != null && t instanceof GridRowTag) {
			inGridRow = true;
		}
		body.append("</select>");
		if (inGridRow) {
			body.append("</td>");
		}
		boolean inFormRow = false;
		if (t != null && t instanceof FormRowTag) {
			inFormRow = true;
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

	public void addDefatultProperty(StringBuffer input) {
		super.addDefatultProperty(input);

	}

	public void clearPropertyValue() {
		super.clearPropertyValue();
	}

}
