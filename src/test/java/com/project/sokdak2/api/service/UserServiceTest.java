package com.project.sokdak2.api.service;

import com.project.sokdak2.api.domain.user.Role;
import com.project.sokdak2.api.domain.user.User;
import com.project.sokdak2.api.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @MockBean
    SimpleMessageListenerContainer simpleMessageListenerContainer;

    @BeforeEach
    void clean(){
        userRepository.deleteAll();
    }

    @DisplayName("유효한 이메일만 리턴한다.")
    @Test
    void test() {
        // given
        List<User> users = IntStream.range(0, 10)
                .mapToObj(i -> {
                        return User.builder()
                        .name("man "+i)
                        .email(i+"mail@valid.com")
                        .password("9999")
                        .role(Role.GENERAL)
                        .build();
                })
                .collect(Collectors.toList());
        users.addAll(IntStream.range(0, 5)
                .mapToObj(i -> {
                    return User.builder()
                            .name("man "+i)
                            .email("invalid.com")
                            .password("9999")
                            .role(Role.GENERAL)
                            .build();
                })
                .collect(Collectors.toList()));

        userRepository.saveAll(users);

        // when
        List<String> validEmails = userService.findUserEmails();

        // then
        Assertions.assertThat(validEmails.size()).isEqualTo(10);
    }
}