package com.myproject.core.consts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 常量注解
 * 2017年2月16日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD )
public @interface ConstName {
	/**
	 * 消息的编码代码（国际化使用）
	 * @return
	 */
	public String code() default "";
	public String value() default "";
}
