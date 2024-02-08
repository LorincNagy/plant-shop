package com.ThreeTree.service;

import com.ThreeTree.dao.CartRepository;
import com.ThreeTree.dto.NewCartItemRequest;
import com.ThreeTree.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {


    @InjectMocks
    private CartService cartService;
    @Mock
    private ProductService productService;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderItemService orderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializálja a mockokat és az injektálást is elvégzi
    }

    @Test
    void testAddOrUpdateCartItems() {

        Long productId1 = 1L;
        Long productId2 = 2L;
        Person person = new Person();
        Cart cart = new Cart();
        person.setCart(cart);

        Product product1 = new Product();
        product1.setProductId(productId1);

        Product product2 = new Product();
        product2.setProductId(productId2);


        NewCartItemRequest addRequest1 = new NewCartItemRequest(product1.getProductId(), 3);
        NewCartItemRequest addRequest2 = new NewCartItemRequest(product2.getProductId(), 2);


        NewCartItemRequest updateRequest1 = new NewCartItemRequest(product1.getProductId(), 5);


        when(productService.findProductById(productId1)).thenReturn(product1);
        when(productService.findProductById(productId2)).thenReturn(product2);


        cartService.addCartItems(List.of(addRequest1, addRequest2), person);


        verify(cartItemService, times(2)).addCartItem(any());


        assertEquals(2, cart.getCartItems().size());


        cartService.addCartItems(List.of(updateRequest1), person);


        verify(cartItemService, times(3)).addCartItem(any());


        assertEquals(2, cart.getCartItems().size());


        CartItem updatedCartItem = cart.findCartItemByProduct(productService.findProductById(productId1));
        System.out.println(updatedCartItem);
        assertNotNull(updatedCartItem);
        assertEquals(5, updatedCartItem.getQuantity());
    }


    @Test
    public void testRemoveFromCart() {

        Long cartItemId = 1L;
        Person person = new Person();
        Cart cart = new Cart();
        person.setCart(cart);


        CartItem cartItemToRemove = new CartItem();
        cartItemToRemove.setId(cartItemId);
        cartItemToRemove.setCart(cart);
        cartItemToRemove.setProduct(new Product());
        cartItemToRemove.setQuantity(3);


        when(cartItemService.findById(cartItemId)).thenReturn(Optional.of(cartItemToRemove));


        cartService.removeFromCart(cartItemId, person);


        verify(cartItemService).delete(cartItemToRemove);


        verify(cartRepository).save(cart);


        assertFalse(cart.getCartItems().contains(cartItemToRemove));
    }


    @Test
    void emptyCart() {

        Person person = new Person();
        Cart cart = new Cart();
        person.setCart(cart);


        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem());
        cartItems.add(new CartItem());
        cartItems.add(new CartItem());
        cart.setCartItems(cartItems);


        cartService.emptyCart(person);


        verify(cartItemService, times(3)).delete(any());


        verify(cart).deleteCartItems();


        verify(cartRepository).save(cart);


        assertEquals(0, cart.getCartItems().size());
    }

    @Test
    void save() {
        Cart cart = new Cart();

        cartService.save(cart);

        verify(cartRepository).save(cart);
    }


    @Test
    void processCartItemsAndCreateOrder() {

        Cart cart = new Cart();
        Order order = new Order();
        CartItem cartItem = new CartItem();
        Product product = new Product();
        product.setStock(100);
        cartItem.setProduct(product);
        cartItem.setQuantity(10);
        cart.addCartItem(cartItem);

        cartService.processCartItemsAndCreateOrder(cart, order);

        verify(orderItemService, times(1)).saveOrderItem(any(OrderItem.class));
        verify(productService, times(1)).saveProduct(any(Product.class));
        verify(cartItemService, times(1)).delete(any(CartItem.class));


        assertEquals(1, order.getOrderItems().size());
        assertEquals(cartItem.getProduct(), order.getOrderItems().get(0).getProduct());
        assertTrue(cart.getCartItems().isEmpty());
        assertEquals(product.getStock(), 90);

    }
}

//Az any(Cart.class) egy Mockito módszer, amelyet arra használnak, hogy a tesztben a paraméterekre való ellenőrzést részben vagy teljesen elkerüljék, és csak azt ellenőrizzék, hogy a metódust meghívták-e vagy sem a megfelelő módon.
//
//Amikor a verify módszert használod a any(Cart.class) paraméterrel, az azt jelenti, hogy nem érdekel minket, hogy a cartRepository.save metódust milyen Cart objektummal hívták meg. Bármilyen Cart objektum meghívásra kerülhetett, és a teszt csak azt ellenőrzi, hogy a metódust meghívták-e vagy sem.
//
//Ez hasznos lehet, ha nem akarod a konkrét paraméterekre való ellenőrzést a tesztben, és csak azt szeretnéd ellenőrizni, hogy a metódust meghívták a megfelelő időpontban vagy megfelelő számú alkalommal.
//Tehát a verify(cartItemService, times(3)).delete(any()); azt jelenti:
//
//verify(cartItemService, times(3)): Ellenőrizzük, hogy a cartItemService objektumon a delete metódust pontosan háromszor hívták meg. Ez azt ellenőrzi, hogy a delete metódus pontosan háromszor futott le a teszted során.
//
//delete(any()): A delete metódusnak bármilyen paraméterrel hívódhatott meg. Az any() azt jelenti, hogy nem érdekel minket, hogy a metódust milyen konkrét paraméterekkel hívták meg, csak az számít, hogy meghívták-e vagy sem.
//
//Tehát ebben az esetben a teszt azt ellenőrzi, hogy a cartItemService objektumon a delete metódust pontosan háromszor hívták meg bármilyen paraméterekkel.
//Elkészíted a szükséges teszt adatokat, például termékazonosítókat (productId1 és productId2), egy személyt (person) és egy kosarat (cart).
//
//Létrehozod a két NewCartItemRequest objektumot (request1 és request2), amelyek új kosár elemeket reprezentálnak a termékekhez.
//
//Mockolod a productService-t, hogy azt szimulálja, hogy megtalálja a termékeket a megadott azonosítók alapján. Ezt teszed azáltal, hogy beállítod a megfelelő viselkedést a when metódus segítségével.
//
//Meghívod a tesztelendő metódust: cartService.addCartItems(List.of(request1, request2), person);.
//
//Ellenőrzöd, hogy a megfelelő metódusok meghívták-e a megfelelő mock objektumokat. Ezt a verify metódusokkal teszed.
//
//Ellenőrzöd, hogy a kosárban megfelelően szerepelnek-e a termékek (CartItem objektumok). Ezt a assertNotNull és az assertEquals metódusokkal teszed.
//
//Ha a teszt sikeresen lefut anélkül, hogy hibát dobna, és a végén az összes ellenőrzés sikeres, akkor az azt mutatja, hogy a cartService.addCartItems metódus helyesen működik a megadott adatokkal, és a kosárba helyesen kerülnek hozzáadásra vagy frissítésre az elemek
