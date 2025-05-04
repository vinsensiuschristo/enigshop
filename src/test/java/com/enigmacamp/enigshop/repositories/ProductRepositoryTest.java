package com.enigmacamp.enigshop.repositories;

import com.enigmacamp.enigshop.models.entities.Product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    ProductRepository productRepository;

    @Test
    void saveProductSuccess() {
        Product product = Product.builder()
                .name("Product 1")
                .description("Product 1 Description")
                .price(1000L)
                .stock(10)
                .build();
        Product savedProduct = productRepository.save(product);

        Product dbProduct = entityManager.find(Product.class, savedProduct.getId());

        assertNotNull(dbProduct);
        assertEquals(savedProduct, dbProduct);
    }

    @Test
    void findByNameContainingIgnoreCase() {
        Product product1 = Product.builder()
                .name("Product 1")
                .description("Product 1 Description")
                .price(1000L)
                .stock(10)
                .build();
        Product product2 = Product.builder()
                .name("Product 2")
                .description("Product 2 Description")
                .price(2000L)
                .stock(20)
                .build();
        Product product3 = Product.builder()
                .name("Product 3")
                .description("Product 3 Description")
                .price(3000L)
                .stock(30)
                .build();
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        String keyword = "product";
        var products = productRepository.findByNameContainingIgnoreCase(keyword);

        assertFalse(products.isEmpty());
        assertTrue(products.stream().allMatch(product -> product.getName().toLowerCase().contains(keyword.toLowerCase())));

    }

}