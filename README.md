### README.md

# Web Scraper Project

This project is a web scraper built with Java, Spring Boot, and Maven. The scrapers fetch product data from ASOS and Debenhams and store the data in an H2 database. The project includes REST endpoints to initiate scraping and retrieve stored data.

## Prerequisites
- Java 22
- Maven
- ChromeDriver (for Selenium)

## Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/AaronTalbot/Webscraper.git
    cd Webscraper
    ```

2. Install dependencies:
    ```bash
    mvn clean install
    ```

3. Run the application:
    ```bash
    mvn spring-boot:run
    ```

## Usage
### REST Endpoints
- **Scrape all products**: `POST /api/scrapers/scrape`
- **Scrape ASOS products**: `POST /api/scrapers/scrape/asos`
- **Scrape ASOS men's products**: `POST /api/scrapers/scrape/asos/mens`
- **Scrape ASOS women's products**: `POST /api/scrapers/scrape/asos/womens`
- **Scrape Debenhams products**: `POST /api/scrapers/scrape/debenhams`
- **Scrape Debenhams category**: `POST /api/scrapers/scrape/debenhams/{category}`

### Data Retrieval Endpoints
- **Get all products**: `GET /api/products`
- **Get products by category**: `GET /api/products/category/{categoryName}`

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/example/webscraperObviously/
│   │       ├── controller/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── scrapers/
│   │       └── service/
│   └── resources/
│       └── application.properties
└── test/
```

## Database Design
- `website`: Stores website details.
- `category`: Stores product categories.
- `product`: Stores product details.
- `scraping_log`: Stores scraping logs.
