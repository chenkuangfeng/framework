package com.ubsoft.framework.core.dal.model;

import java.io.Serializable;

public class QueryModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean page;// 是否分页
	private int pageSize;// 页数据
	private int pageNo;//
	private String orderBy;
	private ConditionTree conditionTree;
	private int limit;
	
	private String unitName;

	private String fdmId;
	
	

	public boolean isPage() {
		return page;
	}

	public void setPage(boolean page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public ConditionTree getConditionTree() {
		return conditionTree;
	}

	public void setConditionTree(ConditionTree conditionTree) {
		this.conditionTree = conditionTree;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getFdmId() {
		return fdmId;
	}

	public void setFdmId(String fdmId) {
		this.fdmId = fdmId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	
	
	
	

}
