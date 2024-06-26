package com.project.sokdak2.api.service;


import com.project.sokdak2.api.request.MailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender mailSender;

    public void sendMail(MailMessage message) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(message.getEmail());
            mimeMessageHelper.setSubject(message.getTitle());
            mimeMessageHelper.setText(message.getContent(), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.debug(e.getMessage());
        }
    }
}
