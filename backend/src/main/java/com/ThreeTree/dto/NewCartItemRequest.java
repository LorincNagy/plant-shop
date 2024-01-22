package com.ThreeTree.dto;


public record NewCartItemRequest(
        Long productId,
        Integer quantity

) {
}
