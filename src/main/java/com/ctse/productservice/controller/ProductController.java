package com.ctse.productservice.controller;

import com.ctse.productservice.dto.ProductCreateRequest;
import com.ctse.productservice.dto.ProductUpdateRequest;
import com.ctse.productservice.entity.Product;
import com.ctse.productservice.service.FileStorageService;
import com.ctse.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;
    private final FileStorageService fileStorageService;

    public ProductController(ProductService service, FileStorageService fileStorageService) {
        this.service = service;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("service", "product-service", "status", "running");
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Product> create(
            @Valid @ModelAttribute ProductCreateRequest req,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {
        String imageUrl = fileStorageService.saveFile(file);
        return ResponseEntity.ok(service.create(req, imageUrl));
    }

    @GetMapping
    public List<Product> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public Product update(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductUpdateRequest req,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {
        String imageUrl = fileStorageService.saveFile(file);
        return service.update(id, req, imageUrl);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        service.delete(id);
        return Map.of("message", "Product deleted successfully");
    }
}