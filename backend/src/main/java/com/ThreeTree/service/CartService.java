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

    private final ProductService productService;
    private final CartItemService cartItemService;
    private final CartRepository cartRepository;


    @Transactional
    public void addCartItems(List<NewCartItemRequest> requests, Person person) {
        Cart cart = person.getCart();

        for (NewCartItemRequest request : requests) {
            Product existingProduct = getExistingProduct(request.productId());

            // Ellenőrizzük, hogy van-e már CartItem ezzel a termékkel a kosárban
            CartItem existingCartItem = cart.findCartItemByProduct(existingProduct);
            if (existingCartItem != null) {
                // Ha már létezik, csak növeld meg a mennyiséget
                existingCartItem.setQuantity(request.quantity());
                cartItemService.addCartItem(existingCartItem); // Frissítsd a meglévő CartItem-et, //ide kellene cartItemService  ami majd meghivja a saját cartItemRepositoryt, azaz mindig a saját servicében legyen meghivva a saját repositorija
            } else {
                // Ha nincs ilyen CartItem, hozz létre egy újat
                CartItem newCartItem = new CartItem();
                newCartItem.setQuantity(request.quantity());
                newCartItem.setCart(cart);
                newCartItem.setProduct(existingProduct);

                cartItemService.addCartItem(newCartItem); // Mentsd el az új CartItem-et
                cart.addCartItem(newCartItem);
            }
        }
        cartRepository.save(cart); // Mentsd el a frissített Cart-ot
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

        // CartItem törlése az adatbázisból
        cart.getCartItems().stream().forEach(cartItemService::delete);//nem tul oop kompatibilis?

        cart.deleteCartItems();

        // Mentsük el a frissített Cart objektumot
        cartRepository.save(cart);
    }

}


//Repository Rétegben: Az Optional<Product> használata a findById metódusban megfelelő, mivel ez lehetővé teszi, hogy kezeljük azokat az eseteket, amikor a termék nem található az adatbázisban.
//
//java
//Copy code
//public interface ProductRepository extends JpaRepository<Product, Long> {
//    Optional<Product> findById(Long id);
//}
//Service Rétegben: A ProductService findProductById metódusa szintén visszaad egy Optional<Product> objektumot, amit közvetlenül a repository-tól kap.
//
//java
//Copy code
//public Optional<Product> findProductById(Long id) {
//    return productRepository.findById(id);
//}
//Egy Másik Szervízben (pl. CartService): Itt kezeled a Optional<Product> objektumot, és kivételt dobsz, ha a termék nem található.
//
//java
//Copy code
//private Product getExistingProduct(Long productId) {
//    Optional<Product> optionalProduct = productService.findProductById(productId);
//    return optionalProduct.orElseThrow(() ->
//            new HttpClientErrorException(HttpStatus.NOT_FOUND, "Nem található termék azonosítóval: " + productId));
//}
//Controllerben: A kivételkezelő (ExceptionHandler) kezeli az HttpClientErrorException kivételeket, és választ küld vissza a kliensnek.
//
//java
//Copy code
//@ExceptionHandler(HttpClientErrorException.class)
//public ResponseEntity<String> missingCartItem(HttpClientErrorException ex) {
//    return new ResponseEntity<>(ex.getMessage(), ex.getStatusCode());
//}
//Ez a megközelítés jól működik a Spring REST API-kban. Az Optional típus használatával rugalmasan kezelheted azokat az eseteket, amikor a termék lehet, hogy nem létezik az adatbázisban. A CartService-ben történő kivétel dobása lehetővé teszi, hogy a hívó réteg (a controller) eldöntse, hogyan kezelje ezt a helyzetet, így a hibakezelés logikája is jól szeparált és kezelhető.

//Ha az Optional objektum nem üres, azaz tartalmaz egy Product példányt (optionalProduct.isPresent() igaz), akkor a orElseThrow() metódus visszaadja ezt a Product példányt.
//Ha az Optional objektum üres (optionalProduct.isPresent() hamis), akkor a orElseThrow() metódus a megadott kivételt dobja.
