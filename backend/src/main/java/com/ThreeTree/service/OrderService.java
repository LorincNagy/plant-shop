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

        return orders.stream().map(NewOrderResponse::new).collect(Collectors.toList());
    }


    //ORDER SERVICE CLASS
    @Transactional
    public void saveOrder(NewOrderRequest request, Person person) {
        Cart cart = person.getCart();

        Order order = new Order();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");
        String formattedDate = LocalDateTime.now().format(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(formattedDate, formatter);
        order.setOrderDate(dateTime);
        order.setOrderTotal(request.orderTotal());
        order.setPerson(person);

        cartService.processCartItemsAndCreateOrder(cart, order);
        orderRepository.save(order);
        cartService.save(cart);
        personService.updatePerson(order, person, request);

    }

}

