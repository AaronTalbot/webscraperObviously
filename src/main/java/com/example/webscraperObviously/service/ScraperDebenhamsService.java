package com.example.webscraperObviously.service;

import com.example.webscraperObviously.scrapers.DebenhamsScraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ScraperDebenhamsService extends ScraperService {

    private static final Logger logger = LoggerFactory.getLogger(ScraperDebenhamsService.class);
    private final DebenhamsScraper debenhamsScraper;

    public ScraperDebenhamsService(ProductService productService) {
        super(productService);
        this.debenhamsScraper = new DebenhamsScraper(productService);
    }

    /**
     * Initiates scraping of all products from Debenhams.
     */
    public void scrapeAll() {
        logger.info("Initiating scraping of all products from Debenhams.");
        debenhamsScraper.getAllProducts();
    }

    /**
     * Initiates scraping of products from a specified category from Debenhams.
     *
     * @param category the category to scrape products from
     */
    public void scrapeCategory(String category) {
        logger.info("Initiating scraping of {} category products from Debenhams.", category);
        debenhamsScraper.getProductsBasedOnCategory(category);
    }

    /**
     * Initiates scraping of products based on sex from Debenhams.
     *
     * @param sex the sex to scrape products for
     */
    public void scrapeSex(String sex) {
        logger.info("Initiating scraping of {} products from Debenhams.", sex);
        debenhamsScraper.getProductsBasedOnSex(sex);
    }
}
