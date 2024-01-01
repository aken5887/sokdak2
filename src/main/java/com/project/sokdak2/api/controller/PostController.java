package com.project.sokdak2.api.controller;

import com.project.sokdak2.api.config.annotation.Users;
import com.project.sokdak2.api.domain.Post;
import com.project.sokdak2.api.exception.InvalidRequestException;
import com.project.sokdak2.api.request.PostCreate;
import com.project.sokdak2.api.request.PostEdit;
import com.project.sokdak2.api.request.PostSearch;
import com.project.sokdak2.api.request.SessionUser;
import com.project.sokdak2.api.response.PostResponse;
import com.project.sokdak2.api.service.PostService;
import com.project.sokdak2.api.util.CommonUtil;
import com.project.sokdak2.api.util.PageMaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;
  @Value("${me.cache}")
  private String cache;

  @GetMapping("/posts")
  public String list(@ModelAttribute PostSearch postSearch, Model model, @Users SessionUser sessionUser) {
    Page<PostResponse> postPage = postService.getListByPage(postSearch);
    PageMaker<PostResponse> response = new PageMaker<>(postPage);
    model.addAttribute("posts", postPage.getContent());
    model.addAttribute("response", response);

    log.info("response -> {}", response.toString());

    return "/posts/list";
  }

  @GetMapping("/posts/{postId}")
  public String get(@Users SessionUser sessionUser,
                    @PathVariable Long postId,
                    @ModelAttribute PostSearch postSearch,
                    Model model, HttpServletRequest req, HttpServletResponse res){

    PostResponse response = postService.get(postId);
    String roleCode = "";
    if(sessionUser != null){
      roleCode = sessionUser.getRole().getCode();
    }

    if(!roleCode.equals("ROLE_ADMIN") &&
        response.getLocked() != null && response.getLocked() == 1 &&
        (postSearch.getPwd() == null || !postSearch.getPwd().equals(response.getPassword()))){
      return "forward:/password/"+postId+"?reqType=2";
    }

    String clientAddress = CommonUtil.getClientIp(req);
    boolean update = true;

    if("cookie".equals(cache)){
      Cookie[] cookies = req.getCookies();
      String cookieName = "VC";
      String cookieValue = "";
      if(cookies != null){
        for(Cookie cookie: cookies){
          if(cookie.getName().equals(cookieName)){
            cookieValue = cookie.getValue();
            break;
          }
        }
      }
      String expectedCookieValue = Base64.encodeBase64String((clientAddress+"-"+postId).getBytes(
          StandardCharsets.UTF_8));
      if(!cookieValue.equals(expectedCookieValue)){
        Cookie cookie = new Cookie(cookieName, expectedCookieValue);
        cookie.setPath("/posts/"+postId);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24);
        res.addCookie(cookie);
      }else{
        update = false;
        log.info("request has duplicated within same address : {}, {}", clientAddress, postId);
      }
    }
    postService.increaseCount(postId, clientAddress, update);
    model.addAttribute("response", response);

    return "/posts/view";
  }

  @GetMapping("/posts/create")
  public String create_page(@Users SessionUser sessionUser, @ModelAttribute PostSearch postSearch){
    return "/posts/create";
  }

  @PostMapping("/posts")
  @ResponseBody
  public PostResponse create(@Valid PostCreate postCreate, BindingResult bindingResult) {
    if(bindingResult.hasErrors()){
      throw new InvalidRequestException(bindingResult);
    }else{
      log.info("PostCreate : {}", postCreate.toString());
      postCreate.validate();
      return postService.save(postCreate);
    }
  }

  @GetMapping("/posts/edit/{postId}")
  public String edit_page(@Users SessionUser sessionUser, @PathVariable long postId,
                          @ModelAttribute PostSearch postSearch, Model model) {
    PostResponse postResponse = postService.get(postId);
    AtomicInteger atomicInteger = new AtomicInteger(1);
    postResponse.getFiles().stream()
            .forEach(file -> model.addAttribute("file"+atomicInteger.getAndIncrement(), file));
    model.addAttribute("response", postResponse);
    return "/posts/edit";
  }

  @PatchMapping("/posts/{postId}")
  @ResponseBody
  public PostResponse edit(@PathVariable long postId, @Valid PostEdit postEdit){
    postEdit.validate();
    return postService.edit(postId, postEdit);
  }

  @DeleteMapping("/posts/{postId}")
  @ResponseBody
  public void delete(@PathVariable long postId, @RequestBody PostEdit postEdit) {
    postService.checkAndDelete(postId, postEdit.getPassword());
  }

  @GetMapping("/password/{postId}")
  public String password(
      @Users SessionUser sessionUser,
      @PathVariable long postId,
      @RequestParam String reqType,
      @ModelAttribute PostSearch postSearch, Model model){
    String roleCode = "";
    if(sessionUser != null){
      roleCode = sessionUser.getRole().getCode();
    }
    if(reqType.equals("2") && roleCode.equals("ROLE_ADMIN")){
      return "forward:/posts/"+postId;
    }
    model.addAttribute("postId", postId);
    model.addAttribute("reqType", reqType);
    return "/posts/password";
  }

  @PostMapping("/password/check/{postId}")
  @ResponseBody
  public Long checkpassword(@PathVariable long postId, @RequestBody PostEdit postEdit) {
    Post post = postService.checkPassword(postId, postEdit.getPassword());
    return post.getId();
  }
}
