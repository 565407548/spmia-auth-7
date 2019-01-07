package com.spmia.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableEurekaClient
public class AuthApplication {

//    @RequestMapping(value = {"/user"}, produces = "application/json")
//    public Map<String, Object> user(OAuth2Authentication user) {
//        Map<String, Object> userInfo = new HashMap<>();
//        userInfo.put("user", user.getUserAuthentication().getPrincipal());
//        userInfo.put("authorities", AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities()));
//        return userInfo;
//    }

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
