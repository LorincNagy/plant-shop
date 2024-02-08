package com.ThreeTree.service;

import com.ThreeTree.dao.CartItemRepository;
import com.ThreeTree.model.CartItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)//jobb mint setupban initMocks vagy openMocks?
class CartItemServiceTest {

    @InjectMocks
    private CartItemService cartItemService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Test
    void testAddCartItem() {
        CartItem cartItem = new CartItem();
        cartItemService.addCartItem(cartItem);
        verify(cartItemRepository, times(1)).save(cartItem);
    }

    @Test
    void testFindById() {
        Long expectedId = 1L;
        CartItem expectedCartItem = new CartItem();
        expectedCartItem.setId(expectedId);


        when(cartItemRepository.findById(expectedId)).thenReturn(Optional.of(expectedCartItem));


        Optional<CartItem> actualResult = cartItemService.findById(expectedId);


        assertTrue(actualResult.isPresent());
        assertEquals(expectedCartItem, actualResult.get());
    }

    @Test
    void testDelete() {
        Long cartItemId = 1L;
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);


        doNothing().when(cartItemRepository).delete(cartItem);


        cartItemService.delete(cartItem);


        verify(cartItemRepository, times(1)).delete(cartItem);
    }
}
