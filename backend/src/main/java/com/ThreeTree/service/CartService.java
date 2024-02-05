package com.ThreeTree.service;

import com.ThreeTree.dao.CartItemRepository;
import com.ThreeTree.dao.CartRepository;
import com.ThreeTree.dao.ProductRepository;
import com.ThreeTree.dto.NewCartItemRequest;
import com.ThreeTree.model.*;
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

    private final ProductService productService;
    private final CartItemService cartItemService;
    private final CartRepository cartRepository;
    private final OrderItemService orderItemService;


    @Transactional
    public void addCartItems(List<NewCartItemRequest> requests, Person person) {
        Cart cart = person.getCart();

        for (NewCartItemRequest request : requests) {
            Product existingProduct = getExistingProduct(request.productId());

            CartItem existingCartItem = cart.findCartItemByProduct(existingProduct);

            if (existingCartItem != null) {
                existingCartItem.setQuantity(request.quantity());
                cartItemService.addCartItem(existingCartItem);
            } else {
                CartItem newCartItem = new CartItem();
                newCartItem.setQuantity(request.quantity());
                newCartItem.setCart(cart);
                newCartItem.setProduct(existingProduct);
                cartItemService.addCartItem(newCartItem);
                cart.addCartItem(newCartItem);
            }
        }
        cartRepository.save(cart);
    }


    @Transactional
    public void removeFromCart(Long cartItemId, Person person) {
        Cart cart = person.getCart();
        CartItem cartItemToRemove = findCartItemById(cartItemId);

        removeCartItemFromCart(cart, cartItemToRemove);


        cartItemService.delete(cartItemToRemove);

        cartRepository.save(cart);
    }


    private CartItem findCartItemById(Long cartItemId) {
        Optional<CartItem> optionalCartItem = cartItemService.findById(cartItemId);
        return optionalCartItem.orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND,
                "Nem található ilyen azonosítójú CartItem: " + cartItemId));
    }

    private void removeCartItemFromCart(Cart cart, CartItem cartItemToRemove) {
        cart.removeCartItem(cartItemToRemove);
    }


    private Product getExistingProduct(Long productId) {
        Optional<Product> optionalProduct = productService.findProductById(productId);
        return optionalProduct.orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND, "Nem található termék azonosítóval: " + productId));
    }


    @Transactional
    public void emptyCart(Person person) {
        Cart cart = person.getCart();
        cart.getCartItems().stream().forEach(cartItemService::delete);//nem tul oop kompatibilis?

        cart.deleteCartItems();
        cartRepository.save(cart);
    }

    public void save(Cart cart) {
        cartRepository.save(cart);
    }


    public void processCartItemsAndCreateOrder(Cart cart, Order order) {//pétertöl megkérdezni oop enkapszulácionak megfelel-e ha nem cart terál hanem cartservicben elkérem cart.getcartitems()?
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());

            Product product = cartItem.getProduct();
            int remainingStock = product.getStock() - cartItem.getQuantity();

            product.setStock(remainingStock);
            productService.saveProduct(product);

            orderItemService.saveOrderItem(orderItem);
            order.addOrderItem(orderItem);

            cartItemService.delete(cartItem);
        }
        cart.deleteCartItems();
    }

}

