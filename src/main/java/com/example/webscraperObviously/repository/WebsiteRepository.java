package com.example.webscraperObviously.repository;

import com.example.webscraperObviously.model.Website;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsiteRepository extends JpaRepository<Website, Long> {}