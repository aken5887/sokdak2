package com.project.dailylog.api.repository;

import com.project.dailylog.api.domain.Post;
import com.project.dailylog.api.domain.QPost;
import com.project.dailylog.api.request.PostSearch;
import com.project.dailylog.api.response.PostResponse;
import com.project.dailylog.api.util.QueryDslUtil;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
  public Page<PostResponse> findPostsByCondition(PostSearch postSearch) {
    QPost post = QPost.post;

    Pageable pageable = postSearch.makePageable();
    List<OrderSpecifier> ORDERS = QueryDslUtil.getAllOrderSpecifiers(pageable);

    List<Post> posts
        = jpaQueryFactory.select(post)
        .from(post)
        .where(post.id.gt(0L)
            .and(equalTypeAndKeyword(post, postSearch)))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
        .fetch();

    List<PostResponse> responses
        = posts.stream()
        .map(PostResponse::new)
        .collect(Collectors.toList());

    long total = jpaQueryFactory
        .select(post.count())
        .from(post)
        .where(post.id.gt(0L)
            .and(equalTypeAndKeyword(post, postSearch)))
        .fetchOne();

    return new PageImpl<>(responses, pageable, total);
  }

  private Predicate equalTypeAndKeyword(QPost post, PostSearch postSearch) {
    String type = postSearch.getKwOpt();
    if("t".equals(type)){
      return post.title.contains(postSearch.getKw());
    }else if("w".equals(type)){
      return post.userId.contains(postSearch.getKw());
    }
    return null;
  }

}
