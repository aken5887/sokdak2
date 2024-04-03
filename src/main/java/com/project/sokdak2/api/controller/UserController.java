package com.project.sokdak2.api.controller;

import com.project.sokdak2.api.request.SessionUser;
import com.project.sokdak2.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * author         : choi
 * date           : 2024-03-22
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "/user/signup";
    }

    @PostMapping("/signup")
    @ResponseBody
    public SessionUser signup(@RequestBody SessionUser sessionUser){
        SessionUser user = userService.signup(sessionUser);
        return user;
    }
}
