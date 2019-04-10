package com.myproject.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Domain对象的基类，包含所有domain的公共属性
 * @author nixianhua
 *
 */
public class BaseDomain implements Serializable{
	protected Date createTime;
	protected Date updateTime;
	protected String createUser;
	protected String updateUser;
	/**
     * 系统内置,const=BoolConsts-2:否:NO;1:是:YES
     */
    protected Byte systemData;

    /**
     * 数据状态,const=DataStateConsts-0:删除:DELETED;1:正常:USING
     */
    protected Byte dataState;
	
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Byte getSystemData() {
		return systemData;
	}
	public void setSystemData(Byte systemData) {
		this.systemData = systemData;
	}
	public Byte getDataState() {
		return dataState;
	}
	public void setDataState(Byte dataState) {
		this.dataState = dataState;
	}
}
