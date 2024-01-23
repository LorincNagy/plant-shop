package com.ThreeTree.service;

import com.ThreeTree.dao.OrderRepository;
import com.ThreeTree.dao.PersonRepository;
import com.ThreeTree.dto.NewOrderRequest;
import com.ThreeTree.model.Cart;
import com.ThreeTree.model.Order;
import com.ThreeTree.model.Person;
import com.ThreeTree.model.Product;
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
public class OrderService {
    private final OrderRepository orderRepository;

    private final ProductService productService;

    private final PersonService personService;
    private final PersonRepository personRepository;

    @Autowired
    public OrderService(
            OrderRepository orderRepository,
            ProductService productService,
            PersonService personService,
            PersonRepository personRepository) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.personService = personService;
        this.personRepository = personRepository;
    }


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

        person.getOrders().add(order);

        person.setAddress(request.address());

        person.setPhoneNumber(request.phone());

        personRepository.save(person);
        orderRepository.save(order);

    }
}
