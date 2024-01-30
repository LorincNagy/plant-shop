package com.ThreeTree.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class NewProductResponse {
    String name;
    String sku;
    String description;
    BigDecimal price;
    Integer stock;
    String image;
    Long productId;

    public NewProductResponse(String name, String sku, String description, BigDecimal price, Integer stock, String image, Long productId) {
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.productId = productId;
    }


}

