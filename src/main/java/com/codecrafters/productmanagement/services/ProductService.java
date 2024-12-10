package com.codecrafters.productmanagement.services;

import com.codecrafters.productmanagement.dtos.ProductRequest;
import com.codecrafters.productmanagement.dtos.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAll();

    ProductResponse getById(Long id);

    ProductResponse create(ProductRequest productRequest);

    ProductResponse update(Long id, ProductRequest productRequest);

    void delete(Long id);

}
