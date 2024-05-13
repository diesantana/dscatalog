package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> resultEntity = productRepository.searchAll(pageable);
        return resultEntity.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = productRepository.searchProductWithCategoriesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
        return new ProductDTO(entity);
    }
    
    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product entity = new Product();
        copyDtoToEntity(productDTO, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }



    @Transactional
    public ProductDTO update(ProductDTO productDTO, Long id) {
        try {
            Product product = productRepository.getReferenceById(id);
            copyDtoToEntity(productDTO, product);
            
            product = productRepository.save(product);
            return new ProductDTO(product);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }
    
    /*  propagation = Propagation.SUPPORTS permite que o spring reconheça exceções relacionadas ao 
        banco de dados  em transações */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        // verifica se a entidade existe
        if(!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            productRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e ) { // erro de integridade referencial
            throw new DatabaseException("Falha de integridade referencial");         
        }
    }

    // Copia as propriedades do DTO para a entidade
    private void copyDtoToEntity(ProductDTO productDTO, Product entity) {
        entity.setName(productDTO.getName());
        entity.setDescription(productDTO.getDescription());
        entity.setPrice(productDTO.getPrice());
        entity.setImgUrl(productDTO.getImgUrl());
        entity.setDate(productDTO.getDate());
        for(CategoryDTO catDTO : productDTO.getCategories()) {
            Category category = categoryRepository.getReferenceById(catDTO.getId());
            entity.getCategories().add(category);
        }
    }

}

