package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj.id = :id")
    Optional<Product> searchProductWithCategoriesById(Long id);

    @Query(value = "SELECT obj FROM Product obj JOIN FETCH obj.categories", 
            countQuery = "SELECT COUNT(obj) FROM Product obj JOIN obj.categories")
    Page<Product> searchAll(Pageable pageable);
    
}
