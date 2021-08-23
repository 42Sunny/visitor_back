package com.ftseoul.visitor.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN", "admin");
    private final String key;
    private final String title;
}
