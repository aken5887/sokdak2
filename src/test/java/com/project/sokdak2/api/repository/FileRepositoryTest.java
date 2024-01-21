package com.project.sokdak2.api.repository;

import com.project.sokdak2.api.domain.common.File;
import com.project.sokdak2.api.domain.post.Post;
import org.hibernate.collection.internal.PersistentBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

//@DataJpaTest
@SpringBootTest
class FileRepositoryTest {

  @Autowired FileRepository fileRepository;
  @Autowired PostRepository postRepository;
  @Value("${file.upload.dir}")
  String uploadDir;
  @MockBean
  SimpleMessageListenerContainer simpleMessageListenerContainer;

  @BeforeEach
  public void cleanup(){
    this.fileRepository.deleteAll();
    this.postRepository.deleteAll();
  }

  @DisplayName("Post와 File이 동시에 정상적으로 저장된다.")
  @Transactional
  @Test
  void test2() {
    // given
    Post post = Post.builder()
        .title("1234")
        .userId("1234")
        .password(1234)
        .build();

    File file = File.builder()
            .uploadPath(uploadDir)
            .originalFileName("12345")
            .realFileName(renameFile(1))
            .build();

    post.addFile(file);

    postRepository.save(post);

    // then
    assertThat(fileRepository.count()).isGreaterThan(0L);
    assertThat(post.getFiles().get(0).getOriginalFileName()).isEqualTo(file.getOriginalFileName());
  }

  @DisplayName("Post 객체를 조회할 때 연관 File 객체는 PersistentBag 객체에 래핑되어있다.")
  @Test
  void test3() {

    Post post = Post.builder()
        .title("1234")
        .userId("1234")
        .password(1234)
        .build();

    postRepository.save(post);

    File file = File.builder()
        .uploadPath(uploadDir)
        .originalFileName("12345")
        .realFileName(renameFile(1))
        .build();

    fileRepository.save(file);

    assertThat(post.getFiles()).isInstanceOf(PersistentBag.class); //
  }

  private String renameFile(long postId){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String timestamp = sdf.format(new Date());
    String newFileName = "post_"+postId+"_"+timestamp;
    return newFileName;
  }
}