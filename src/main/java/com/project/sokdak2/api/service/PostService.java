package com.project.sokdak2.api.service;

import com.project.sokdak2.api.domain.File;
import com.project.sokdak2.api.domain.Post;
import com.project.sokdak2.api.domain.PostEditor;
import com.project.sokdak2.api.exception.InvalidPasswordException;
import com.project.sokdak2.api.exception.PostNotFoundException;
import com.project.sokdak2.api.repository.FileRepository;
import com.project.sokdak2.api.repository.PostRepository;
import com.project.sokdak2.api.request.PostCreate;
import com.project.sokdak2.api.request.PostEdit;
import com.project.sokdak2.api.request.PostSearch;
import com.project.sokdak2.api.response.PostResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

  private final PostRepository postRepository;
  private final RedisService redisService;
  @Value("${me.cache}")
  private String cacheStore;
  private final FileService fileService;
  private final FileRepository fileRepository;

  @Transactional
  public PostResponse save(PostCreate postCreate) {
    Post post = new Post().toEntity(postCreate);
    postRepository.save(post);

    List<File> files = new ArrayList<>();
    if(postCreate.getFiles() != null){
      for(MultipartFile file:postCreate.getFiles()){
        String[] upload = fileService.uploadFiles(file);
        files.add(File.builder()
            .realFileName(upload[1])
            .originalFileName(file.getOriginalFilename())
            .fileSize(file.getSize())
            .uploadPath(upload[0])
            .post(post)
            .build());
      }
    }
    fileRepository.saveAll(files);

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

  public void checkAndDelete(long postId, Integer password){
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new PostNotFoundException());

    if(password != null && post.getPassword() != password){
      throw new InvalidPasswordException();
    }

    postRepository.delete(post);
  }

  @Transactional
  public void increaseCount(long postId, String clientAddress, boolean update){
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new PostNotFoundException());

    if("redis".equals(cacheStore)){
      if(redisService.isFirstIpRequest(clientAddress, postId)){
        post.increaseCount();
        redisService.writeClientRequest(clientAddress, postId);
      }else{
        log.info("request has duplicated within same address : {}, {}", clientAddress, postId);
      }
    }else if("cookie".equals(cacheStore) && update){
      post.increaseCount();
    }
  }
}