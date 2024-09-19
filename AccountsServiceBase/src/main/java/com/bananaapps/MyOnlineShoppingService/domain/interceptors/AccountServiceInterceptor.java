package com.bananaapps.MyOnlineShoppingService.domain.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AccountServiceInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info(
                "REQUEST URI" + request.getRequestURI() +
                        " METHOD " + request.getMethod() +
                        "QUERY PARAMS " + request.getQueryString() +
                        " IP" + request.getRemoteAddr()
        );
        response.addHeader("accounts-request-reviewed", "true");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("RESPONSE STATUS " + response.getStatus());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("After completion:  Request completed for URI " +  request.getRequestURI());
    }

    private String getRemoteAddress(HttpServletRequest request) {
        String ipHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipHeader != null && ipHeader.length() > 0) {
            logger.info("IP from  - X-FORWARED-FOR " + ipHeader);
        }
        return request.getRemoteAddr();
    }
}
