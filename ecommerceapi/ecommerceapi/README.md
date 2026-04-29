# E-Commerce API

## Project Overview
This is a RESTful API built with Spring Boot for managing an e-commerce product catalog. It supports standard CRUD operations, filtering, and data validation.

---

## Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Maven or Gradle build tool
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/lucillerapsing31-svg/ecommerce-api.git

 2.  Navigate to the project folder:
    ```bash
   cd ecommerce-api

 3. Build and run the application using Maven:
   ```bash
   mvn spring-boot:run

 4. The server will start on port 8080. 
   ```bash 
   Base URL: http://localhost:8080/api/v1


API Endpoints
   | Method | Endpoint | Description |
| :--- | :--- | :--- |
| GET | `/products` | Get all products |
| GET | `/products/{id}` | Get product by ID |
| POST | `/products` | Create new product |
| PUT | `/products/{id}` | Update product |
| DELETE | `/products/{id}` | Delete product |
| GET | `/products/filter/name` | Filter by name |
| GET | `/products/filter/category` | Filter by category |
| GET | `/products/filter/price` | Filter by price range |

Example Request (POST /products)
Body:
{
  "name": "Sample Product",
  "description": "Product description",
  "price": 100.00,
  "category": "Electronics",
  "stockQuantity": 10
}

Error Handling
ProductNotFoundException: Custom exception returned when product ID is not found.
Uses standard HTTP Status Codes: 200 OK, 404 Not Found, 400 Bad Request.

## Database Schema

  Table Name | Columns 

  products   | id, name, description, price, stockQuantity, imageUrl, category_id 
  categories | id, name 
  orders     | id, orderDate, totalAmount, user_id 
  order_items| id, quantity, price, product_id, order_id 
  users      | id, username, password, email, role 

# Relationships:
- One Category → Many Products
- One Order → Many Order Items
- One Product → Many Order Items 

Table Name	    Columns
products	      id, name, description, price, stockQuantity, imageUrl, category_id
categories	    id, name
orders	        id, orderDate, totalAmount, user_id
order_items	    id, quantity, price, product_id, order_id
users	          id, username, password, email, role

c:\Users\acer\OneDrive\Pictures\Screenshots\database_table.png.png
c:\Users\acer\OneDrive\Pictures\Screenshots\browser_console.png.png



# Contributors:
Gepollo, Malou C.
Rapsing, Lucille A.
