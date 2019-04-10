package com.myproject.core.consts;

import com.myproject.core.utils.ClassUtils;
import com.myproject.core.utils.KV;
import com.myproject.core.utils.LocaleMessageSourceService;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

public class ConstUtils {
	public static final String SCAN_PACKAGE_NAME = "cn.cxby.cerp.core.consts";
	public static final String MSG_CODE_PREFIX = "const.msg.code.";
	public static final String BIS_CODE_PREFIX = "const.bis.code.";
	private final Map<Integer,ConstMsg> CODESMAP = new TreeMap<Integer,ConstMsg>();
	private final Map<Class,List<KV<Integer,ConstMsg>>> BISMAP =  new HashMap<>();
	private static ConstUtils instance;
	
	public ConstUtils() {
		initCodesMap();
	}
	
	public static ConstUtils me(){
		if(null==instance){
			instance =  new ConstUtils();
		}
		return instance;
	}
	
	/**
	 * 初始化代码表
	 */
	private void initCodesMap(){
		List<Class<?>> constClassList = null;
		constClassList = ClassUtils.getClassList(SCAN_PACKAGE_NAME, true);
		for(Class<?> c:constClassList){
			if(ICodes.class.isAssignableFrom(c)){

				List<KV<Integer,ConstMsg>> kvList = getKvList(c,MSG_CODE_PREFIX);
				for(KV<Integer,ConstMsg> kv:kvList){
					CODESMAP.put(kv.getK(), kv.getV());
				}
			}else if(c.getSimpleName().endsWith("Consts")){
				List<KV<Integer,ConstMsg>> kvList = getKvList(c,BIS_CODE_PREFIX);
				BISMAP.put(c, kvList);
			}
		}
	}
	
	
	/**
	 * @param clazz 常量类
	 * @return 返回 常量<值,消息>键值对列表
	 */
	private List<KV<Integer,ConstMsg>> getKvList(Class<?> clazz,String codePrefix){
		if(BIS_CODE_PREFIX.equals(codePrefix)){
			codePrefix += clazz.getSimpleName() + "."; 
		}
		Field fields[]=clazz.getDeclaredFields();
		List<KV<Integer,ConstMsg>> constList = new ArrayList<KV<Integer,ConstMsg>>();
		for(Field f:fields){
			try {
				Integer k = new Integer(f.getInt(null));
				ConstName cn = f.getAnnotation(ConstName.class);
				if(null!=cn){
					String value = cn.value();
					String code = cn.code();
					if(StringUtils.isBlank(code)){
						code=codePrefix+k;
					}else{
						code=codePrefix+code;
					}
					ConstMsg msg = ConstMsg.me().setCode(k).setKeyCode(code).setName(value);
					KV<Integer,ConstMsg> kv = new KV<Integer, ConstMsg>(k,msg);
					constList.add(kv);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return constList;
	
	}
	
	
	/**
	 * 读取指定消息代码的消息文本
	 * @param code 消息代码
	 * @return 返回消息
	 */
	public ConstMsg getMsgConst(int code){
		return CODESMAP.get(code);
	}
	
	/**
	 * 获取业务常量的映射列表
	 * @param clazz 常量类
	 * @return
	 */
	public List<KV<Integer, ConstMsg>> getBisConstList(Class clazz){
		return BISMAP.get(clazz);
	}

	public Map<Class, List<KV<Integer, ConstMsg>>> getBISMAP() {
		return BISMAP;
	}

	public Map<Integer, ConstMsg> getCODESMAP() {
		return CODESMAP;
	}
	
	/**
	 * 根据业务常量类和对应的值获取常量的名字
	 * @param BisConstClass 业务常量类
	 * @param constValue 业务常量值
	 * @return
	 */
	public Object getBisConstName(Class<?> bisConstClass,Byte constValue){
		return getBisConstName(bisConstClass, constValue.intValue());
	}
	public Object getBisConstName(Class<?> bisConstClass,Integer constValue){
		List<KV<Integer, ConstMsg>> kvList = getBisConstList(bisConstClass);
		for (KV<Integer, ConstMsg> kv : kvList) {
			Integer k = kv.getK();
			ConstMsg msg = kv.getV();
			String name = LocaleMessageSourceService.getMessage(msg.getKeyCode(),null,msg.getName());
			if(constValue.equals(k)){
				return name;
			}
		}
		return null;
	}
}