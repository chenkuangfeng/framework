package com.ubsoft.framework.web.tag;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

import com.ubsoft.framework.core.context.AppContext;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.system.cache.MemoryLookup;
import com.ubsoft.framework.system.entity.LookupDetail;
import com.ubsoft.framework.system.service.ILookupDetailService;

public class BaseTag extends TagSupport {
	protected String id;
	protected String width;
	protected String height;
	protected String enable;
	protected String visible;;
	protected String style;
	protected String css;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	protected void addStringProperty(StringBuffer sb, String key, String value) {
		if (!StringUtil.isEmpty(value)) {
			sb.append(" " + key + "=\"" + value + "\"");
		}
	}

	public void addDefatultProperty(StringBuffer body) {
		addStringProperty(body, "id", id);
		addStringProperty(body, "width", width);
		addStringProperty(body, "height", height);
		
		//addStringProperty(body, "enable", enable);
		//addStringProperty(body, "visible", visible);
		addStringProperty(body, "style", style);
		addStringProperty(body, "class", css);

	}

	public void clearPropertyValue() {
		id = null;
		width = null;
		height = null;
		enable = null;
		visible = null;
		style = null;
		css = null;

	}

	protected String addSessionIdParams(String url) {
		String sessionId = this.pageContext.getRequest().getParameter("sid") == null ? "-1" : this.pageContext.getRequest().getParameter("sid");
		if (url.indexOf("?") == -1) {
			url += "?sid=" + sessionId;
		} else {
			url += "&sid=" + sessionId;
		}
		return url;

	}

	protected String getWebRoot() {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		return request.getContextPath();
	}

	protected void addSelectOptions(String code, StringBuffer body) {

		List<LookupDetail> listResult = MemoryLookup.getInstance().get(code);
		if (listResult == null) {
			ILookupDetailService lookupService = (ILookupDetailService) AppContext.getBean("lookupDetailService");
			listResult = lookupService.gets("lkKey=? and status=?", "seq", new Object[] { code, "1" });
			LookupDetail lkd = new LookupDetail();
			lkd.setLkdKey("");
			lkd.setLkdName("----");
			listResult.add(0, lkd);
			MemoryLookup.getInstance().put(code, listResult);
		}
		for (LookupDetail dtl : listResult) {
			body.append("<option value=\"").append(dtl.getLkdKey()).append("\">");
			body.append(dtl.getLkdName());
			body.append("</option>"); 
		}

	}

	protected List<LookupDetail> getLookupByCode(String code) {
		List<LookupDetail> listResult = MemoryLookup.getInstance().get(code);
		if (listResult == null) {
			ILookupDetailService lookupService = (ILookupDetailService) AppContext.getBean("lookupDetailService");
			listResult = lookupService.gets("lkKey=? and status=?", "seq", new Object[] { code, "1" });
			LookupDetail lkd = new LookupDetail();
			lkd.setLkdKey("");
			lkd.setLkdName("----");
			listResult.add(0, lkd);
			MemoryLookup.getInstance().put(code, listResult);
		}
		return listResult;
	}

}
