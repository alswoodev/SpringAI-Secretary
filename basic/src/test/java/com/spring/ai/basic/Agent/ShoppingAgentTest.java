package com.spring.ai.basic.Agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.spring.ai.basic.agent.ShoppingAgent;
import com.spring.ai.basic.entity.User;
import com.spring.ai.basic.entity.enums.ShoppingItem.ShoppingItemStatus;
import com.spring.ai.basic.repository.ShoppingItemRepository;
import com.spring.ai.basic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.spring.ai.basic.entity.ShoppingItem;
import java.util.List;

/*
 * WARNING:
 * This test class triggers actual AI responses and is for verifying model behavior only.
 * Do NOT use it for real API calls to avoid consuming your API key or incurring charges.
 */
/*@SpringBootTest
public class ShoppingAgentTest {
    @Autowired
    private ShoppingAgent shoppingAgent;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

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
        String userMessage = "장보기 물품에 우유, 소고기 추가해줘";
        String userMessage2 = "장보러 갈려고 하는데 장보기 리스트 알려줘";
        String userId = testUser.getUserId().toString();


        String response = shoppingAgent.process(userMessage, userId);
        System.out.println("Agent Response: " + response);

        List<ShoppingItem> items = shoppingItemRepository.findByUserUserIdAndStatus(testUser.getUserId(), ShoppingItemStatus.PENDING);
        items.forEach(task -> System.out.println("Item in DB: " + task.getItemName() + ", " + task.getQuantity() ));
        
        String response2 = shoppingAgent.process(userMessage2, userId);
        System.out.println("Agent Response: " + response2);
    }
}*/