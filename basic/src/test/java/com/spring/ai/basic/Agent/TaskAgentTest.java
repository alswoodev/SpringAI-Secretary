package com.spring.ai.basic.Agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.spring.ai.basic.agent.TaskAgent;
import com.spring.ai.basic.entity.User;
import com.spring.ai.basic.repository.TaskRepository;
import com.spring.ai.basic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SpringBootTest
public class TaskAgentTest {
    @Autowired
    private TaskAgent taskAgent;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
       testUser = User.builder()
                .email("asdf@gmail.com")
                .name("Test User")
                .build();
        userRepository.save(testUser);
    }

    @Test
    public void processTest() {
        String userMessage = "내일 김철수님이랑 종로 스타벅스에서 미팅 일정이 생겼어";
        String userId = testUser.getUserId().toString();


        String response = taskAgent.process(userMessage, userId);
        System.out.println("Agent Response: " + response);

        System.out.println("Tasks for user: " + taskRepository.findByUserUserId(testUser.getUserId()));
    }
}
