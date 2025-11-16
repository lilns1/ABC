package org.example.abc.repository;

import org.example.abc.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    Page<Product> findByCategory(Product.Category cat, Pageable pageable);
}