package com.project.dailylog.api.service;

import com.project.dailylog.api.domain.Post;
import com.project.dailylog.api.domain.PostEditor;
import com.project.dailylog.api.repository.PostRepository;
import com.project.dailylog.api.request.PostCreate;
import com.project.dailylog.api.request.PostEdit;
import com.project.dailylog.api.request.PostSearch;
import com.project.dailylog.api.response.PostResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다. id="+postId));
    return new PostResponse(post);
  }

  public List<PostResponse> getList(PostSearch postSearch) {
    return postRepository.findAllByPage(postSearch)
        .stream()
        .map(PostResponse::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public PostResponse edit(long postId, PostEdit postEdit) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다. id="+postId));

    PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();
    PostEditor postEditor = postEditorBuilder
        .title(postEdit.getTitle())
        .content(postEdit.getContent())
        .build();

    post.edit(postEditor);

    return new PostResponse(post);
  }
}
