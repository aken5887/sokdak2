package com.project.dailylog.api.repository;

import com.project.dailylog.api.domain.Post;
import com.project.dailylog.api.request.PostSearch;
import com.project.dailylog.api.util.PageMaker;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
class PostRepositoryTest {

  @Autowired
  PostRepository postRepository;

  @DisplayName("Pageable 객체를 이용해서 조회한다.")
  @Test
  void test() {
    // given
    postRepository.saveAll(
        IntStream.range(0, 31)
            .mapToObj(i -> Post.builder()
                .title("제목")
                .content("내용")
                .userId("writer")
                .build())
            .collect(Collectors.toList())
    );
    PostSearch postSearch = PostSearch.builder().build();

    // when
    Page<Post> result = postRepository.findPostsByCondition(postSearch);
    PageMaker<Post> returnResult = new PageMaker<>(result);
    returnResult.getResult().getContent().stream()
        .forEach(post -> {
          System.out.println(post.getTitle());
        });
  }
}