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


    @OneToOne(mappedBy = "cart")
    private Person person;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cart_product",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    public void addProducts(Product cartItem) {
        products.add(cartItem);
    }

}

// Ha van egy Cart entitás, és ebben az entitásban van egy person nevű attribútum, amely egy Person entitásra mutat, akkor a @OneToOne(mappedBy = "cart") azt jelenti, hogy a Cart entitásban található person attribútum vezérli a kapcsolatot a Person entitással.
//Cart entitásban található person nevű mező felelős a kapcsolat irányításáért a Person entitással.
//
//Például, ha van egy Cart osztály, amely tartalmaz egy person nevű attribútumot, és a @OneToOne(mappedBy = "cart") annotációval ellátjuk ezt az attribútumot, akkor ez azt jelenti, hogy a person mező irányítja a kapcsolatot a Person entitással.
//
//Ez a jelölés azt mondja meg a JPA (Java Persistence API) számára, hogy hogyan kell kezelni ezt a kapcsolatot az adatbázisban. Azáltal, hogy a "cart" nevű mezőt állítjuk be a "mappedBy" attribútumként, az JPA tudni fogja, hogy a kapcsolat irányítása és a kulcsok kezelése a "cart" mezőn keresztül történik. Ez lehetővé teszi a kapcsolat két entitás közötti megfeleltetését az adatbázisban.
//
//Tehát, ha a Cart entitásban egy person nevű mező vezérli a kapcsolatot, akkor a JPA a "cart" mező alapján tudja majd, hogy melyik Person entitáshoz tartozik a Cart. Ezáltal könnyen lehet lekérdezéseket végezni és az adatbázisban megfelelő kapcsolatokat létrehozni az entitások között.
