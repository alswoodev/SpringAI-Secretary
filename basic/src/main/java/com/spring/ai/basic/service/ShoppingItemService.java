package com.spring.ai.basic.service;

import org.springframework.stereotype.Service;
import com.spring.ai.basic.entity.ShoppingItem;
import com.spring.ai.basic.entity.enums.ShoppingItem.ShoppingItemStatus;
import com.spring.ai.basic.repository.ShoppingItemRepository;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShoppingItemService {
    private final ShoppingItemRepository shoppingItemRepository;

    // Add one shopping item
    public ShoppingItem addShoppingItem(String userId, String itemName, int quantity, String notes){
        nullValidate(userId);
        if(quantity == 0){
            quantity++;
        }
        return shoppingItemRepository.save(ShoppingItem.builder()
                .userId(userId)
                .itemName(itemName)
                .quantity(quantity)
                .notes(notes)
                .build());
    }

    // Retreive all shopping items
    public List<ShoppingItem> getShoppingItems(String userId){
        nullValidate(userId);
        return shoppingItemRepository.findByUserUserIdAndStatus(userId, ShoppingItemStatus.PENDING);
    }

    // Delete one shopping item
    public ShoppingItem deleteShoppingItem(String userId, Long itemId){
        nullValidate(userId);
        ShoppingItem item = shoppingItemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("ShoppingItem not found"));
        item.setStatus(ShoppingItemStatus.CANCELLED);
        return shoppingItemRepository.save(item);
    }

    // Set as purchased all pending items
    public List<ShoppingItem> setPurchasedAll(String userId, LocalDate purchasedAt){
        nullValidate(userId);
        List<ShoppingItem> items = shoppingItemRepository.findByUserUserIdAndStatus(userId, ShoppingItemStatus.PENDING);
        items.forEach(item -> {
            item.setStatus(ShoppingItemStatus.PURCHASED);
            item.setPurchasedAt(purchasedAt);
        });
        return shoppingItemRepository.saveAll(items);
    }

    // Set as purchased some pending items
    public List<ShoppingItem> setPurchasedSome(String userId, List<Long> itemIds, LocalDate purchasedAt){
        nullValidate(userId);
        List<ShoppingItem> updatedItems = itemIds.stream()
                                                .map(itemId -> shoppingItemRepository.findById(itemId)
                                                                .orElseThrow(() -> new IllegalArgumentException("ShoppingItem not found")))
                                                .peek(item -> {
                                                    item.setStatus(ShoppingItemStatus.PURCHASED);
                                                    item.setPurchasedAt(purchasedAt);
                                                })  // 상태 변경
                                                .toList();
        return shoppingItemRepository.saveAll(updatedItems);
    }

    // Set as purchased excluding some pending items
    public List<ShoppingItem> setPurchasedExcept(String userId, List<Long> excludeItemIds, LocalDate purchasedAt){
        nullValidate(userId);
        List<ShoppingItem> items = shoppingItemRepository.findByUserUserIdAndStatus(userId, ShoppingItemStatus.PENDING);
        List<ShoppingItem> updatedItems = items.stream()
                                            .filter(item -> !excludeItemIds.contains(item.getItemId()))
                                            .peek(item->{
                                                item.setStatus(ShoppingItemStatus.PURCHASED);
                                                item.setPurchasedAt(purchasedAt);
                                            })
                                            .toList();
        return shoppingItemRepository.saveAll(updatedItems);
    }


    // Dummy implementation for user validation
    private void nullValidate(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
    }
}
