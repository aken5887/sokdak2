package com.project.sokdak2.api.repository;

import com.project.sokdak2.api.domain.post.Post;
import com.project.sokdak2.api.request.PostSearch;
import com.project.sokdak2.api.response.PostResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface PostRepositoryCustom {
  List<Post> findAllByPage(PostSearch postSearch);
  Page<PostResponse> findPostsByCondition(PostSearch postSearch);
}
