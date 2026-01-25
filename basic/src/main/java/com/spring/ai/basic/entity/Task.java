package com.spring.ai.basic.entity;

import jakarta.persistence.*;
import lombok.*;
import com.spring.ai.basic.entity.enums.task.*;

import java.time.LocalDate;
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

    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate endDate;
    
    @Column(length = 20)
    @Builder.Default
    private String type = "EVENT"; // meeting, task, reminder, event
    
    @Column(length = 20)
    @Builder.Default
    private String priority = "MEDIUM"; // high, medium, low
    
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TaskStatus status = TaskStatus.SCHEDULED; // scheduled, completed, cancelled, undone
    
    @Column
    private LocalDateTime completedAt;

    /**
     * Static factory method to create a Task instance.
     * - essential field validation: user, title, description, startDate, endDate
     * - optional fields: type, priority, status are set via Builder.Default
     */
    public static Task createTask(String userId, String title, String description, LocalDate startDate, LocalDate endDate, String type, String priority) {
        if( userId == null ) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("StartDate and EndDate cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be before endDate");
        }
        
        return Task.builder()
                .userId(userId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .type(type)
                .priority(priority)
                .status(TaskStatus.SCHEDULED)
                .build();
    }
}