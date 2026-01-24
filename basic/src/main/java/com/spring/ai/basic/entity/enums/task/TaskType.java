package com.spring.ai.basic.entity.enums.task;

import java.util.Arrays;

public enum TaskType {
    MEETING,
    TASK,
    REMINDER,
    EVENT;

    public static TaskType from(String value) {
        return Arrays.stream(values())
            .filter(t -> t.name().equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid TaskType: " + value));
    }
}
