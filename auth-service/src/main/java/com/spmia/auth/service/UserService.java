package com.spmia.auth.service;

import com.spmia.auth.data.User;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-1-9 下午5:25
 */
public interface UserService {
    User getUserByName(String name);
    void insertUser(User user);
    void updateUser(User user);
}
