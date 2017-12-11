package com.ubsoft.framework.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;

public class PanelTag extends BaseTag {

	private String title;
	private String iconCls;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int doStartTag() throws JspTagException {
		Tag t = this.getParent();
		StringBuffer body = new StringBuffer();
		
		body.append("<div style=\"margin:0px;width:99%\" class=\"box box-primary\">  ");
		body.append("<div class=\"box-header with-border\">");
		if (iconCls != null) {
			body.append("<i class=\"" + iconCls + "\"></i>");
		}
		body.append("<h5 class=\"box-title\">");
		body.append(title).append("</h5>");
		body.append("</div>");
		body.append(" <div class=\"box-body\">");
		this.addDefatultProperty(body);

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
		body.append("</div>");
		body.append("</div>");
		

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
		title = null;

	}
}
