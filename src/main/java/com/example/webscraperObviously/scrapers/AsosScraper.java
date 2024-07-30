package com.example.webscraperObviously.scrapers;

import com.example.webscraperObviously.model.Category;
import com.example.webscraperObviously.model.Product;
import com.example.webscraperObviously.service.ProductService;
import com.google.gson.JsonObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AsosScraper extends BaseScraper {

    private static final Logger logger = LoggerFactory.getLogger(AsosScraper.class);
    private static final String BASE_URL = "https://www.asos.com/";
    private final ProductService productService;

    public AsosScraper(ProductService productService) {
        this.productService = productService;
        this.initializeDriver();
    }

    @Override
    public void initializeDriver() {
        driver = new ChromeDriver();
        driver.manage().window().maximize(); // Asos needs maximise window screen to show navigation bar
    }

    /**
     * Scrapes the landing page of the specified URL.
     *
     * @param landingURL the URL of the landing page to scrape.
     */
    private void scrapeLandingPage(String landingURL) {
        Document document = getDocument(landingURL);
        logger.info("Scraping landing page: {}", landingURL);

        // Add an explicit wait
        try {
            Thread.sleep(5000);  // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            logger.error("Error occurred while waiting", e);
            throw new RuntimeException(e);
        }

        Elements productTypeElements = document.getElementsByClass("c2oEXGw");
        logger.debug("Product type elements: {}", productTypeElements);
    }

    /**
     * Scrapes the specified category.
     *
     * @param categoryUrl the URL of the category to scrape.
     * @param category the name of the category.
     */
    private void scrapeCategory(String categoryUrl, String category) {
        driver.get(categoryUrl);
        Document document = getDocument(categoryUrl);
        Elements allProducts = new Elements();
        logger.info("Scraping category: {}", category);

        while (true) {
            try {
                WebElement loadMoreButton = driver.findElement(By.cssSelector("a.loadButton_wWQ3F[data-auto-id=\"loadMoreProducts\"]"));

                // Scroll into view
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loadMoreButton);
                Elements products = document.getElementsByClass("productTile_U0clN");
                allProducts.addAll(products);
                loadMoreButton.click();

                // Wait for new content to load
                Thread.sleep(2000);
            } catch (Exception e) {
                logger.debug("No more 'Load More' buttons found.");
                break;
            }
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error("Error occurred while waiting", e);
            throw new RuntimeException(e);
        }

        for (Element product : allProducts) {
            String productUrl = product.select("a").attr("href");

            // Convert to absolute URL if necessary
            productUrl = makeAbsoluteUrl(categoryUrl, productUrl);

            // Direct call to scrapeProduct for debugging
            scrapeProduct(productUrl, category);
        }
    }

    /**
     * Scrapes the specified product.
     *
     * @param productUrl the URL of the product to scrape.
     * @param category the category of the product.
     */
    private void scrapeProduct(String productUrl, String category) {
        try {
            Document productDocument = getDocument(productUrl);
            logger.info("Scraping product URL: {}", productUrl);

            Element scriptElement = productDocument.getElementById("split-structured-data");

            String jsonLd = scriptElement.html();
            JsonObject productData = parseJsonLd(jsonLd);

            String title = productData.get("name").getAsString();
            String sku = productData.get("sku").getAsString();
            JsonObject offers = productData.getAsJsonObject("offers");
            double price = offers.get("lowPrice").getAsDouble();

            if (title == null || sku == null || price <= 0.0) {
                logger.error("Missing required product data for URL: {}", productUrl);
                logger.debug("Title: {}, SKU: {}, Price: {}", title, sku, price);
                return;
            }

            Product product = new Product();
            product.setTitle(title);
            product.setSku(sku);
            product.setPrice(price);
            product.setUrl(productUrl);
            Category cat = new Category();
            cat.setName(category);
            product.setCategory(cat);
            productService.saveProduct(product);
        } catch (Exception e) {
            logger.error("An error occurred while scraping product: {}", productUrl, e);
        }
    }

    /**
     * Retrieves subcategory links from the document.
     *
     * @param document the document to scrape.
     * @param navHeading the navigation heading to filter links.
     * @return a map of category names to URLs.
     */
    private Map<String, String> getSubcategoryLinks(Document document, String navHeading) {
        Elements navLinks = document.select("a.R5kwVNg.ZHWKoMf.leavesden3.ByM_HVJ.TYb4J9A");
        Map<String, String> links = getFilteredLinks(navLinks, navHeading.toLowerCase());
        logger.debug("Subcategory links for {}: {}", navHeading, links);
        return links;
    }

    /**
     * Filters the links based on the specified heading.
     *
     * @param navLinks the navigation links to filter.
     * @param heading the heading to filter by.
     * @return a map of filtered category names to URLs.
     */
    private Map<String, String> getFilteredLinks(Elements navLinks, String heading) {
        Map<String, String> categoryMap = new HashMap<>();
        for (Element link : navLinks) {
            String href = link.attr("href");
            String category = link.text();
            if (link.classNames().size() == 5 && href.contains("/" + heading + "/") && !isBroaderLink(href) && !isBroaderCategory(category)) {
                categoryMap.put(category, href);
            }
        }
        return categoryMap;
    }

    /**
     * Checks if the category is broader.
     *
     * @param category the category to check.
     * @return true if the category is broader, false otherwise.
     */
    private Boolean isBroaderCategory(String category) {
        String[] broaderCategories = {"new in", "clothing", "view all", "latest drop", "top rated", "%", "new", "a-z", "shipped from", "sale"};
        for (String broaderCategory : broaderCategories) {
            if (category.toLowerCase().contains(broaderCategory)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the link is broader.
     *
     * @param link the link to check.
     * @return true if the link is broader, false otherwise.
     */
    private Boolean isBroaderLink(String link) {
        String[] broaderCategories = {"new+in", "a-to-z"};
        for (String broaderCategory : broaderCategories) {
            if (link.toLowerCase().contains(broaderCategory)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Scrapes all men's products.
     */
    public void getAllMensProducts() {
        String mensUrl = BASE_URL + "men/";
        Document document = getDocument(mensUrl);
        Map<String, String> links = getSubcategoryLinks(document, "MEN");
        logger.info("Scraping all men's products.");
        for (Map.Entry<String, String> entry : links.entrySet()) {
            scrapeCategory(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Scrapes all women's products.
     */
    public void getAllWomensProducts() {
        String womensUrl = BASE_URL + "women/";
        Document document = getDocument(womensUrl);
        Map<String, String> links = getSubcategoryLinks(document, "WOMEN");
        logger.info("Scraping all women's products.");
        for (Map.Entry<String, String> entry : links.entrySet()) {
            scrapeCategory(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Scrapes products based on the specified sex.
     *
     * @param sex the sex to scrape products for.
     */
    public void scrapeBasedOnSex(String sex) {
        if (sex.equalsIgnoreCase("male")) {
            getAllMensProducts();
        } else if (sex.equalsIgnoreCase("female")) {
            getAllWomensProducts();
        } else {
            logger.error("Invalid sex specified: {}", sex);
        }
    }

    /**
     * Scrapes all products.
     */
    public void scrapeAll() {
        getAllWomensProducts();
        getAllMensProducts();
    }
}
