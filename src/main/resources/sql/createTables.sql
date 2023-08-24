CREATE TABLE shop
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    address VARCHAR(100)
);

CREATE TABLE productType
(
    id      BIGSERIAL NOT NULL PRIMARY KEY,
    product_type VARCHAR(50)
);

CREATE TABLE products
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    product VARCHAR(10),
    type_id INTEGER,
    FOREIGN KEY (type_id) REFERENCES productType (id)
);

CREATE TABLE generalTable
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    shop_id INTEGER,
    product_id INTEGER,
    balance INTEGER,
    FOREIGN KEY (shop_id) REFERENCES shop (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);
