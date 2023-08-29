package com.project.dailylog.api.repository;

import com.project.dailylog.api.domain.Post;
import com.project.dailylog.api.request.PostSearch;
import java.util.List;
import org.springframework.data.domain.Page;

public interface PostRepositoryCustom {
  List<Post> findAllByPage(PostSearch postSearch);
  Page<Post> findPostsByCondition(PostSearch postSearch);
}
