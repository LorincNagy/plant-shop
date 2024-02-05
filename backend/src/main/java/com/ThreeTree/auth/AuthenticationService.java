package com.ThreeTree.auth;

import com.ThreeTree.config.JwtService;
import com.ThreeTree.dao.CartRepository;
import com.ThreeTree.dao.PersonRepository;
import com.ThreeTree.model.Cart;
import com.ThreeTree.model.Person;
import com.ThreeTree.model.Role;
import com.ThreeTree.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder; // TODO: has to be final to be picked up by constructor
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final CartRepository cartRepository;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        Person existingPerson = personRepository.findByEmail(request.getEmail()).orElse(null);

        if (existingPerson != null) {
            throw new DataIntegrityViolationException("This email address is already exists.");
        }

        Person person = new Person();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());


        String encodedPassword = passwordEncoder.encode(request.getPassword());
        person.setPassword(encodedPassword);

        person.setEmail(request.getEmail());
        person.setPhoneNumber(request.getPhoneNumber());
        person.setAddress(request.getAddress());
        person.setRole(Role.USER);

        Cart cart = new Cart();
        person.setCart(cart);
        cart.setPerson(person);
        personRepository.save(person);
        cartRepository.save(cart);


        var jwtToken = jwtService.generateToken(person);
        sendRegistrationConfirmationEmail(person);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    private void sendRegistrationConfirmationEmail(Person person) {
        emailService.sendSimpleEmail(person.getEmail(), "Successful Registration", "Dear " + person.getFirstName() + "! You have successfully registered in our application.");
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var person = personRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(person);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();


    }
}


