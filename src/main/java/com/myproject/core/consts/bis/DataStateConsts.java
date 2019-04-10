package com.myproject.core.consts.bis;

import com.myproject.core.consts.ConstName;

public interface DataStateConsts {
	@ConstName("删除")
	public static byte DELETED  =  0;
	@ConstName("正常")
	public static byte USING  =  1;
}