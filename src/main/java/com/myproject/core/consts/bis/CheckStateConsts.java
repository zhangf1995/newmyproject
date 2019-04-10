package com.myproject.core.consts.bis;

import com.myproject.core.consts.ConstName;

public interface CheckStateConsts {
	@ConstName("待审核")
	public static byte WAIT  =  0;
	@ConstName("审核通过")
	public static byte CHECKED  =  1;
	@ConstName("审核失败")
	public static byte FAILED  =  2;
}