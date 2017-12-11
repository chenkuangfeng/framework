package com.ubsoft.framework.web.tag;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;

/**
 * 查询弹出框标签
 * 
 * @author chenkf
 * 
 */
public class SearchTag extends InputTag {

	public int doStartTag() throws JspTagException {
		Tag t = this.getParent();
		StringBuffer body = new StringBuffer();
		boolean inGridRow = false;
		boolean inFormRow = false;
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
			body.append("<div class=\"input-group input-group-sm\">");

		}
		String searchFunName = null;

		// 添加公共属性
		body.append(" <input ");
		type = "search";
		addDefatultProperty(body);
		if (css == null) {
			// this.addStringProperty(body, "class", "easyui-searchbox");
		}
		// this.addStringProperty(body, "class", "form-control");

		if (id == null && field != null) {
			this.addStringProperty(body, "id", field);
		}
		if (css == null) {
			this.addStringProperty(body, "class", "form-control");
		}
		if (url != null && fromFields != null) {
			// this.addStringProperty(body, "data-options",
			// "searcher:"+searchFunName);
		}
		this.addStringProperty(body, "type", type);
		body.append(">");
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
		boolean inFormRow = false;
		StringBuffer body = new StringBuffer();
		if (t != null && t instanceof GridRowTag) {
			inGridRow = true;
		}
		body.append("</input>");
		
		if (inGridRow) {
			body.append("</td>");
		}
		if (t != null && t instanceof FormRowTag) {
			inFormRow = true;
		}
		if (inFormRow) {
			body.append("<div class=\"input-group-btn\">");
			body.append("<button id=\"" + id + "_btn\"" + " type=\"button\" style=\"padding:6px\" class=\"btn btn-default\" ><span style=\"font-size:16px\"	class=\"glyphicon glyphicon-search\"/>" + "</button>");
			body.append("</div>");
			body.append("</div>");
			body.append("</div>");
			body.append("</div>");

		}
		if (url != null && fromFields != null) {
			addSearchFuction(body, id);
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

	// 选择框选择事件
	private String addSearchFuction(StringBuffer body, String id) {
		String searchFunName = "F_" + UUID.randomUUID().toString().replaceAll("-", "");
		body.append("<script>");
		// 单击选择事件
		body.append("function ").append(searchFunName).append("(value,name){");
		if (url.indexOf("?") == -1) {
			url += "?callback=" + searchFunName + "_callback";
		}
		body.append("app.form.openSelectWindow('" + label + "选择','" + url + "','800','500');");
		body.append("};");
		// 选择后回调函数
		body.append("function ").append(searchFunName + "_callback").append("(rows){");
		body.append("return app.form.setSelectResult(rows," + "\"" + fromFields + "\"," + "\"" + toFields + "\");");
		body.append("};");
		body.append("$(\"#" + id + "\").click(function() {");
		body.append(searchFunName+"('','');");
		body.append("});");
		body.append("$(\"#" + id + "_btn\").click(function() {");
		body.append(searchFunName+"('','');");
		body.append("});");

		body.append("</script>");
		return searchFunName;
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
