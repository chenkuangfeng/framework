package com.ubsoft.framework.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;

public class TreeTag extends BaseTag {
	private String checkable;
	private String url;

	public String getCheckable() {
		return checkable;
	}

	public void setCheckable(String checkable) {
		this.checkable = checkable;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int doStartTag() throws JspTagException {
		Tag t = this.getParent();
		StringBuffer body = new StringBuffer();
		body.append("<ul ");
		if (css == null) {
			this.addStringProperty(body, "class", "easyui-tree");
			String dataOptions = "lines:false";
			if (url != null) {
				dataOptions += ",url:'" + url + "'";
			}
			if (checkable != null) {
				// dataOptions+=",url:'"+url+"'";
			}
			this.addStringProperty(body, "data-options", dataOptions);

		}
		addDefatultProperty(body);
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
		StringBuffer body = new StringBuffer();
		body.append("</ul>");
		JspWriter out = this.pageContext.getOut();
		try {
			out.write(body.toString());
			out.flush();
		} catch (IOException e) {
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
		checkable = null;
		url = null;

	}
}
