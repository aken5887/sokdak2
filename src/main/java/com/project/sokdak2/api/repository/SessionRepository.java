package com.project.sokdak2.api.repository;

import com.project.sokdak2.api.domain.Session;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
  Optional<Session> findByAccessToken(String accessToken);
}
