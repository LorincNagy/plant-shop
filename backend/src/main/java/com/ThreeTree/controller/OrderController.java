package com.ThreeTree.controller;

import com.ThreeTree.dto.NewOrderRequest;
import com.ThreeTree.model.Order;
import com.ThreeTree.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public void addOrder(@RequestBody NewOrderRequest newOrderRequest) {
       orderService.saveOrder(newOrderRequest);
    }

    @PutMapping("/{id}")
    public void updateOrderById(@PathVariable("id") Long id, @RequestBody Order order) {
        orderService.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
    }

}
