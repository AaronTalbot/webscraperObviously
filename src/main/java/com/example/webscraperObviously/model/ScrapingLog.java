package com.example.webscraperObviously.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class ScrapingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime scrapedAt;
    private String status;
    @ManyToOne
    @JoinColumn(name = "website_id")
    private Website website;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}