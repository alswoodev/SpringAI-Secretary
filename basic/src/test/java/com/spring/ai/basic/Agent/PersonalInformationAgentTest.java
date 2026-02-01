package com.spring.ai.basic.Agent;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.ai.basic.agent.PersonalInformationAgent;
import com.spring.ai.basic.entity.User;
import com.spring.ai.basic.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/*
 * WARNING:
 * This test class triggers actual AI responses and is for verifying model behavior only.
 * Do NOT use it for real API calls to avoid consuming your API key or incurring charges.
 */
/*@SpringBootTest
public class PersonalInformationAgentTest {
    @Autowired
    private PersonalInformationAgent personalInformationAgent;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VectorStore vectorStore;

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
        String userMessage = "나는 매일 김밥을 먹는다";
        String userId = testUser.getUserId().toString();

        String response = personalInformationAgent.process(userMessage, userId);
        System.out.println("Agent Response: " + response);

        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("내가 매일 먹는 음식")
                        .topK(1)
                        .filterExpression("user_id == '" + userId + "'")
                        .build()
        );

        assertFalse(results.isEmpty());
        assertTrue(results.get(0).getText().contains("김밥"));
    }
}*/