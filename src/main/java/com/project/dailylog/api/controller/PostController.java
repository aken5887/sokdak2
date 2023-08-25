package com.project.dailylog.api.controller;

import com.project.dailylog.api.request.PostCreate;
import com.project.dailylog.api.response.PostResponse;
import com.project.dailylog.api.service.PostService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @GetMapping("/posts/{postId}")
  public PostResponse get(@PathVariable Long postId){
    return postService.get(postId);
  }

  @GetMapping("/posts")
  public List<PostResponse> list() {
    return postService.getList();
  }

}
