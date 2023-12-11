package com.ThreeTree.dao;

import com.ThreeTree.model.Person;
import com.ThreeTree.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Initializer {

    private final ProductRepository productRepository;
    private final PersonRepository personRepository;

    @Autowired
    public Initializer(ProductRepository productRepository, PersonRepository personRepository) {
        this.productRepository = productRepository;
        this.personRepository = personRepository;
    }

    @Transactional
    public void init() {
        createPerson("John", "Doe", "password", "shdvcjhcs", "123456789", "Budapest");
        createProduct("Cactus", "A small and prickly cactus plant", new BigDecimal("10.99"), 100, "https://images.unsplash.com/photo-1622599806389-9c6e0eb0fcec?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2940&q=80");
        createProduct("Spider Plant", "An easy-to-care-for indoor plant", new BigDecimal("12.99"), 80, "https://images.unsplash.com/photo-1610551724444-5fb527b5ad20?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2615&q=80");
        createProduct("Succulent", "A collection of beautiful succulent plants", new BigDecimal("15.99"), 120, "https://images.unsplash.com/photo-1463320898484-cdee8141c787?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2940&q=80");
        createProduct("Peace Lily", "A classic indoor plant with white flowers", new BigDecimal("14.99"), 90, "https://images.unsplash.com/photo-1652731706545-901f1026da4d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2787&q=80");
        createProduct("Snake Plant", "A hardy and low-maintenance plant", new BigDecimal("11.99"), 110, "https://images.unsplash.com/photo-1616961002389-504228edfcb7?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fFNuYWtlJTIwUGxhbnR8ZW58MHx8MHx8fDA%3D&auto=format&fit=crop&w=800&q=60");
        createProduct("Rubber Plant", "A popular indoor plant with glossy leaves", new BigDecimal("16.99"), 70, "https://images.unsplash.com/photo-1616132803693-958f93bffb5b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2864&q=80");
        createProduct("Aloe Vera", "A versatile plant known for its healing properties", new BigDecimal("13.99"), 95, "https://images.unsplash.com/photo-1613372978247-de50228e8033?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2787&q=80");
        createProduct("Monstera", "A trendy houseplant with unique leaves", new BigDecimal("19.99"), 60, "https://plus.unsplash.com/premium_photo-1664015821116-218d91eb62ed?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2864&q=80");
        createProduct("Bamboo Palm", "A tropical palm plant for your home", new BigDecimal("18.99"), 75, "https://images.unsplash.com/photo-1558543942-5cb506ae9707?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2787&q=80");
        createProduct("Bamboo Palm", "A tropical palm plant for your home", new BigDecimal("18.99"), 75, "https://images.unsplash.com/photo-1558543942-5cb506ae9707?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2787&q=80");
        createProduct("Rose", "Classic and romantic, perfect for gardens", new BigDecimal("15.99"), 90, "https://images.unsplash.com/photo-1559563362-c667ba5f5480?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8cm9zZXxlbnwwfHwwfHx8MA%3D%3D&w=800");
        createProduct("Lavender", "Fragrant herb used in aromatherapy and cooking", new BigDecimal("9.99"), 75, "https://images.unsplash.com/photo-1604695324248-d8f54a5ae30e?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTQ4fHxsYXZlbmRlcnxlbnwwfHwwfHx8MA%3D%3D&w=800");
        createProduct("Sunflower", "Bright and cheerful flowers, symbolizing happiness", new BigDecimal("12.49"), 85, "https://images.unsplash.com/photo-1512238701577-f182d9ef8af7?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8N3x8c3VuZmxvd2VyfGVufDB8fDB8fHww&w=800");
        createProduct("Bonsai Tree", "Miniature trees cultivated in pots, an art form in itself", new BigDecimal("29.99"), 50, "https://images.unsplash.com/photo-1539176170444-67d6c8f356d1?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTZ8fGJvbnNhaXxlbnwwfHwwfHx8MA%3D%3D&w=800");
        createProduct("Orchid", "Elegant and exotic, known for its unique flowers", new BigDecimal("18.99"), 60, "https://images.unsplash.com/photo-1618080578815-335456280012?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTh8fG9yY2hpZHxlbnwwfHwwfHx8MA%3D%3D&w=800");
        createProduct("Fiddle Leaf Fig", "Trendy indoor plant with large, violin-shaped leaves", new BigDecimal("24.99"), 40, "https://images.unsplash.com/photo-1503149779833-1de50ebe5f8a?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MzR8fEZJRERMRSUyMExFQUYlMjBGSUd8ZW58MHx8MHx8fDA%3D&w=800");
        createProduct("Tulip", "Colorful spring flowers, signifying perfect love", new BigDecimal("8.99"), 100, "https://images.unsplash.com/photo-1624696503378-069979d46b71?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OHx8dHVsaXB8ZW58MHx8MHx8fDA%3D&w=800");
        createProduct("African Violet", "Small indoor plants with vibrant purple flowers", new BigDecimal("14.49"), 65, "https://images.unsplash.com/photo-1674542923980-a390925e17e6?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTJ8fGFmcmljYW4lMjB2aW9sZXR8ZW58MHx8MHx8fDA%3D&w=800");
        createProduct("Jasmine", "Fragrant flowers used in teas and perfumes", new BigDecimal("11.99"), 70, "https://images.unsplash.com/photo-1627389232316-88126bb63d3c?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MzF8fGphc21pbmV8ZW58MHx8MHx8fDA%3D&w=800");
        createProduct("Daisy", "Simple and cheerful flowers, symbolizing purity", new BigDecimal("7.99"), 110, "https://images.unsplash.com/photo-1606041008023-472dfb5e530f?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8ZGFpc3l8ZW58MHx8MHx8fDA%3D&w=800");
        createProduct("Succulent Variety Pack", "Assorted collection of low-maintenance succulents", new BigDecimal("19.99"), 30, "https://images.unsplash.com/photo-1508692360355-77f7802ac548?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjR8fHN1Y2N1bGVudCUyMHZhcmlldHl8ZW58MHx8MHx8fDA%3D&w=800");
        createProduct("Bird of Paradise", "Large tropical plant with banana-like leaves", new BigDecimal("32.99"), 25, "https://images.unsplash.com/photo-1618975499114-2e851f121468?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fGJpcmQlMjBvZiUyMHBhcmFkaXNlfGVufDB8fDB8fHww&w=800");
        createProduct("Pothos", "Popular indoor plant with heart-shaped leaves", new BigDecimal("13.49"), 85, "https://images.unsplash.com/photo-1622515658990-017acdeeade3?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTZ8fHBvdGhvc3xlbnwwfHwwfHx8MA%3D%3D&w=800");
        createProduct("Areca Palm", "Air-purifying palm tree, perfect for indoors", new BigDecimal("27.99"), 35, "https://images.unsplash.com/photo-1498735599329-ed6e3798cdcb?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MzV8fGFyZWNhJTIwcGFsbXxlbnwwfHwwfHx8MA%3D%3D&w=800");
        createProduct("Money Tree", "Believed to bring good luck and prosperity", new BigDecimal("22.99"), 45, "https://images.unsplash.com/photo-1443948308135-d57fc66de368?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nzh8fG1vbmV5JTIwdHJlZXxlbnwwfHwwfHx8MA%3D%3D&w=800");
        createProduct("Carnation", "Fragrant flowers often used in bouquets", new BigDecimal("9.49"), 95, "https://plus.unsplash.com/premium_photo-1677178631517-9da32f2001b8?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NTN8fGNhcm5hdGlvbnxlbnwwfHwwfHx8MA%3D%3D&w=800");
        createProduct("Peace Lily", "Classic indoor plant with white flowers", new BigDecimal("16.99"), 55, "https://images.unsplash.com/photo-1593691509543-c55fb32d8de5?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8cGVhY2UlMjBsaWx5fGVufDB8fDB8fHww&w=800");
        createProduct("Bamboo Plant", "Symbolizes luck and good fortune in many cultures", new BigDecimal("14.99"), 65, "https://images.unsplash.com/photo-1585155844510-de0df7f65c6f?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8N3x8YmFtYm9vJTIwcGxhbnR8ZW58MHx8MHx8fDA%3D&w=800");
        createProduct("Foxglove", "Tall, bell-shaped flowers, adds charm to gardens", new BigDecimal("11.49"), 40, "https://images.unsplash.com/photo-1591271358290-30f25c332dd8?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NTR8fGZveGdsb3ZlfGVufDB8fDB8fHww&w=800");
        createProduct("Chrysanthemum", "Colorful flowers, used in traditional medicine", new BigDecimal("12.99"), 75, "https://images.unsplash.com/photo-1596632654856-5835bb275a2f?auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTh8fGNocnlzYW50aGVtdW18ZW58MHx8MHx8fDA%3D&w=800");

    }

    private void createProduct(String name, String description, BigDecimal price, int stock, String image) {
        Product product = new Product();
        product.setName(name);
        product.setSku(generateSku());
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setImage(image);

        productRepository.save(product);
    }


    public void createPerson(String firstName, String lastName, String password, String email, String phoneNumber, String address) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setPassword(password);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
        person.setAddress(address);

        personRepository.save(person);
    }

    private String generateSku() {
        return "SKU-" + System.currentTimeMillis();
    }
}
