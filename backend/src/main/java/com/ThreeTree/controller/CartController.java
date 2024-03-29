package com.ThreeTree.controller;

import com.ThreeTree.dto.NewCartItemRequest;
import com.ThreeTree.model.Cart;
import com.ThreeTree.model.Person;
import com.ThreeTree.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> missingCartItem(NoSuchElementException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> missingCartItem(HttpClientErrorException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());
    }

//    @GetMapping
//    public List<CartItem> getCartItems() {
//        return cartService.getCartItems();
//    }

//    @GetMapping("/{cartItemId}")
//    public CartItem getCartItemById(@PathVariable("cartItemId") Long id) {
//        return cartService.getCartItemById(id);
//    }

    @PostMapping
    public void addCartItem(@RequestBody List<NewCartItemRequest> request, @AuthenticationPrincipal Person person) {
        cartService.addCartItems(request, person);
    }


    @DeleteMapping("/{cartItemID}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long cartItemID, @AuthenticationPrincipal Person person) {
        cartService.removeFromCart(cartItemID, person);
        return ResponseEntity.ok("Item successfully removed from cart");
    }


    @DeleteMapping("/empty-cart")
    public ResponseEntity<String> emptyCart(@AuthenticationPrincipal Person person) {
        cartService.emptyCart(person);
        return ResponseEntity.ok("{\"message\": \"Cart successfully emptied\"}");

    }

}


