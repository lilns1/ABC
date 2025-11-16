package org.example.abc.controller;

import org.example.abc.model.ProductImage;
import org.example.abc.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-images")
@CrossOrigin(origins = "*")
public class ProductImageController {

    @Autowired
    private ProductImageRepository productImageRepository;

    // 查询某个商品的所有图片
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductImage>> getProductImagesByProductId(@PathVariable Integer productId) {
        List<ProductImage> images = productImageRepository.findByProductId(productId);
        return ResponseEntity.ok(images);
    }

    // 根据 ID 查询单个图片
    @GetMapping("/{id}")
    public ResponseEntity<ProductImage> getProductImageById(@PathVariable Integer id) {
        Optional<ProductImage> image = productImageRepository.findById(id);
        return image.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}