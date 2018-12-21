package com.spmia.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 18-12-21 上午9:34
 */
@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
