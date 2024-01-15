package com.project.sokdak2.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.sokdak2.api.domain.post.Category;
import com.project.sokdak2.api.domain.post.Post;
import com.project.sokdak2.api.repository.PostRepository;
import com.project.sokdak2.api.repository.UserRepository;
import com.project.sokdak2.api.request.MailMessage;
import com.project.sokdak2.api.request.PostCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@AutoConfigureMockMvc
@SpringBootTest
class AwsSQSSenderTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AwsSQSSender awsSQSSender;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("sqs에 postCreate를 보낸다")
    @Test
    void test() throws JsonProcessingException {
        String title = "Amazon SQS trial2";
        // given
        PostCreate postCreate = PostCreate.builder()
                .title(title)
                .content("test")
                .userId("sqs")
                .password(9999)
                .locked(0)
                .build();

        // when
        awsSQSSender.sendMessage(postCreate);
    }

    @DisplayName("sqs에 sendMail을 보낸다.")
    @Test
    void test2() throws JsonProcessingException {
        // given Post
        Post post = Post.builder()
                .title("Send Mail Test")
                .content("Send Mail Test Content")
                .userId("admin")
                .password(999)
                .locked(0)
                .category(Category.NEWS)
                .build();
        postRepository.save(post);

        // when
        awsSQSSender.sendMessageSendMail(MailMessage.builder()
                        .content(post.getContent())
                        .title(post.getTitle())
                        .build());
    }
}