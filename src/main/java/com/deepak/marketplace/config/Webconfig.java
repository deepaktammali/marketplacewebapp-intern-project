package com.deepak.marketplace.config;

import com.deepak.marketplace.interceptor.AuthInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Webconfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String authInterceptionPaths[] = new String[]{"/getcompleteinvoice","/addproduct","/invoicesearch","/addadmin"};
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns(authInterceptionPaths);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/addproduct").setViewName("addproduct");
    }
}
