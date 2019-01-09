package com.spmia.auth.data;

import lombok.Data;

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


    public User toUser() {
        return new User(username, password);
    }
}