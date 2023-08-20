package com.project.dailylog.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
  @GetMapping("/posts/list")
  public String list(){
    return "/posts/list";
  }
}
