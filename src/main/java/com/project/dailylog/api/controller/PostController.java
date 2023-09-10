package com.project.dailylog.api.controller;

import com.project.dailylog.api.request.PostCreate;
import com.project.dailylog.api.request.PostEdit;
import com.project.dailylog.api.request.PostSearch;
import com.project.dailylog.api.response.PostResponse;
import com.project.dailylog.api.service.PostService;
import com.project.dailylog.api.util.PageMaker;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
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
  @Value("${me.cache}")
  private String cache;

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
  public String get(@PathVariable Long postId, @ModelAttribute PostSearch postSearch,
      Model model, HttpServletRequest req, HttpServletResponse res){

    String clientAddress = getClientIp(req);
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
    PostResponse response = postService.get(postId);
    model.addAttribute("response", response);

    return "/posts/view";
  }

  private String getClientIp(HttpServletRequest req){
    String ip = req.getRemoteAddr();
    if(ip == null) ip = req.getHeader("X-Forwarded-For");
    return ip;
  }

  @GetMapping("/posts/create")
  public String create_page(@ModelAttribute PostSearch postSearch){
    return "/posts/create";
  }

  @PostMapping("/posts")
  @ResponseBody
  public PostResponse create(@RequestBody @Valid PostCreate postCreate){
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

  public static void main(String[] args) {
    System.out.println(Base64.encodeBase64String("VC".getBytes(StandardCharsets.UTF_8)));
  }
}
