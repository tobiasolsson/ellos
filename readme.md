# Ellos Products API

This is a simple Java Spring Boot application that provides a REST API for querying product data from the Ellos website.

## How It Works

The application exposes a single GET endpoint that accepts two parameters:

- `path` (mandatory): Specifies the path for querying product data from the Ellos API. This parameter corresponds to the category or section of products on the Ellos website.
- `filter` (optional): Specifies a filter string to filter the products based on their names. Only products whose names contain the filter string will be returned.

When a request is made to the endpoint with the specified parameters, the application queries the Ellos API to retrieve product data. It then parses the JSON response, extracts the relevant product information, and calculates the average price of the products.

If a filter parameter is provided, the application filters the products based on the specified criteria before calculating the average price.

The application returns the filtered products along with the average price as a JSON response.

## How to Use
```http request
GET http://localhost:8080/products?path=barn%2Fbabyklader-stl-50-92
```
This will return a JSON response containing the products from the specified path and their average price.

```http request
GET http://localhost:8080/products?path=barn%2Fbabyklader-stl-50-92&filter=coco
```
This will return a JSON response containing the products from the specified path that contain "coco" in their names, along with their average price.

## Dependencies

- Java 17 
- Maven
- Spring Boot 3.2
- Gson (for JSON parsing)
- Spring Web (for REST API)