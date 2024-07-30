package com.example.webscraperObviously.service;

import com.example.webscraperObviously.scrapers.AsosScraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ScraperAsosService extends ScraperService {

    private static final Logger logger = LoggerFactory.getLogger(ScraperAsosService.class);
    private final AsosScraper asosScraper;

    public ScraperAsosService(ProductService productService) {
        super(productService);
        this.asosScraper = new AsosScraper(productService);
    }

    /**
     * Initiates scraping of all products from ASOS.
     */
    public void scrapeAll() {
        logger.info("Initiating scraping of all products from ASOS.");
        asosScraper.scrapeAll();
    }

    /**
     * Initiates scraping of products based on sex from ASOS.
     *
     * @param sex the sex to scrape products for
     */
    public void scrapeSex(String sex) {
        logger.info("Initiating scraping of {} products from ASOS.", sex);
        asosScraper.scrapeBasedOnSex(sex);
    }
}
