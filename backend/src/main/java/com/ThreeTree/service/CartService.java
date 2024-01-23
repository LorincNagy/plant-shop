package com.ThreeTree.service;

import com.ThreeTree.dao.CartItemRepository;
import com.ThreeTree.dao.CartRepository;
import com.ThreeTree.dao.ProductRepository;
import com.ThreeTree.dto.NewCartItemRequest;
import com.ThreeTree.model.Cart;
import com.ThreeTree.model.CartItem;
import com.ThreeTree.model.Person;
import com.ThreeTree.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;


    @Transactional
    public void addCartItems(List<NewCartItemRequest> requests, Person person) {
        Cart cart = person.getCart();

        for (NewCartItemRequest request : requests) {
            Product existingProduct = getExistingProduct(request.productId());

            // Ellenőrizzük, hogy van-e már CartItem ezzel a termékkel a kosárban
            CartItem existingCartItem = findCartItemByProduct(cart, existingProduct);
            if (existingCartItem != null) {
                // Ha már létezik, csak növeld meg a mennyiséget
                existingCartItem.setQuantity(request.quantity());
                cartItemRepository.save(existingCartItem); // Frissítsd a meglévő CartItem-et
            } else {
                // Ha nincs ilyen CartItem, hozz létre egy újat
                CartItem newCartItem = new CartItem();
                newCartItem.setQuantity(request.quantity());
                newCartItem.setCart(cart);
                newCartItem.setProduct(existingProduct);

                cartItemRepository.save(newCartItem); // Mentsd el az új CartItem-et
                cart.getCartItems().add(newCartItem);
            }
        }
        cartRepository.save(cart); // Mentsd el a frissített Cart-ot
    }


    @Transactional
    public void removeFromCart(Long cartItemId, Person person) {
        Cart cart = person.getCart();
        CartItem cartItemToRemove = findCartItemById(cartItemId);

        removeCartItemFromCart(cart, cartItemToRemove);

        cartItemToRemove.setCart(null);
        cartItemToRemove.setProduct(null);
        cartItemRepository.delete(cartItemToRemove);

        cartRepository.save(cart);
    }


    private CartItem findCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND,
                        "Nem található ilyen azonosítójú CartItem: " + cartItemId));
    }

    private void removeCartItemFromCart(Cart cart, CartItem cartItemToRemove) {
        CartItem itemToRemove = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemToRemove.getId())) // Az equals metódus használata
                .findFirst()
                .orElse(null);
        if (itemToRemove != null) {
            cart.getCartItems().remove(itemToRemove);
        }
    }


    private Product getExistingProduct(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        return optionalProduct.orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND, "Nem található termék azonosítóval: " + productId));
    }

    private CartItem findCartItemByProduct(Cart cart, Product product) {
        // Check if a cart item with the same product already exists in the cart
        return cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getProductId().equals(product.getProductId()))
                .findFirst()
                .orElse(null);
    }

    @Transactional
    public void emptyCart(Person person) {
        Cart cart = person.getCart();

        // Keresd meg és távolítsd el az összes CartItem-et
        cart.getCartItems().stream().forEach(cartItem -> {
            cartItem.setCart(null); // Kapcsolat megszüntetése a Cart és CartItem között
            cartItem.setProduct(null); // Kapcsolat megszüntetése a Product és CartItem között
            cartItemRepository.delete(cartItem); // CartItem törlése az adatbázisból
        });

        // Töröljük a CartItem-eket a Cart objektumból
        cart.getCartItems().clear();

        // Mentsük el a frissített Cart objektumot
        cartRepository.save(cart);
    }

}
