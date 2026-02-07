package com.spring.ai.basic.entity;

import java.time.LocalDate;
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
@Builder(builderClassName = "ShoppingItemBuilder") // 커스텀 빌더 사용
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
    private Integer quantity;

    //@Column(length = 20)
    //private String category; // 식품, 생필품, 전자제품, etc.

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private ShoppingItemStatus status; // pending, purchased, cancelled

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column
    private LocalDate purchasedAt;

    /**
     * Custom Builder:
     * - Null check for mandatory fields
     * - Applies default values for optional fields
     * - Throws exceptions on invalid input
     */
    public static class ShoppingItemBuilder {
        public ShoppingItem build() {
            // Null check for mandatory fields
            if (userId == null || userId.isBlank()) {
                throw new IllegalArgumentException("User ID cannot be null or blank");
            }
            if (itemName == null || itemName.isBlank()) {
                throw new IllegalArgumentException("Item name cannot be null or blank");
            }

            // Set default values
            if (quantity == null) quantity = 1;
            if (status == null) status = ShoppingItemStatus.PENDING;
            if (notes == null) notes = "";

            return new ShoppingItem(itemId, userId, user, itemName, quantity, status, notes, purchasedAt);
        }
    }
}
