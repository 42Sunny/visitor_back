package com.ftseoul.visitor.policy;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReserveType {
    REPRESENTATIVE("REPRESENTATIVE"), DEFAULT("DEFAULT");

    private final String title;

    public String getTitle() {
        return title;
    }
}
