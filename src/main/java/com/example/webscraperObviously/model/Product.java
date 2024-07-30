package com.example.webscraperObviously.model;

import lombok.Data;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sku;
    private String title;
    private String url;
    private double price;
    private LocalDateTime lastUpdated;
    private String sex;  // optional
    @ManyToOne
    @JoinColumn(name = "website_id")
    private Website website;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}