package com.spring.ai.basic.tool;

import com.spring.ai.basic.dto.KnowledgeManageToolDTO;
import com.spring.ai.basic.entity.User;
import com.spring.ai.basic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * WARNING:
 * This test class triggers actual AI responses and is for verifying model behavior only.
 * Do NOT use it for real API calls to avoid consuming your API key or incurring charges.
 */
/*@SpringBootTest
class KnowledgeUpsertToolTest {

    @Autowired
    private KnowledgeManageTool knowledgeManageTool;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private UserRepository userRepository;

    String userId;

    @BeforeEach
    void setUp() {
        // Generate test user
        userRepository.deleteAll();
        User user = userRepository.save(User.builder()
                .email("test@example.com")
                .name("Test User")
                .build());
        userId = user.getUserId();
    }

    @Test
    void testUpsertAndSearch() {
        //given
        KnowledgeManageToolDTO.Request request = new KnowledgeManageToolDTO.Request(userId, List.of("오늘은 김밥을 먹었다"));

        //when
        knowledgeManageTool.upsert(request);
        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("오늘 김밥 먹음")
                        .topK(1)
                        .filterExpression("user_id == '" + userId + "'")
                        .build()
        );

        //then
        assertFalse(results.isEmpty());
        assertTrue(results.get(0).getText().contains("김밥"));
    }
}*/