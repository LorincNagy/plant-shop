package com.ThreeTree.service;

import com.ThreeTree.dao.CartRepository;
import com.ThreeTree.dao.OrderRepository;
import com.ThreeTree.dao.PersonRepository;
import com.ThreeTree.dto.NewOrderRequest;
import com.ThreeTree.model.Cart;
import com.ThreeTree.model.Order;
import com.ThreeTree.model.Person;
import com.ThreeTree.model.Product;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final PersonService personService;
    private final CartService cartService;


    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        orderRepository.delete(order);
    }


    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void saveOrder(NewOrderRequest request, Person person) {
        Cart cart = person.getCart();

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setOrderTotal(request.orderTotal());
        order.setPerson(person);
        order.setCart(cart);
        cart.addOrder(order);

        personService.updatePerson(order, person, request);
        orderRepository.save(order);
        cartService.save(cart);

    }
}


//Egy meglévő Cart objektumot (ami CartItem-eket tartalmaz) használsz fel egy új Order (rendelés) létrehozásához. Itt az Order entitás kap egy hivatkozást a Cart-ra, de fordítva nem történik hivatkozás beállítása. Ez a megközelítés teljesen érvényes egy adott forgatókönyvben, ahol a rendelés létrehozásakor nem szükséges, vagy nem kívánt, hogy a kosár is tudjon a létrejött rendelésről.
//
//Ebben a kódrészletben a következő történik:
//
//Rendelés Létrehozása: Egy új Order objektumot hozol létre, beállítva annak dátumát, összegét, a hozzárendelt vásárlót (Person), és a kosarat (Cart).
//
//Személyi Adatok Frissítése: A vásárló (Person) címét és telefonszámát frissíted a NewOrderRequest alapján.
//
//Rendelés Hozzáadása a Személyhez: Az új rendelést hozzáadod a vásárló rendeléseinek listájához.
//
//Mentés az Adatbázisban: Elmented a módosított Person objektumot és az új Order objektumot az adatbázisban.
//
//Az, hogy a Cart-ban nem állítasz be hivatkozást az Order-re, azt jelenti, hogy a kapcsolat ebben az esetben csak egyirányú. Ez lehet egy tudatos döntés, amelyet az üzleti logika vagy az alkalmazás működési követelményei indokolnak. Például, ha egy kosarat többször is fel lehet használni rendelésekhez, vagy ha egyszerűen nincs szükség arra, hogy a kosár tudjon a belőle létrehozott rendelésekről.


//Az Order entitás elmentésekor hozzákapcsolod a Cart-ot, így amikor lekérdezed egy személy (Person) rendeléseit, hozzáférhetsz az Order-hez tartozó Cart-hoz és annak CartItem-jeihez is. Ez a megközelítés lehetővé teszi, hogy nyomon kövess minden rendelés részleteit, beleértve a rendelés létrehozásakor a kosárban lévő tételeket.
