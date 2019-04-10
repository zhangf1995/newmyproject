package com.myproject.query;

import com.myproject.core.consts.bis.DataStateConsts;

/**
 * 所有Query对象的基类
 * @author nixianhua
 *
 */
public class BaseQuery {
	public static final String ASC = "asc";
	public static final String DESC = "desc";
	// 每页返回行数
	private int rows = 200;
	// 当前页码
	private int page = 1;
	// 排序列名
	private String sort;
	// 排序顺序
	private String order = ASC;
	// 通用关键字
	private String q;
	// ID
	private String id;
	// 数据状态
	private Byte dataState = DataStateConsts.USING;

	public int getRows() {
		return rows;
	}

	public void setRows(int pageSize) {
		this.rows = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
		if(null!=this.q){
			this.q = this.q.trim();
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Byte getDataState() {
		return dataState;
	}

	/**
	 * 获取数据的起始位置
	 * 
	 * @return
	 */
	public int getStart() {
		return (page - 1) * rows;
	}
}
