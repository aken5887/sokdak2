package com.project.sokdak2.api.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MailMessage {
    private String email;
    private String content;
    private String title;

    @Builder
    public MailMessage(String content, String title) {
        this.title = title;
        this.content = content;
    }

    public void updateEmail(String email){
        this.email = email;
    }
}
