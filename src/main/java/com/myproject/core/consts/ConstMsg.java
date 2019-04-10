package com.myproject.core.consts;
/**
 * 封装常量信息的对象
 * @author nixianhua
 *
 */
public class ConstMsg {
	/**
	 * 常量的值
	 */
	private Integer code;
	/**
	 * 常量默认的名字
	 */
	private String name;
	/**
	 * 常量对应的国际化语言变量
	 */
	private String keyCode;
	public static ConstMsg me(){
		return new ConstMsg();
	}
	public Integer getCode() {
		return code;
	}
	public ConstMsg setCode(Integer code) {
		this.code = code;
		return this;
	}
	public String getName() {
		return name;
	}
	public ConstMsg setName(String name) {
		this.name = name;
		return this;
	}
	public String getKeyCode() {
		return keyCode;
	}
	public ConstMsg setKeyCode(String keyCode) {
		this.keyCode = keyCode;
		return this;
	}
	
}
