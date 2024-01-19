package com.ThreeTree.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "person_id") // Megadható a kapcsoló kulcs neve
    private Person person;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems;

    @OneToOne(mappedBy = "cart")
    private Order order;


//    public void addProducts(Product cartItem) {
//        products.add(cartItem);
//    }

}

