package com.myproject.generator.ext;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Ext {
	public static final Map<String, List<BisConst>> bisConstMap = new ConcurrentHashMap<String,List<BisConst>>();
	public static final Set<String> relatedClasses = new HashSet<String>();
	
	public Ext(String optionDataType, String data) {
		if(StringUtils.isBlank(data)){
			throw new RuntimeException("必须配置对应data的值");
		}
		
		this.optionDataType = optionDataType;
		
		if(OptionDataType.DOMAIN.equals(optionDataType)){
			this.data = data;
			relatedClasses.add(data.substring(0,1).toUpperCase()+data.substring(1));
		}else if(OptionDataType.CONST.equals(optionDataType)){
			String[] dataArr = data.split("-");
			String constName = dataArr[0];
			this.data = constName;
			if(dataArr.length>1){
				String optionsData = dataArr[1];
				String[] kvArr = optionsData.split(";");
//				1:男:MALE,2:女:FEMALE
				if(!bisConstMap.containsKey(this.data)){
					List<BisConst> bisConsts = new ArrayList<>();
					for (String kvs : kvArr) {
						String[] constArr = kvs.split(":");
						if(constArr.length!=3){
							throw new RuntimeException("常量的配置必须包含三项数据，如  1:男:MALE");
						}
						
						BisConst bisConst = new BisConst();
						bisConst.setKey(Integer.parseInt(constArr[0]));
						bisConst.setName(constArr[1]);
						bisConst.setConstField(constArr[2]);
						
						bisConsts.add(bisConst);
					}
					bisConstMap.put(this.data, bisConsts);
				}
			}
		}else{
			throw new RuntimeException("关联选择类型必须为domain或const");
		}
	}
	
	private String optionDataType;
	private String data;
	public String getOptionDataType() {
		return optionDataType;
	}
	public String getData() {
		return data;
	}
}
