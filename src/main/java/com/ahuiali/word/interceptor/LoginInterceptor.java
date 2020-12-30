package com.ahuiali.word.interceptor;

import com.ahuiali.word.common.constant.Constant;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        // 临时添加
        if (request.getHeader(Constant.LEARNER_ID) != null) {
            session.setAttribute(Constant.LEARNER_ID, Integer.parseInt(request.getHeader(Constant.LEARNER_ID)));
            return true;
        }
//        session.setAttribute(Constant.LEARNER_ID, 21);
        if (session.getAttribute(Constant.LEARNER_ID) != null) {
            return true;
        } else {
            response.sendRedirect("/learner/gotoLogin");
        }
        return false;
    }
}
