package com.ThreeTree.service;

import com.ThreeTree.dao.ProductRepository;
import com.ThreeTree.dto.NewProductResponse;
import com.ThreeTree.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;


    public List<NewProductResponse> getProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> new NewProductResponse(
                        product.getName(),
                        product.getSku(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStock(),
                        product.getImage(),
                        product.getProductId()))
                .collect(Collectors.toList());
    }


    public void saveProduct(Product product) {
        productRepository.save(product);
    }


    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
    }


    public NewProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));

        return new NewProductResponse(
                product.getName(),
                product.getSku(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImage(),
                product.getProductId()
        );
    }

    public List<NewProductResponse> findProductsInRange(int start, int end) {
        List<Product> products = productRepository.findProductsInRange(start, end);
        return convertToNewProductResponseList(products);
    }

    private List<NewProductResponse> convertToNewProductResponseList(List<Product> products) {
        return products.stream()
                .map(product -> new NewProductResponse(
                        product.getName(),
                        product.getSku(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStock(),
                        product.getImage(),
                        product.getProductId()))
                .collect(Collectors.toList());
    }
}
