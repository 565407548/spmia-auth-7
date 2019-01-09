package com.spmia.auth.controller;

import com.spmia.auth.data.RegisterForm;
import com.spmia.auth.data.UserRepository;
import com.spmia.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-1-7 上午11:01
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    UserService userService;

    public RegistrationController() {
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegisterForm form) {
        userService.insertUser(form.toUser());
        return "redirect:/login";
    }

}
