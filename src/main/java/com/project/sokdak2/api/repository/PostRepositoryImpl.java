package com.project.sokdak2.api.repository;

import com.project.sokdak2.api.domain.post.Post;
import com.project.sokdak2.api.domain.post.QPost;
import com.project.sokdak2.api.request.PostSearch;
import com.project.sokdak2.api.response.PostResponse;
import com.project.sokdak2.api.util.QueryDslUtil;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

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
    String type = postSearch.getKw_opt();
    BooleanExpression booleanExpression = null;
    if("title".equals(type)){
      booleanExpression = post.title.contains(postSearch.getKw());
    }else if("userId".equals(type)){
      booleanExpression = post.userId.contains(postSearch.getKw());
    }

    if(postSearch.getCategory() != null){
      // 여기에 추가
      if(booleanExpression != null){
        return booleanExpression.and(post.category.eq(postSearch.getCategory()));
      }else{
        return post.category.eq(postSearch.getCategory());
      }
    }

    if(postSearch.getSearchUserId() != null){
      // 여기에 추가
      if(booleanExpression != null){
        return booleanExpression.and(post.userId.eq(postSearch.getSearchUserId()));
      }else{
        return post.userId.eq(postSearch.getSearchUserId());
      }
    }

    return booleanExpression;
  }

}
