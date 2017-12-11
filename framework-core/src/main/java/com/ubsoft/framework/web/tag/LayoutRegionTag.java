package com.ubsoft.framework.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;

public class LayoutRegionTag extends BaseTag {

	protected String name;
	protected String split;
	protected String title;
	public String getName() {
		return name;
	}	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSplit() {
		return split;
	}
	public void setSplit(String split) {
		this.split = split;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int doStartTag() throws JspTagException {
		Tag t = this.getParent();
		
		StringBuffer body = new StringBuffer();

		body.append("<div  ");
		this.addDefatultProperty(body);
		String dataOptions="region:'"+name+"',border:false";
		if(split!=null){
			dataOptions+=",split:"+split;
		}	
		if(title!=null){
			dataOptions+=",title:'"+title+"' ";
		}	
		this.addStringProperty(body, "class", "easyui-panel");	
		this.addStringProperty(body, "data-options", dataOptions);	
		//overflow:hidden
		//this.addStringProperty(body, "fit", "true");	

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
		name=null;
		split=null;
				
		
		
	}
}
