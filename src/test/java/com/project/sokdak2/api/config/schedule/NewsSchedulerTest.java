package com.project.sokdak2.api.config.schedule;

import com.project.sokdak2.api.domain.post.Post;
import com.project.sokdak2.api.repository.PostRepository;
import com.project.sokdak2.api.service.NewsService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = {"schedules.cron.newsArticle-get : 0/5 * * * * ?"})
class NewsSchedulerTest {

    @Autowired
    PostRepository postRepository;

    @SpyBean
    NewsScheduler newsScheduler;

    @Autowired
    NewsService newsService;

    @DisplayName("오늘 뉴스가 없으면 오늘자 뉴스가 등록된다.")
    @Test
    void test() {
        // given
        String title = "["+LocalDate.now()+"] 오늘의 뉴스";

        // when
        Awaitility.await()
                .atMost(15, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                   verify(newsScheduler, atLeast(1)).getNewsArticle();
                });
        // then
        List<Post> posts = postRepository.findAllByTitle(title);
        if(!posts.isEmpty()){
            System.out.println(posts.get(0).getContent());
        }
    }
}