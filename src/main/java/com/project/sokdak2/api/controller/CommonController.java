package com.project.sokdak2.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {
    @GetMapping("/resume")
    public String resume() {
        return "/common/resume";
    }
}
