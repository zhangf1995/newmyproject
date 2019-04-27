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
import com.myproject.domain.Order;
import com.myproject.query.Page;
import com.myproject.query.Ret;
import com.myproject.query.OrderQuery;
import com.myproject.service.IOrderService;
@Controller
@RequestMapping("/"+OrderController.DOMAIN)
public class OrderController {
	public static final String DOMAIN = "order";
	
	@Autowired
	private IOrderService orderService;
	
	@RequestMapping(WebConsts.URL_INDEX)
	public String index(){
		return DOMAIN + WebConsts.URL_INDEX;
	}
	
	@RequestMapping(WebConsts.URL_JSON)
	@ResponseBody
	public Page<Order> json(OrderQuery query){
		return orderService.queryPage(query);
	}
	
	@RequestMapping(WebConsts.URL_EDIT)
	public String edit(Long id, Model model) {
		model.addAttribute("id", id);
		return DOMAIN + WebConsts.VIEW_EDIT;
	}
	
	@RequestMapping(WebConsts.URL_DETAIL)
	@ResponseBody
	public Object detail(Long id){
		return orderService.get(id);
	}

	@RequestMapping(WebConsts.URL_STORE)
	@ResponseBody
	public Ret store(Order order) {
		if(null==order.getId()){
			orderService.save(order);
		}else{
			orderService.update(order);
		}
		return Ret.me().setData(order);
	}

	@RequestMapping(WebConsts.URL_DELETE)
	@ResponseBody
	public Ret delete(String id) {
		if(StringUtils.isBlank(id)){
			return Ret.me().setSuccess(false).setCode(ICodes.FAILED);
		}
		String[] idArr = id.split(",");
		orderService.deleteLogic(idArr);
		return Ret.me();
	}

	@RequestMapping(WebConsts.URL_SHOW)
	public String show(Long id, Model model) {
		model.addAttribute("o", orderService.get(id));
		return DOMAIN + WebConsts.VIEW_SHOW;
	}
}
