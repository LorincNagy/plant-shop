package com.ThreeTree.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendSimpleEmail() {
        // Arrange
        String toEmail = "recipient@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act
        emailService.sendSimpleEmail(toEmail, subject, body);

        // Assert
        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
}


//A tesztben használt any(SimpleMailMessage.class) rész arra szolgál, hogy megadja a Mockito keretrendszernek, hogy a javaMailSender mock objektumnak bármilyen SimpleMailMessage objektumot elfogadhat paraméterként, amikor meghívják a send metódust a sendSimpleEmail metódusban. Ez azt jelenti, hogy a teszt nem vizsgálja a konkrét SimpleMailMessage objektum tartalmát vagy tulajdonságait, csak azt ellenőrzi, hogy az emailService a megfelelő típusú objektumot adja át a javaMailSender-nek.
//
//        Ez a megközelítés hasznos lehet abban az esetben, amikor a SimpleMailMessage objektum konkrét tartalma vagy tulajdonságai nem lényegesek a teszt szempontjából, és csak azt szeretnénk ellenőrizni, hogy a megfelelő típusú objektumot küldte el a metódus.
//
//        Ha a teszt részletesen ellenőrizni szeretné a SimpleMailMessage tartalmát vagy tulajdonságait, akkor konkrét SimpleMailMessage objektumot is használhatna a tesztben, és az ellenőrzéseket arra az objektumra alkalmazhatná.
