package com.codecrafters.productmanagement.repositories;

import com.codecrafters.productmanagement.entities.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class ProductRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    static void setUp() {
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    }

    @Test
    void testProductCRUD() {

        Product product = new Product();
        product.setName("Laptop");
        product.setDescription("A gaming laptop");
        product.setPrice(1200.0);
        product.setQuantity(10);

        Product savedProduct = productRepository.save(product);
        Optional<Product> retrievedProduct = productRepository.findById(savedProduct.getId());

        assertTrue(retrievedProduct.isPresent());
        assertEquals(product.getId(), retrievedProduct.get().getId());
        assertEquals(product, retrievedProduct.get());

        Product requestedProduct = new Product();
        requestedProduct.setId(savedProduct.getId());
        requestedProduct.setName("Mouse");
        requestedProduct.setDescription("A gaming Mouse");
        requestedProduct.setPrice(149.99);
        requestedProduct.setQuantity(17);
        Product updatedProduct = productRepository.save(requestedProduct);

        assertEquals(requestedProduct, updatedProduct);

        productRepository.deleteById(savedProduct.getId());

        assertEquals(Optional.empty(), productRepository.findById(savedProduct.getId()));
    }

}
