package com.codecrafters.productmanagement.services.implementations;

import com.codecrafters.productmanagement.dtos.ProductRequest;
import com.codecrafters.productmanagement.dtos.ProductResponse;
import com.codecrafters.productmanagement.entities.Product;
import com.codecrafters.productmanagement.exceptions.ProductNotFoundException;
import com.codecrafters.productmanagement.mappers.ProductMapper;
import com.codecrafters.productmanagement.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductById_ProductExists() {
        // Arrange
        Product product = new Product(1L, "Laptop", "A gaming laptop", 1200.0, 10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Laptop");
        productResponse.setPrice(1200.0);
        productResponse.setQuantity(10);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(productResponse, result);
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_ProductNotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.getById(1L));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateProduct() {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Laptop");
        productRequest.setDescription("A gaming laptop");
        productRequest.setPrice(1200.0);
        productRequest.setQuantity(10);

        Product product = new Product();
        when(productMapper.toEntity(productRequest)).thenReturn(product);

        when(productRepository.save(product)).thenReturn(product);

        // Act
        productService.create(productRequest);

        // Assert
        verify(productRepository, times(1)).save(product);
    }


    @Test
    void testUpdateProduct_ProductExists() {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Laptop");
        productRequest.setDescription("A gaming laptop");
        productRequest.setPrice(1200.0);
        productRequest.setQuantity(10);

        Product existingProduct = new Product(1L, "Mouse", "A gaming Mouse", 89.99, 14);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        Product product = new Product(1L, "Laptop", "A gaming laptop", 1200.0, 10);
        when(productRepository.save(any())).thenReturn(product);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Laptop");
        productResponse.setDescription("A gaming laptop");
        productResponse.setPrice(1200.0);
        productResponse.setQuantity(10);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.update(1L, productRequest);

        // Assert
        assertNotNull(result);
        assertEquals(productResponse, result);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);

    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.update(1L, any()));
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(0)).save(any());

    }

    @Test
    void testDelete_ProductExists() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);

        // Act
        productService.delete(1L);

        // Assert
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);

    }

    @Test
    void testDelete_ProductNotFound() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.delete(1L));
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(0)).deleteById(1L);
    }
}