CREATE TABLE website (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         url VARCHAR(255) NOT NULL
);

CREATE TABLE category (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);

CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         sku VARCHAR(255) NOT NULL,
                         title VARCHAR(255) NOT NULL,
                         category VARCHAR(255) NOT NULL,
                         price DECIMAL(10, 2) NOT NULL,
                         last_updated TIMESTAMP,
                         sex VARCHAR(50),  -- optional
                         website_id BIGINT,
                         category_id BIGINT,
                         FOREIGN KEY (website_id) REFERENCES website(id),
                         FOREIGN KEY (category_id) REFERENCES category(id)
);


CREATE TABLE product_detail (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                product_id BIGINT,
                                attribute_name VARCHAR(255),
                                attribute_value VARCHAR(255),
                                FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE scraping_log (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              website_id BIGINT,
                              category_id BIGINT,
                              scraped_at TIMESTAMP,
                              status VARCHAR(50),
                              FOREIGN KEY (website_id) REFERENCES website(id),
                              FOREIGN KEY (category_id) REFERENCES category(id)
);
