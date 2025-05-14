package com.reborn.back.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RebirthStep {

    WASH("wash"),
    DRESS("dress"),
    RIBBON("ribbon"),
    POST("post"),
    OUTRO("outro");

    @JsonValue
    private final String value;

    RebirthStep(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
