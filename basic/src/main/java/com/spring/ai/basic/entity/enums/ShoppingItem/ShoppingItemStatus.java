package com.spring.ai.basic.entity.enums.ShoppingItem;

import java.util.Arrays;


public enum ShoppingItemStatus {
    PENDING,
    PURCHASED,
    CANCELLED;

    public static ShoppingItemStatus from(String value) {
        return Arrays.stream(values())
            .filter(t -> t.name().equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid ShoppingItemStatus: " + value));
    }
}