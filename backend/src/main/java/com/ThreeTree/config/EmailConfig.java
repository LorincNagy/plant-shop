package com.ThreeTree.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String emailPassword;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); // SMTP szerver címe (Gmail esetén)
        mailSender.setPort(587); // SMTP szerver portja (Gmail esetén)
        mailSender.setUsername(emailUsername); // Az email fiók felhasználóneve
        mailSender.setPassword(emailPassword); // Az email fiók jelszava vagy alkalmazás jelszó (biztonságosabb)

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // Ezzel a sorral megjelenítjük a debug információkat

        return mailSender;
    }
}
