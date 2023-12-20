package com.mdgspace.activityleaderboard.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @WebFilter("/*")
public class CorsSecurityConfig implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain){
        HttpServletResponse httpServletResponse= (HttpServletResponse) response;
        httpServletResponse.setHeader(
          "Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader(
          "Access-Control-Allow-Methods", "*");
        
          httpServletResponse.setHeader(
          "Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");

        //   chain.doFilter(request, response);
        try{
            chain.doFilter(request, response);
        }catch (Exception e){
          log.error("Filter error", e);
        }
    }

        @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // ...
    }

    @Override
    public void destroy() {
        // ...
    }

    
}
