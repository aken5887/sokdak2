package com.project.dailylog.api.controller;

import com.project.dailylog.api.request.PostCreate;
import com.project.dailylog.api.service.PostService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping("/posts")
  public void create(@RequestBody @Valid PostCreate postCreate){
    log.info("PostCreate : {}", postCreate.toString());
    postService.save(postCreate);
  }
}
