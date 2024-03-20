package com.project.sokdak2.api.domain.common;

import java.time.LocalDateTime;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
// jpa entity 클래스들이 createTime, modiedTime 필드를 칼럼으로 인식
@MappedSuperclass
// 해당클래스를 auditing 기능(시간에 대해서 자동으로 값을 넣어줌)을을 포함
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
  @CreatedDate
  protected LocalDateTime createdTime;
  @LastModifiedDate
  protected LocalDateTime lastUpdatedTime;
}
