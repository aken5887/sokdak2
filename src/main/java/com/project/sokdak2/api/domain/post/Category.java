package com.project.sokdak2.api.domain.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    NEWS("NEWS"), BBS("BBS"), BATCH("BATCH");
    private final String code;
}
