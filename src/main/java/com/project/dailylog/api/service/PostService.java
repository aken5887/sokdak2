package com.project.dailylog.api.service;

import com.project.dailylog.api.domain.Post;
import com.project.dailylog.api.domain.PostRepository;
import com.project.dailylog.api.request.PostCreate;
import com.project.dailylog.api.response.PostResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

  private final PostRepository postRepository;

  public void save(PostCreate postCreate){
    Post post = new Post().toEntity(postCreate);
    postRepository.save(post);
  }

  public PostResponse get(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("존재ㅐ하지 않는 게시글 입니다. id="+postId));
    return new PostResponse(post);
  }

  public List<PostResponse> getList() {
    List<Post> posts = postRepository.findAll();
    return posts.stream()
        .map(PostResponse::new)
        .collect(Collectors.toList());
  }
}
