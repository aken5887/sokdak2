package com.project.dailylog.api.service;

import com.project.dailylog.api.domain.Post;
import com.project.dailylog.api.domain.PostRepository;
import com.project.dailylog.api.request.PostCreate;
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
}
