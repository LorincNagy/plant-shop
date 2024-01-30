package com.ThreeTree.dto;


import com.ThreeTree.model.Order;
import com.ThreeTree.model.Person;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class NewOrderResponse {

    private Long id;
    private LocalDateTime orderDate;
    private BigDecimal orderTotal;
    private NewPersonResponse person;
    private List<NewOrderItemResponse> orderItemResponses;

    public NewOrderResponse(Order order) {
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.orderTotal = order.getOrderTotal();
        this.person = order.getPerson() != null ? new NewPersonResponse(order.getPerson()) : null;
        this.orderItemResponses = order.getOrderItems().stream()
                .map(NewOrderItemResponse::new)
                .collect(Collectors.toList());
    }

}

