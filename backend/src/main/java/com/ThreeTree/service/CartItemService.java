package com.ThreeTree.service;

import com.ThreeTree.dao.CartItemRepository;
import com.ThreeTree.model.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;


    @Transactional
    public void addCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    @Transactional
    public Optional<CartItem> findById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId);
    }

    @Transactional
    public void delete(CartItem cartItemToRemove) {
        cartItemRepository.delete(cartItemToRemove);
    }


}

