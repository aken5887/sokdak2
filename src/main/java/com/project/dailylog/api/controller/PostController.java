package com.project.dailylog.api.controller;

import com.project.dailylog.api.request.PostCreate;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PostController {
  @PostMapping("/posts")
  public Map<String, Object> create(@RequestBody @Valid PostCreate postCreate){
    log.info("PostCreate : {}", postCreate.toString());
    return Map.of();
  }
}
