package com.ThreeTree.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String name;
    private String sku;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String image;


//    @OneToMany(mappedBy = "product")
//    private List<OrderItem> orderItems;

//    @OneToMany(mappedBy = "product")
//    private List<CartItem> cartItemList;
}

//Hivatkozás a CartItem-ra a Product oldaláról hiányzik:
//Ebben az esetben a Product entitás nem rendelkezik közvetlen információval vagy kapcsolattal arról, hogy mely CartItem-ek tartalmazzák az adott terméket. A Product entitás önmagában nem képes nyomon követni, hogy milyen kosár tételekben szerepel.
//
//A CartItem továbbra is hivatkozik a Product-ra:
//Mivel a CartItem-ben @ManyToOne kapcsolat van a Product-tal, a CartItem tudja, hogy melyik termékre hivatkozik. Ezáltal lekérdezések során, amikor egy CartItem entitást kezelsz, látni fogod a hozzá kapcsolódó Product entitást vagy annak azonosítóját.
//
//Lekérdezések:
//Ha szeretnéd megtudni, hogy egy adott termék mely CartItem-ekben szerepel, akkor ezt a lekérdezést közvetlenül a CartItem entitásokon keresztül kell megtenned, például a Product ID alapján szűrve a CartItem-ek között.
//
//Összefoglalva, ha a Product entitásban nincs @OneToMany kapcsolat a CartItem-ek felé, akkor ez nem befolyásolja a CartItem képességét arra, hogy tudja, melyik Product-ra hivatkozik. Csak azt jelenti, hogy a Product oldaláról nem tudsz közvetlenül a hozzá kapcsolódó CartItem-ekre hivatkozni.
