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
import com.myproject.domain.SysUser;
import com.myproject.query.Page;
import com.myproject.query.Ret;
import com.myproject.query.SysUserQuery;
import com.myproject.service.ISysUserService;
@Controller
@RequestMapping("/"+SysUserController.DOMAIN)
public class SysUserController {
	public static final String DOMAIN = "sysUser";
	
	@Autowired
	private ISysUserService sysUserService;
	
	@RequestMapping(WebConsts.URL_INDEX)
	public String index(){
		return DOMAIN + WebConsts.URL_INDEX;
	}
	
	@RequestMapping(WebConsts.URL_JSON)
	@ResponseBody
	public Page<SysUser> json(SysUserQuery query){
		return sysUserService.queryPage(query);
	}
	
	@RequestMapping(WebConsts.URL_EDIT)
	public String edit(String id, Model model) {
		model.addAttribute("id", id);
		return DOMAIN + WebConsts.VIEW_EDIT;
	}
	
	@RequestMapping(WebConsts.URL_DETAIL)
	@ResponseBody
	public Object detail(String id){
		return sysUserService.get(id);
	}

	@RequestMapping(WebConsts.URL_STORE)
	@ResponseBody
	public Ret store(SysUser sysUser) {
		if(StringUtils.isBlank(sysUser.getId())){
			sysUserService.save(sysUser);
		}else{
			sysUserService.update(sysUser);
		}
		return Ret.me().setData(sysUser);
	}

	@RequestMapping(WebConsts.URL_DELETE)
	@ResponseBody
	public Ret delete(String id) {
		if(StringUtils.isBlank(id)){
			return Ret.me().setSuccess(false).setCode(ICodes.FAILED);
		}
		String[] idArr = id.split(",");
		sysUserService.deleteLogic(idArr);
		return Ret.me();
	}

	@RequestMapping(WebConsts.URL_SHOW)
	public String show(String id, Model model) {
		model.addAttribute("o", sysUserService.get(id));
		return DOMAIN + WebConsts.VIEW_SHOW;
	}
}
