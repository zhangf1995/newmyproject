package com.myproject.core.consts;
/**
 * 
 * 通用消息状态码常量
 * 所有业务消息需继承该接口，参考SampleMsgConsts
 * 2017年2月16日
 * @author nixianhua
 */
public interface ICodes {
	@ConstName(value="成功",code="SUCCESS")
	public static final int SUCCESS = 0;
	@ConstName("失败")
	public static final int FAILED = 1;
	@ConstName("需要登录")
	public static final int UNAUTHED = 2;
	@ConstName("非法访问")
	public static final int ILLEGAL_ACCESS = 3;
	@ConstName("用户名密码错误")
	public static final int INCORRECT_USR_PWD = 4;
	@ConstName("禁止登录")
	public static final int LOGON_FORBIDDEN = 5;
	@ConstName("用户名已注册")
	public static final int USERNAME_EXIST = 6;
	@ConstName("禁止删除")
	public static final int SYSDATA_DELETE_FORBIDDEN = 7;
	@ConstName("禁止作废")
	public static final int SYSDATA_OBSOLETE_FORBIDDEN = 8;
	@ConstName("没有权限")
	public static final int UNAUTHORIZED = 9;
	@ConstName("重复提交")
	public static final int REPEAT_SUBMIT = 10;
	@ConstName("禁止设备登录该账号")
	public static final int DEVICE_FORBIDDEN = 11;
	@ConstName("设备已绑定过账号")
	public static final int DEVICE_BIND_REPEAT = 12;
	@ConstName("无法确认默认门店")
	public static final int EMPTY_STORE = 13;
	@ConstName("无法确认默认仓库")
	public static final int EMPTY_WAREHOUSE = 14;
	@ConstName("已作废,禁止编辑")
	public static final int EDIT_FORBIDDEN_ON_DISCARD = 16;
}
