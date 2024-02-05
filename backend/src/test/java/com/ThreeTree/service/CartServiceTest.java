package com.ThreeTree.service;

import com.ThreeTree.dao.CartRepository;
import com.ThreeTree.dto.NewCartItemRequest;
import com.ThreeTree.model.Cart;
import com.ThreeTree.model.CartItem;
import com.ThreeTree.model.Person;
import com.ThreeTree.model.Product;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

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
        // Elkészítünk néhány teszt adatot
        Long productId1 = 1L;
        Long productId2 = 2L;
        Person person = new Person();
        Cart cart = new Cart();
        person.setCart(cart);

        Product product1 = new Product();
        product1.setProductId(productId1);

        Product product2 = new Product();
        product2.setProductId(productId2);

        // Létrehozunk egy NewCartItemRequest-et az új elemek hozzáadásához
        NewCartItemRequest addRequest1 = new NewCartItemRequest(product1.getProductId(), 3);
        NewCartItemRequest addRequest2 = new NewCartItemRequest(product2.getProductId(), 2);

        // Létrehozunk egy NewCartItemRequest-et a már meglévő elemek frissítéséhez
        NewCartItemRequest updateRequest1 = new NewCartItemRequest(product1.getProductId(), 5);

        // Mockoljuk a ProductService-t
        when(productService.findProductById(productId1)).thenReturn(Optional.of(product1));
        when(productService.findProductById(productId2)).thenReturn(Optional.of(product2));

        // Teszteljük a CartService addCartItems metódusát az új elemek hozzáadásával
        cartService.addCartItems(List.of(addRequest1, addRequest2), person);

        // Ellenőrizzük, hogy a megfelelő metódusokat hívták-e meg az új elemek hozzáadása során
        verify(cartItemService, times(2)).addCartItem(any());

        // Ellenőrizzük, hogy a CartItemek száma megfelelő
        assertEquals(2, cart.getCartItems().size());

        // Teszteljük a CartService addCartItems metódusát a már meglévő elemek frissítésével
        cartService.addCartItems(List.of(updateRequest1), person);

        // Ellenőrizzük, hogy a megfelelő metódusokat hívták-e meg a frissítés során
        verify(cartItemService, times(3)).addCartItem(any());

        // Ellenőrizzük, hogy a CartItemek száma változatlan
        assertEquals(2, cart.getCartItems().size());

        // Ellenőrizzük, hogy a már meglévő CartItem frissítése helyesen történt
        CartItem updatedCartItem = cart.findCartItemByProduct(productService.findProductById(productId1).orElse(null));
        System.out.println(updatedCartItem);
        assertNotNull(updatedCartItem);
        assertEquals(5, updatedCartItem.getQuantity());
    }


    @Test
    void removeFromCart() {

    }

    @Test
    void emptyCart() {
    }

    @Test
    void save() {
    }

    @Test
    void processCartItemsAndCreateOrder() {
    }
}

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
