package com.project.sokdak2.api.request;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewsArticle {
    private String section;
    private String title;
    private String press;
    private String summary;
    private String link;
}
