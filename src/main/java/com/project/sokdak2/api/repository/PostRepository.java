package com.project.sokdak2.api.repository;

import com.project.sokdak2.api.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
  List<Post> findAllByTitle(String title);
  List<Post> findAllByUserId(String userId);
}
