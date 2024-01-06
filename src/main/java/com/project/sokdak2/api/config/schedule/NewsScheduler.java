package com.project.sokdak2.api.config.schedule;


import com.project.sokdak2.api.domain.post.Post;
import com.project.sokdak2.api.repository.PostRepository;
import com.project.sokdak2.api.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class NewsScheduler {
    private final PostRepository postRepository;
    private final NewsService newsService;
    @Scheduled(cron="${schedules.cron.newsArticle-get}")
    public void getNewsArticle() {
        String newsContent = newsService.getNewsArticleHtml();
        String title = "["+LocalDate.now()+"] 오늘의 뉴스";

        List<Post> today = postRepository.findAllByTitle(title);
        if(today.isEmpty()){
            Post post = Post.builder()
                    .title(title)
                    .content(newsContent)
                    .userId("뉴스정리 봇")
                    .password(9999)
                    .locked(0)
                    .build();
            postRepository.save(post);
        }
    }
}
