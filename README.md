# Stock Trading Platform

A Spring Boot application for managing stock portfolios and transactions.

## Features

- User authentication and authorization with JWT
- Stock management and tracking
- Portfolio management
- Transaction history
- RESTful API endpoints

## Technologies

- Java 
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT Authentication
- Maven
- RESTful Architecture

## Setup

1. Clone the repository
2. Make sure you have Java and Maven installed
3. Configure your database settings in `application.properties`
4. Run the application using: `mvn spring-boot:run`

## API Endpoints

### Authentication
- POST /api/auth/register - Register new user
- POST /api/auth/login - Login user

### Stocks
- GET /api/stocks - Get all stocks
- GET /api/stocks/{id} - Get stock by ID
- POST /api/stocks - Create new stock
- PUT /api/stocks/{id} - Update stock
- DELETE /api/stocks/{id} - Delete stock

### Portfolio
- GET /api/portfolio - Get user's portfolio
- POST /api/portfolio/transaction - Create new transaction

## Security

The application uses JWT (JSON Web Tokens) for authentication. All endpoints except registration and login require a valid JWT token.
