# Retail Store Reward Program
This application is used to generate Reward points for each recorded Purchase and display monthly rewards.

# Features
- Save Customers
- Update Customers
- Partially update customers
- Save Purchase orders based on Customers
- Calculate Reward Points based on Purchase Orders
- Fetch Customer based Order history

# Tech Stack
- Java 17
- Spring Boot 3.4.1
- PostgreSql

# API Details
- **To save Customer** -'http://localhost:8080/api/customer/saveCustomer' 
- **Method**: POST
  --data 
```json
{
  "name":"Gowtham",
  "phoneNumber":"8883615249"
}
```

- **Response**:
```json
{
  "status": "SUCCESS",
  "message": "Customer Saved Successfully!",
  "details": {
    "id": 1,
    "name": "Gowtham",
    "phoneNumber": "8883615249"
  }
}
```
- **To Update Customer** -'http://localhost:8080/api/customer/updateCustomer/{id}' 
- **Method**: PUT\
    --data 
```json
{
  "name": "Gowtham",
  "phoneNumber": "1234567890"
}
  ```
- **Response**:
```json
{
  "status": "SUCCESS",
  "message": "Customer Updated Successfully!",
  "details": {
    "id": 1,
    "name": "Gowtham",
    "phoneNumber": "1234567890"
  }
}
```

- **To Partially Update Customer** -'http://localhost:8080/api/customer/patchCustomer/{id}' 
- **Method**: PATCH\
    --data 
```json
 {
  "phoneNumber": "9990005299"
}
  ```
- **Response**:
```json
{
  "status": "SUCCESS",
  "message": "Customer Partially Updated Successfully!",
  "details": {
    "id": 2,
    "name": "Vijay",
    "phoneNumber": "9990005299"
  }
}
```
- **To save Purchase Order** -'http://localhost:8080/api/purchaseDetails/savePurchaseDetails'
- **Method**: POST

  --data 
```json
 {
  "customerId":"1",
  "purchaseDate":"2025-01-01",
  "transactionAmount":"65"
}
```
- **Response**:
```json
 {
  "status": "SUCCESS",
  "message": "Purchase Details Saved!",
  "details": {
    "id": 1,
    "customerId": "1",
    "purchaseDate": "2025-01-01",
    "transactionAmount": 65.0,
    "overallRewards": 15.0
  }
}
```
- **To Fetch Order history** --location 'http://localhost:8080/api/purchaseDetails/retrieveCustomerPurchases' \
  --header 'customerId: 2' \
  --header 'lastThreeMonths: true'
   - If lastThreeMonths tag is given as true then the last three months data is populated.
   - If none of the options are chosen by default it populates all orders for the customer
   - *Response*
```json
{
  "details": {
    "Customer Name": "Gowtham",
    "Customer PhoneNumber": "1234567890",
    "Total Purchases": 4,
    "Total Transaction Amount": 820.0,
    "Overall Reward Points": 1075.0,
    "Monthly Rewards": {
      "JANUARY-2025": 1075.0
    },
    "PurchaseDetails": [
      {
        "id": 1,
        "customerId": "1",
        "purchaseDate": "2025-01-01",
        "transactionAmount": 65.0,
        "overallRewards": 15.0
      },
      {
        "id": 2,
        "customerId": "1",
        "purchaseDate": "2025-01-02",
        "transactionAmount": 105.0,
        "overallRewards": 60.0
      },
      {
        "id": 3,
        "customerId": "1",
        "purchaseDate": "2025-01-03",
        "transactionAmount": 150.0,
        "overallRewards": 150.0
      },
      {
        "id": 4,
        "customerId": "1",
        "purchaseDate": "2025-01-04",
        "transactionAmount": 500.0,
        "overallRewards": 850.0
      }
    ]
  }
}
```

# Unit Test Results
![Unit Test Results](https://github.com/Gowthamb98/Retail-Rewards/blob/main/src/main/resources/testresults/TestResult.png)