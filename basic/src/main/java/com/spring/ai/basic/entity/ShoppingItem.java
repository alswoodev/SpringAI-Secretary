package com.spring.ai.basic.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false, length = 200)
    private String itemName;
    
    @Column
    @Builder.Default
    private Integer quantity = 1;
    
    //@Column(length = 20)
    //private String category; // 식품, 생필품, 전자제품, etc.
    
    @Column(length = 20, nullable = false)
    @Builder.Default
    private String status = "pending"; // pending, purchased, cancelled
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column
    private LocalDateTime purchasedAt;
} 