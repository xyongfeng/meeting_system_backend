package com.xyongfeng.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configurable
public class SwaggerConfiguration {
    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .contact(new Contact("xyongfeng", "url", "979204556@qq.com"))
                .title("会议签到系统 - 在线API接口文档")
                .version("1.0")
                .build();
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xyongfeng.controller"))
                .build();

    }

}
