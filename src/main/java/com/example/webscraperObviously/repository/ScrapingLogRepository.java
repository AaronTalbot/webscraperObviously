package com.example.webscraperObviously.repository;

import com.example.webscraperObviously.model.ScrapingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapingLogRepository extends JpaRepository<ScrapingLog, Long> {}
