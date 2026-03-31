import mysql.connector
import getpass

def verify_mysql_tables(username, password):
    print(f"\n--- Verifying MySQL Tables (User: {username}) ---")
    
    try:
        # Connect to MySQL
        conn = mysql.connector.connect(
            host="localhost",
            user=username,
            password=password,
            database="ecommerce_dw"
        )
        cursor = conn.cursor()
        
        # 1. List all tables
        cursor.execute("SHOW TABLES")
        tables = cursor.fetchall()
        
        if not tables:
            print("No tables found in 'ecommerce_dw'.")
            return
            
        print(f"{'Table Name':<25} | {'Row Count':<10}")
        print("-" * 40)
        
        for (table_name,) in tables:
            # 2. Get row count for each table
            cursor.execute(f"SELECT COUNT(*) FROM {table_name}")
            (count,) = cursor.fetchone()
            print(f"{table_name:<25} | {count:<10}")
            
        # 3. Show a preview of the orders table
        print("\n--- Preview of 'stg_orders' (First 3 rows) ---")
        cursor.execute("SELECT order_id, customer_id, order_status FROM stg_orders LIMIT 3")
        rows = cursor.fetchall()
        for row in rows:
            print(row)
            
        cursor.close()
        conn.close()
        
    except mysql.connector.Error as err:
        print(f"Error: {err}")

if __name__ == "__main__":
    user = input("Enter MySQL username (default: root): ") or "root"
    pwd = input(f"Enter MySQL password for '{user}': ")
    verify_mysql_tables(user, pwd)
