package com.ThreeTree.service;

import com.ThreeTree.dao.OrderRepository;
import com.ThreeTree.dto.NewOrderRequest;
import com.ThreeTree.model.Order;
import com.ThreeTree.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final ProductService productService;

    private final PersonService personService;

    @Autowired
    public OrderService(
            OrderRepository orderRepository,
            ProductService productService,
            PersonService personService
    ) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.personService = personService;
    }

    public void saveOrder(NewOrderRequest newOrderRequest) {
        if (newOrderRequest.productsQuantities() != null) {
            Order newOrder = new Order();

            Map<Product, Integer> productsQuantities = newOrderRequest.productsQuantities().entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> productService.findProductById(entry.getKey()),
                            Map.Entry::getValue
                    ));

            newOrder.setProductsQuantities(productsQuantities);
            newOrder.setOrderDate(LocalDate.now());
            newOrder.setPerson(personService.getCustomerById(newOrderRequest.customerId()));

            BigDecimal total = calculateOrderTotal(productsQuantities);
            newOrder.setOrderTotal(total);

            orderRepository.save(newOrder);
        }
    }

    private BigDecimal calculateOrderTotal(Map<Product, Integer> productsQuantities) {
        return productsQuantities.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(new BigDecimal(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        orderRepository.delete(order);
    }

    public void updateOrder(Long id, Order order) {
        Order orderToUpdate = orderRepository.findById(id).orElseThrow();
        orderToUpdate.setProductsQuantities(order.getProductsQuantities());
        orderRepository.save(orderToUpdate);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }
}
