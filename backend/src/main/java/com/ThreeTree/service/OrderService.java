package com.ThreeTree.service;

import com.ThreeTree.dao.CartRepository;
import com.ThreeTree.dao.OrderRepository;
import com.ThreeTree.dao.PersonRepository;
import com.ThreeTree.dto.NewOrderRequest;
import com.ThreeTree.dto.NewOrderResponse;
import com.ThreeTree.model.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final PersonService personService;
    private final CartService cartService;


    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found for id: " + id));
    }

    @Transactional
    public List<NewOrderResponse> getOrders() {
        List<Order> orders = orderRepository.findAll();

        if (orders.isEmpty()) {
            throw new NoSuchElementException("No orders found");
        }

        return orders.stream().map(NewOrderResponse::new).collect(Collectors.toList());
    }


    //ORDER SERVICE CLASS
    @Transactional
    public Order saveOrder(NewOrderRequest request, Person person) {
        Cart cart = person.getCart();

        Order order = new Order();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");
        String formattedDate = LocalDateTime.now().format(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(formattedDate, formatter);
        order.setOrderDate(dateTime);
        order.setOrderTotal(request.orderTotal());
        order.setPerson(person);


//        System.out.println(cart.getCartItems());
        cartService.processCartItemsAndCreateOrder(cart, order);

//        System.out.println(order.getOrderItems());
        orderRepository.save(order);
        cartService.save(cart);
        personService.updatePerson(order, person, request);
        return order;
    }

}


//Hogyan működik a folyamat a Repositorytól a Controllerig?
//Repository Szint: A findById metódus egy Optional<T> objektumot ad vissza, amely vagy tartalmaz egy entitást, ha az megtalálható (Optional.of(entity)), vagy nem tartalmaz semmit, ha nem (Optional.empty()).
//
//Service Szint: Itt dönthetsz arról, hogy hogyan kezeled az Optional-t. Két fő út létezik:
//
//Az Optional-t közvetlenül visszaadni: Ez lehetővé teszi a hívó számára (pl. egy kontroller), hogy maga döntse el, hogyan kezelje az üres esetet.
//Kivétel dobása, ha az Optional üres: Ebben az esetben egy explicit kivételt dobsz (pl. NoSuchElementException), ha az entitás nem található. Ezt megteheted az orElseThrow() metódus használatával, amely lehetővé teszi egy kivétel dobását, ha az Optional üres. Például:
//java
//Copy code
//public Order getOrderById(Long id) {
//    return orderRepository.findById(id)
//        .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + id));
//}
//Controller Szint: A kontroller feladata, hogy kezelje a kéréseket és válaszokat. Ha a szolgáltatás kivételt dob, a kontrollerben eldöntheted, hogy ezt a kivételt hogyan kezeled. Például try-catch blokk használatával elkapod a kivételt és visszaadsz egy megfelelő HTTP választ, vagy használhatsz egy globális kivételkezelőt (@ControllerAdvice) a kivételek egységes kezelésére.
//Összefoglalás
//Nem kötelező, hogy a szolgáltatási réteg Optional-t adjon vissza csak azért, mert a repository Optional-t ad vissza. Ez a döntés attól függ, hogy szeretnéd-e a hívó félre hagyni az üres Optional kezelését, vagy inkább explicit kivételt dobni az entitás hiánya esetén.
//A szolgáltatási rétegben való kivétel dobás a keresett entitás hiányakor (és ennek kezelése a kontroller szintjén) egy tiszta és világos megközelítés, amely elősegíti a hibák explicit kezelését és a REST API válaszok pontosabb szabályozását.
