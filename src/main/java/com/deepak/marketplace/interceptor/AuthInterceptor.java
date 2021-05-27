package com.deepak.marketplace.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
                System.out.println(request.getSession().getAttribute("isAdmin"));
                Boolean isLoggedIn = (Boolean)request.getSession().getAttribute("isAdmin");
                if(isLoggedIn!=null & isLoggedIn==true){
                    return true;
                }
                response.sendRedirect("/signin");
                return false;
    }
}
