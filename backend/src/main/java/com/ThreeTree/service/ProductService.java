package com.ThreeTree.service;

import com.ThreeTree.dao.ProductRepository;
import com.ThreeTree.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;


    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }


    public void saveProduct(Product product) {
        productRepository.save(product);
    }


    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }
}
