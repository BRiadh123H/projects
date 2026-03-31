import pandas as pd
import os
from sqlalchemy import create_engine, text

# Database configuration (User will be prompted)
DB_HOST = "localhost"
DB_PORT = "3306"
DB_NAME = "ecommerce_dw"
CLEAN_DATA_PATH = "data/clean"

datasets = ["orders", "customers", "order_items", "products", "payments"]

def load_data_to_mysql(username, password):
    print(f"\n--- Starting Data Load to MySQL (User: {username}) ---")
    
    # Connection URL to the server
    server_url = f"mysql+mysqlconnector://{username}:{password}@{DB_HOST}:{DB_PORT}"
    db_url = f"{server_url}/{DB_NAME}"
    
    try:
        # 1. Connect to server and create database
        server_engine = create_engine(server_url)
        with server_engine.connect() as conn:
            print(f"Connected to MySQL server at {DB_HOST}:{DB_PORT}")
            conn.execute(text(f"CREATE DATABASE IF NOT EXISTS {DB_NAME}"))
            conn.execute(text(f"USE {DB_NAME}"))
            print(f"Database '{DB_NAME}' verified/created.")

        # 2. Connect to the specific database
        engine = create_engine(db_url)
        
        for name in datasets:
            file_path = os.path.join(CLEAN_DATA_PATH, f"{name}_clean.csv")
            table_name = f"stg_{name}"
            
            if os.path.exists(file_path):
                print(f"Loading {name} to {table_name}...")
                df = pd.read_csv(file_path)
                
                # Load to SQL (replace if exists)
                df.to_sql(table_name, engine, if_exists='replace', index=False)
                print(f"   SUCCESS: Loaded {name} ({df.shape[0]} rows)")
            else:
                print(f"   WARNING: {file_path} not found. Skipping.")
                
        print("\nAll data loaded successfully into MySQL staging tables!")
        
    except Exception as e:
        print(f"\nERROR: Failed to load data to MySQL. {e}")
        if "10061" in str(e):
            print("\n>>> TIP: Your MySQL service is STOPPED. Please start it using:")
            print("   Start-Service -Name 'MySQL80' (in Admin PowerShell)")
        else:
            print("Tip: Check your password and ensuring the MySQL service is running.")


if __name__ == "__main__":
    user = input("Enter MySQL username (default: root): ") or "root"
    pwd = input(f"Enter MySQL password for '{user}': ")
    load_data_to_mysql(user, pwd)
