package com.ctse.productservice.service;

import com.ctse.productservice.dto.ProductCreateRequest;
import com.ctse.productservice.dto.ProductUpdateRequest;
import com.ctse.productservice.entity.Product;
import com.ctse.productservice.repo.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public Product create(ProductCreateRequest req) {
        if (repo.existsBySku(req.getSku())) {
            throw new IllegalArgumentException("SKU already exists: " + req.getSku());
        }

        Product p = new Product();
        p.setSku(req.getSku());
        p.setName(req.getName());
        p.setPrice(req.getPrice());
        p.setStock(req.getStock());
        p.setCategory(req.getCategory());
        p.setDescription(req.getDescription());
        p.setImageUrl(null); // image will be uploaded separately

        return repo.save(p);
    }

    @Override
    public List<Product> list() {
        return repo.findAll();
    }

    @Override
    public Product get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    @Override
    public Product update(Long id, ProductUpdateRequest req) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        product.setName(req.getName());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());
        product.setCategory(req.getCategory());
        product.setDescription(req.getDescription());

        return repo.save(product);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Product not found: " + id);
        }

        repo.deleteById(id);
    }

    @Override
    public Map<String, Object> checkStock(Long productId, Integer quantity) {
        Product product = repo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        boolean available = product.getStock() >= quantity;

        return Map.of(
                "productId", product.getId(),
                "productName", product.getName(),
                "requestedQuantity", quantity,
                "availableStock", product.getStock(),
                "available", available
        );
    }

    @Override
    public Map<String, Object> reduceStock(Long productId, Integer quantity) {
        Product product = repo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product ID: " + productId);
        }

        product.setStock(product.getStock() - quantity);
        repo.save(product);

        return Map.of(
                "message", "Stock reduced successfully",
                "productId", product.getId(),
                "productName", product.getName(),
                "reducedBy", quantity,
                "remainingStock", product.getStock()
        );
    }
}