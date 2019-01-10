package com.spmia.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyUserDetailService userDetailsService;

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService);
    }

    //tag::configureHttpSecurity[]
    //tag::authorizeRequests[]
    //tag::customLoginPage[]
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()

                .and()
                .formLogin().loginPage("/login")
                .permitAll()

                .and()
                .logout()
                .permitAll()

                .and()
                .csrf()
                .disable();

//        http
//                .authorizeRequests()
//                .antMatchers("/design", "/orders")
//                .access("hasRole('ROLE_USER')")
//                .antMatchers("/", "/**").access("permitAll")
//                //end::authorizeRequests[]
//
//                .and()
//                .authorizeRequests()
//                .antMatchers("/register")
//                .permitAll()
//
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                //end::customLoginPage[]
//
//                // tag::enableLogout[]
//                .and()
//                .logout()
//                .logoutSuccessUrl("/login")
//                // end::enableLogout[]
//
//                // Make H2-Console non-secured; for debug purposes
//                // tag::csrfIgnore[]
//                .and()
//                .csrf()
//                .ignoringAntMatchers("/h2-console/**")
//                // end::csrfIgnore[]
//
//                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
//                // tag::frameOptionsSameOrigin[]
//                .and()
//                .headers()
//                .frameOptions()
//                .sameOrigin();
        // end::frameOptionsSameOrigin[]

        //tag::authorizeRequests[]
        //tag::customLoginPage[]
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService)
//                /*.passwordEncoder(encoder())*/;
//    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean
//    public PasswordEncoder encoder() {
////        return new StandardPasswordEncoder("53cr3t");
//        return new BCryptPasswordEncoder();
//    }
}
