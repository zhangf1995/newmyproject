package com.myproject.core.consts.bis;

import com.myproject.core.consts.ConstName;

/**
 * @author nixianhua
 */
public interface ${constName} {
	<#list valueList as consts>
	@ConstName("${consts.name}")
	public static byte ${consts.constField}  =  ${consts.key};
	</#list>
}