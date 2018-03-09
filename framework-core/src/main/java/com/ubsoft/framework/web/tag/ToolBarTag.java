package com.ubsoft.framework.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;

public class ToolBarTag extends BaseTag {
	protected String showPager = "true";
	protected String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShowPager() {
		return showPager;
	}

	public void setShowPager(String showPager) {
		this.showPager = showPager;
	}

	public int doStartTag() throws JspTagException {
		Tag t = this.getParent();
		StringBuffer body = new StringBuffer();
		body.append("<div class=\"btn-toolbar\" style=\"padding:0px\">");
		title=null;
		if (title != null) {
			body.append("<div class=\"btn-group\"> ");
			body.append("	<div class=\"box-header\" style=\"padding-top:5px;padding-bottom:2px\">");
			body.append("<i class=\"fa fa-th\"></i>");
			body.append("<h7 style=\"font-size:16px;\" class=\"box-title\">" + title + "</h7>");
			body.append("</div>");
			body.append("</div>");
		}

		// addStringProperty(body, "id", id);
		// body.append(">");
		if (showPager == null || showPager.equals("true")) {
			body.append("<div class=\"btn-group\"> ");
			body.append("<div  ");
			addStringProperty(body, "id", id + "_pagerbar");
			addStringProperty(body, "style", "color:black;margin-top:0px;margin-bottom:0px;height:25px");// background:#efefef;border:1px
			// solid
			//addStringProperty(body, "class", "easyui-pagination");
																		// #ddd;
			// addStringProperty(body, "data-options",
			// "buttons:$('#"+id+"_pg')");
			// addStringProperty(body, "showPageList", "false");
			// addStringProperty(body, "showRefresh", "false");
			// addStringProperty(body, "displayMsg", "");
			body.append("></div>");
			body.append("</div>");

			// //pager buttons
			// body.append("<div class=\"btn-group\" ");
			// addStringProperty(body, "id", id+"_pg");
			// addStringProperty(body, "style",
			// "background:#efefef;border:1px solid #ddd;height:35px;");

		}
		body.append("<div class=\"btn-group\"> ");

		// } else {
		//
		// addStringProperty(body, "id", id);
		// //addStringProperty(body, "style",
		// "background:#efefef;border:1px solid #ddd;height:35px");
		// body.append(">");
		// if(title!=null){
		// body.append("<div>").append(title).append("</div>");
		// }
		// }

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
		this.showPager = null;
		title = null;

	}

}
