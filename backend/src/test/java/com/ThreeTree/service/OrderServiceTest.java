package com.ThreeTree.service;

import com.ThreeTree.dao.OrderRepository;
import com.ThreeTree.dto.NewOrderRequest;
import com.ThreeTree.dto.NewOrderResponse;
import com.ThreeTree.model.Cart;
import com.ThreeTree.model.CartItem;
import com.ThreeTree.model.Order;
import com.ThreeTree.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {


    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    PersonService personService;

    @Mock
    CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getOrderById_ReturnsOrder_WhenOrderExists() {

        Long orderId = 1L;
        Order expectedOrder = new Order();
        expectedOrder.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));


        Order actualOrder = orderService.getOrderById(orderId);


        assertEquals(expectedOrder, actualOrder);
        verify(orderRepository).findById(orderId);
    }

    @Test
    public void getOrderById_ThrowsNoSuchElementException_WhenOrderDoesNotExist() {

        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());


        assertThrows(NoSuchElementException.class, () -> {
            orderService.getOrderById(orderId);
        });


        verify(orderRepository).findById(orderId);
    }


    @Test
    void getOrders_shouldReturnListOfOrders() {
        // Arrange
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());

        when(orderRepository.findAll()).thenReturn(orders);

        // Act
        List<NewOrderResponse> orderResponses = orderService.getOrders();

        // Assert
        assertNotNull(orderResponses);
        assertEquals(2, orderResponses.size());
    }

    @Test
    void getOrders_shouldThrowNoSuchElementException_whenNoOrdersFound() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            orderService.getOrders();
        });
    }


    @Test
    void saveOrder_shouldSaveOrderAndProcessCart() {
        // Arrange
        NewOrderRequest request = new NewOrderRequest(
                "123 Main Street",
                "example@example.com",
                "+1234567890",
                BigDecimal.valueOf(100.00)
        );

        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cart.addCartItem(cartItem);
        Person person = new Person();
        person.setCart(cart);

        // Act
        orderService.saveOrder(request, person);


        verify(cartService, times(1)).processCartItemsAndCreateOrder(any(Cart.class), any(Order.class));//ezek nem kötelezők csak ha void methodot tesztlek , a mockokat nem kötelező megnézni hogy meghivodtak e azt majd a saját service tesztjében
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartService, times(1)).save(any(Cart.class));
        verify(personService, times(1)).updatePerson(any(Order.class), any(Person.class), any());
    }

}

//A Mockito azt várja, hogy ha matchereket használsz, akkor minden argumentumhoz matchert kell használnod, nem keverheted a matchereket a "nyers" értékekkel.
//
//Ebben az esetben a processCartItemsAndCreateOrder metódus első paramétere egy konkrét Cart objektum (ami rendben van, ha ez a metódus csak ezzel a konkrét objektummal hívható meg a teszt során), de a második paraméterhez any() matchert használsz.
//
//Ha a metódusnak több paramétere van és már használsz matchert, akkor mindkét paraméterhez matchert kell használnod. Ebben az esetben az első paraméterhez is használhatsz egy matchert, például eq(cart)-ot, ha pontosan azt a Cart objektumot várod, vagy any(Cart.class), ha bármilyen Cart objektum megfelelő lehet.
