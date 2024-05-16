package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
    
    @InjectMocks      
    private ProductService service;
    
    @Mock               
    private ProductRepository productRepository;
    
    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));

        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);
        
        // findById returna um optional com a entidade
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        
        // findById returna um optional vazio
        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        
        // findAll
        Mockito.when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
        
        //save
        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
        
        // Não fazer nada quando o id existe
        Mockito.doNothing().when(productRepository).deleteById(existingId);
        // Lançar o ResourceNotFoundException quando o id não existe
        Mockito.doThrow(ResourceNotFoundException.class).when(productRepository).deleteById(nonExistingId);
        // Lançar o DataIntegrityViolationException quando erro de integridade referencial
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }

    @Test
    // delete deve não fazer nada quando eu passar um id que existe
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
    }
    
    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           service.delete(nonExistingId); 
        });
    }
    
    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        Assertions.assertThrows(DatabaseException.class, () -> {
           service.delete(dependentId); 
        });
    }
}
