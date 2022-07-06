package com.xyongfeng.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xyongfeng
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置MVC静态资源访问路径
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry // 设置访问url
                .addResourceHandler("/static/**")
                // 设置储存路径
                .addResourceLocations("classpath:/static/");
    }
}
