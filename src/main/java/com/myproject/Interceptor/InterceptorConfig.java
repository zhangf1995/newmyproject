package com.myproject.Interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @program: myproject
 * @description: 自定义拦截器
 * @author: zf
 * @create: 2019-04-07 09:42
 **/
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 拦截规则
        registry.addInterceptor(new MyInterceptorConfig()).addPathPatterns("/plat/**")
                .excludePathPatterns("/common/**","/test/**");
        super.addInterceptors(registry);
    }
}
