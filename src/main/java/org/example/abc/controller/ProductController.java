package org.example.abc.controller;

import org.example.abc.model.Product;
import org.example.abc.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // 分页查询所有商品（含图片）
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(pageable);
        return ResponseEntity.ok(products);
    }

    // 根据 ID 查询单个商品
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 按分类查询商品
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<Product>> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Product.Category cat = Product.Category.valueOf(category.toUpperCase());
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> products = productRepository.findByCategory(cat, pageable);
            return ResponseEntity.ok(products);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 1. 安全校验：category 转枚举
        Product.Category cat = null;
        if (category != null && !category.trim().isEmpty()) {
            String catStr = category.trim();
            cat = Arrays.stream(Product.Category.values())
                    .filter(c -> c.name().equalsIgnoreCase(catStr))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("无效的商品分类: " + category));
        }

        // 2. 价格范围校验（可选增强）
        if (minPrice != null && minPrice < 0) minPrice = 0f;
        if (maxPrice != null && maxPrice < 0) maxPrice = null;
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            float temp = minPrice;
            minPrice = maxPrice;
            maxPrice = temp;
        }

        // 3. 分页限制（防刷）
        size = Math.min(size, 50);

        // 4. 查询
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.searchProducts(
                keyword, cat, minPrice, maxPrice, inStock, pageable
        );

        return ResponseEntity.ok(products);
    }
}