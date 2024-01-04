package com.project.sokdak2.api.config.schedule;


import com.project.sokdak2.api.domain.common.Visits;
import com.project.sokdak2.api.domain.post.Post;
import com.project.sokdak2.api.repository.PostRepository;
import com.project.sokdak2.api.repository.VisitsDao;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
public class VisitsScheduler {
    private final VisitsDao visitsDao;
    private final PostRepository postRepository;
    @Transactional
    @Scheduled(cron = "${schedules.cron.visits-remove}")
    public void removeVisits(){
        List<Visits> visitsList = visitsDao.selectUselessVisits();
        String title = "["+ LocalDate.now()+"] 청소 기록";
        String content = "금일은 방문기록이 0건 정리 되었습니다.";

        if(!visitsList.isEmpty()){
            long count = visitsDao.deleteUselessVisits();
            Visits visits = visitsList.get(0);
            content = "<p>금일은 방문기록이 <span style='font-weight:bold;'>'"+visits.getUri()+"'외 "+(count-1)+"건</span>이 정리 되었습니다.</p>";
        }

        Post post = Post.builder()
                .title(title)
                .content(content)
                .userId("스케쥴 봇")
                .password(9999)
                .locked(0)
                .build();
        postRepository.save(post);
    }
}
