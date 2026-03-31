import pandas as pd
import os

# Define paths
raw_data_path = "data/raw"

datasets = {
    "orders": "olist_orders_dataset.csv",
    "customers": "olist_customers_dataset.csv",
    "order_items": "olist_order_items_dataset.csv",
    "products": "olist_products_dataset.csv",
    "payments": "olist_order_payments_dataset.csv"
}

def ingest_raw_data():
    loaded_data = {}
    print("--- Starting Raw Data Ingestion ---")
    
    for name, filename in datasets.items():
        file_path = os.path.join(raw_data_path, filename)
        try:
            df = pd.read_csv(file_path)
            loaded_data[name] = df
            print(f"LOADED: {name}: {df.shape[0]} rows")
        except FileNotFoundError:
            print(f"ERROR: {filename} not found in {raw_data_path}")
        except Exception as e:
            print(f"WARNING: Unexpected error loading {name}: {e}")
            
    return loaded_data

if __name__ == "__main__":
    data = ingest_raw_data()
    if data:
        print("\nAll datasets processed. Ready for cleaning.")
    else:
        print("\nNo data loaded. Please check your data/raw directory.")
