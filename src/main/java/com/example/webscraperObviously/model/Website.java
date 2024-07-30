package com.example.webscraperObviously.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class Website {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
}