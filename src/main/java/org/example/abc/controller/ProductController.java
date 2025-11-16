package org.example.abc.controller;

import org.example.abc.model.Product;
import org.example.abc.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") // 开发阶段允许跨域
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
}