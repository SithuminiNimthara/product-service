package com.ctse.productservice.controller;

import com.ctse.productservice.entity.Product;
import com.ctse.productservice.repo.ProductRepository;
import com.ctse.productservice.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductRepository repo;
    private final CloudinaryService cloudinaryService;

    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        Product product = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "File is empty"));
        }


        String imageUrl = cloudinaryService.uploadImage(file, "product-images");
        product.setImageUrl(imageUrl);
        repo.save(product);

        return ResponseEntity.ok(Map.of(
                "message", "Image uploaded successfully",
                "productId", product.getId(),
                "imageUrl", imageUrl
        ));
    }
}