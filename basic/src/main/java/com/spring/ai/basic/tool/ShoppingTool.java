package com.spring.ai.basic.tool;

import com.spring.ai.basic.dto.SecretaryDTOs;
import com.spring.ai.basic.entity.ShoppingItem;
import com.spring.ai.basic.service.ShoppingItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class ShoppingTool {

    private final ShoppingItemService shoppingItemService;

    public ShoppingTool(@Autowired ShoppingItemService shoppingItemService) {
        this.shoppingItemService = shoppingItemService;
    }

    @Tool(description = "고객의 id를 바탕으로 장보기 물품을 추가합니다")
    public SecretaryDTOs.ShoppingItemResponse addShoppingItem(SecretaryDTOs.ShoppingItemAddRequest request){
        ShoppingItem item = shoppingItemService.addShoppingItem(request.userId(), request.itemName(), parseInt(request.quantity()), request.notes());
        return new SecretaryDTOs.ShoppingItemResponse(item.getItemId().toString(), item.getItemName(), item.getQuantity().toString());
    }

    @Tool(description = "고객의 id를 바탕으로 장보기 물품을 조회합니다")
    public List<SecretaryDTOs.ShoppingItemResponse> getShoppingItems(SecretaryDTOs.UserId request){
        return mapToTaskResponses(shoppingItemService.getShoppingItems(request.userId()));
    }

    @Tool(description = "고객의 id와, 장보기 물품의 id를 바탕으로 장보기 물품을 취소합니다")
    public SecretaryDTOs.ShoppingItemResponse deleteShoppingItem(SecretaryDTOs.ShoppingItemCancleRequest request){
        ShoppingItem item = shoppingItemService.deleteShoppingItem(request.userId(), parseLong(request.itemId()));
        return new SecretaryDTOs.ShoppingItemResponse(item.getItemId().toString(), item.getItemName(), item.getQuantity().toString());
    }
   
    @Tool(description = "모든 장보기 물품을 구매처리합니다.")
    public List<SecretaryDTOs.ShoppingItemResponse> setPurchasedAll(SecretaryDTOs.ShoppingItemPurchaseRequest request){
        LocalDate purchasedAt = parseDate(request.purchasedAt());
        return mapToTaskResponses(shoppingItemService.setPurchasedAll(request.userId(), purchasedAt));
    }

    @Tool(description = "고객 id와 장보기 물품 id들을 바탕으로 장보기 물품을 구매처리합니다")
    public List<SecretaryDTOs.ShoppingItemResponse> setPurchasedSome(SecretaryDTOs.ShpopingItemSome request){
        LocalDate purchasedAt = parseDate(request.purchasedAt());
        List<Long> itemIds = request.itemIds().stream().map(id -> parseLong(id)).toList();
        List<ShoppingItem> items = shoppingItemService.setPurchasedSome(request.userId(), itemIds, purchasedAt);
        return mapToTaskResponses(items);
    }

    @Tool(description = "고객 id를 바탕으로 장보기 물품을 모두 조회한 후에 특정 장보기 물품 id들을 제외하고 모두 구매처리 합니다")
    public List<SecretaryDTOs.ShoppingItemResponse> setPurchasedExcept(SecretaryDTOs.ShpopingItemExcept request){
        LocalDate purchasedAt = parseDate(request.purchasedAt());
        List<Long> itemIds = request.itemIds().stream().map(id -> parseLong(id)).toList();
        List<ShoppingItem> items = shoppingItemService.setPurchasedExcept(request.userId(), itemIds, purchasedAt);
        return mapToTaskResponses(items);
    }

    public LocalDate parseDate(String str){
        LocalDate purchasedAt;

        try{
            purchasedAt = LocalDate.parse(str);
        }
        catch(DateTimeParseException e){
            throw new IllegalArgumentException("str must be in 'yyyy-MM-dd' format (e.g., 2024-01-31).");
        }

        return purchasedAt;
    }

    public int parseInt(String str) {
        try {
            return Integer.parseInt(str);  // 문자열을 int로 변환
        } catch (NumberFormatException e) {
            // 변환 실패 시 기본값 반환
            System.out.println(str + "' is not a valid integer.");
            return 0;
        }
    }

    public Long parseLong(String str){
        try {
            return Long.parseLong(str);  // 문자열을 int로 변환
        } catch (NumberFormatException e) {
            // 변환 실패 시 기본값 반환
            System.out.println(str + "' is not a valid Long.");
            return 0L;
        }
    }

    // Helper method to map Task entities to TaskResponse DTOs
    private List<SecretaryDTOs.ShoppingItemResponse> mapToTaskResponses(List<ShoppingItem> items) {
        return items.stream()
                .map(item -> new SecretaryDTOs.ShoppingItemResponse(
                        item.getItemId().toString(),
                        item.getItemName(),
                        item.getQuantity().toString()
                ))
                .toList();
    }
}