package com.project.sokdak2.api.domain.post;

import com.project.sokdak2.api.domain.common.File;
import com.project.sokdak2.api.repository.FileRepository;
import com.project.sokdak2.api.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * author         : choi
 * date           : 2024-03-13
 */
@SpringBootTest
class PostTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    FileRepository fileRepository;

    @MockBean
    SimpleMessageListenerContainer simpleMessageListenerContainer;

    @DisplayName("cascade 설정을 했을 때")
    @Test
    public void test2() {
        Post post = Post.builder()
                .title("cacade true")
                .password(1234)
                .userId("testuser")
                .build();

        File file = File.builder()
                .fileSize(10)
                .realFileName("test")
                .originalFileName("test")
                .uploadPath("temp")
                .build();

        post.addFile(file);
        postRepository.save(post);

        assertThat(fileRepository.count()).isEqualTo(1);

//        post.getFiles().clear();
//        postRepository.save(post);
//        assertThat(fileRepository.count()).isEqualTo(0);

        postRepository.delete(post);
        assertThat(fileRepository.count()).isEqualTo(0);
    }
}