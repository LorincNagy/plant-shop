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

    //Ha a orderService.getOrderById(orderId) hívás eredménye egy üres Optional, akkor a orElseThrow metódus kivételt dob. Az assertThrows metódus ezt a kivételt várja, és ha ténylegesen dobódik, akkor a teszt sikeres lesz, mivel az elvárt viselkedést érzékeli. Ha a getOrderById metódus üres Optional-t ad vissza, akkor a NoSuchElementException kivétel dobódik, amit a assertThrows metódus megfelelően kezel. Ezáltal a teszt ellenőrzi, hogy a getOrderById helyesen dob-e kivételt az üres Optional esetén.
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

    // saveOrder_shouldSaveOrderAndProcessCart metódus
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
        Order order = orderService.saveOrder(request, person);

        // Assert
        // Itt újrahasználjuk az order objektumot, amit a saveOrder metódusban használtunk
        verify(cartService, times(1)).processCartItemsAndCreateOrder(cart, order);
        verify(orderRepository, times(1)).save(order);
//        assertEquals(1, order.getOrderItems().size());
        verify(cartService, times(1)).save(cart);
//        assertTrue(cart.getCartItems().isEmpty());
        verify(personService, times(1)).updatePerson(order, person, request);
    }

}

//Az assertThrows metódus arra szolgál, hogy tesztelje, hogy egy adott kódrészlet kivételt dob-e a futás során. A lambda kifejezésben lévő kódot hívja meg, és ellenőrzi, hogy az adott kód valóban dob-e kivételt, és ha igen, akkor a várt kivételtípus-e a dobott kivétel. Ebben az esetben az orderService.getOrderById(orderId) hívás kivételt dob, ha az üres Optional-t kapja vissza, és az assertThrows metódus ezt a kivételt ellenőrzi. Ha a dobott kivétel egy NoSuchElementException, akkor a teszt sikeres lesz. Ezáltal a teszt azt ellenőrzi, hogy a getOrderById metódus helyesen dob-e kivételt az üres Optional esetén. Nagyszerűen foglaltad össze!
