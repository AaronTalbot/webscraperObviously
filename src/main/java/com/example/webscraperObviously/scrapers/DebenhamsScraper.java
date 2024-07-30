package com.example.webscraperObviously.scrapers;

import com.example.webscraperObviously.model.Category;
import com.example.webscraperObviously.model.Product;
import com.example.webscraperObviously.service.ProductService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebenhamsScraper extends BaseScraper {

    private static final Logger logger = LoggerFactory.getLogger(DebenhamsScraper.class);
    private final String BASE_URL = "https://www.debenhams.com";
    private final String[] CATEGORIES = {"KIDS", "SHOES", "BEAUTY", "HOME", "GARDEN", "ELECTRICALS", "HOLIDAY", "WEDDING", "SPORTS"};
    private final ProductService productService;

    public DebenhamsScraper(ProductService productService) {
        this.productService = productService;
        this.initializeDriver();
    }

    /**
     * Scrapes products from a specified category URL.
     *
     * @param categoryUrl the URL of the category to scrape
     * @param category the name of the category
     * @return a list of scraped products
     */
    private ArrayList<Product> scrapeCategory(String categoryUrl, String category) {
        ArrayList<Product> products = new ArrayList<>();
        try {
            logger.info("Scraping category: {}", category);
            driver.get(categoryUrl);

            // Keep clicking the "Load More" button until it disappears
            while (true) {
                try {
                    WebElement loadMoreButton = driver.findElement(By.cssSelector("[data-test-id='pagination-load-more']"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loadMoreButton);
                    loadMoreButton.click();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    logger.debug("No more 'Load More' buttons found.");
                    break;
                }
            }

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[data-test-id=product-card]")));

            String pageSource = driver.getPageSource();
            Document document = Jsoup.parse(pageSource);
            Elements productCards = document.select("div[data-test-id=product-card]");
            logger.debug("Number of product cards found: {}", productCards.size());

            // Process each product card
            for (Element productCard : productCards) {
                Element linkElement = productCard.selectFirst("a[href]");
                if (linkElement != null) {
                    String href = linkElement.attr("href");
                    String fullHref = BASE_URL + href;

                    Product p = scrapeProduct(fullHref, category);
                    if (p != null) {
                        Category cat = new Category();
                        cat.setName(category);
                        p.setCategory(cat);
                        products.add(p);
                        productService.saveProduct(p);
                    }
                } else {
                    logger.warn("No link found for product card.");
                }
            }
        } catch (Exception e) {
            logger.error("An error occurred while scraping the category: {}", category, e);
        } finally {
            driver.quit();
        }
        return products;
    }

    /**
     * Scrapes product details from a specified product URL.
     *
     * @param productUrl the URL of the product to scrape
     * @param category the category of the product
     * @return the scraped product
     */
    private Product scrapeProduct(String productUrl, String category) {
        try {
            logger.info("Scraping product URL: {}", productUrl);
            Document productDocument = getDocument(productUrl);

            if (productDocument == null) {
                logger.error("Failed to fetch the product page for URL: {}", productUrl);
                return null;
            }

            Element scriptElement = productDocument.selectFirst("script[type=application/ld+json]");
            if (scriptElement != null) {
                String jsonData = scriptElement.html();
                JsonElement jsonElement = JsonParser.parseString(jsonData);

                if (jsonElement.isJsonArray()) {
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    for (JsonElement element : jsonArray) {
                        if (element.isJsonObject()) {
                            JsonObject jsonObject = element.getAsJsonObject();
                            return extractProductData(jsonObject);
                        }
                    }
                } else if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    return extractProductData(jsonObject);
                } else {
                    logger.error("The extracted data is not a valid JSON structure.");
                }
            } else {
                logger.error("No product found for URL: {}", productUrl);
            }
        } catch (Exception e) {
            logger.error("An error occurred while scraping the product: {}", productUrl, e);
        }
        return null;
    }

    /**
     * Extracts product data from the given JSON object.
     *
     * @param jsonObject the JSON object containing product data
     * @return the extracted product
     */
    private Product extractProductData(JsonObject jsonObject) {
        try {
            if ("Product".equals(jsonObject.get("@type").getAsString())) {
                String name = jsonObject.get("name").getAsString();
                String sku = jsonObject.get("sku").getAsString();
                double price = jsonObject.getAsJsonObject("offers").get("price").getAsDouble();

                Product p = new Product();
                p.setSku(sku);
                p.setPrice(price);
                p.setTitle(name);
                p.setLastUpdated(LocalDateTime.now());
                logger.debug("Extracted product data: {}", p);
                return p;
            }
        } catch (Exception e) {
            logger.error("An error occurred while extracting product data", e);
        }
        return null;
    }
    /**
     * Scrapes all products.
     */
    public void getAllProducts() {
        logger.info("Scraping all products.");
        getAllMensProducts();
        getAllWomensProducts();
        getAllOtherProducts();
    }

    /**
     * Scrapes products based on the specified category.
     *
     * @param category the category to scrape
     */
    public void getProductsBasedOnCategory(String category) {
        logger.info("Scraping products based on category: {}", category);
        for (String cat : CATEGORIES) {
            if (cat.equalsIgnoreCase(category)) {
                driver.get(BASE_URL);
                Document document = getDocument(BASE_URL);
                Map<String, String> links = getSubcategoryLinks(document, category);
                for (Map.Entry<String, String> entry : links.entrySet()) {
                    scrapeCategory(entry.getValue(), entry.getKey());
                }
                break;
            }
        }
    }

    /**
     * Scrapes products for all other categories.
     */
    private void getAllOtherProducts() {
        logger.info("Scraping all other categories.");
        for (String cat : CATEGORIES) {
            driver.get(BASE_URL);
            Document document = getDocument(BASE_URL);
            Map<String, String> links = getSubcategoryLinks(document, cat);
            for (Map.Entry<String, String> entry : links.entrySet()) {
                scrapeCategory(entry.getValue(), entry.getKey());
            }
        }
    }

    /**
     * Scrapes all women's products.
     */
    public void getAllWomensProducts() {
        logger.info("Scraping all women's products.");
        driver.get(BASE_URL);
        Document document = getDocument(BASE_URL);
        Map<String, String> links = getSubcategoryLinks(document, "WOMENS");
        for (Map.Entry<String, String> entry : links.entrySet()) {
            scrapeCategory(entry.getValue(), entry.getKey());
        }
    }

    /**
     * Scrapes all men's products.
     */
    public void getAllMensProducts() {
        logger.info("Scraping all men's products.");
        driver.get(BASE_URL);
        Document document = getDocument(BASE_URL);
        Map<String, String> links = getSubcategoryLinks(document, "MENS");
        for (Map.Entry<String, String> entry : links.entrySet()) {
            scrapeCategory(entry.getValue(), entry.getKey());
        }
    }

    /**
     * Retrieves subcategory links from the document based on navigation heading.
     *
     * @param document the document to scrape
     * @param navHeading the navigation heading to filter links
     * @return a map of category names to URLs
     */
    public Map<String, String> getSubcategoryLinks(Document document, String navHeading) {
        Elements navLinks = document.select("a[data-test-id^='desktop-nav:']");
        return getFilteredLinks(navLinks, navHeading.toLowerCase());
    }

    /**
     * Filters links based on the specified URL pattern.
     *
     * @param elements the elements to filter
     * @param urlPattern the URL pattern to filter by
     * @return a map of filtered category names to URLs
     */
    public Map<String, String> getFilteredLinks(Elements elements, String urlPattern) {
        List<String> filteredLinks = new ArrayList<>();
        Map<String, String> categoryMap = new HashMap<>();

        for (Element element : elements) {
            String href = element.attr("href");
            String[] parts = href.split("[/-]");
            logger.debug("Processing href: {}", href);

            for (int i = 0; i < parts.length; i++) {
                if (urlPattern.equals(parts[i])) {
                    filteredLinks.add(href);

                    // Get the rest of the list and join with "-"
                    String category = String.join("-", java.util.Arrays.copyOfRange(parts, i + 1, parts.length));
                    categoryMap.put(category, href);

                    break; // Exit the loop once the pattern is found
                }
            }
        }
        return categoryMap;
    }

    /**
     * Scrapes products based on the specified sex.
     *
     * @param sex the sex to scrape products for
     */
    public void getProductsBasedOnSex(String sex) {
        if (sex.equalsIgnoreCase("male")) {
            getAllMensProducts();
        } else if (sex.equalsIgnoreCase("female")) {
            getAllWomensProducts();
        } else {
            logger.error("Invalid sex specified: {}", sex);
        }
    }
}
