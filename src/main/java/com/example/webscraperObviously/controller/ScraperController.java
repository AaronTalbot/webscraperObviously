package com.example.webscraperObviously.controller;

import com.example.webscraperObviously.service.ScraperAsosService;
import com.example.webscraperObviously.service.ScraperDebenhamsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scrapers")
public class ScraperController {

    private static final Logger logger = LoggerFactory.getLogger(ScraperController.class);

    private final ScraperAsosService scraperAsosService;
    private final ScraperDebenhamsService scraperDebenhamsService;

    public ScraperController(ScraperAsosService scraperAsosService, ScraperDebenhamsService scraperDebenhamsService) {
        this.scraperAsosService = scraperAsosService;
        this.scraperDebenhamsService = scraperDebenhamsService;
    }

    /**
     * Initiates scraping for all websites and categories.
     *
     * @return ResponseEntity with a success message.
     */
    @PostMapping("/scrape")
    public ResponseEntity<String> scrapeAll() {
        try {
            logger.info("Initiating scraping for all websites and categories.");
            scraperAsosService.scrapeAll();
            scraperDebenhamsService.scrapeAll();
            return ResponseEntity.ok("Scraping initiated for all websites and categories");
        } catch (Exception e) {
            logger.error("Error occurred while scraping all websites and categories", e);
            return ResponseEntity.status(500).body("Error occurred while scraping all websites and categories");
        }
    }

    /**
     * Initiates scraping for ASOS website.
     *
     * @return ResponseEntity with a success message.
     */
    @PostMapping("/scrape/asos")
    public ResponseEntity<String> scrapeAsos() {
        try {
            logger.info("Initiating scraping for ASOS.");
            scraperAsosService.scrapeAll();
            return ResponseEntity.ok("Scraping initiated for Scraper A");
        } catch (Exception e) {
            logger.error("Error occurred while scraping ASOS", e);
            return ResponseEntity.status(500).body("Error occurred while scraping ASOS");
        }
    }

    /**
     * Initiates scraping for men's products on ASOS.
     *
     * @return ResponseEntity with a success message.
     */
    @PostMapping("/scrape/asos/mens")
    public ResponseEntity<String> scrapeAsosMens() {
        try {
            logger.info("Initiating scraping for ASOS men's products.");
            scraperAsosService.scrapeSex("male");
            return ResponseEntity.ok("Scraping initiated for Scraper A men's products");
        } catch (Exception e) {
            logger.error("Error occurred while scraping ASOS men's products", e);
            return ResponseEntity.status(500).body("Error occurred while scraping ASOS men's products");
        }
    }

    /**
     * Initiates scraping for women's products on ASOS.
     *
     * @return ResponseEntity with a success message.
     */
    @PostMapping("/scrape/asos/womens")
    public ResponseEntity<String> scrapeAsosWomens() {
        try {
            logger.info("Initiating scraping for ASOS women's products.");
            scraperAsosService.scrapeSex("female");
            return ResponseEntity.ok("Scraping initiated for Scraper A women's products");
        } catch (Exception e) {
            logger.error("Error occurred while scraping ASOS women's products", e);
            return ResponseEntity.status(500).body("Error occurred while scraping ASOS women's products");
        }
    }

    /**
     * Initiates scraping for Debenhams website.
     *
     * @return ResponseEntity with a success message.
     */
    @PostMapping("/scrape/debenhams")
    public ResponseEntity<String> scrapeDebenhams() {
        try {
            logger.info("Initiating scraping for Debenhams.");
            scraperDebenhamsService.scrapeAll();
            return ResponseEntity.ok("Scraping initiated for Debenhams");
        } catch (Exception e) {
            logger.error("Error occurred while scraping Debenhams", e);
            return ResponseEntity.status(500).body("Error occurred while scraping Debenhams");
        }
    }
    /**
     * Initiates scraping for electricals products on Debenhams.
     *
     * @return ResponseEntity with a success message.
     */
    @PostMapping("/scrape/debenhams/electricals")
    public ResponseEntity<String> scrapeDebenhamsElectricals() {
        try {
            logger.info("Initiating scraping for Debenhams electricals products.");
            scraperDebenhamsService.scrapeCategory("electricals");
            return ResponseEntity.ok("Scraping initiated for Debenhams electricals products");
        } catch (Exception e) {
            logger.error("Error occurred while scraping Debenhams electricals products", e);
            return ResponseEntity.status(500).body("Error occurred while scraping Debenhams electricals products");
        }
    }

    /**
     * Initiates scraping for holiday products on Debenhams.
     *
     * @return ResponseEntity with a success message.
     */
    @PostMapping("/scrape/debenhams/holiday")
    public ResponseEntity<String> scrapeDebenhamsHoliday() {
        try {
            logger.info("Initiating scraping for Debenhams holiday products.");
            scraperDebenhamsService.scrapeCategory("holiday");
            return ResponseEntity.ok("Scraping initiated for Debenhams holiday products");
        } catch (Exception e) {
            logger.error("Error occurred while scraping Debenhams holiday products", e);
            return ResponseEntity.status(500).body("Error occurred while scraping Debenhams holiday products");
        }
    }

    /**
     * Initiates scraping for wedding products on Debenhams.
     *
     * @return ResponseEntity with a success message.
     */
    @PostMapping("/scrape/debenhams/wedding")
    public ResponseEntity<String> scrapeDebenhamsWedding() {
        try {
            logger.info("Initiating scraping for Debenhams wedding products.");
            scraperDebenhamsService.scrapeCategory("wedding");
            return ResponseEntity.ok("Scraping initiated for Debenhams wedding products");
        } catch (Exception e) {
            logger.error("Error occurred while scraping Debenhams wedding products", e);
            return ResponseEntity.status(500).body("Error occurred while scraping Debenhams wedding products");
        }
    }

    /**
     * Initiates scraping for sports products on Debenhams.
     *
     * @return ResponseEntity with a success message.
     */
    @PostMapping("/scrape/debenhams/sports")
    public ResponseEntity<String> scrapeDebenhamsSports() {
        try {
            logger.info("Initiating scraping for Debenhams sports products.");
            scraperDebenhamsService.scrapeCategory("sports");
            return ResponseEntity.ok("Scraping initiated for Debenhams sports products");
        } catch (Exception e) {
            logger.error("Error occurred while scraping Debenhams sports products", e);
            return ResponseEntity.status(500).body("Error occurred while scraping Debenhams sports products");
        }
    }
}
