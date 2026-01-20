package com.spring.ai.basic.repository;

import com.spring.ai.basic.entity.Task;
import com.spring.ai.basic.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.spring.ai.basic.entity.enums.task.*;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Generate Test User
        testUser = User.builder()
                .email("test@example.com")
                .name("Test User")
                .build();
        testUser = userRepository.save(testUser);
    }

    @Test
    void testFindByUserUserId() {
        // given
        Task task1 = Task.builder()
                .user(testUser)
                .title("Task 1")
                .description("Description 1")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .status(TaskStatus.SCHEDULED)
                .build();

        Task task2 = Task.builder()
                .user(testUser)
                .title("Task 2")
                .description("Description 2")
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(2))
                .status(TaskStatus.COMPLETED)
                .build();

        taskRepository.save(task1);
        taskRepository.save(task2);
        entityManager.flush();
        entityManager.clear();

        // when
        List<Task> tasks = taskRepository.findByUserUserId(testUser.getUserId());

        // then
        assertEquals(2, tasks.size());
    }

    @Test
    void testFindHighPriorityTask() {
        // given
        Task highPriorityTask = Task.builder()
                .user(testUser)
                .title("High Priority Task")
                .priority(TaskPriority.HIGH)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .status(TaskStatus.SCHEDULED)
                .build();

        Task lowPriorityTask = Task.builder()
                .user(testUser)
                .title("Low Priority Task")
                .priority(TaskPriority.LOW)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .status(TaskStatus.SCHEDULED)
                .build();

        taskRepository.save(highPriorityTask);
        taskRepository.save(lowPriorityTask);
        entityManager.flush();

        // when
        List<Task> highPriorityTasks = taskRepository.findTaskWithPriority(testUser.getUserId(), TaskPriority.HIGH);

        // then
        assertEquals(1, highPriorityTasks.size());
        assertEquals(TaskPriority.HIGH, highPriorityTasks.get(0).getPriority());
    }

    @Test
    void testFindTaskByRange() {
        // given
        LocalDateTime today = LocalDateTime.now();
        
        Task todayTask = Task.builder()
                .user(testUser)
                .title("Today Task")
                .startDate(today)
                .endDate(today.plusDays(1))
                .status(TaskStatus.SCHEDULED)
                .build();

        Task todayTask2 = Task.builder()
                .user(testUser)
                .title("Today Task 2")
                .startDate(today)
                .endDate(today)
                .status(TaskStatus.SCHEDULED)
                .build();

        Task futureTask = Task.builder()
                .user(testUser)
                .title("Future Task")
                .startDate(today.plusDays(5))
                .endDate(today.plusDays(6))
                .status(TaskStatus.SCHEDULED)
                .build();

        taskRepository.save(todayTask);
        taskRepository.save(todayTask2);
        taskRepository.save(futureTask);
        entityManager.flush();

        // when
        List<Task> tasksRange7Days = taskRepository.findTask(testUser.getUserId(), today, today.plusDays(7), TaskStatus.SCHEDULED);

        // then
        assertEquals(3, tasksRange7Days.size());
    } 
}
