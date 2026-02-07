package com.spring.ai.basic.service;

import org.springframework.stereotype.Service;
import com.spring.ai.basic.entity.Task;
import com.spring.ai.basic.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import com.spring.ai.basic.entity.enums.task.TaskStatus;
import java.util.List;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    // Retrieve tasks by user ID
    public List<Task> getTaskByUserId(String userId) {
        nullValidate(userId);
        List<Task> tasks = taskRepository.findByUserUserId(userId);
        return tasks;
    }

    // Retrieve future tasks by user ID
    public List<Task> getFutureTaskByUserId(String userId) {
        nullValidate(userId);
        return taskRepository.findUpcomingTasks(userId);
    }

    // Retrieve tasks by date range
    public List<Task> getTask(String userId, LocalDate startDate, LocalDate endDate) {
        nullValidate(userId);
        if(startDate.isAfter(endDate)){
            throw new IllegalArgumentException("Invalid date range: 'from' date is after 'to' date");
        }
        return taskRepository.findTask(userId, startDate, endDate, TaskStatus.SCHEDULED);
    }

    // Retrieve tasks by priority
    public List<Task> getTaskWithPriority(String userId, String priority) {
        nullValidate(userId);
        return taskRepository.findTaskWithPriority(userId, priority, TaskStatus.SCHEDULED);
    }

    // Add a new task
    public Task addTask(String userId, String title, String description, LocalDate startDate, LocalDate endDate, String type, String priority) {
        nullValidate(userId);
        if(startDate.isAfter(endDate)){
            throw new IllegalArgumentException("Invalid date range: 'from' date is after 'to' date");
        }
        return taskRepository.save(Task.builder()
                .userId(userId)
                .title(title)
                .description(description)
                .startDate(startDate)
                .endDate(endDate)
                .type(type)
                .priority(priority)
                .build());
    }

    // Cancel a task
    public Task cancelTask(String userId, Long taskId) {
        nullValidate(userId);
        // Implementation for cancelling a task
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        
        if (!task.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User does not have permission to cancel this task");
        }

        task.setStatus(TaskStatus.CANCELLED);
        taskRepository.save(task);
        return task;
    }

    // Dummy implementation for user validation
    private void nullValidate(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
    }
}