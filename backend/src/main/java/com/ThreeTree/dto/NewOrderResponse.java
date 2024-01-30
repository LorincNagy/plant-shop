package com.ThreeTree.dto;


import com.ThreeTree.model.Order;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class NewOrderResponse {

    private Long id;
    private LocalDateTime orderDate;
    private BigDecimal orderTotal;
    private Long personId;
    private List<NewOrderItemResponse> orderItemResponses;

    public NewOrderResponse(Order order) {
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.orderTotal = order.getOrderTotal();
        this.personId = order.getPerson() != null ? order.getPerson().getId() : null;
        this.orderItemResponses = order.getOrderItems().stream()
                .map(NewOrderItemResponse::new)
                .collect(Collectors.toList());
    }

}

