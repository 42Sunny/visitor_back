package com.ftseoul.visitor.policy;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReserveType {
    REPRESENTATIVE("REPRESENTATIVE"), DEFAULT("DEFAULT");

    private final String title;

    public String getTitle() {
        return title;
    }
}
