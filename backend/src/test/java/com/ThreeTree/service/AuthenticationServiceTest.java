package com.ThreeTree.service;

import com.ThreeTree.auth.AuthenticationRequest;
import com.ThreeTree.auth.AuthenticationResponse;
import com.ThreeTree.auth.AuthenticationService;
import com.ThreeTree.auth.RegisterRequest;
import com.ThreeTree.config.JwtService;
import com.ThreeTree.dao.CartRepository;
import com.ThreeTree.dao.PersonRepository;
import com.ThreeTree.model.Cart;
import com.ThreeTree.model.Person;
import com.ThreeTree.model.Role;
import com.ThreeTree.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailService emailService;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_Success() {
        // Arrange
        RegisterRequest request = new RegisterRequest("John", "Doe", "john@example.com", "password", "1234567890", "New Address");
        Person person = new Person();
        person.setEmail(request.getEmail());

        // Mock personRepository.findByEmail(...) hívás
        when(personRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Mock passwordEncoder.encode(...) hívás
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // Mock jwtService.generateToken(...) hívás
        when(jwtService.generateToken(any(Person.class))).thenReturn("jwtToken");

        // Act
        AuthenticationResponse response = authenticationService.register(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getToken());

        // Ellenőrizzük, hogy a personRepository.save(...) és cartRepository.save(...) metódusokat pontosan egyszer hívtuk-e
        verify(personRepository, times(1)).save(any(Person.class));
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testRegister_EmailAlreadyExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest("John", "Doe", "john@example.com", "password", "1234567890", "New Address");
        when(personRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new Person()));

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> authenticationService.register(request));
    }

    @Test
    public void testAuthenticate_Success() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("john@example.com", "password");
        Person person = new Person();
        person.setEmail(request.getEmail());
        when(personRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(person));
        when(jwtService.generateToken(person)).thenReturn("jwtToken");

        // Act
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getToken());
    }

    @Test
    public void testAuthenticate_InvalidCredentials() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("john@example.com", "password");
        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(request));
    }
}
