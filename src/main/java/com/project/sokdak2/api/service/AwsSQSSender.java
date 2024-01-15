package com.project.sokdak2.api.service;


import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sokdak2.api.request.MailMessage;
import com.project.sokdak2.api.request.PostCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class AwsSQSSender {
    @Value("${cloud.aws.sqs.queue.url}")
    private String url;

    private final ObjectMapper objectMapper;
    private final AmazonSQS amazonSQS;

    public SendMessageResult sendMessage(PostCreate postCreate) throws JsonProcessingException {
        SendMessageRequest sendMessageRequest =
                new SendMessageRequest(url, objectMapper.writeValueAsString(postCreate))
                        .withMessageGroupId("post-create")
                        .withMessageDeduplicationId(UUID.randomUUID().toString());
        return amazonSQS.sendMessage(sendMessageRequest);
    }

    public SendMessageResult sendMessageSendMail(MailMessage mailMessage) throws JsonProcessingException {
        SendMessageRequest request =
                new SendMessageRequest(url, objectMapper.writeValueAsString(mailMessage))
                        .withMessageGroupId("send-mail")
                        .withMessageDeduplicationId(UUID.randomUUID().toString());
        return amazonSQS.sendMessage(request);
    }
}
