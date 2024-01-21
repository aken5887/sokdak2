package com.project.sokdak2.api.config.schedule;

import com.project.sokdak2.api.domain.common.Visits;
import com.project.sokdak2.api.domain.post.Post;
import com.project.sokdak2.api.repository.PostRepository;
import com.project.sokdak2.api.repository.VisitsRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.atLeast;

@SpringBootTest(properties = {
        "schedules.cron.visits-remove=0/5 * * * * ?",
        })
class VisitsSchedulerTest {
    @Autowired
    PostRepository postRepository;
    @Autowired
    VisitsRepository visitsRepository;
    @SpyBean
    VisitsScheduler visitsScheduler;
    @MockBean
    SimpleMessageListenerContainer simpleMessageListenerContainer;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @DisplayName("5초마다 Visits 기록이 삭제되고 Post가 기록되는지 확인한다.")
    @Test
    void test() {
        // given
        List<Visits> visits = IntStream.range(0, 20)
                .mapToObj(i -> Visits.builder().uri("/posts/"+i)
                            .ip("127.0.0.1")
                            .build())
                .collect(Collectors.toList());

        visitsRepository.saveAll(visits);

        // when
        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(visitsScheduler, atLeast(1)).removeVisits();
                    assertThat(postRepository.findAllByUserId("스케쥴 봇").size()).isGreaterThan(0);
                });

        Post post = postRepository.findAllByUserId("스케쥴 봇").get(0);
        System.out.println(post.getTitle());
        System.out.println(post.getContent());
    }
}