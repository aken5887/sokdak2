package com.project.sokdak2.api.domain.user;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * author         : choi
 * date           : 2024-03-24
 */
@Getter
public class UserPrincipal extends User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String name;
    public UserPrincipal(com.project.sokdak2.api.domain.user.User user){
        super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole().getCode())));
        this.id = user.getId();
        this.username = user.getEmail();
        this.name = user.getName();
    }
}
