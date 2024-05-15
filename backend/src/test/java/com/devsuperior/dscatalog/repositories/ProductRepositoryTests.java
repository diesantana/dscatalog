package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    private long existingId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
    }

    @Autowired
    private ProductRepository repository;
    
    // deletar  se o id existir
    @Test
    public void deleteShouldDeleteObjWhenIdExists() {
        repository.deleteById(existingId);
        Optional<Product> result = repository.findById(existingId);
//        Assertions.assertFalse(result.isPresent());
        Assertions.assertTrue(result.isEmpty());
    }
    
}
