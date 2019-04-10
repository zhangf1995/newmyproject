package com.myproject.exception;

import com.myproject.query.Ret;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
	public ModelAndView globalExceptionHandler(RuntimeException ex, HttpServletResponse response,HttpServletRequest request){
		//记录日志
		StringBuilder sb = new StringBuilder("\n===========================exception begin=================\n");
		StackTraceElement[] stackTrace = ex.getStackTrace();
		String requestURI = request.getRequestURI();
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		sb.append("requestURI : "+requestURI+"\n");
		sb.append("params : "+params+"\n");
		sb.append("localizedMessage : "+ex.getLocalizedMessage()+"\n");
		sb.append("exception : "+ex.toString()+"\n");
		sb.append("message : "+ex.getMessage()+"\n");
		for (StackTraceElement stackTraceElement : stackTrace) {
			String stackLine = stackTraceElement.toString();
			//只记录项目中发生的错误
			if(stackLine.startsWith("com.myproject")){
				//排除掉CGLIB代理的方法
				if(stackLine.contains("CGLIB")){
					continue;
				}
				sb.append(stackTraceElement.toString()+"\n");
			}
		}
		sb.append("===========================exception end=================\n");
		//返回视图
		
		BisException bisException;
		Ret ret = null;
        if(ex instanceof BisException) {
        	bisException = (BisException) ex;
        	log.warn(sb.toString()); //业务异常记录WARN日志
        } else {
        	bisException = BisException.me();
        	log.error(sb.toString());//未知异常记录ERROR日志
        }
        ret = bisException.getRet();
        ex.printStackTrace();
        
/*
        String retJson = JSON.toJSONString(ret);
*/
/*        try {
        	response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(retJson);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("my_error");
        return modelAndView;
	}
	
}
