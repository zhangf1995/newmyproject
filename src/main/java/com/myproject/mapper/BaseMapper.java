package com.myproject.mapper;


import com.myproject.domain.BaseDomain;
import com.myproject.query.BaseQuery;

import java.io.Serializable;
import java.util.List;

public interface BaseMapper<T extends BaseDomain> {
	void insert(T o);
	void insertSelective(T o);
	void deleteByPrimaryKey(Serializable id);
	void updateByPrimaryKey(T o);
	void updateByPrimaryKeySelective(T o);
	T selectByPrimaryKey(Serializable id);
	
	void deleteBatch(Serializable[] idArr);
	int queryTotal(BaseQuery query);
	List<T> query(BaseQuery query);
	
	void deleteLogic(String[] idArr);
}
