package com.ftseoul.visitor.data.visitor;

public enum PlaceCode {
    P1("개포"),
    P2("서초"),
    P3("ALL");

    private final String place;

    PlaceCode(String place) {
        this.place = place;
    }

    public String getName() {
        return place;
    }
}
