package com.project.sokdak2.api.domain;

import com.project.sokdak2.api.config.Role;
import com.project.sokdak2.api.request.SessionUser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="tb_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotBlank
  private String name;

  @NotBlank
  private String password;

  private String email;

  // session을 db에 저장하는 경우에 사용
  @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
  private List<Session> sessions = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Builder
  public User(String name, String password, String email, Role role) {
    this.name = name;
    this.password = password;
    this.email = email;
    this.role = role;
  }

  public Session addSession(){
    Session newSession = Session.builder()
        .user(this)
        .build();
    sessions.add(newSession);

    return newSession;
  }

  public SessionUser toSessionUser(){
    return SessionUser.builder()
            .id(this.id)
            .email(this.email)
            .name(this.name)
            .role(this.role)
            .build();
  }
}
