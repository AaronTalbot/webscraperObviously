package com.example.webscraperObviously.service;

import com.example.webscraperObviously.model.Product;
import com.example.webscraperObviously.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return a list of all products
     */
    public List<Product> getAllProducts() {
        logger.info("Fetching all products from the database.");
        return productRepository.findAll();
    }

    /**
     * Retrieves products by category name.
     *
     * @param categoryName the name of the category
     * @return a list of products in the specified category
     */
    public List<Product> getProductsByCategory(String categoryName) {
        logger.info("Fetching products by category: {}", categoryName);
        return productRepository.findByCategoryName(categoryName);
    }

    /**
     * Saves a product to the database.
     *
     * @param product the product to save
     * @return the saved product
     */
    @Transactional
    public Product saveProduct(Product product) {
        logger.info("Saving product with SKU: {} to the database.", product.getSku());
        return productRepository.save(product);
    }

    /**
     * Checks if a product exists by its URL.
     *
     * @param url the URL of the product
     * @return true if the product exists, false otherwise
     */
    public boolean existsByUrl(String url) {
        logger.info("Checking existence of product with URL: {}", url);
        return productRepository.existsByUrl(url);
    }

    /**
     * Checks if the data for a given category is fresh (updated within the last day).
     *
     * @param categoryName the name of the category
     * @return true if the data is fresh, false otherwise
     */
    public boolean isDataFresh(String categoryName) {
        logger.info("Checking if data for category: {} is fresh.", categoryName);
        LocalDateTime threshold = LocalDateTime.now().minusDays(1);
        return productRepository.findFirstByCategoryNameOrderByLastUpdatedDesc(categoryName)
                .map(product -> product.getLastUpdated().isAfter(threshold))
                .orElse(false);
    }
}
