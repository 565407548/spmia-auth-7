package com.spmia.auth.data;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-1-7 上午11:02
 */
@Data
public class RegisterForm {

    private String username;
    private String password;
    private String fullname;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(username,
                passwordEncoder.encode(password));
    }
}