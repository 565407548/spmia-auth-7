package com.spmia.auth.service;

import com.spmia.auth.data.User;
import com.spmia.auth.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-1-9 下午5:27
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private PasswordEncoder encoder;

    @Override
    public User getUserByName(String name) {
        User user = userRepository.findByUsername(name);
        return user;
    }

    @Override
    public void insertUser(User user) {
        User newUser = new User(user.getUsername(), new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(newUser);
    }

    @Override
    public void updateUser(User user) {
        User newUser = new User(user.getUsername(), new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(newUser);
    }
}
