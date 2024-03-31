package com.project.sokdak2.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * author         : choi
 * date           : 2024-03-22
 */

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "/login/login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "/login/signup";
    }
}
