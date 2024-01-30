package com.ThreeTree.service;

import com.ThreeTree.dao.CartRepository;
import com.ThreeTree.dao.OrderRepository;
import com.ThreeTree.dao.PersonRepository;
import com.ThreeTree.dto.NewOrderRequest;
import com.ThreeTree.dto.NewOrderResponse;
import com.ThreeTree.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final CartItemService cartItemService;
    private final OrderItemService orderItemService;


    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        orderRepository.delete(order);
    }


    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    @Transactional
    public List<NewOrderResponse> getOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(NewOrderResponse::new)
                .collect(Collectors.toList());
    }

//    @Transactional
//    public List<NewOrderResponse> getOrders() {
//        List<Order> orders = orderRepository.findAll();
//
//
//        List<NewOrderResponse> response = orders.stream().map(NewOrderResponse::new).collect(Collectors.toList());
//
//        // A végleges NewOrderResponse lista kiírása
//        System.out.println("NewOrderResponses: " + response);
//
//        return response;
//    }

    @Transactional
    public void saveOrder(NewOrderRequest request, Person person) {
        Cart cart = person.getCart();
        List<CartItem> originalCartItems = cart.getCartItems();

        Order order = new Order();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");
        String formattedDate = LocalDateTime.now().format(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(formattedDate, formatter);
        order.setOrderDate(dateTime);
        order.setOrderTotal(request.orderTotal());
        order.setPerson(person);

        for (CartItem cartItem : originalCartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItemService.saveOrderItem(orderItem);
            order.addOrderItem(orderItem);
            cartItemService.delete(cartItem);
        }

        cart.deleteCartItems();
        // Mentés az Order entitásnak
        orderRepository.save(order);


        cartService.save(cart); // Mentsd el a kiürített Cart-ot


        personService.updatePerson(order, person, request); // Frissítsd a Person-t az új rendeléssel
//        System.out.println(order.getOrderItems());
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
