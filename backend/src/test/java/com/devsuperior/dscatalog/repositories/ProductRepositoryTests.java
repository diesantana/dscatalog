package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        countTotalProducts = 25;
        nonExistingId = 100L;
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
    
    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        Product product = Factory.createProduct();
        product.setId(null);
        
        product = repository.save(product);
        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1L, product.getId());
    }
    

//    findById deveria retornar um Optional<Product> não vazio quando o id existir
    @Test
    public void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
        
        Optional<Product> result = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }
    
//    findById deveria retornar um Optional<Product> vazio quando o id não existir
    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
    
        Optional<Product> result = repository.findById(nonExistingId);
        Assertions.assertTrue(result.isEmpty());
    }
    
}
