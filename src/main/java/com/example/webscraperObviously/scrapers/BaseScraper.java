package com.example.webscraperObviously.scrapers;

import com.example.webscraperObviously.service.ProductService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public abstract class BaseScraper {

    protected WebDriver driver;
    protected ExecutorService executor;
    private static final Logger logger = LoggerFactory.getLogger(BaseScraper.class);

    public BaseScraper() {
        initializeDriver();
    }

    /**
     * Initializes the WebDriver for scraping.
     */
    public void initializeDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\aaron\\Development\\Webscraper\\src\\main\\java\\org\\example\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.managed_default_content_settings.images", 2);
        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        logger.info("WebDriver initialized.");
    }

    /**
     * Fetches and parses the document from the given URL.
     *
     * @param url the URL to fetch the document from.
     * @return the parsed Document.
     */
    protected Document getDocument(String url) {
        try {
            driver.get(url);
            logger.info("Fetching document from URL: {}", url);
            return Jsoup.parse(driver.getPageSource());
        } catch (Exception e) {
            logger.error("Error occurred while fetching document from URL: {}", url, e);
            return null;
        }
    }

    /**
     * Shuts down the WebDriver and executor service.
     */
    public void shutdown() {
        if (driver != null) {
            driver.quit();
            logger.info("WebDriver shut down.");
        }
        if (executor != null) {
            executor.shutdown();
            logger.info("Executor service shut down.");
        }
    }

    /**
     * Parses JSON-LD data.
     *
     * @param jsonLd the JSON-LD data to parse.
     * @return the parsed JsonObject.
     */
    public static JsonObject parseJsonLd(String jsonLd) {
        Gson gson = new Gson();
        logger.debug("Parsing JSON-LD data.");
        return gson.fromJson(jsonLd, JsonObject.class);
    }

    /**
     * Converts a relative URL to an absolute URL.
     *
     * @param baseUrl the base URL.
     * @param relativeUrl the relative URL.
     * @return the absolute URL.
     */
    public String makeAbsoluteUrl(String baseUrl, String relativeUrl) {
        try {
            URL base = new URL(baseUrl);
            URL absolute = new URL(base, relativeUrl);
            logger.debug("Converted relative URL to absolute URL: {}", absolute.toString());
            return absolute.toString();
        } catch (MalformedURLException e) {
            logger.error("Error occurred while converting URL: baseUrl={}, relativeUrl={}", baseUrl, relativeUrl, e);
            return relativeUrl;
        }
    }
}
