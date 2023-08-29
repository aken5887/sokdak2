package com.project.dailylog.api.repository;

import com.project.dailylog.api.domain.Post;
import com.project.dailylog.api.domain.QPost;
import com.project.dailylog.api.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Slf4j
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom{

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Post> findAllByPage(PostSearch postSearch) {
    return jpaQueryFactory.selectFrom(QPost.post)
        .limit(postSearch.getSize())
        .offset(postSearch.getOffSet())
        .orderBy(QPost.post.id.desc())
        .fetch();
  }

  @Override
  public Page<Post> findPostsByCondition(PostSearch postSearch) {
    QPost post = QPost.post;

//    Predicate predicate = post.title.eq(postSearch.getKeyword());

    Pageable pageable = PageRequest.of(postSearch.getPage(), postSearch.getSize());

    List<Post> posts = jpaQueryFactory.selectFrom(post)
//        .where(predicate)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    long total = jpaQueryFactory
        .select(post.count())
        .from(post)
        .fetchOne();

    return new PageImpl<>(posts, pageable, total);
  }

}
