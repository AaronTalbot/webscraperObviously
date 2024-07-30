package com.example.webscraperObviously.repository;

import com.example.webscraperObviously.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryName(String categoryName);
    boolean existsByUrl(String url);
    Optional<Product> findFirstByCategoryNameOrderByLastUpdatedDesc(String categoryName);
}
