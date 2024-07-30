package com.example.webscraperObviously.model;


import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String attributeName;
    private String attributeValue;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}