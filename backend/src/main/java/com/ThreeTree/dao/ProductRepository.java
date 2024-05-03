package com.ThreeTree.dao;

import com.ThreeTree.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.productId BETWEEN :start AND :end")
    List<Product> findProductsInRange(@Param("start") int start, @Param("end") int end);

}
