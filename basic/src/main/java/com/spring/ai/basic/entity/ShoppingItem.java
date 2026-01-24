package com.spring.ai.basic.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import com.spring.ai.basic.entity.enums.ShoppingItem.ShoppingItemStatus;

@Entity
@Table(name = "shopping_items", indexes = {
    @Index(name = "idx_user_status", columnList = "user_id, status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingItem extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;
    
    @Column(nullable = false, length = 200)
    private String itemName;
    
    @Column
    @Builder.Default
    private Integer quantity = 1;
    
    //@Column(length = 20)
    //private String category; // 식품, 생필품, 전자제품, etc.
    
    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ShoppingItemStatus status = ShoppingItemStatus.PENDING; // pending, purchased, cancelled
    
    @Column(columnDefinition = "TEXT")
    @Builder.Default
    private String notes = "";
    
    @Column
    private LocalDateTime purchasedAt;

/**
     * Static factory method to create a ShoppingItem instance.
     * - essential field validation: user, itemName
     * - optional fields: quantity, notes, status are set via Builder.Default
     */
    public static ShoppingItem createItem(String userId,
                                          String itemName,
                                          Integer quantity,
                                          ShoppingItemStatus status,
                                          String notes) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (itemName == null || itemName.isBlank()) {
            throw new IllegalArgumentException("Item name cannot be null or blank");
        }

        return ShoppingItem.builder()
                .userId(userId)
                .itemName(itemName)
                .quantity(quantity)
                .status(status)
                .notes(notes)
                .build();
    }
} 