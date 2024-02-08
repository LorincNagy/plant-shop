package com.ThreeTree.service;

import com.ThreeTree.dao.PersonRepository;
import com.ThreeTree.dto.NewOrderRequest;
import com.ThreeTree.dto.NewPersonRequest;
import com.ThreeTree.model.Order;
import com.ThreeTree.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCustomers() {
        // Arrange
        List<Person> customers = new ArrayList<>();
        customers.add(new Person());
        when(personRepository.findAll()).thenReturn(customers);

        // Act
        List<Person> result = personService.getCustomers();

        // Assert
        assertEquals(customers.size(), result.size());
    }

    @Test
    public void testGetCustomerById() {
        // Arrange
        Long id = 1L;
        Person person = new Person();
        when(personRepository.findById(id)).thenReturn(Optional.of(person));

        // Act
        Person result = personService.getCustomerById(id);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testSaveCustomer() {
        // Arrange
        NewPersonRequest request = new NewPersonRequest("John", "Doe", "john@example.com", "123456789", "Address", "password");

        // Act
        personService.saveCustomer(request);

        // Assert
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    public void testUpdateCustomerById() {
        // Arrange
        Long id = 1L;
        Person existingPerson = new Person();
        when(personRepository.findById(id)).thenReturn(Optional.of(existingPerson));

        Person updatedPerson = new Person();
        updatedPerson.setFirstName("UpdatedFirstName");

        // Act
        personService.updateCustomerById(id, updatedPerson);

        // Assert
        verify(personRepository, times(1)).save(existingPerson);
        assertEquals(updatedPerson.getFirstName(), existingPerson.getFirstName());
    }

    @Test
    public void testDeleteCustomerById() {
        // Arrange
        Long id = 1L;

        // Act
        personService.deleteCustomerById(id);

        // Assert
        verify(personRepository, times(1)).deleteById(id);
    }

    @Test
    public void testUpdatePerson() {
        // Arrange
        Person person = new Person();
        person.setOrders(new ArrayList<>()); // inicializáljuk az orders listát
        Order order = new Order();
        NewOrderRequest orderRequest = new NewOrderRequest("NewAddress", "NewEmail", "NewPhone", BigDecimal.valueOf(100.00));

        // Act
        personService.updatePerson(order, person, orderRequest);

        // Assert
        assertEquals(order, person.getOrders().get(0));
        assertEquals(orderRequest.address(), person.getAddress());
        assertEquals(orderRequest.phone(), person.getPhoneNumber());
        verify(personRepository, times(1)).save(person);
    }


    //Amikor a PersonService osztály updatePerson metódusát teszteljük, és a Person objektumot csak a teszten belül hozzuk létre, a valódi Person entitásnak nincs semmilyen kapcsolata az ott létrehozott objektummal. Tehát a tesztben hozzáadott Order objektumok csak a tesztben létrehozott Person objektum orders listájában lesznek, nem a valós adatbázisban lévő Person entitás orders listájában.
    //
    //Ezért az updatePerson metódus nem fogja frissíteni a valódi adatbázisban lévő Person entitás orders listáját. Ehelyett csak a teszt során létrehozott Person objektumot módosítja, amelynek semmi köze a valódi adatbázishoz.


}
