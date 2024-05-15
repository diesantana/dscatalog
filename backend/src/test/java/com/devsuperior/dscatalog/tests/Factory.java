package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {
    
    public static Product createProduct() {
        Product product =  new Product("Phone", 1L, 800.0, "Good Phone", "https://raw.githubusercontent.com/img/1-big.jpg", Instant.parse("2020-05-15T15:00:00Z"));
        product.getCategories().add(new Category(2L, "Eletrônicos"));
        return  product;
    }
    
    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product);
    }
}
