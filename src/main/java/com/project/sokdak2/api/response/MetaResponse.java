package com.project.sokdak2.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MetaResponse {
    /**
     *      createMetaTag('description', postTitle);
     *     createMetaTag('og:title', postTitle);
     *     createMetaTag('og:url', location.href);
     *     createMetaTag('og:image', thumbnailImage);
     *     createMetaTag('og:description', postTitle);
     *
     *     createMetaTag('twitter:title', postTitle);
     *     createMetaTag('twitter:card', 'summary_large_image');
     *     createMetaTag('twitter:image', thumbnailImage);
     *     createMetaTag('twitter:description', postTitle);
     */
    private String postTitle;
    private String url;
    private String image;
    private String description;
    private String twitterCard = "summary_large_image";

    @Builder
    public MetaResponse(String postTitle, String url, String image, String description, String twitterCard) {
        this.postTitle = postTitle;
        this.url = url;
        this.image = image;
        this.description = description;
        this.twitterCard = twitterCard;
    }
}
