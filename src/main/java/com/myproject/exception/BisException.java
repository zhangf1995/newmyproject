package com.myproject.exception;


import com.myproject.core.consts.ConstMsg;
import com.myproject.core.consts.ConstUtils;
import com.myproject.core.consts.ICodes;
import com.myproject.core.utils.LocaleMessageSourceService;
import com.myproject.query.Ret;

/**
 * 通用业务异常类
 * @author nixianhua
 *
 */
public class BisException extends RuntimeException{
	private static final long serialVersionUID = -4640391473282791792L;
	/**
	 * 全局消息码
	 */
	private int code = ICodes.FAILED;
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
	/**
	 * 转换后对应的Ret对象
	 */
	private Ret ret = null;
	
	public static BisException me(){
		return new BisException();
	}
	
	public BisException() {
		this.ret = Ret.me().setSuccess(false);
	}

	public BisException setCode(int code){
		this.code = code;
		this.ret.setSuccess(false).setCode(code);
		return this;
	}
	
	public int getCode() {
		return code;
	}

	/**
	 * 会根据消息码查找全局消息常量，并返回对应的消息内容（如果有国际化要求，会去找国际化配置中的消息）
	 * @return
	 */
	public String getMessage() {
		ConstMsg msgConst = ConstUtils.me().getMsgConst(code);
		String _msg = LocaleMessageSourceService.getMessage(msgConst.getKeyCode(),null,msgConst.getName());
		if(null==_msg) _msg = "";
		message = _msg;
		return message;
	}
	
	public String getInfo(){
		return this.info;
	}
	
	public BisException setInfo(String info){
		this.info = info;
		this.ret.setInfo(info);
		return this;
	}
	
	public BisException setData(Object data){
		this.data = data;
		this.ret.setData(data);
		return this;
	}
	
	public Object getData(){
		return this.data;
	}
	
	public Ret getRet(){
		return this.ret;
	}
	
	public String toString(){
		return this.ret.toString();
	}
}