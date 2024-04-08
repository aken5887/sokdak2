package com.project.sokdak2.api.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sokdak2.api.request.MailMessage;
import com.project.sokdak2.api.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsSQSListener {

    private final PostService postService;
    private final ObjectMapper objectMapper;
    private final MailService mailService;
    private final UserService userService;

    @SqsListener(value="${cloud.aws.sqs.queue.name}",
                    deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    public void listen(@Payload String info, @Headers Map<String, String > headers,
                       Acknowledgment ack) throws IOException {
        log.info("-------------------------");
        log.info(" 1. info : {}", info);
        log.info(" 2. headers : {}", headers);
        log.info("-------------------------");
        String messageGroupId = headers.get("MessageGroupId");

        System.out.println(headers.get("MessageGroupId"));

        switch(messageGroupId){
            case "post-create":
                PostCreate postCreate = objectMapper.readValue(info.getBytes(), PostCreate.class);
                postService.save(postCreate);
                ack.acknowledge(); // 수신후 삭제처리
                break;
            case "send-mail":
                List<String> emails = userService.findUserEmails();
                MailMessage mailMessage = objectMapper.readValue(info.getBytes(), MailMessage.class);
                for(String email:emails) {
                    log.info("Valid Email : {}", email);
                    mailMessage.updateEmail(email);
                    mailService.sendMail(mailMessage);
                }
                ack.acknowledge(); // 수신후 삭제처리
                break;
        }
    }
}
