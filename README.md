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
| Coulmn | Type     | Constraint          |
| :-------- | :------- | :------------------------- |
| `transactionId` | `BIGINT` | Primary Key, Auto-generated |
| `customerId` | `BIGINT` | Not Null |
| `amount` | `DOUBLE` | Not Null, Positive Value |
| `rewardPoints` | `BIGINT` | Computed based on transaction amount |
| `date` | `DATE` | Not Null, Past or Present |



