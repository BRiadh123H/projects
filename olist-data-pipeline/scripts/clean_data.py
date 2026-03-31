import pandas as pd
import os
from ingest_data import ingest_raw_data

# Define paths
clean_data_path = "data/clean"

# Create clean data directory if it doesn't exist
os.makedirs(clean_data_path, exist_ok=True)

def clean_and_save_data(datasets):
    print("\n--- Starting Data Cleaning ---")
    
    for name, df in datasets.items():
        print(f"Cleaning {name}...")
        
        # 1. Drop duplicates
        initial_rows = df.shape[0]
        df = df.drop_duplicates()
        
        # 2. Handle missing values (simple dropna for this project)
        df = df.dropna()
        dropped_rows = initial_rows - df.shape[0]
        
        # 3. Type Conversion for specific columns
        if name == 'orders':
            df['order_purchase_timestamp'] = pd.to_datetime(df['order_purchase_timestamp'])
        
        # 4. Save cleaned data
        save_path = os.path.join(clean_data_path, f"{name}_clean.csv")
        df.to_csv(save_path, index=False)
        print(f"SUCCESS: Saved cleaned {name} to {save_path} (Dropped {dropped_rows} rows)")

if __name__ == "__main__":
    from ingest_data import ingest_raw_data
    raw_datasets = ingest_raw_data()
    if raw_datasets:
        clean_and_save_data(raw_datasets)
    else:
        print("Cleaning aborted: No raw data found.")
