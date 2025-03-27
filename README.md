# reward-points
# Overview
This Spring Boot application provides RESTful APIs to manage customer transactions and calculate reward points based on purchase amounts. The system records transactions and computes rewards based on the following rules:
1. Customers earn 1 point per dollar spent over $50.
2. Customers earn 2 points per dollar spent over $100.
   
# Features
1. Add a new transaction for a customer
2. Retrieve transactions by customer ID
3. Calculate reward points for a customer within a date range
4. Compute rewards per month for the last three months
5. Get total rewards for all customers
6. Fetch rewards for a specific month and year

# Technologies Used
1. Java 17
2. Spring 3.3.6
3. Spring Data Jpa
4. Lombok
5. Jakarta Validation
6. Maven

# API Endpoints
## API Reference

| Http Method | Endpoint     | Description          |
| :-------- | :------- | :------------------------- |
| `POST` | `/transaction/add` | Add a new transaction |
| `GET` | `/transaction/get/{customerId}` | Get all transactions for a customer |
| `GET` | `/transaction/getByMonth/{customerId}` | Get reward points per month for a customer |
| `GET` | `/transaction/getByDate/{customerId}/{start}/{end}` | Get reward points for a customer in a date range |
| `GET` | `/transaction/getAll` | Get reward details of all customers |
| `GET` | `/transaction/getRewardsByParticularMonth/{customerId}/{month}/{year}` | Get reward details for a customer for a specific month and year |

# Exception Handling
The application includes a global exception handler to manage errors gracefully. If a requested customer does not exist, a **CustomerNotFoundException** is thrown with an appropriate message.

## Database Schema
### Transactions Table
| Column | Type     | Constraint          |
| :-------- | :------- | :------------------------- |
| `transactionId` | `BIGINT` | Primary Key, Auto-generated |
| `customerId` | `BIGINT` | Not Null |
| `amount` | `DOUBLE` | Not Null, Positive Value |
| `rewardPoints` | `BIGINT` | Computed based on transaction amount |
| `date` | `DATE` | Not Null, Past or Present |

## API Result

### Post - http://localhost:8081/transaction/add
#### Body
```json
{
    "customerId": 2,
    "amount": 150.55,
    "date": "2025-03-23"
}
```
#### Response
```json
{
    Transaction is successfully added.
}
```

### Get - http://localhost:8081/transaction/get/1
#### Response
```json
[
    {
        "transactionId": 1,
        "customerId": 1,
        "amount": 120.0,
        "rewardPoints": 90,
        "date": "2025-03-23"
    },
    {
        "transactionId": 2,
        "customerId": 1,
        "amount": 100.0,
        "rewardPoints": 50,
        "date": "2025-03-23"
    },
    {
        "transactionId": 3,
        "customerId": 1,
        "amount": 50.0,
        "rewardPoints": 0,
        "date": "2025-03-23"
    },
    {
        "transactionId": 52,
        "customerId": 1,
        "amount": 150.0,
        "rewardPoints": 150,
        "date": "2025-03-23"
    }
]
```

### Get - http://localhost:8081/transaction/getByMonth/1
#### Response
```json
{
    "March 2025": 290,
    "February 2025": 0,
    "January 2025": 0
}
```

### Get - http://localhost:8081/transaction/getByDate/1/2025-01-01/2025-03-24
#### Response
```json
{
    290
}
```

### Get - http://localhost:8081/transaction/getAll
#### Response
```json
[
    {
        "customerId": 1,
        "rewardPoints": 290,
        "totalAmountSpent": 420.0
    },
    {
        "customerId": 2,
        "rewardPoints": 452,
        "totalAmountSpent": 451.65000000000003
    }
]
```

### Get - http://localhost:8081/transaction/getRewardsByParticularMonth/1/3/2025
#### Response
```json
{
    "customerId": 1,
    "rewardPoints": 290,
    "totalAmountSpent": 420.0
}
```


