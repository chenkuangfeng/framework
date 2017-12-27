package com.ubsoft.framework.mainframe.widgets.component.tree;

import java.io.Serializable;
import java.util.List;

public class TreeNodeModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String icon;
	private String text;
	private String module;
	private Object[] params;

	private List<TreeNodeModel> children;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<TreeNodeModel> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNodeModel> children) {
		this.children = children;
	}

}
