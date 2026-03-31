# dbt Snowflake Project: Daily Order Revenue Analysis

This project implements a dbt (data build tool) pipeline on Snowflake for daily order revenue analysis. It transforms raw data into analytics-ready marts using a staging and marts architecture.

## Project Structure

```bash
.
├── files/               # Source raw data files (CSV)
├── snowflake_data/      # dbt project root
│   ├── models/
│   │   ├── staging/     # Raw-to-clean transformations
│   │   └── marts/       # Business-logic models (Aggregated revenue)
│   ├── dbt_project.yml
│   └── ...
└── README.md            # You are here!
```

## Data Journey

1.  **Sources (`raw_data`)**: Customers, Orders, Order Items, and Products are stored in the `FINANCE_DB.RAW` schema.
2.  **Staging Layer**: Standardizes and cleans the raw data from the sources.
3.  **Marts Layer**: Calculates business-ready metrics like `fct_daily_order_revenue`.

## Getting Started

### Prerequisites

1.  **Snowflake account**
2.  **dbt-snowflake adapter installed** (`pip install dbt-snowflake`)

### Configuration

Ensure your `~/.dbt/profiles.yml` is configured for the `snowflake_data` profile:

```yaml
snowflake_data:
  outputs:
    dev:
      type: snowflake
      account: <your_snowflake_account> # e.g., sl05443.eu_central_2.aws
      user: <your_username>
      password: <your_password>
      role: ACCOUNTADMIN
      database: FINANCE_DB
      warehouse: FINANCE_WH
      schema: RAW
      threads: 4
  target: dev
```

### Loading the Raw Data

Ensure the files in the `files/` folder are loaded into your Snowflake `FINANCE_DB.RAW` schema as tables:
- `customers`
- `orders`
- `order_items`
- `products`

You can use the Snowflake UI or `dbt seed` if you move them into the `seeds/` folder of the dbt project.

### Running the dbt Pipeline

Navigate into the `snowflake_data` directory and run:

```bash
cd snowflake_data
dbt run
```

To run tests:
```bash
dbt test
```

## Summary of Models

-   **staging/stg_customers.sql**: Cleans customer IDs and names.
-   **staging/stg_orders.sql**: Processes order dates and IDs.
-   **staging/stg_products.sql**: Formats product names and prices.
-   **marts/fct_daily_order_revenue.sql**: Joins orders and items to provide a daily view of revenue by product category.
