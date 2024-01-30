package com.ThreeTree.dto;

import com.ThreeTree.model.OrderItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewOrderItemResponse {

    private Long id;
    private Integer quantity;
    private NewProductResponse productResponse;

    public NewOrderItemResponse(OrderItem orderItem) {

        this.id = orderItem.getId();
        this.quantity = orderItem.getQuantity();
        if (orderItem.getProduct() != null) {
            this.productResponse = new NewProductResponse(
                    orderItem.getProduct().getName(),
                    orderItem.getProduct().getSku(),
                    orderItem.getProduct().getDescription(),
                    orderItem.getProduct().getPrice(),
                    orderItem.getProduct().getStock(),
                    orderItem.getProduct().getImage(),
                    orderItem.getProduct().getProductId()
            );
        } else {
            this.productResponse = null;
        }
    }
}

