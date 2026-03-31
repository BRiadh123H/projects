# Brazilian E-commerce (Olist) Data Pipeline

A modern Python-based ETL pipeline designed to ingest, clean, and transform the Olist E-commerce dataset. This project is currently transitioning from a legacy Docker/Airflow/dbt setup to a leaner, more modular Python/SQL architecture using modern data concepts.

### 📁 Project Structure

```text
├── data/
│   ├── raw/                 # Original CSV files from the Olist dataset
│   └── clean/               # Processed and cleaned data ready for analysis
├── scripts/
│   ├── ingest_data.py       # Ingestion logic for raw CSVs
│   ├── clean_data.py        # Data cleaning and type conversion
│   ├── load_to_mysql.py     # New script to load clean data to MySQL
│   ├── build_warehouse.py   # Transform staging into Star Schema (MySQL)
│   └── verify_mysql_data.py # Quick data quality check (MySQL)
├── sql/                     # Schema and Star Schema definition files
└── README.md                # Project documentation
```

## 🚀 Getting Started

### Prerequisites

- Python 3.8+
- MySQL Server (installed and running)
- Pandas, SQLAlchemy, mysql-connector-python

### Installation

1. Clone the repository or download the project files.
2. Install dependencies:
   ```bash
   pip install pandas sqlalchemy mysql-connector-python
   ```

### Running the Pipeline

The pipeline should be executed in this order:

1. **Ingest and Clean Data**:
   ```bash
   python scripts/ingest_data.py
   python scripts/clean_data.py
   ```
2. **Load to Database**:
   ```bash
   python scripts/load_to_mysql.py
   ```
3. **Build Data Warehouse (Star Schema)**:
   ```bash
   python scripts/build_warehouse.py
   ```
4. **Verify Data**:
   ```bash
   python scripts/verify_mysql_data.py
   ```

## 🛠️ Modernization Roadmap (In Progress)

- [ ] Refactor ETL scripts to use **Polars** for better performance.
- [ ] Implement a **Medallion Architecture** (Bronze, Silver, Gold layers).
- [ ] Integrate **DuckDB** for fast, local analytical querying.
- [ ] Add data quality validation using **Pandera** or **Pydantic**.
- [ ] Transition SQL transformations to a modular structure.

## 📊 Dataset

The project uses the [Brazilian E-commerce Public Dataset by Olist](https://www.kaggle.com/datasets/olistbr/brazilian-ecommerce). This dataset contains information on 100k orders from 2016 to 2018.
