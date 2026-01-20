package com.spring.ai.basic.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks", indexes = {
    @Index(name = "idx_user_date", columnList = "user_id, startDate"),
    @Index(name = "idx_type_status", columnList = "type, status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private LocalDateTime startDate;
    
    @Column(nullable = false)
    private LocalDateTime endDate;
    
    @Column(length = 20, nullable = false)
    @Builder.Default
    private String type = "event"; // meeting, task, reminder, event
    
    @Column(length = 20)
    @Builder.Default
    private String priority = "medium"; // high, medium, low
    
    @Column(length = 20, nullable = false)
    @Builder.Default
    private String status = "scheduled"; // scheduled, completed, cancelled
    
    @Column
    private LocalDateTime completedAt;
}