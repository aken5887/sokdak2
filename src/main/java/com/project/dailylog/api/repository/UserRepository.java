package com.project.dailylog.api.repository;

import com.project.dailylog.api.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUserIdAndPassword(String userId, String password);
}
