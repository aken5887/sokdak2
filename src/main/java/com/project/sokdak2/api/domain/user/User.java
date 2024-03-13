package com.project.sokdak2.api.domain.user;

import com.project.sokdak2.api.domain.common.BaseTimeEntity;
import com.project.sokdak2.api.request.SessionUser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Entity
@Table(name="tb_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotBlank
  private String name;

  @NotBlank
  private String password;

  private String email;

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

  public SessionUser toSessionUser(){
    return SessionUser.builder()
            .id(this.id)
            .email(this.email)
            .name(this.name)
            .role(this.role)
            .build();
  }
}
