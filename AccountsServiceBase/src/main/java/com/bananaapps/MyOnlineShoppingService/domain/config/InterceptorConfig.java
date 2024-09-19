package com.bananaapps.MyOnlineShoppingService.domain.config;

import com.bananaapps.MyOnlineShoppingService.domain.interceptors.AccountServiceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    AccountServiceInterceptor accountServiceInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accountServiceInterceptor).addPathPatterns("/accounts");
    }

}
