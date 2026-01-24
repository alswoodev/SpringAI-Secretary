package com.spring.ai.basic.entity.enums.task;

import java.util.Arrays;

public enum TaskStatus {
    SCHEDULED,
    COMPLETED,
    CANCELLED,
    UNDONE;

    public static TaskStatus from(String value) {
        return Arrays.stream(values())
            .filter(t -> t.name().equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid TaskStatus: " + value));
    }
}
