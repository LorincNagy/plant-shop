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




    //A CascadeType.ALL azt jelenti, hogy minden JPA cascade művelet (mint például mentés, frissítés, törlés) a "szülő" entitáson (ebben az esetben a Cart entitáson) automatikusan alkalmazva lesz a "gyermek" entitásokra (itt a CartItem és Order entitásokra) is.
    //A @OneToOne(mappedBy = "cart") annotáció azt jelenti, hogy a "cart" nevű mező az "Order" entitásban az, ami az adatbázisban fogja reprezentálni a kapcsolatot. Tehát a Cart entitásban nem fog létrejönni egy "order_id" mező, hanem az "Order" entitásban lesz egy "cart_id" mező, amely a kapcsolatot fogja reprezentálni.
    //

    //A CascadeType.ALL azt jelenti, hogy minden JPA cascade művelet (mint például mentés, frissítés, törlés) a "szülő" entitáson (ebben az esetben a Cart entitáson) automatikusan alkalmazva lesz a "gyermek" entitásokra (itt a CartItem és Order entitásokra) is.
    //
    //Ez pontosan a következőket jelenti:
    //
    //PERSIST: Ha a Cart entitást mented, akkor az összes hozzá tartozó CartItem és Order entitás is mentésre kerül.
    //REMOVE: Ha a Cart entitást törölni szeretnéd, akkor az összes hozzá tartozó CartItem és Order entitás is törlésre kerül.
    //REFRESH: Ha a Cart entitás frissítése történik, akkor az összes hozzá tartozó CartItem és Order entitás is frissítésre kerül.
    //MERGE: Ha a Cart entitást egy másik entitással egyesíted, akkor az összes hozzá tartozó CartItem és Order entitás is egyesítésre kerül.
    //DETACH: Ha a Cart entitást elválasztod a perzisztencia kontextustól, akkor az összes hozzá tartozó CartItem és Order entitás is elválasztásra kerül.
    //Az "Order" entitásban található "cart" nevű mező az, amely kapcsolódik a Cart entitáshoz, és az adatbázisban ez a mező fogja tartalmazni a kapcsolatot a "cart_id" segítségével. Ez a módszer segít a kapcsolat egyértelmű azonosításában és a táblák közötti kapcsolat kialakításában.
//    public void addCartItem(CartItem cartItem) {
//        cartItems.add(cartItem);
//    }

    //A kaskád műveletek csak azokat a műveleteket automatizálják, amelyek az adott entitásra vonatkoznak (például a kosár törlésekor a hozzárendelt rendelések törlődnek). Az új rendelések létrehozása és hozzárendelése a kosárhoz külön műveletet igényel.

    //Az fetch attribútum a JPA (Java Persistence API) annotáció része, és azt határozza meg, hogy az egyoldalú (one-to-many vagy many-to-one) kapcsolatban lévő entitásokat hogyan töltse be az ORM (Object-Relational Mapping) keretrendszer.
    //
    //FetchType.LAZY: Ez a beállítás azt jelenti, hogy az entitásokat csak akkor tölti be, amikor rájuk való hivatkozás történik. Tehát például, ha egy Cart entitás rendelkezik egy orders listával, és a fetch típusa LAZY, akkor az orders lista elemei csak akkor kerülnek betöltésre az adatbázisból, ha valaki hozzáfér hozzájuk. Ezáltal csökkenti a felesleges adatbázis-lekérdezéseket.
    //
    //FetchType.EAGER: Ez a beállítás azt jelenti, hogy az entitásokat azonnal betölti, amikor az őket tartalmazó entitás (a tulajdonos entitás) betöltődik. Tehát ha a Cart entitás orders listájának fetch típusa EAGER, akkor minden alkalommal, amikor egy Cart entitást lekérdeznek az adatbázisból, az összes hozzárendelt rendelés (Order) entitás is betöltődik.
    //
    //Az EAGER típus általában akkor hasznos, ha biztosítani szeretnénk, hogy minden kapcsolt entitás betöltődjön azonnal, és ne kelljen további lekérdezéseket végrehajtani az adatbázisban azok lekérdezéséhez. Azonban fontos megjegyezni, hogy ez könnyen túlzott adatbetöltéshez vezethet, különösen nagy és összetett adatmodell esetén.
    //
    //Az LAZY típus a megszokott beállítás, mivel csökkenti az adatbázis lekérdezések számát, és lehetővé teszi a hatékonyabb adatlekérdezést és teljesítményt, kivéve, ha kifejezetten szükség van az összes kapcsolt entitás azonnali betöltésére.
}

