package com.myproject.service.impl;

import com.myproject.core.consts.ICodes;
import com.myproject.core.consts.bis.BoolConsts;
import com.myproject.core.consts.bis.DataStateConsts;
import com.myproject.core.utils.ClassUtils;
import com.myproject.core.utils.StrUtils;
import com.myproject.domain.BaseDomain;
import com.myproject.exception.BisException;
import com.myproject.mapper.BaseMapper;
import com.myproject.query.BaseQuery;
import com.myproject.query.Page;
import com.myproject.service.IBaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
public abstract class BaseServiceImpl<T extends BaseDomain> implements IBaseService<T> {
	@Autowired
	protected BaseMapper<T> baseMapper;
	
	private Serializable getId(T o){
		try{
			Method method = o.getClass().getMethod("getId");
			Serializable id = (Serializable) method.invoke(o);
			return id;
		}catch (Exception e) {
			throw BisException.me().setCode(ICodes.FAILED);
		}
	}
	
	@Transactional
	@Override
	public void save(T o) {
		try{
			Field idField = o.getClass().getDeclaredField("id");
			String idType = idField.getType().getName();
			if("java.lang.String".equals(idType)){
				Method getMethod = o.getClass().getMethod("getId");
				Object existId = getMethod.invoke(o);
				if(null==existId || "".equals(existId.toString().trim())){
					Method setMethod = o.getClass().getMethod("setId", String.class);
					String id = StrUtils.getId();
					setMethod.invoke(o, id);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		o.setCreateTime(new Date());
		if(StringUtils.isBlank(o.getCreateUser())){
			/*o.setCreateUser(UserContext.getUserSeq());*/
		}
		o.setUpdateTime(o.getCreateTime());
		o.setUpdateUser(o.getCreateUser());
		o.setDataState(DataStateConsts.USING);
		o.setSystemData(BoolConsts.NO);
		baseMapper.insert(o);
		ClassUtils.copyPropertiesIgnoreNull(get(getId(o)), o);
	}

	@Transactional
	@Override
	public void savePart(T o) {
		try{
			Field idField = o.getClass().getDeclaredField("id");
			String idType = idField.getType().getName();
			if("java.lang.String".equals(idType)){
				Method getMethod = o.getClass().getMethod("getId");
				Object existId = getMethod.invoke(o);
				if(null==existId || "".equals(existId.toString().trim())){
					Method setMethod = o.getClass().getMethod("setId", String.class);
					String id = StrUtils.getId();
					setMethod.invoke(o, id);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		o.setCreateTime(new Date());
		if(StringUtils.isBlank(o.getCreateUser())){
			/*o.setCreateUser(UserContext.getUserSeq());*/
		}
		o.setUpdateTime(o.getCreateTime());
		o.setUpdateUser(o.getCreateUser());
		o.setDataState(DataStateConsts.USING);
		o.setSystemData(BoolConsts.NO);
		baseMapper.insertSelective(o);
		ClassUtils.copyPropertiesIgnoreNull(get(getId(o)), o);
	}

	@Transactional
	@Override
	public void delete(Serializable id) {
		baseMapper.deleteByPrimaryKey(id);
	}
	
	@Transactional
	@Override
	public void deleteBatch(Serializable[] idArr) {
		baseMapper.deleteBatch(idArr);
	}

	@Transactional
	@Override
	public void update(T o) {
		T dbObj = null;
		try{
			Method method = o.getClass().getMethod("getId");
			Serializable id = (Serializable) method.invoke(o);
			dbObj = get(id);
			ClassUtils.copyPropertiesIgnoreNull(o, dbObj);
		}catch (Exception e) {
			e.printStackTrace();
		}
		dbObj.setUpdateTime(new Date());
		if(StringUtils.isBlank(o.getUpdateUser())){
			/*dbObj.setUpdateUser(UserContext.getUserSeq());*/
		}
		baseMapper.updateByPrimaryKey(dbObj);
		ClassUtils.copyPropertiesIgnoreNull(dbObj, o);
	}

	@Transactional
	@Override
	public void updatePart(T o) {
		o.setUpdateTime(new Date());
		if(StringUtils.isBlank(o.getUpdateUser())){
			/*o.setUpdateUser(UserContext.getUserSeq());*/
		}
		baseMapper.updateByPrimaryKeySelective(o);
		ClassUtils.copyPropertiesIgnoreNull(get(getId(o)), o);
	}
	
	@Transactional
	@Override
	public void deleteLogic(Serializable[] idArr) {
		for (Serializable id : idArr) {
			T t = get(id);
			if(t.getSystemData()!=null && BoolConsts.YES == t.getSystemData()){
				throw BisException.me().setCode(ICodes.SYSDATA_DELETE_FORBIDDEN);
			}
		}
		baseMapper.deleteLogic(idArr);
	}

	@Override
	public T get(Serializable id) {
		return baseMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public int queryTotal(BaseQuery query) {
		return baseMapper.queryTotal(query);
	}
	
	@Override
	public List<T> query(BaseQuery query) {
		if(null==query){
			query=new BaseQuery();
		}
		query.setRows(Integer.MAX_VALUE);
		return baseMapper.query(query);
	}
	
	@Override
	public List<T> query() {
		return this.query(null);
	}
	
	@Override
	public Page<T> queryPage(BaseQuery query) {
		int total = baseMapper.queryTotal(query);
		List<T> rows = baseMapper.query(query);
		return new Page<>(rows, total, query);
	}
}
