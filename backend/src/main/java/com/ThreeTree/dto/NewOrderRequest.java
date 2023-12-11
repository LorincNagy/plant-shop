package com.ThreeTree.dto;

import java.util.Map;

public record NewOrderRequest(Long customerId,
                              Map<Long, Integer> productsQuantities) {
}
