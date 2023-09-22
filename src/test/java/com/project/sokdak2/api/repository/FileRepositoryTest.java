package com.project.sokdak2.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.sokdak2.api.domain.File;
import com.project.sokdak2.api.domain.Post;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FileRepositoryTest {

  @Autowired FileRepository fileRepository;
  @Autowired PostRepository postRepository;
  @Value("${file.upload.dir}")
  String uploadDir;

  @DisplayName("파일이 정상적으로 저장된다.")
  @Test
  void test1() {
    // given
    File file = File.builder()
        .uploadPath(uploadDir)
        .originalFileName("12345")
        .realFileName(renameFile(1))
        .build();
    // when
    fileRepository.save(file);
    // then
    assertThat(fileRepository.count()).isGreaterThan(0L);
  }

  @DisplayName("Post와 File이 동시에 정상저거으로 저장된다.")
  @Test
  void test2() {
    // given
    File file = File.builder()
        .uploadPath(uploadDir)
        .originalFileName("12345")
        .realFileName(renameFile(1))
        .build();

    fileRepository.save(file);

    Post post = Post.builder()
        .title("1234")
        .userId("1234")
        .password(1234)
        .files(Arrays.asList(file))
        .build();

    // when
    postRepository.save(post);

    // then
    assertThat(fileRepository.count()).isGreaterThan(0L);
    assertThat(post.getFiles()).isNotNull();
  }

  private String renameFile(long postId){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String timestamp = sdf.format(new Date());
    String newFileName = "post_"+postId+"_"+timestamp;
    return newFileName;
  }
}