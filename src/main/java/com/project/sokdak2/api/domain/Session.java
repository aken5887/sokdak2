package com.project.sokdak2.api.domain;

import com.project.sokdak2.api.request.SessionUser;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name="tb_session")
@Entity
public class Session {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String accessToken;

  private LocalDateTime expiredDate;

  @ManyToOne
  private User user;

  @Builder
  public Session(User user) {
    this.accessToken = UUID.randomUUID().toString();
    this.expiredDate = LocalDateTime.now().plusMonths(1L);
    this.user = user;
  }

  public SessionUser toSessionUser(){
    return SessionUser.builder()
        .id(this.id)
        .build();
  }
}
