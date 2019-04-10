package com.myproject.core.utils;

import java.io.Serializable;

public class KV<K, V> implements Serializable {

	private static final long serialVersionUID = -137786708574057953L;
	private K k;
	private V v;

	public KV(K k, V v) {
		this.k = k;
		this.v = v;
	} // 初始化key和value

	public K getK() {
		return k;
	}

	public V getV() {
		return v;
	}

	public void setK(K key) {
		this.k = key;
	}

	public void setV(V value) {
		this.v = value;
	}

	@Override
	public int hashCode() {
		Integer result = 17;
		Integer c = 0;
		if(k instanceof Boolean){
			c = (Boolean)k ? 1 : 0;
			return 37*result+c; 
		}
		if(k instanceof Byte || k instanceof Character || k instanceof Short || k instanceof Integer){
			c = (Integer)k;
			return 37*result+c; 
		}
		if(k instanceof Long){
			Long temp = (Long)k;
			c = Integer.parseInt(""+(temp^(temp>>>32)));
			return 37*result+c; 
		}
		if(k instanceof Float){
			Float temp = (Float)k;
			c = Float.floatToIntBits(temp);
			return 37*result+c; 
		}
		if(k instanceof Double){
			Long temp = Double.doubleToLongBits((Double)k);
			c = Integer.parseInt(""+(temp ^ (temp>>>32)));
			return 37*result+c; 
		}
		if(k instanceof Object){
			c = k.hashCode();
			return 37*result+c;
		}
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(null==obj || !(obj instanceof KV)) return false;
		return this.getK().equals(((KV<?, ?>)obj).getK());
	}
	
	public String toString() {
		return "{\"k\":\"" + k + "\",\"v\":\"" + v + "\"}";
	}
}