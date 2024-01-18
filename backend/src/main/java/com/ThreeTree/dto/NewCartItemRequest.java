package com.ThreeTree.dto;

import java.math.BigDecimal;

public record NewCartItemRequest(
        String name,
        String sku,
        String description,
        BigDecimal price,
        Integer stock,
        String image
) {}
