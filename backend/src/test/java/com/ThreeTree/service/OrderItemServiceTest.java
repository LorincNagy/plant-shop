package com.ThreeTree.service;

import com.ThreeTree.dao.OrderItemRepository;
import com.ThreeTree.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class OrderItemServiceTest {

    @InjectMocks
    OrderItemService orderItemService;

    @Mock
    OrderItemRepository orderItemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveOrderItem() {

        OrderItem orderItem = new OrderItem();

        orderItemService.saveOrderItem(orderItem);

        verify(orderItemRepository).save(orderItem);

    }
}
