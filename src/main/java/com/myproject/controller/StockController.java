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
import com.myproject.domain.Stock;
import com.myproject.query.Page;
import com.myproject.query.Ret;
import com.myproject.query.StockQuery;
import com.myproject.service.IStockService;
@Controller
@RequestMapping("/"+StockController.DOMAIN)
public class StockController {
	public static final String DOMAIN = "stock";
	
	@Autowired
	private IStockService stockService;
	
	@RequestMapping(WebConsts.URL_INDEX)
	public String index(){
		return DOMAIN + WebConsts.URL_INDEX;
	}
	
	@RequestMapping(WebConsts.URL_JSON)
	@ResponseBody
	public Page<Stock> json(StockQuery query){
		return stockService.queryPage(query);
	}
	
	@RequestMapping(WebConsts.URL_EDIT)
	public String edit(Long id, Model model) {
		model.addAttribute("id", id);
		return DOMAIN + WebConsts.VIEW_EDIT;
	}
	
	@RequestMapping(WebConsts.URL_DETAIL)
	@ResponseBody
	public Object detail(Long id){
		return stockService.get(id);
	}

	@RequestMapping(WebConsts.URL_STORE)
	@ResponseBody
	public Ret store(Stock stock) {
		if(null==stock.getId()){
			stockService.save(stock);
		}else{
			stockService.update(stock);
		}
		return Ret.me().setData(stock);
	}

	@RequestMapping(WebConsts.URL_DELETE)
	@ResponseBody
	public Ret delete(String id) {
		if(StringUtils.isBlank(id)){
			return Ret.me().setSuccess(false).setCode(ICodes.FAILED);
		}
		String[] idArr = id.split(",");
		stockService.deleteLogic(idArr);
		return Ret.me();
	}

	@RequestMapping(WebConsts.URL_SHOW)
	public String show(Long id, Model model) {
		model.addAttribute("o", stockService.get(id));
		return DOMAIN + WebConsts.VIEW_SHOW;
	}
}
