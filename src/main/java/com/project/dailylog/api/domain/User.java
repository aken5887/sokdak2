package com.project.dailylog.api.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name="tb_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotBlank
  private String userId;

  @NotBlank
  private String name;

  @NotBlank
  private String password;

  private String email;

  @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
  private List<Session> sessions = new ArrayList<>();

  @Builder
  public User(String userId, String name, String password, String email) {
    this.userId = userId;
    this.name = name;
    this.password = password;
    this.email = email;
  }

  public Session addSession(){
    Session newSession = Session.builder()
        .user(this)
        .build();
    sessions.add(newSession);

    return newSession;
  }
}
