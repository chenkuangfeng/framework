package com.ubsoft.framework.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;

import com.ubsoft.framework.system.model.Subject;

public class ToolBarButtonTag extends BaseTag {
	protected String iconCls;// 图标
	protected String permKey;// 权限ID

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getPermKey() {
		return permKey;
	}

	public void setPermKey(String permKey) {
		this.permKey = permKey;
	}

	public int doStartTag() throws JspTagException {
		
		Tag t = this.getParent();
		if(this.permKey!=null){
			if(!Subject.getSubject().isPermitted(permKey)){
				return Tag.SKIP_BODY;
			}
		}
		StringBuffer body = new StringBuffer();
		body.append("<button style=\"margin-top:2px;margin-bottom:2px;padding-left:5px;padding-rigth:5px;padding-top:2px;padding-bottom:2px\" class=\"btn btn-small btn-default\"");
		if (permKey != null) {
			if (Subject.getSubject() != null && !Subject.getSubject().isPermitted(permKey)) {
				this.addStringProperty(body, "disabled", "disabled");
			}
		}
		addDefatultProperty(body);
		body.append(">");
		body.append("<span");
		if (iconCls != null) {
			this.addStringProperty(body, "class", iconCls);
		}
		body.append("></span>");
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
		if(this.permKey!=null){
			if(!Subject.getSubject().isPermitted(permKey)){
				return Tag.SKIP_BODY;
			}
		} 
		
		StringBuffer body = new StringBuffer();
		body.append("</button>");

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

	public void addDefatultProperty(StringBuffer body) {
		super.addDefatultProperty(body);

	}

	public void clearPropertyValue() {
		super.clearPropertyValue();
		iconCls = null;
		permKey = null;

	}

}
