package com.myproject.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myproject.consts.WebConsts;
import com.myproject.core.consts.ICodes;
import com.myproject.core.utils.StrUtils;
import com.myproject.domain.${tableClass.shortClassName};
import com.myproject.query.Page;
import com.myproject.query.Ret;
import com.myproject.query.${tableClass.shortClassName}Query;
import com.myproject.service.I${tableClass.shortClassName}Service;
<#assign idType = "Long">
<#if tableClass.pkFields??>
<#list tableClass.pkFields as field>
<#if field.jdbcType?contains('CHAR')>
<#assign idType = "String">
</#if>
</#list>
</#if>
@Controller
@RequestMapping("/"+${tableClass.shortClassName}Controller.DOMAIN)
public class ${tableClass.shortClassName}Controller {
	public static final String DOMAIN = "${tableClass.variableName}";
	
	@Autowired
	private I${tableClass.shortClassName}Service ${tableClass.variableName}Service;
	
	@RequestMapping(WebConsts.URL_INDEX)
	public String index(){
		return DOMAIN + WebConsts.URL_INDEX;
	}
	
	@RequestMapping(WebConsts.URL_JSON)
	@ResponseBody
	public Page<${tableClass.shortClassName}> json(${tableClass.shortClassName}Query query){
		return ${tableClass.variableName}Service.queryPage(query);
	}
	
	@RequestMapping(WebConsts.URL_EDIT)
	public String edit(${idType} id, Model model) {
		model.addAttribute("id", id);
		return DOMAIN + WebConsts.VIEW_EDIT;
	}
	
	@RequestMapping(WebConsts.URL_DETAIL)
	@ResponseBody
	public Object detail(${idType} id){
		return ${tableClass.variableName}Service.get(id);
	}

	@RequestMapping(WebConsts.URL_STORE)
	@ResponseBody
	public Ret store(${tableClass.shortClassName} ${tableClass.variableName}) {
		<#if idType == "String">
		if(StringUtils.isBlank(${tableClass.variableName}.getId())){
		<#else>
		if(null==${tableClass.variableName}.getId()){
		</#if>
			${tableClass.variableName}Service.save(${tableClass.variableName});
		}else{
			${tableClass.variableName}Service.update(${tableClass.variableName});
		}
		return Ret.me().setData(${tableClass.variableName});
	}

	@RequestMapping(WebConsts.URL_DELETE)
	@ResponseBody
	public Ret delete(String id) {
		if(StringUtils.isBlank(id)){
			return Ret.me().setSuccess(false).setCode(ICodes.FAILED);
		}
		<#if idType == "Long">
		Long[] idArr = StrUtils.splitToLong(id);
		<#else>
		String[] idArr = id.split(",");
		</#if>
		${tableClass.variableName}Service.deleteLogic(idArr);
		return Ret.me();
	}

	@RequestMapping(WebConsts.URL_SHOW)
	public String show(${idType} id, Model model) {
		model.addAttribute("o", ${tableClass.variableName}Service.get(id));
		return DOMAIN + WebConsts.VIEW_SHOW;
	}
}
