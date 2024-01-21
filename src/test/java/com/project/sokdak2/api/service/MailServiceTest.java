package com.project.sokdak2.api.service;

import com.project.sokdak2.api.request.MailMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;

@SpringBootTest
class MailServiceTest {

    @MockBean
    MailService mailService;
    @MockBean
    SimpleMessageListenerContainer simpleMessageListenerContainer;

    @DisplayName("메일이 정상적으로 발송 된다.")
    @Test
    void test() {
        //given
        MailMessage mailMessage = MailMessage.builder()
                .title("오늘의 기사")
                .content("<h3>섹션[A1면]</h3><ul><li>대한항공·아시아나 합병…EU '최종 승인' 방침<a href='https://n.news.naver.com/article/newspaper/015/0004936380?date=20240113' target='_blank' style='color:#1111df;'> [링크]</a></li>")
                .build();
        mailMessage.updateEmail("crimenut@naver.com");
        // when
        mailService.sendMail(mailMessage);
    }
}