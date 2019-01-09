package com.spmia.auth.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-1-9 下午3:55
 */
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/authentication/github", "/authentication/qq").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/**/*.jpg", "/**/*.png", "/**/*.jpeg").permitAll()
                .antMatchers("/roles/**").permitAll()
                .antMatchers("/users/**", "/menus/**", "/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated();
    }
}
