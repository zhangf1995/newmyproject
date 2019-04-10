package com.myproject.query;

import com.alibaba.fastjson.JSON;
import com.myproject.core.consts.ConstMsg;
import com.myproject.core.consts.ConstUtils;
import com.myproject.core.consts.ICodes;
import com.myproject.core.utils.LocaleMessageSourceService;

import java.io.Serializable;

/**
 * 通用AJAX请求后的结果数据
 */
public class Ret implements Serializable{
	private static final long serialVersionUID = 8777098949345414562L;

	public static final String quote = "\"";
	/**
	 * 业务处理是否成功的状态
	 */
	private boolean success = true;
	/**
	 * 全局消息码
	 */
	private int code = ICodes.SUCCESS;
	/**
	 * 消息(根据消息常量的配置或国际化语言配置自动获取)
	 */
	private String message = "";
	/**
	 * 附加消息 ： 额外返回的消息
	 */
	private String info = "";
	/**
	 * 返回的数据
	 */
	private Object data = null;

	public static Ret me() {
		return new Ret();
	}

	public Ret() {
		this.code = ICodes.SUCCESS;
	}

	public Ret(int code) {
		this.code = code;
	}

	public Ret(boolean isSuccess, int code) {
		this.success = isSuccess;
		this.code = code;
	}

	public Ret(boolean success, int code, Object data) {
		this.data = data;
		this.success = success;
		this.code = code;
	}

	public Ret setSuccess(boolean isSuccess) {
		this.success = isSuccess;
		if (!this.success)
			this.code = ICodes.FAILED;
		return this;
	}

	public boolean isSuccess() {
		return this.success;
	}

	public Ret setCode(int code) {
		this.code = code;
		if(ICodes.FAILED==code){
			this.success = false;
		}else if (ICodes.SUCCESS == code) {
			this.success = true;
		}
		return this;
	}

	public int getCode() {
		return this.code;
	}

	/**
	 * 会根据消息码查找全局消息常量，并返回对应的消息内容（如果有国际化要求，会去找国际化配置中的消息）
	 * @return
	 */
	public String getMessage() {
		ConstMsg msgConst = ConstUtils.me().getMsgConst(code);
		String _msg = null;
		try{
			_msg = LocaleMessageSourceService.getMessage(msgConst.getKeyCode(),null,msgConst.getName());
		}catch (Exception e) {
			e.printStackTrace();
		}
		if (_msg == null)
			_msg = "";
		message = _msg;
		return message;
	}

	public String getInfo() {
		return this.info;
	}

	public Ret setInfo(String info) {
		this.info = info;
		return this;
	}

	public Ret setData(Object data) {
		this.data = data;
		return this;
	}

	public Object getData() {
		return this.data;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append(quote + "code" + quote + ":");
		sb.append(code);
		sb.append("," + quote + "success" + quote + ":");
		sb.append(success);
		sb.append("," + quote + "message" + quote + ":");
		sb.append(quote + encode(this.getMessage()) + quote);
		sb.append("," + quote + "info" + quote + ":");
		sb.append(quote + encode(info) + quote);
		if (null != this.data) {
			sb.append("," + quote + "data" + quote + ":");
			if (data instanceof String) {
				sb.append(data);
			} else {
				sb.append(JSON.toJSONString(data));
			}
		}
		sb.append("}");
		return sb.toString();
	}

	private String encode(String text) {
		if (null == text)
			return "";
		String _text = text.replace("\"", "“");
		return _text;
	}

}
