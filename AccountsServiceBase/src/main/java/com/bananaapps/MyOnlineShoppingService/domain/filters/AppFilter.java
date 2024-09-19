/*
package com.bananaapps.MyOnlineShoppingService.domain.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Profile("prod")
public class AppFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AppFilter.class);

    private static final List<String> allowedIPs = List.of(
            "192.168.1.100",
            "192.168.1.101"
    );


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initializing AppFilter...");
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //Cast a HTTP para manejar el código de estado 403
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String clientIP = request.getRemoteAddr();
        logger.info("Client IP: " + clientIP);

        if (allowedIPs.contains(clientIP)) {
            logger.info("Allowerd IP: " + clientIP);
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            logger.warn("Not allowed IP: " + clientIP);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
        }
    }

    @Override
    public void destroy() {
        logger.info("Destroying AppFilter");
    }
}
*/
