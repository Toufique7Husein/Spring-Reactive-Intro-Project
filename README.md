# Spring Boot WebFlux with DynamoDB

## 📌 Project Overview
This project is a **reactive Spring Boot application** that integrates **Amazon DynamoDB** using the **AWS SDK v2 Enhanced Client**. It leverages **Spring WebFlux** to handle non-blocking, asynchronous operations efficiently.

## 🚀 Technologies Used
- **Spring Boot 3.x**
- **Spring WebFlux** (Reactive programming)
- **AWS SDK v2 (DynamoDB Enhanced Client)**
- **Lombok** (Boilerplate code reduction)
- **Project Reactor** (Reactive programming support)
- **JUnit 5** (Testing)

## 🏗 Project Structure
```
/src/main/java/com/curde/demo
├── controller          # REST APIs
├── service             # Business logic
├── repo                # Repository layer (DynamoDB integration)
├── entity              # Data models
├── dto                 # Data transfer objects
├── constant            # Constant values
└── error               # Custom exception handling
```

## ⚡️ Getting Started
### 1️⃣ Clone the Repository
```sh
git clone https://github.com/your-repo.git
cd your-repo
```

### 2️⃣ Configure AWS DynamoDB
- **Local DynamoDB Setup:** Run DynamoDB locally using the AWS SDK.
- **AWS Credentials:** Ensure `~/.aws/credentials` is properly set up.

### 3️⃣ Build and Run the Application
```sh
./gradlew clean build
./gradlew bootRun
```

### 4️⃣ Test the API Endpoints
Use **Postman** or **cURL** to test the REST API:
```sh
# Get all products
curl -X GET http://localhost:8080/products

# Save a new product
curl -X POST http://localhost:8080/products -H "Content-Type: application/json" -d '{"name":"Laptop","price":1200}'
```

## 📜 API Endpoints
| Method | Endpoint          | Description                |
|--------|------------------|----------------------------|
| POST   | `/products`      | Create a new product       |
| GET    | `/products`      | Fetch all products        |
| GET    | `/products/{id}` | Get product by ID         |
| PUT    | `/products`      | Update product details    |
| DELETE | `/products/{id}` | Delete product by ID      |

## ✅ Running Tests
```sh
./gradlew test
```

## 🎯 Future Improvements
- Implement proper error handling.
- Add authentication & authorization (OAuth2, JWT).
- Improve logging & monitoring.

## 📄 License
This project is licensed under the [MIT License](LICENSE).

---
💡 **Need Help?** Feel free to raise an issue or contribute to the project! 🚀

