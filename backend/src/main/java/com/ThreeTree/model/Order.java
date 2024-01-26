package com.ThreeTree.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Entity(name = "Orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime orderDate;
    private BigDecimal orderTotal;

    @ManyToOne
    private Person person;

    @ManyToOne
    private Cart cart;
}

//A mappedBy attribútum használata kétirányú kapcsolatot jelent a JPA (Java Persistence API) kontextusában. Amikor egy kapcsolatot kétirányúvá teszel, az azt jelenti, hogy mindkét entitás tud a másikról, és kölcsönösen hivatkoznak egymásra.
//
//A mappedBy attribútumot azon az oldalon használod, amelyik a kapcsolat "nem tulajdonos" oldala. Ez az oldal nem felelős a kapcsolat adatbázisban való tárolásáért, hanem csak a kapcsolat logikai reprezentációjáért. A "tulajdonos" oldal, amelyik nem használja a mappedBy attribútumot, tartalmazza a kapcsolatot reprezentáló külső kulcsot (foreign key).
//A mappedBy attribútumot azon az oldalon kell használni, amelyik nem tartalmazza a kapcsolatot reprezentáló külső kulcsot (foreign key). Ez a kapcsolat "inverz" vagy "nem tulajdonos" oldala.
//
//Tehát a példában, ahol a Cart és az Order entitások között van egy OneToOne kapcsolat:
//
//Ha az Order entitásban van egy Cart referenciád, és itt szeretnéd tárolni a külső kulcsot (foreign key), akkor az Order entitás a kapcsolat "tulajdonos" oldala lesz. Itt nem kell mappedBy attribútumot használni.
//

//@Entity
//public class Order {
//    @OneToOne
//    private Cart cart;
//    // ... többi attribútum és metódus
//}
//A Cart entitásban, ahol a kapcsolatot az Order-rel definiálod, a mappedBy attribútumot kell használni, mivel ez az oldal nem tartalmazza a külső kulcsot. A mappedBy értéke a "tulajdonos" oldalon lévő mező nevére utal, ami ebben az esetben a cart.
//

//@Entity
//public class Cart {
//    @OneToOne(mappedBy = "cart")
//    private Order order;
//    // ... többi attribútum és metódus
//}
//A mappedBy értékének ("cart") azon a mező nevének kell lennie, amely az ellenkező oldalon (tulajdonos oldalon) a kapcsolatot reprezentálja. Ez a megközelítés biztosítja a kapcsolat helyes irányítását és az adatbázisban való megfelelő reprezentációját.
