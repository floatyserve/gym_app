package com.example.demo.common;

public enum ResourceType {
    ACCESS_CARD,
    CUSTOMER,
    WORKER,
    USER,
    VISIT,
    LOCKER,
    MEMBERSHIP;

    public String code() {
        return name();
    }
}