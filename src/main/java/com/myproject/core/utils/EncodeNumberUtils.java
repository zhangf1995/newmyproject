package com.myproject.core.utils;

import java.util.HashMap;

/**
 * 64进制和10进制的转换类
 */
public class EncodeNumberUtils {
	private static final String X36 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String[] X36_ARRAY = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
	/**
	 * 把10进制的数字转换成36进制
	 * 
	 * @param num
	 * @return
	 */
	public static String encode36(long num) {
		StringBuffer sBuffer = new StringBuffer();

		if (num == 0) {
			sBuffer.append("0");
		}
		while (num > 0) {
			sBuffer.append(X36_ARRAY[(int) (num % 36)]);
			num = num / 36;
		}

		return sBuffer.reverse().toString();
	}

	/**
	 * 把36进制的字符串转换成10进制
	 * 
	 * @param decodeStr
	 * @return
	 */
	public static long decode36(String decodeStr) {

		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		for (int i = 0; i < X36.length(); i++) {
			map.put(X36.charAt(i), i);
		}

		int size = decodeStr.length();
		long num = 0;
		for (int i = 0; i < size; i++) {
			String char2str = String.valueOf(decodeStr.charAt(i)).toUpperCase();
			num = (long) (map.get(char2str.charAt(0)) * Math.pow(36, size - i - 1) + num);
		}

		return num;
	}
}