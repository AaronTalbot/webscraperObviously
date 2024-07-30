package com.example.webscraperObviously.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public abstract class ScraperService {

    protected static final Logger logger = LoggerFactory.getLogger(ScraperService.class);
    protected final ProductService productService;

    protected ScraperService(ProductService productService) {
        this.productService = productService;
        logger.info("ScraperService initialized with ProductService.");
    }

    /**
     * Logs the scraping action.
     *
     * @param message the message to log
     */
    protected void logScrapingAction(String message) {
        logger.info(message);
    }

    /**
     * Logs the scraping error.
     *
     * @param message the error message
     * @param exception the exception to log
     */
    protected void logScrapingError(String message, Exception exception) {
        logger.error(message, exception);
    }
}
