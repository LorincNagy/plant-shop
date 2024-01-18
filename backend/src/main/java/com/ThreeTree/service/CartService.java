package com.ThreeTree.service;

import com.ThreeTree.dao.CartRepository;
import com.ThreeTree.dao.ProductRepository;
import com.ThreeTree.dto.NewCartItemRequest;
import com.ThreeTree.model.Cart;
import com.ThreeTree.model.Person;
import com.ThreeTree.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;


    public void addCartItems(List<NewCartItemRequest> requests, Person person) {
        Cart cart = person.getCart();

        for (NewCartItemRequest request : requests) {

            Product existingProduct = getExistingProduct(request);


            cart.addProducts(existingProduct);

        }

        cartRepository.save(cart);
    }


    private Product getExistingProduct(NewCartItemRequest request) {
        return productRepository.findBySku(request.sku()).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Can't find product with SKU: " + request.sku()));
    }


    public void emptyCart(Person person) {
        Cart cart = person.getCart();
        if (cart != null) {
            cart.getProducts().clear();
            cartRepository.save(cart);
        }
    }

    public void removeFromCart(Integer cartItemIndex, Person person) {
        Cart cart = person.getCart();

        if (cartItemIndex < 0 || cartItemIndex >= cart.getProducts().size()) {
            throw new NoSuchElementException("Nincs ilyen indexű termék a kosárban.");
        }

        Product productToRemove = cart.getProducts().get(cartItemIndex);

        cart.getProducts().remove(productToRemove);

        cartRepository.save(cart);
    }


}


//    public void updateCartItem(Long id, CartItem updatedCartItem) {
//        // Keresd meg a cartItem-et az id alapján, majd frissítsd adatait az updatedCartItem segítségével
//    }

//    public void removeCartItem(Long id) {
//        // Távolítsd el a cartItem-et az id alapján a cartItems listából
//    }

//    public void clearCart() {
//        cartItems.clear();
//    }

//    public CartItem getCartItemById(Long id) {
//        return CartItem;
//    }


//    public void removeFromCart(Long id) {
//    }


//Az @Transactional annotációt az általában az üzleti szolgáltatások (Service réteg) metódusaiban alkalmazzák, nem pedig az entitások (Entity) osztályokban. A @Transactional annotáció az üzleti logikát tartalmazó rétegben szokott lenni, és azt hivatott jelezni, hogy egy adott metódus tranzakciós műveleteket végez.
