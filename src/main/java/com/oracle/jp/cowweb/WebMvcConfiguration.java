package com.oracle.jp.cowweb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAccessLogInterceptor())
                .excludePathPatterns("/cowsay/api/ping");
    }

    @Bean
    AccessLogInterceptor getAccessLogInterceptor() {
        return new AccessLogInterceptor();
    }

}
