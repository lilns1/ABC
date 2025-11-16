package org.example.abc.repository;

import org.example.abc.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    Page<Product> findByCategory(Product.Category cat, Pageable pageable);

    // 综合查询：使用 JPQL + 动态条件
    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR p.name LIKE %:keyword%) AND " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:inStock IS NULL OR " +
            "(:inStock = true AND p.stock > 0) OR " +
            "(:inStock = false AND p.stock <= 0))")
    Page<Product> searchProducts(
            @Param("keyword") String keyword,
            @Param("category") Product.Category category,
            @Param("minPrice") Float minPrice,
            @Param("maxPrice") Float maxPrice,
            @Param("inStock") Boolean inStock,
            Pageable pageable
    );
}