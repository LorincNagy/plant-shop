package com.ThreeTree.service;

import com.ThreeTree.dao.ProductRepository;
import com.ThreeTree.dto.NewProductResponse;
import com.ThreeTree.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetProducts() {
        // Test data
        Long productIdLong = 1L;
        String nameString = "Test Product";
        String skuString = "SKU001";
        String descriptionString = "Test Description";
        BigDecimal priceBigDecimal = new BigDecimal("10.99");
        Integer stockInt = 100;
        String imageString = "test_image.jpg";


        Product product1 = new Product(productIdLong, nameString, skuString, descriptionString, priceBigDecimal, stockInt, imageString);
        Product product2 = new Product(productIdLong + 1, nameString + " 2", skuString + "002", descriptionString + " 2", priceBigDecimal.add(BigDecimal.ONE), stockInt + 50, imageString + "_2");


        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(product1);
        mockProducts.add(product2);

        when(productRepository.findAll()).thenReturn(mockProducts);

        List<NewProductResponse> products = productService.getProducts();
        System.out.println(products);

        // Verify that the repository's findAll method was called
        verify(productRepository).findAll();

        // Assert the size of the returned products list
        assertEquals(2, products.size());

        // Assert the details of the returned products
        assertEquals(nameString, products.get(0).getName());
        assertEquals(skuString, products.get(0).getSku());
        assertEquals(descriptionString, products.get(0).getDescription());
        assertEquals(priceBigDecimal, products.get(0).getPrice());
        assertEquals(stockInt, products.get(0).getStock());
        assertEquals(imageString, products.get(0).getImage());
        assertEquals(productIdLong, products.get(0).getProductId());

        assertEquals(nameString + " 2", products.get(1).getName());
        assertEquals(skuString + "002", products.get(1).getSku());
        assertEquals(descriptionString + " 2", products.get(1).getDescription());
        assertEquals(priceBigDecimal.add(BigDecimal.ONE), products.get(1).getPrice());
        assertEquals(stockInt + 50, products.get(1).getStock());
        assertEquals(imageString + "_2", products.get(1).getImage());
        assertEquals(productIdLong + 1, products.get(1).getProductId());
    }

    @Test
    void saveProduct() {
    }

    @Test
    void findProductById() {
    }
}
