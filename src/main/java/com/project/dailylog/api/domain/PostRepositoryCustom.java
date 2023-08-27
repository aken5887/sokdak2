package com.project.dailylog.api.domain;

import com.project.dailylog.api.request.PostSearch;
import java.util.List;

public interface PostRepositoryCustom {
  List<Post> findAllByPage(PostSearch postSearch);
}
