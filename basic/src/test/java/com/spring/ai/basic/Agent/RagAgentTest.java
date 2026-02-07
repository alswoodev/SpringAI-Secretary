package com.spring.ai.basic.Agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.spring.ai.basic.entity.User;
import com.spring.ai.basic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.spring.ai.basic.agent.RagAgent;

/*
 * WARNING:
 * This test class triggers actual AI responses and is for verifying model behavior only.
 * Do NOT use it for real API calls to avoid consuming your API key or incurring charges.
 */
/*@SpringBootTest
public class RagAgentTest {
    @Autowired
    private RagAgent ragAgent;

    @Autowired
    private UserRepository userRepository;

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
        String userMessage = "한국의 물가와 소비자물가지수(CPI) 변화에 대해서 설명해줘";
        String userId = testUser.getUserId().toString();

        String response = ragAgent.process(userMessage, userId);
        System.out.println("Agent Response: " + response);
    }
}*/