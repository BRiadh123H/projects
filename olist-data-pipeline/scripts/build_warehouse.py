import mysql.connector
from sqlalchemy import create_engine, text
import os

# Database configuration
DB_HOST = "localhost"
DB_PORT = "3306"
DB_NAME = "ecommerce_dw"
SQL_FILE_PATH = "sql/warehouse_schema.sql"

def build_star_schema(username, password):
    print(f"\n--- Building Star Schema (User: {username}) ---")
    
    db_url = f"mysql+mysqlconnector://{username}:{password}@{DB_HOST}:{DB_PORT}/{DB_NAME}"
    
    try:
        engine = create_engine(db_url)
        
        # Read the SQL file
        if not os.path.exists(SQL_FILE_PATH):
            print(f"ERROR: SQL file not found at {SQL_FILE_PATH}")
            return

        with open(SQL_FILE_PATH, 'r') as f:
            # Splitting by semicolon to run commands individually
            # This is safer for some SQL drivers
            sql_commands = f.read().split(';')
            
        with engine.begin() as conn:
            print("Connected to database. Executing transformations...")
            for command in sql_commands:
                cmd = command.strip()
                if cmd:
                    conn.execute(text(cmd))
            print("SUCCESS: Star Schema created successfully!")

        # Verification: List new tables
        with engine.connect() as conn:
            result = conn.execute(text("SHOW TABLES LIKE 'dim_%'"))
            print("\nCreated Dimensions:")
            for row in result:
                print(f" - {row[0]}")
            
            result = conn.execute(text("SHOW TABLES LIKE 'fact_%'"))
            print("Created Facts:")
            for row in result:
                print(f" - {row[0]}")

    except Exception as e:
        print(f"\nERROR: Failed to build warehouse. {e}")

if __name__ == "__main__":
    user = input("Enter MySQL username (default: root): ") or "root"
    pwd = input(f"Enter MySQL password for '{user}': ")
    build_star_schema(user, pwd)
