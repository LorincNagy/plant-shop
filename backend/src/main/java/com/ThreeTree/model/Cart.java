package com.ThreeTree.model;

import com.ThreeTree.service.CartItemService;
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

    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToOne(mappedBy = "cart")
    private Order order;


    public CartItem findCartItemByProduct(Product product) {
        return this.cartItems.stream()
                .filter(cartItem -> cartItem.hasProduct(product.getProductId()))
                .findFirst()
                .orElse(null);
    }


    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
    }


    public void removeCartItem(CartItem cartItem) {
        cartItems.removeIf(item -> item.getId().equals(cartItem.getId()));
    }


    public void deleteCartItems() {

        this.cartItems.clear();
    }

    //A @OneToOne(mappedBy = "cart") annotáció azt jelenti, hogy a "cart" nevű mező az "Order" entitásban az, ami az adatbázisban fogja reprezentálni a kapcsolatot. Tehát a Cart entitásban nem fog létrejönni egy "order_id" mező, hanem az "Order" entitásban lesz egy "cart_id" mező, amely a kapcsolatot fogja reprezentálni.
    //
    //Az "Order" entitásban található "cart" nevű mező az, amely kapcsolódik a Cart entitáshoz, és az adatbázisban ez a mező fogja tartalmazni a kapcsolatot a "cart_id" segítségével. Ez a módszer segít a kapcsolat egyértelmű azonosításában és a táblák közötti kapcsolat kialakításában.
//    public void addCartItem(CartItem cartItem) {
//        cartItems.add(cartItem);
//    }


}

