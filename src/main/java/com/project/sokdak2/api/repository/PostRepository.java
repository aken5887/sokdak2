package com.project.sokdak2.api.repository;

import com.project.sokdak2.api.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
  List<Post> findAllByTitle(String title);
  List<Post> findAllByUserId(String userId);
  @Query("select distinct p from Post p left join fetch p.files left join p.replies")
  List<Post> findAllJPQLFetch();
}
