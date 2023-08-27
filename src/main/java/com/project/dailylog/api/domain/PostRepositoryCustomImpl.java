package com.project.dailylog.api.domain;

import com.project.dailylog.api.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom{

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Post> findAllByPage(PostSearch postSearch) {
    log.info("PostSearch : {}", postSearch.toString());
    return jpaQueryFactory.selectFrom(QPost.post)
        .limit(postSearch.getSize())
        .offset(postSearch.getOffSet())
        .orderBy(QPost.post.id.desc())
        .fetch();
  }
}
