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

import java.time.LocalDate;
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
                .userId(testUser.getUserId())
                .title("Task 1")
                .description("Description 1")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .status(TaskStatus.SCHEDULED)
                .build();

        Task task2 = Task.builder()
                .userId(testUser.getUserId())
                .title("Task 2")
                .description("Description 2")
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(2))
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
                .userId(testUser.getUserId())
                .title("High Priority Task")
                .priority("HIGH")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .status(TaskStatus.SCHEDULED)
                .build();

        Task lowPriorityTask = Task.builder()
                .userId(testUser.getUserId())
                .title("Low Priority Task")
                .priority("LOW")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .status(TaskStatus.SCHEDULED)
                .build();

        taskRepository.save(highPriorityTask);
        taskRepository.save(lowPriorityTask);
        entityManager.flush();

        // when
        List<Task> highPriorityTasks = taskRepository.findTaskWithPriority(testUser.getUserId(), "HIGH", TaskStatus.SCHEDULED);

        // then
        assertEquals(1, highPriorityTasks.size());
        assertEquals("HIGH", highPriorityTasks.get(0).getPriority());
    }

    @Test
    void testFindTaskByRange() {
        // given
        LocalDate today = LocalDate.now();
        
        Task todayTask = Task.builder()
                .userId(testUser.getUserId())
                .title("Today Task")
                .startDate(today)
                .endDate(today.plusDays(1))
                .status(TaskStatus.SCHEDULED)
                .build();

        Task todayTask2 = Task.builder()
                .userId(testUser.getUserId())
                .title("Today Task 2")
                .startDate(today)
                .endDate(today)
                .status(TaskStatus.SCHEDULED)
                .build();

        Task futureTask = Task.builder()
                .userId(testUser.getUserId())
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
        List<Task> tasksRange7Days = taskRepository.findTask(testUser.getUserId(), today, today, TaskStatus.SCHEDULED);

        // then
        assertEquals(2, tasksRange7Days.size());
    } 

    @Test
    void testFindFutureTask(){
        //given
        String userId = testUser.getUserId();

        //past task
        Task task1 = Task.builder()
                .userId(testUser.getUserId())
                .title("Task 1")
                .description("Description 1")
                .startDate(LocalDate.now().minusDays(1))
                .endDate(LocalDate.now().plusDays(1))
                .status(TaskStatus.SCHEDULED)
                .build();

        //future(now) task
        Task task2 = Task.builder()
                .userId(testUser.getUserId())
                .title("Task 2")
                .description("Description 2")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .status(TaskStatus.SCHEDULED)
                .build();

        taskRepository.save(task1);
        taskRepository.save(task2);

        //when
        List<Task> tasks = taskRepository.findUpcomingTasks(userId);

        //then
        assertEquals(1, tasks.size());
    }
}
