package com.ctse.productservice.service;

import com.ctse.productservice.dto.ProductCreateRequest;
import com.ctse.productservice.dto.ProductUpdateRequest;
import com.ctse.productservice.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Product create(ProductCreateRequest req);
    List<Product> list();
    Product get(Long id);
    Product update(Long id, ProductUpdateRequest req);
    void delete(Long id);

    Map<String, Object> checkStock(Long productId, Integer quantity);
    Map<String, Object> reduceStock(Long productId, Integer quantity);
}