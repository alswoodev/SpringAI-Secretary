package com.spring.ai.basic.entity.enums.task;

import java.util.Arrays;

public enum TaskPriority {
    HIGH,
    MEDIUM,
    LOW;

    public static TaskPriority from(String value) {
        return Arrays.stream(values())
                .filter(t -> t.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid TaskPriority: " + value));
    }
}