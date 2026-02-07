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
@Builder(builderClassName = "TaskBuilder") // Use Custom Builder
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
    private String type; // meeting, task, reminder, event

    @Column(length = 20)
    private String priority; // high, medium, low

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private TaskStatus status; // scheduled, completed, cancelled, undone

    @Column
    private LocalDateTime completedAt;

    /**
     * Custom Builder:
     * - Null check for mandatory fields
     * - Applies default values for optional fields
     * - Throws exceptions on invalid input
     */
    public static class TaskBuilder {
        public Task build() {
            // Check mandatory fields
            if (userId == null || userId.isBlank()) {
                throw new IllegalArgumentException("User ID cannot be null or blank");
            }
            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("Title cannot be null or blank");
            }
            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("StartDate and EndDate cannot be null");
            }
            // Check date validity
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("startDate must be before endDate");
            }

            // Set default values
            if (type == null || type.isBlank()) type = "EVENT";
            if (priority == null || priority.isBlank()) priority = "MEDIUM";
            if (status == null) status = TaskStatus.SCHEDULED;

            return new Task(taskId, userId, user, title, description, startDate, endDate, type, priority, status, completedAt);
        }
    }
}
