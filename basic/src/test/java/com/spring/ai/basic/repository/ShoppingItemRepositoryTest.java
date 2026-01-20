package com.spring.ai.basic.repository;

import com.spring.ai.basic.entity.ShoppingItem;
import com.spring.ai.basic.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import com.spring.ai.basic.entity.enums.ShoppingItem.*;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingItemRepositoryTest {

    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Generate Test User
        testUser = User.builder()
                .email("shopper@example.com")
                .name("Test Shopper")
                .build();
        testUser = userRepository.save(testUser);
    }

    @Test
    void testFindByUserId() {
        // given
        ShoppingItem item1 = ShoppingItem.builder()
                .user(testUser)
                .itemName("Milk")
                .quantity(2)
                .build();

        ShoppingItem item2 = ShoppingItem.builder()
                .user(testUser)
                .itemName("Bread")
                .quantity(1)
                .status(ShoppingItemStatus.PURCHASED)
                .build();

        shoppingItemRepository.save(item1);
        shoppingItemRepository.save(item2);
        entityManager.flush();
        entityManager.clear();

        // when
        List<ShoppingItem> items = shoppingItemRepository.findByUserUserIdAndStatus(testUser.getUserId(), ShoppingItemStatus.PENDING);

        System.out.println("Retrieved Items: " + items);
        // then
        assertEquals(1, items.size());
    }

    @Test
    void testFindByUserIdAndStatus() {
        // given
        ShoppingItem pendingItem = ShoppingItem.builder()
                .user(testUser)
                .itemName("Eggs")
                .quantity(1)
                .build();

        ShoppingItem purchasedItem = ShoppingItem.builder()
                .user(testUser)
                .itemName("Cheese")
                .quantity(2)
                .status(ShoppingItemStatus.PURCHASED)
                .build();

        shoppingItemRepository.save(pendingItem);
        shoppingItemRepository.save(purchasedItem);
        entityManager.flush();
        entityManager.clear();

        // when
        List<ShoppingItem> pendingItems = shoppingItemRepository.findByUserUserIdAndStatus(testUser.getUserId(), ShoppingItemStatus.PENDING);

        // then
        assertEquals(1, pendingItems.size());
        assertEquals("Eggs", pendingItems.get(0).getItemName());
        assertEquals(ShoppingItemStatus.PENDING, pendingItems.get(0).getStatus());
    }

    @Test
    void testSaveShoppingItem() {
        // given
        ShoppingItem item = ShoppingItem.builder()
                .user(testUser)
                .itemName("Apple")
                .quantity(5)
                .notes("Fresh apples")
                .build();

        // when
        ShoppingItem savedItem = shoppingItemRepository.save(item);
        entityManager.flush();
        entityManager.clear();

        // then
        assertNotNull(savedItem.getItemId());
        assertEquals("Apple", savedItem.getItemName());
        assertEquals(ShoppingItemStatus.PENDING , savedItem.getStatus());
    }

    @Test
    void testFindByUserUserIdAndStatusMultiple() {
        // given
        for (int i = 0; i < 3; i++) {
            ShoppingItem item = ShoppingItem.builder()
                    .user(testUser)
                    .itemName("Item " + i)
                    .quantity(1)
                    .build();
            shoppingItemRepository.save(item);
        }
        entityManager.flush();
        entityManager.clear();

        // when
        List<ShoppingItem> pendingItems = shoppingItemRepository.findByUserUserIdAndStatus(testUser.getUserId(), ShoppingItemStatus.PENDING);

        // then
        assertEquals(3, pendingItems.size());
    }
}
