package com.spmia.auth.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-1-7 上午11:12
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // tag::customLoginViewController[]
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/abc").setViewName("home");
        registry.addViewController("/login");
    }
    // end::customLoginViewController[]

}
