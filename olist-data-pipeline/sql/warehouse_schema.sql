-- Step 6: Create Star Schema (Fact and Dimension Tables)
USE ecommerce_dw;

-- 1. Create Dimension: dim_customers
DROP TABLE IF EXISTS dim_customers;
CREATE TABLE dim_customers (
    customer_id VARCHAR(50) PRIMARY KEY,
    customer_unique_id VARCHAR(50),
    customer_city VARCHAR(100),
    customer_state CHAR(2),
    customer_zip_code_prefix INT
);

INSERT INTO dim_customers
SELECT 
    customer_id,
    customer_unique_id,
    customer_city,
    customer_state,
    customer_zip_code_prefix
FROM stg_customers;

-- 2. Create Dimension: dim_products
DROP TABLE IF EXISTS dim_products;
CREATE TABLE dim_products (
    product_id VARCHAR(50) PRIMARY KEY,
    product_category_name VARCHAR(100),
    product_weight_g INT,
    product_length_cm INT,
    product_height_cm INT,
    product_width_cm INT
);

INSERT INTO dim_products
SELECT 
    product_id,
    product_category_name,
    product_weight_g,
    product_length_cm,
    product_height_cm,
    product_width_cm
FROM stg_products;

-- 3. Create Dimension: dim_date
DROP TABLE IF EXISTS dim_date;
CREATE TABLE dim_date (
    date_key DATE PRIMARY KEY,
    year INT,
    month INT,
    day INT,
    quarter INT,
    month_name VARCHAR(20)
);

INSERT INTO dim_date
SELECT DISTINCT
    DATE(order_purchase_timestamp) as date_key,
    YEAR(order_purchase_timestamp) as year,
    MONTH(order_purchase_timestamp) as month,
    DAY(order_purchase_timestamp) as day,
    QUARTER(order_purchase_timestamp) as quarter,
    MONTHNAME(order_purchase_timestamp) as month_name
FROM stg_orders
WHERE order_purchase_timestamp IS NOT NULL;

-- 4. Create Fact Table: fact_sales
DROP TABLE IF EXISTS fact_sales;
CREATE TABLE fact_sales (
    order_id VARCHAR(50),
    order_item_id INT,
    customer_id VARCHAR(50),
    product_id VARCHAR(50),
    seller_id VARCHAR(50),
    date_key DATE,
    price DECIMAL(10, 2),
    freight_value DECIMAL(10, 2),
    total_value DECIMAL(10, 2),
    order_status VARCHAR(20),
    PRIMARY KEY (order_id, order_item_id)
);

INSERT INTO fact_sales
SELECT 
    oi.order_id,
    oi.order_item_id,
    o.customer_id,
    oi.product_id,
    oi.seller_id,
    DATE(o.order_purchase_timestamp) as date_key,
    oi.price,
    oi.freight_value,
    (oi.price + oi.freight_value) as total_value,
    o.order_status
FROM stg_order_items oi
JOIN stg_orders o ON oi.order_id = o.order_id;

