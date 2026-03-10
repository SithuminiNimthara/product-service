package com.ctse.productservice.service;

import com.ctse.productservice.dto.ProductCreateRequest;
import com.ctse.productservice.dto.ProductUpdateRequest;
import com.ctse.productservice.entity.Product;
import com.ctse.productservice.repo.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public Product create(ProductCreateRequest req, String imageUrl) {
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
        p.setImageUrl(imageUrl);

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
    public Product update(Long id, ProductUpdateRequest req, String imageUrl) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        product.setName(req.getName());
        product.setPrice(req.getPrice());
        product.setStock(req.getStock());
        product.setCategory(req.getCategory());
        product.setDescription(req.getDescription());

        if (imageUrl != null && !imageUrl.isBlank()) {
            product.setImageUrl(imageUrl);
        }

        return repo.save(product);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Product not found: " + id);
        }

        repo.deleteById(id);
    }
}