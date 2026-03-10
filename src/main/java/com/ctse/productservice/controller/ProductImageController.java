package com.ctse.productservice.controller;

import com.ctse.productservice.entity.Product;
import com.ctse.productservice.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductImageController {

    private final ProductRepository repo;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.base-url}")
    private String baseUrl;

    public ProductImageController(ProductRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadImage(@PathVariable Long id,
                                         @RequestParam("file") MultipartFile file) throws Exception {

        Product product = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "File is empty"));
        }

        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (ext != null ? "." + ext : "");

        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        Path path = Paths.get(uploadDir).resolve(filename);
        Files.write(path, file.getBytes());

        String imageUrl = baseUrl + "/uploads/" + filename;
        product.setImageUrl(imageUrl);
        repo.save(product);

        return ResponseEntity.ok(Map.of("message", "Image uploaded", "imageUrl", imageUrl));
    }
}