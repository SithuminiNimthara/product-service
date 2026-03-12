package com.ctse.productservice.controller;

import com.ctse.productservice.dto.CheckStockRequest;
import com.ctse.productservice.dto.ProductCreateRequest;
import com.ctse.productservice.dto.ProductUpdateRequest;
import com.ctse.productservice.dto.ReduceStockRequest;
import com.ctse.productservice.entity.Product;
import com.ctse.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("service", "product-service", "status", "running");
    }

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody ProductCreateRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @GetMapping
    public List<Product> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        service.delete(id);
        return Map.of("message", "Product deleted successfully");
    }

    @PostMapping("/check-stock")
    public ResponseEntity<Map<String, Object>> checkStock(@Valid @RequestBody CheckStockRequest request) {
        return ResponseEntity.ok(service.checkStock(request.getProductId(), request.getQuantity()));
    }

    @PostMapping("/reduce-stock")
    public ResponseEntity<Map<String, Object>> reduceStock(@Valid @RequestBody ReduceStockRequest request) {
        return ResponseEntity.ok(service.reduceStock(request.getProductId(), request.getQuantity()));
    }
}