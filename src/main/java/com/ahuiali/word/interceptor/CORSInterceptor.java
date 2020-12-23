package com.ahuiali.word.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CORSInterceptor
 * 跨域拦截器
 * @author ZhengChaoHui
 * @date 2020/12/22 22:33
 */
@Component
public class CORSInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 服务器 headers 白名单，可以让客户端进行访问操作的属性
        response.setHeader("Access-Control-Expose-Headers", "token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        return true;
    }
}
