package com.ubsoft.framework.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;

public class FormTag extends BaseTag {	
	
	public int doStartTag() throws JspTagException {		
		
		StringBuffer body = new StringBuffer();
		body.append(" <form role=\"form\"");
		this.addDefatultProperty(body);
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
		body.append("</form>");
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
	}
}
