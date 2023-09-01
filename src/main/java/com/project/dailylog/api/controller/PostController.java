package com.project.dailylog.api.controller;

import com.project.dailylog.api.request.PostCreate;
import com.project.dailylog.api.request.PostEdit;
import com.project.dailylog.api.request.PostSearch;
import com.project.dailylog.api.response.PostResponse;
import com.project.dailylog.api.service.PostService;
import com.project.dailylog.api.util.PageMaker;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @GetMapping("/posts")
  public String list(@ModelAttribute PostSearch postSearch, Model model) {
    Page<PostResponse> postPage = postService.getListByPage(postSearch);
    PageMaker<PostResponse> response = new PageMaker<>(postPage);
    model.addAttribute("posts", postPage.getContent());
    model.addAttribute("response", response);

    log.info("response -> {}", response.toString());

    return "/posts/list";
  }

  @GetMapping("/posts/{postId}")
  public String get(@PathVariable Long postId, @ModelAttribute PostSearch postSearch, Model model){
    postService.increaseCount(postId);
    PostResponse response = postService.get(postId);
    model.addAttribute("response", response);
    return "/posts/view";
  }

  @GetMapping("/posts/create")
  public String create_page(@ModelAttribute PostSearch postSearch){
    return "/posts/create";
  }

  @PostMapping("/posts")
  @ResponseBody
  public PostResponse create(@RequestBody @Valid PostCreate postCreate, RedirectAttributes rttr){
    log.info("PostCreate : {}", postCreate.toString());
    postCreate.validate();
    return  postService.save(postCreate);
  }

  @GetMapping("/posts/edit/{postId}")
  public String edit_page(@PathVariable long postId,
      @ModelAttribute PostSearch postSearch, Model model) {
    PostResponse postResponse = postService.get(postId);
    model.addAttribute("response", postResponse);
    return "/posts/edit";
  }

  @PatchMapping("/posts/{postId}")
  @ResponseBody
  public PostResponse edit(@PathVariable long postId, @RequestBody @Valid PostEdit postEdit, RedirectAttributes rttr){
    postEdit.validate();
    return postService.edit(postId, postEdit);
  }

  @DeleteMapping("/posts/{postId}")
  @ResponseBody
  public void delete(@PathVariable long postId) {
    postService.delete(postId);
  }
}
