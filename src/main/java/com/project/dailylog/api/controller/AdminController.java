package com.project.dailylog.api.controller;

import com.project.dailylog.api.request.PostCreate;
import com.project.dailylog.api.request.PostSearch;
import com.project.dailylog.api.request.SessionUser;
import com.project.dailylog.api.response.PostResponse;
import com.project.dailylog.api.service.PostService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminController {

  private final PostService postService;

  @GetMapping("/admin")
  @ResponseBody
  public String admin(SessionUser sessionUser){
    log.info("SessionUser : {}", sessionUser.getUserName());
    return sessionUser.getUserName();
  }

  @GetMapping("/admin/create")
  public String get_create(@ModelAttribute PostSearch postSearch) {
    return "/admin/create";
  }

  @PostMapping("/admin/create")
  @ResponseBody
  public PostResponse post_create(@RequestBody @Valid PostCreate postCreate) {
    PostResponse postResponse = postService.save(postCreate);
    return postResponse;
  }
}
