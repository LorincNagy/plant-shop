package com.ThreeTree.dto;

import com.ThreeTree.model.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewOrderItemResponse {

    private Long id;
    private Integer quantity;
    private Long orderId;



    public NewOrderItemResponse(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.quantity = orderItem.getQuantity();
        this.orderId = orderItem.getOrder() != null ? orderItem.getOrder().getId() : null;
    }

}
