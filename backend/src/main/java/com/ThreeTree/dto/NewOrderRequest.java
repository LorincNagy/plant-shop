package com.ThreeTree.dto;

import com.ThreeTree.model.CartItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record NewOrderRequest(
                              String address,
                              String email,
                              String phone,
                              BigDecimal orderTotal
) {
}
