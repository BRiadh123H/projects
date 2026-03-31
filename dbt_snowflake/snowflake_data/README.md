# snowflake_data dbt Project

This dbt project manages the data transformations for Snowflake.

## Project Structure

-   **staging/** - Models for cleaning and standardizing raw source data.
-   **marts/** - Business-logic transformations and calculations.

## Configuration

This project expects a `snowflake_data` profile to be configured in your `profiles.yml`. 

## Data Lineage

`Raw Data -> Staging Models -> Marts Models`

## Running the Project

-   `dbt run`
-   `dbt test`
-   `dbt docs generate && dbt docs serve`

