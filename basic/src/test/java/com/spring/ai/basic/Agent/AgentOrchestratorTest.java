package com.spring.ai.basic.Agent;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.ai.basic.agent.AgentOrchestrator;
import com.spring.ai.basic.entity.User;
import com.spring.ai.basic.repository.UserRepository;

/*
 * WARNING:
 * This test class triggers actual AI responses and is for verifying model behavior only.
 * Do NOT use it for real API calls to avoid consuming your API key or incurring charges.
 */
/*@SpringBootTest
public class AgentOrchestratorTest {
    @Autowired
    private AgentOrchestrator agentOrchestrator;

    @Autowired 
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp(){
        testUser = User.builder()
                .email("asdf@gmail.com")
                .name("Test User")
                .build();
        userRepository.save(testUser);
    }

    @Test
    public void chatTest(){
        String userMessage = "한국의 물가와 소비자지수(CPI) 변화에 대해서 설명해줘";
        String userId = testUser.getUserId().toString();
        String conversationId = UUID.randomUUID().toString();

        String response = agentOrchestrator.chat(userMessage, userId, conversationId);
        System.out.println("Response: " + response);
    }
}*/
