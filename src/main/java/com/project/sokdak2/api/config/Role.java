package com.project.sokdak2.api.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN", "관리자"),
    GENERAL("ROLE_GENERAL", "회원");

    private final String code;
    private final String name;
}
