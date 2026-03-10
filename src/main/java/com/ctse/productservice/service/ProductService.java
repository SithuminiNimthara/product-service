package com.ctse.productservice.service;

import com.ctse.productservice.dto.ProductCreateRequest;
import com.ctse.productservice.dto.ProductUpdateRequest;
import com.ctse.productservice.entity.Product;

import java.util.List;

public interface ProductService {
    Product create(ProductCreateRequest req, String imageUrl);
    List<Product> list();
    Product get(Long id);
    Product update(Long id, ProductUpdateRequest req, String imageUrl);
    void delete(Long id);
}