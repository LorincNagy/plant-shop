package com.ThreeTree.controller;

import com.ThreeTree.dto.NewPersonRequest;
import com.ThreeTree.model.Person;
import com.ThreeTree.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/customers")
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> missingPerson(NoSuchElementException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public List<Person> getCustomers() {
        return personService.getCustomers();
    }

    @GetMapping("/{customerId}")
    public Person getCustomerById(@PathVariable("customerId") Long id) {
        return personService.getCustomerById(id);
    }

    @PostMapping
    public void addCustomer(@RequestBody NewPersonRequest request) {
        personService.saveCustomer(request);
    }

    @PutMapping("/{customerId}")
    public void updateCustomerById(@PathVariable("customerId") Long id, @RequestBody Person person) {
        personService.updateCustomerById(id, person);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomerById(@PathVariable("customerId") Long id) {
        personService.deleteCustomerById(id);
    }

}
