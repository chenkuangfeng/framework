package com.ubsoft.framework.core.dal.model;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果类
 * 
 * @author Administrator
 * 
 * @param <T>
 */
public class PageResult<T> implements Serializable {


	private static final long serialVersionUID = 1L;

	private int pageNumber = 1;// 当前页码

	private int pageSize = 20;// 每页记录数
	private int total = 0;// 总记录数
	private int pageCount = 0;// 总页数?

	private List rows;// 数据List

	public PageResult() {
	}

	public int getPageNumber() {
		if (pageNumber > getPageCount())
			pageNumber = pageCount;
		if (pageNumber == 0)
			pageNumber = 1;
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize < 1) {
			pageSize = 1;
		}
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageCount() {
		pageCount = total / pageSize;
		if (total % pageSize > 0) {
			pageCount++;
		}
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

}
