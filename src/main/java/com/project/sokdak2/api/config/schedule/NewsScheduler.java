package com.project.sokdak2.api.config.schedule;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.sokdak2.api.domain.post.Category;
import com.project.sokdak2.api.domain.post.Post;
import com.project.sokdak2.api.repository.PostRepository;
import com.project.sokdak2.api.request.MailMessage;
import com.project.sokdak2.api.service.AwsSQSSender;
import com.project.sokdak2.api.service.MailService;
import com.project.sokdak2.api.service.NewsService;
import com.project.sokdak2.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewsScheduler {
    private final PostRepository postRepository;
    private final NewsService newsService;
    private final AwsSQSSender sqsSender;
    private final MailService mailService;
    private final UserService userService;

    @Scheduled(cron = "${schedules.cron.newsArticle-get}")
    public void getNewsArticle() throws JsonProcessingException {
        String newsContent = newsService.getNewsArticleHtml();
        String title = "[" + LocalDate.now() + "] 오늘의 뉴스";

        List<Post> today = postRepository.findAllByTitle(title);
        if (today.isEmpty()) {
            Post post = Post.builder()
                    .title(title)
                    .content(newsContent)
                    .userId("뉴스정리 봇")
                    .password(9999)
                    .locked(0)
                    .category(Category.NEWS)
                    .build();
            postRepository.save(post);
            MailMessage mailMessage = MailMessage.builder()
                    .title(title)
                    .content(newsContent)
                    .build();
            // TODO spring boot 3 업데이트 이후 sqs listen 안되는 현상
//            sqsSender.sendMessageSendMail(mailMessage);
            List<String> emails = userService.findUserEmails();
            for (String email : emails) {
                log.info("Valid Email : {}", email);
                mailMessage.updateEmail(email);
                mailService.sendMail(mailMessage);
            }
        }
    }
}
