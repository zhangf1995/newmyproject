package com.myproject.query;

import java.io.Serializable;
import java.util.List;

public class Pagefooter<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//当前页的数据
	private List<T> rows;
	//总的记录数
	private int total = 0;
	//当前第几页
	private int curPage = 1;
	//每页多少条数据，分页大小
	private int pageSize = 10;
	
	private List<T> footer;

	public List<T> getFooter() {
		return footer;
	}

	public void setFooter(List<T> footer) {
		this.footer = footer;
	}

	



	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	
	

	public Pagefooter() {
		super();
	}

	public Pagefooter(List<T> rows, int total, List<T> footer,BaseQuery query) {
		super();
		this.rows = rows;
		this.total = total;
		this.curPage = query.getPage();
		this.pageSize = query.getRows();
		this.footer = footer;
	}
	
}
