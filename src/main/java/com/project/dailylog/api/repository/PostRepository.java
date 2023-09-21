package com.project.dailylog.api.repository;

import com.project.dailylog.api.domain.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
  List<Optional<Post>> findAllByTitle(String title);
}
