package com.spring.ai.basic.service;

import com.spring.ai.basic.entity.Task;
import com.spring.ai.basic.entity.enums.task.TaskStatus;
import com.spring.ai.basic.repository.TaskRepository;
import com.spring.ai.basic.repository.UserRepository;
import com.spring.ai.basic.entity.User;

import java.time.LocalDate;
import java.util.List;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskServiceTest {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    private String userId;
    private Task task;

    @BeforeEach
    void setUp() {
        // Generate Test User
        User user = User.builder()
                .email("taskService@example.com")
                .name("Test User")
                .build();
        userRepository.save(user);

        userId = userRepository.findAll().get(0).getUserId();

        // Generate Test Task
        task = Task.builder()
                .userId(userId)
                .title("Test Task")
                .description("Test Description")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .type("EVENT")
                .priority("MEDIUM")
                .status(TaskStatus.SCHEDULED)
                .build();
        taskRepository.save(task);
    }

    @Test
    void testGetTaskByUserId() {
        //given
        //userId

        //when
        List<Task> tasks = taskService.getTaskByUserId(userId);

        //then
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
    }

    @Test
    void testGetFutureTaskByUserId() {
        //given
        //userId
        Task task2 = Task.builder()
                        .userId(userId)
                        .title("Test Task2")
                        .description("Test Description2")
                        .startDate(LocalDate.now().plusDays(3))
                        .endDate(LocalDate.now().plusDays(7))
                        .build();
        taskRepository.save(task2);

        //when
        List<Task> tasks = taskService.getFutureTaskByUserId(userId);

        //then
        assertEquals(2, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        assertEquals("Test Task2", tasks.get(1).getTitle());
    }

    @Test
    void testGetTaskByDateRange() {
        //given
        //userId
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(1);

        Task task2 = Task.builder()
                        .userId(userId)
                        .title("Test Task2")
                        .description("Test Description2")
                        .startDate(LocalDate.now().plusDays(1))
                        .endDate(LocalDate.now().plusDays(3))
                        .build();
        taskRepository.save(task2);

        //when
        List<Task> tasks = taskService.getTask(userId, startDate, endDate);

        //then
        assertEquals(2, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        assertEquals("Test Task2", tasks.get(1).getTitle());
    }

    @Test
    void testAddTask() {
        //given
        //userId
        String title = "New Task";
        String description = "New description";
        LocalDate startDate =LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        String type = "MEETING";
        String priority = "HIGH";

        //when
        Task task = taskService.addTask(userId, title, description, startDate, endDate, type, priority); //Vertify Return Value
        List<Task> savedTasks = taskRepository.findByUserUserId(userId); //Vertify Task is really in DB

        //then
        assertEquals("New Task", task.getTitle());
        
        assertEquals(2, savedTasks.size()); 
    }

    @Test
    void testCancelTask() {
        //given
        //userId
        Long taskId = task.getTaskId();

        //when
        Task task = taskService.cancelTask(userId, taskId); //Vertify Return Value
        Task cancelledTask = taskRepository.findById(task.getTaskId()).orElseThrow(); //Vertify Task is really in DB

        //then
        assertEquals(TaskStatus.CANCELLED, task.getStatus());

        assertEquals(TaskStatus.CANCELLED, cancelledTask.getStatus());
    }

    @Test
    void testGetTaskWithPriority() {
        //given
        //userId
        String priority = "HIGH";

        Task task2 = Task.builder()
                        .userId(userId)
                        .title("Test Task2")
                        .description("Test Description2")
                        .startDate(LocalDate.now().plusDays(3))
                        .endDate(LocalDate.now().plusDays(7))
                        .priority("HIGH")
                        .build();
        taskRepository.save(task2);

        //when
        List<Task> tasks = taskService.getTaskWithPriority(userId, priority);

        //then
        assertEquals(1, tasks.size());
        assertEquals("Test Task2", tasks.get(0).getTitle());
    }
}
