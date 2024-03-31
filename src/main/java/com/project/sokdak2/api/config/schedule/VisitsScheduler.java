package com.project.sokdak2.api.config.schedule;


import com.project.sokdak2.api.domain.common.Visits;
import com.project.sokdak2.api.domain.post.Category;
import com.project.sokdak2.api.domain.post.Post;
import com.project.sokdak2.api.repository.PostRepository;
import com.project.sokdak2.api.repository.VisitsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class VisitsScheduler {
    private final VisitsRepository visitsRepository;
    private final PostRepository postRepository;
    @Transactional
    @Scheduled(cron = "${schedules.cron.visits-remove}")
    public void removeVisits(){
        List<Visits> visitsList = visitsRepository.selectUselessVisits();
        String title = "["+ LocalDate.now()+"] 청소 기록";
        String content = "금일은 방문기록이 0건 정리 되었습니다.";

        if(!visitsList.isEmpty()){
            long count = visitsRepository.deleteUselessVisits();
            Visits visits = visitsList.get(0);
            content = "<p>금일은 방문기록이 <span style='font-weight:bold;'>'"+visits.getUri()+"'외 "+(count-1)+"건</span>이 정리 되었습니다.</p>";
        }

        Post post = Post.builder()
                .title(title)
                .content(content)
                .userId("스케쥴 봇")
                .password(9999)
                .locked(0)
                .category(Category.BATCH)
                .build();
        postRepository.save(post);
    }
}
