package org.example.formgenerator.vo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        startTime.set(System.currentTimeMillis());
        String requestURI = request.getRequestURI();
        log.info("开始处理请求:{}",requestURI);
        Object user = request.getSession().getAttribute("user");
        if(user == null)
        {
            response.setStatus(401);
            response.getWriter().write("Please login first.");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long costTime = System.currentTimeMillis() - startTime.get();
        startTime.remove();
        log.info("请求{}处理完成，耗时：{}ms，异常{}",
                request.getRequestURI(), costTime, ex==null?"无":ex.getMessage());
    }
}
