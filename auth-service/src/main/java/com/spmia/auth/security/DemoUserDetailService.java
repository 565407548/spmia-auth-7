package com.spmia.auth.security;

import com.spmia.auth.data.User;
import com.spmia.auth.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 18-12-19 下午5:17
 */
@Service
public class DemoUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> demoUser= userRepository.findById(s);
        return demoUser.get();
    }
}
