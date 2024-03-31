package com.project.sokdak2.api.repository;

import com.project.sokdak2.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findUserByEmail(String email);
  Optional<User> findUserByEmailAndPassword(String email, String password);
}
