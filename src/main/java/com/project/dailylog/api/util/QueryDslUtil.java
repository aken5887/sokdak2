package com.project.dailylog.api.util;

import com.project.dailylog.api.domain.QPost;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

@Slf4j
public class QueryDslUtil {

  /**
   * Order, Path, FieldName을 전달하면 OrderSpecifier 객체를 리턴하는 메소드
   * @param order
   * @param 'Path' compileQuerydsl 빌드를 통해서 생성된 Q타입 클래스 객체
   * @param fieldName
   * @return
   */
  public static OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName){
    Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
    log.info("fieldPath : {}", fieldPath);
    return new OrderSpecifier(order, fieldPath);
  }

  /**
   * Pageable 객체의 sort 필드를 체크해서 OrderrSpecifier 리스트 객체를 생성한다.
   * @param pageable
   * @return
   */
  public static List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
    List<OrderSpecifier> ORDERS = new ArrayList<>();

    if(!ObjectUtils.isEmpty(pageable.getSort())){
      for(Sort.Order order : pageable.getSort()){
         Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
         OrderSpecifier<?> orderSpec = QueryDslUtil.getSortedColumn(direction, QPost.post, order.getProperty());
         ORDERS.add(orderSpec);
      }
    }

    return ORDERS;
  }
}
