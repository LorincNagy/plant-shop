package com.ThreeTree.service;

import com.ThreeTree.dao.ProductRepository;
import com.ThreeTree.dto.NewProductResponse;
import com.ThreeTree.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;


    public List<NewProductResponse> getProducts() {
        List<Product> products = productRepository.findAll();
        List<NewProductResponse> productDTOs = new ArrayList<>();

        for (Product product : products) {
            NewProductResponse newProductResponse = new NewProductResponse(
                    product.getName(),
                    product.getSku(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getImage(),
                    product.getProductId()
            );

            productDTOs.add(newProductResponse);
        }

        return productDTOs;
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
