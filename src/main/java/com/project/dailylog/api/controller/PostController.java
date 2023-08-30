package com.project.dailylog.api.controller;

import com.project.dailylog.api.domain.Post;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @GetMapping("/posts")
  public String list(PostSearch postSearch, Model model) {
    Page<Post> postPage = postService.getListByPage(postSearch);
    model.addAttribute("posts", postPage.getContent());
    model.addAttribute("response", new PageMaker<>(postPage));
    return "/posts/list";
  }

  @GetMapping("/posts/{postId}")
  public String get(@PathVariable Long postId, Model model){
    PostResponse response = postService.get(postId);
    model.addAttribute("response", response);
    return "/posts/view";
  }

  @PostMapping("/posts")
  public String create(@RequestBody @Valid PostCreate postCreate, RedirectAttributes rttr){
    log.info("PostCreate : {}", postCreate.toString());
    postCreate.validate();
    PostResponse postResponse = postService.save(postCreate);
    rttr.addFlashAttribute("response", postResponse);
    return "redirect:/posts/"+postResponse.getId();
  }

  @PatchMapping("/posts/{postId}")
  public String edit(@PathVariable long postId, @RequestBody @Valid PostEdit postEdit, RedirectAttributes rttr){
    postEdit.validate();
    PostResponse postResponse = postService.edit(postId, postEdit);
    rttr.addFlashAttribute("postResponse", postResponse);
    return "redirect:/posts/"+postResponse.getId();
  }

  @DeleteMapping("/posts/{postId}")
  public String delete(@PathVariable long postId) {
    postService.delete(postId);
    return "redirect:/posts";
  }
}
