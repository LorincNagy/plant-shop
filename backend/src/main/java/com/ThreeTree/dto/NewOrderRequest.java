package com.ThreeTree.dto;

import com.ThreeTree.model.CartItem;

import java.math.BigDecimal;


public record NewOrderRequest(
                              String address,
                              String email,
                              String phone,
                              BigDecimal orderTotal
) {
}
