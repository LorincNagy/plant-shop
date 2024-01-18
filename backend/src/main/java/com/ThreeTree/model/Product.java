package com.ThreeTree.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @SequenceGenerator(
            name = "product_id_sequence",
            sequenceName = "product_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_id_sequence"
    )
    private Long productId;
    private String name;
    private String sku;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String image;

    @JsonIgnore
    @ManyToMany
    private Set<Order> orders;

    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    private Set<Cart> carts;

}


//Person és Cart között van egy egyszerű egy-egy kapcsolat (@OneToOne). Ez azt jelenti, hogy egy felhasználóhoz (Person) egy kosár (Cart) tartozik.
//
//Cart és Product között van egy egy-minden kapcsolat (@OneToMany). Ez azt jelenti, hogy egy kosárhoz több termék (Product) is tartozhat. Ez jó megközelítés lehet, ha egy felhasználó több terméket is hozzáadhat a kosarához.
//
//Product és Order között van egy sok-minden kapcsolat (@ManyToMany). Ez azt jelenti, hogy egy termék több rendeléshez (Order) is tartozhat, és egy rendelés több terméket is tartalmazhat.
