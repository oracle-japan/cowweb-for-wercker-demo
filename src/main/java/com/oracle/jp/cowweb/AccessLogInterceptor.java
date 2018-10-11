package com.oracle.jp.cowweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class AccessLogInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(AccessLogInterceptor.class);

    @Autowired
    private ApplicationProperties properties;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("version: " + properties.getVersion());
        return true;
    }

}
