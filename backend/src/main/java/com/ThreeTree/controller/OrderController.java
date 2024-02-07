package com.ThreeTree.controller;

import com.ThreeTree.dto.NewOrderRequest;
import com.ThreeTree.dto.NewOrderResponse;
import com.ThreeTree.model.Order;
import com.ThreeTree.model.Person;
import com.ThreeTree.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> missingOrder(NoSuchElementException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<NewOrderResponse>> getAllOrders() {
        List<NewOrderResponse> orderResponses = orderService.getOrders();
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable("orderId") Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public void addOrder(@RequestBody NewOrderRequest newOrderRequest, @AuthenticationPrincipal Person person) {
        orderService.saveOrder(newOrderRequest, person);
    }


}


// A NoSuchElementException kezelése a controller rétegben történik az @ExceptionHandler annotáció segítségével. Amikor a getOrderById metódus hívása során a szolgáltatás rétegben dobódik egy ilyen kivétel, a vezérlőrétegben lévő missingOrder metódus kezeli azt, és visszaadja a megfelelő HTTP választ a kliensnek a hibaüzenettel és a 404-es státuszkóddal. Ez a megközelítés megfelel az általad leírt igényeknek.
//Szolgáltatási Réteg (Service Layer)
//Amikor a findById metódus egy üres Optional-t ad vissza (tehát az entitás nem található), az orElseThrow segítségével egy NoSuchElementException (vagy más, előre meghatározott típusú kivételt) dobunk.
//
//java
//Copy code
//public Order getOrderById(Long id) {
//    return orderRepository.findById(id)
//            .orElseThrow(() -> new NoSuchElementException("Order not found for id: " + id));
//}
//2. Kontroller Réteg (Controller Layer)
//A kontrollerben meghívod a szolgáltatás metódusát, hogy lekérj egy entitást. Ha a szolgáltatás kivételt dob, ez a kivétel automatikusan "továbbítódik" a Spring MVC keretrendszeren keresztül.
//
//java
//Copy code
//@GetMapping("/{orderId}")
//public ResponseEntity<Order> getOrderById(@PathVariable("orderId") Long orderId) {
//    Order order = orderService.getOrderById(orderId);
//    return ResponseEntity.ok(order);
//}
//3. Kivételkezelés (Exception Handling)
//Ha a szolgáltatásban dobott NoSuchElementException eléri a kontrollert, és nincs lokális @ExceptionHandler a kontrollerben, amely kezelné ezt a típusú kivételt, akkor egy globális @ControllerAdvice osztályban definiált kivételkezelő fogja elkapni és kezelni. Ha azonban a kontrollerben van egy megfelelő @ExceptionHandler, akkor az fogja kezelni a kivételt.
//
//java
//Copy code
//@ExceptionHandler(NoSuchElementException.class)
//public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
//    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
//}
//Összefoglalva
//A folyamat tehát így néz ki:
//
//A szolgáltatási rétegben dobott kivétel (például NoSuchElementException) a kontroller metódus végrehajtása során keletkezik.
//Ez a kivétel "továbbítódik" a Spring keretrendszeren keresztül, amíg el nem éri egy megfelelő kivételkezelőt (@ExceptionHandler), amely vagy a kontrollerben vagy egy globális kivételkezelő osztályban (@ControllerAdvice) van definiálva.
//A kivételkezelő megfelelő HTTP választ állít elő, amely tartalmazza a kivétel üzenetét és a megfelelő HTTP státuszkódot (például HttpStatus.NOT_FOUND).
//Ez a mechanizmus biztosítja, hogy az alkalmazásod különböző rétegei közötti kommunikáció tiszta és jól kezelhető legyen, miközben hatékony hibakezelést biztosít.
