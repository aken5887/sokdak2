package com.project.sokdak2.api.controller;


import com.project.sokdak2.api.domain.post.Category;
import com.project.sokdak2.api.domain.post.Post;
import com.project.sokdak2.api.repository.PostRepository;
import com.project.sokdak2.api.response.PostResponse;
import com.project.sokdak2.api.util.PageMaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class PostControllerTest2 {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }
    @DisplayName("category가 NEWS인 경우만 조회된다.")
    @Test
    void test() throws Exception {
        //given
        List<Post> postNews = IntStream.range(1, 11)
                .mapToObj(i -> {
                         return Post.builder()
                            .title("뉴스 ["+i+"]")
                            .content("NEWS")
                            .userId("USERID")
                            .category(Category.NEWS)
                            .password(9999)
                            .build();
                }).collect(Collectors.toList());

        List<Post> postBBS = IntStream.range(11, 21)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("게시판 ["+i+"]")
                            .content("NEWS")
                            .userId("USERID")
                            .category(Category.BBS)
                            .password(9999)
                            .build();
                }).collect(Collectors.toList());

        postRepository.saveAll(postNews);
        postRepository.saveAll(postBBS);

        //when
        MvcResult mvcResult =
            this.mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                    .param("category", Category.NEWS.getCode()))
                    .andExpect(status().isOk())
                    .andReturn();

        //then
        PageMaker<PostResponse> response
                = (PageMaker<PostResponse>) mvcResult.getModelAndView().getModel().get("response");
        List<PostResponse> list = response.getResult().getContent();
        list.stream().forEach(s-> System.out.println(s.getTitle()));
        assertThat(response.getResult().getSize()).isEqualTo(10);
        assertThat(list.get(0).getTitle()).isEqualTo("뉴스 [10]");
    }

    @DisplayName("category가 null인 경우 모두 조회된다.")
    @Test
    void test2() throws Exception {
        //given
        List<Post> postNews = IntStream.range(1, 6)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("뉴스 ["+i+"]")
                            .content("NEWS")
                            .userId("USERID")
                            .category(Category.NEWS)
                            .password(9999)
                            .build();
                }).collect(Collectors.toList());

        List<Post> postBBS = IntStream.range(6, 11)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("게시판 ["+i+"]")
                            .content("NEWS")
                            .userId("USERID")
                            .category(Category.BBS)
                            .password(9999)
                            .build();
                }).collect(Collectors.toList());

        postRepository.saveAll(postNews);
        postRepository.saveAll(postBBS);

        //when
        MvcResult mvcResult =
                this.mockMvc.perform(MockMvcRequestBuilders.get("/posts"))
                        .andExpect(status().isOk())
                        .andReturn();

        //then
        PageMaker<PostResponse> response
                = (PageMaker<PostResponse>) mvcResult.getModelAndView().getModel().get("response");
        List<PostResponse> list = response.getResult().getContent();
        list.stream().forEach(s-> System.out.println(s.getTitle()));
        assertThat(response.getResult().getSize()).isEqualTo(10);
        assertThat(list.get(0).getTitle()).isEqualTo("게시판 [10]");
        assertThat(list.get(5).getTitle()).isEqualTo("뉴스 [5]");
    }
}
