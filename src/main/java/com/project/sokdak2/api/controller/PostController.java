package com.project.sokdak2.api.controller;

import com.project.sokdak2.api.config.annotation.Users;
import com.project.sokdak2.api.domain.post.Post;
import com.project.sokdak2.api.exception.InvalidRequestException;
import com.project.sokdak2.api.request.PostCreate;
import com.project.sokdak2.api.request.PostEdit;
import com.project.sokdak2.api.request.PostSearch;
import com.project.sokdak2.api.request.SessionUser;
import com.project.sokdak2.api.response.MetaResponse;
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

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
    return "/posts/list";
  }

  @GetMapping("/posts/{postId}")
  public String get(@Users SessionUser sessionUser,
                    @PathVariable Long postId,
                    @ModelAttribute PostSearch postSearch,
                    Model model, HttpServletRequest req,
                    HttpServletResponse res){

    PostResponse response = postService.get(postId);
    String roleCode = "";
    if(sessionUser != null && sessionUser.getRole() != null){
      roleCode = sessionUser.getRole().getCode();
    }

    if(!roleCode.equals("ROLE_ADMIN") &&
        response.getLocked() != null && response.getLocked() == 1 &&
        (postSearch.getPwd() == null || !postSearch.getPwd().equals(response.getPassword()))){
      return "forward:/password/"+postId+"?reqType=2";
    }

    String clientAddress = CommonUtil.getClientIp(req);
    HttpSession session = req.getSession();
    String sessionId = session.getId();
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
      String expectedCookieValue = Base64.encodeBase64String((sessionId+"-"+clientAddress+"-"+postId).getBytes(
          StandardCharsets.UTF_8));
      if(!cookieValue.equals(expectedCookieValue)){
        Cookie cookie = new Cookie(cookieName, expectedCookieValue);
        cookie.setPath("/posts/"+postId);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24);
        res.addCookie(cookie);
      }else{
        update = false;
        log.debug("request has duplicated within same address : {}, {}, {}", sessionId, clientAddress, postId);
      }
    }
    postService.increaseCount(postId, clientAddress, update);
    model.addAttribute("response", response);

    /* meta 태그 */
    /*
    createMetaTag('description', postTitle);
    createMetaTag('og:title', postTitle);
    createMetaTag('og:url', location.href);
    createMetaTag('og:image', thumbnailImage);
    createMetaTag('og:description', postTitle);

    createMetaTag('twitter:title', postTitle);
    createMetaTag('twitter:card', 'summary_large_image');
    createMetaTag('twitter:image', thumbnailImage);
    createMetaTag('twitter:description', postTitle);
    */

    MetaResponse metaResponse = MetaResponse.builder()
            .postTitle(response.getTitle())
            .description(response.getTitle())
            .url(req.getRequestURL().toString())
            .image(req.getRequestURL().toString()+response.getThumbnailImage())
            .twitterCard("summary_large_image")
            .build();

    model.addAttribute("meta", metaResponse);
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
      log.debug("PostCreate : {}", postCreate.toString());
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

  @PatchMapping("/posts")
  @ResponseBody
  public PostResponse edit(@Valid PostEdit postEdit){
    postEdit.validate();
    return postService.edit(postEdit.getId(), postEdit);
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
