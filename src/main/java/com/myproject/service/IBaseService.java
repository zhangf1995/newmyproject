package com.myproject.service;

import com.myproject.domain.BaseDomain;
import com.myproject.query.BaseQuery;
import com.myproject.query.Page;

import java.io.Serializable;
import java.util.List;

public interface IBaseService<T extends BaseDomain> {
	void save(T o);
	void savePart(T o);
	void delete(Serializable id);
	void deleteBatch(Serializable[] idArr);
	void update(T o);
	void updatePart(T o);
	
	T get(Serializable id);
	/**
	 * 查询满足条件的总记录数
	 * @param query
	 * @return
	 */
	int queryTotal(BaseQuery query);
	/**
	 * 根据条件查询返回所有记录，注意：该方法不分页，如果要分页请使用queryPage方法
	 * @param query
	 * @return
	 */
	List<T> query(BaseQuery query);
	/**
	 * 查询所有数据
	 * @return
	 */
	List<T> query();
	/**
	 * 分页查询
	 * @param query
	 * @return
	 */
	Page<T> queryPage(BaseQuery query);
	
	/**
	 * 逻辑删除
	 * @param idArr
	 */
	void deleteLogic(Serializable[] idArr);
}
