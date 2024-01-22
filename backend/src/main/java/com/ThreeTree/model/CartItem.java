package com.ThreeTree.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private Integer quantity;


    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

//    public void addProduct(Product existingProduct) {
//        products.add(existingProduct);
//    }
}
//
//    A @ManyToOne kapcsolat a CartItem osztályban, amely a Product-hoz kapcsolódik, azt jelenti, hogy több különböző CartItem (tehát potenciálisan több különböző felhasználó kosarában) tartalmazhatja ugyanazt a Product-ot, azaz ugyanazt a terméket. Mindegyik CartItem külön-külön hivatkozik a Product-ra, de több ilyen CartItem is lehet, amelyek ugyanarra a Product-ra mutatnak.
//
//        Ez a kapcsolat típus nagyon gyakori az e-kereskedelmi és bevásárlókosár alkalmazásokban, ahol több felhasználó is ugyanazt a terméket veheti fel a kosarába. Mindegyik CartItem külön entitás a rendszerben, amely saját mennyiséggel (quantity) rendelkezik, de ugyanarra a konkrét termékre (Product) hivatkozik.
//
//        Például:
//
//        Felhasználó A hozzáad egy CartItem-et a kosarához, amely egy bizonyos Product-ot (mondjuk egy könyvet) tartalmaz.
//        Ugyanakkor Felhasználó B is hozzáadhat egy CartItem-et a saját kosarához, ami ugyanazt a Product-ot tartalmazza.
//        Mindkét CartItem-ben a product mező ugyanarra a Product entitásra (ugyanarra a könyvre) mutat.
