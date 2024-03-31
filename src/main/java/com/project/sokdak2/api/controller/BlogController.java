package com.project.sokdak2.api.controller;

import com.project.sokdak2.api.domain.post.Category;
import com.project.sokdak2.api.request.PostSearch;
import com.project.sokdak2.api.response.PostResponse;
import com.project.sokdak2.api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * author         : choi
 * date           : 2024-03-30
 */

@RequiredArgsConstructor
@Controller
public class BlogController {

    private final PostService postService;
    @RequestMapping("/blog")
    public String blog(@ModelAttribute PostSearch postSearch, Model model) {
        Integer page = postSearch.getPage();
        /**
         * view에서 list로 돌아올 경우 기존 페이지 만큼 조회
         */
        if(postSearch.getPage() > 1){
            postSearch.setSize(postSearch.getPage() * 9);
            postSearch.setPage(1);
        } else {
            postSearch.setSize(9);
        }
        postSearch.setCategory(Category.BBS);
        Page<PostResponse> postPage = postService.getListByPage(postSearch);
        // 페이지 값 원상복구
        postSearch.setPage(page);
        model.addAttribute("posts", postPage.getContent());
        return "/blog/main";
    }

    @GetMapping("/blog/{page}")
    @ResponseBody
    public List<PostResponse> page(@ModelAttribute PostSearch postSearch, @PathVariable Integer page) {
        postSearch.setSize(9);
        postSearch.setCategory(Category.BBS);
        postSearch.setPage(page);
        Page<PostResponse> postPage = postService.getListByPage(postSearch);
        return postPage.getContent();
    }
}
