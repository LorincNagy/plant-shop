package com.ThreeTree.controller;

import com.ThreeTree.dto.NewOrderRequest;
import com.ThreeTree.model.Order;
import com.ThreeTree.model.Person;
import com.ThreeTree.service.OrderService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/orders")
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
    public List<Order> getAllOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable("orderId") Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public void addOrder(@RequestBody NewOrderRequest newOrderRequest, @AuthenticationPrincipal Person person) {
        System.out.println(newOrderRequest);
        orderService.saveOrder(newOrderRequest, person);
    }


}
