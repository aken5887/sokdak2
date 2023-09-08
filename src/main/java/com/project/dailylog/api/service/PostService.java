package com.project.dailylog.api.service;

import com.project.dailylog.api.domain.Post;
import com.project.dailylog.api.domain.PostEditor;
import com.project.dailylog.api.exception.InvalidPasswordException;
import com.project.dailylog.api.exception.PostNotFoundException;
import com.project.dailylog.api.repository.PostRepository;
import com.project.dailylog.api.request.PostCreate;
import com.project.dailylog.api.request.PostEdit;
import com.project.dailylog.api.request.PostSearch;
import com.project.dailylog.api.response.PostResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

  private final PostRepository postRepository;
  private final RedisService redisService;
  @Value("${me.cache}")
  private String cacheStore;

  public PostResponse save(PostCreate postCreate){
    Post post = new Post().toEntity(postCreate);
    postRepository.save(post);
    return new PostResponse(post);
  }

  public PostResponse get(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new PostNotFoundException());
    return new PostResponse(post);
  }

  public List<PostResponse> getList(PostSearch postSearch) {
    return postRepository.findAllByPage(postSearch)
        .stream()
        .map(PostResponse::new)
        .collect(Collectors.toList());
  }

  public Page<PostResponse> getListByPage(PostSearch postSearch) {
    return postRepository.findPostsByCondition(postSearch);
  }

  @Transactional
  public PostResponse edit(long postId, PostEdit postEdit) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new PostNotFoundException());

    if(postEdit.getPassword() != null && post.getPassword() != postEdit.getPassword()){
      throw new InvalidPasswordException();
    }

    PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();
    PostEditor postEditor = postEditorBuilder
        .title(postEdit.getTitle())
        .content(postEdit.getContent())
        .build();

    post.edit(postEditor);

    return new PostResponse(post);
  }

  public void delete(long postId){
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new PostNotFoundException());
    postRepository.delete(post);
  }

  @Transactional
  public void increaseCount(long postId, String clientAddress){
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new PostNotFoundException());
    if("redis".equals(cacheStore)){
      if(redisService.isFirstIpRequest(clientAddress, postId)){
        post.increaseCount();
        redisService.writeClientRequest(clientAddress, postId);
      }else{
        log.info("request has duplicated within same address : {}, {}", clientAddress, postId);
      }
    }else{
      post.increaseCount();
    }
  }
}
