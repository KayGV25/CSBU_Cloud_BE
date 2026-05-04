# CSBU Microservices Backend - API Documentation

This document outlines the available REST API endpoints exposed by the various microservices in the system.

---

## Finance Service
**Base Path:** `/api/v1/finance`
**Default Port:** 8050

### Transactions

* **`POST /api/v1/finance/transactions`**
  * **Description:** Create a new transaction.
  * **Request Body (JSON):**
    ```json
    {
      "id": "TXN-2024-001",
      "transactionType": "EXPENSE",
      "amount": 5000,
      "currency": "USD",
      "description": "Office supplies purchase",
      "budgetId": "BDG-2024-001"
    }
    ```
  * **Returns:** 201 Created

* **`GET /api/v1/finance/transactions`**
  * **Description:** Retrieve paginated transactions with optional filters.
  * **Query Parameters:**
    * `searchQuery` (String, optional)
    * `startDate` (ISO Date, optional) — e.g. `2024-01-01`
    * `endDate` (ISO Date, optional) — e.g. `2024-12-31`
    * `currency` (String, optional)
    * `status` (String, optional)
    * `page` (Integer, default `1`)
    * `size` (Integer, default `10`)
  * **Returns:** 200 OK
    ```json
    {
      "currentPage": 1,
      "totalPages": 5,
      "pageSize": 10,
      "totalElement": 48,
      "data": [
        {
          "id": "TXN-2024-001",
          "transactionType": "EXPENSE",
          "amount": 5000,
          "currency": "USD",
          "status": false,
          "transactionDate": "2024-03-15T10:30:00",
          "description": "Office supplies purchase",
          "budget_id": "BDG-2024-001",
          "image": "https://s3.amazonaws.com/bucket/receipt.jpg"
        }
      ]
    }
    ```

* **`DELETE /api/v1/finance/transactions/{id}`**
  * **Description:** Delete a specific transaction by its ID.
  * **Path Variable:** `id`
  * **Returns:** 200 OK

* **`PUT /api/v1/finance/transactions/{id}/status`**
  * **Description:** Upload a receipt image for a transaction, update its status to approved, and deduct the transaction amount from the linked budget's remaining balance.
  * **Path Variable:** `id`
  * **Request:** `multipart/form-data`
    * `file` — Image file (JPEG/PNG)
  * **Returns:** 200 OK (Success) / 400 Bad Request (exceeds budget or invalid file format)

---

### Budgets

* **`POST /api/v1/finance/budgets`**
  * **Description:** Create a new budget.
  * **Request Body (JSON):**
    ```json
    {
      "id": "BDG-2024-001",
      "description": "Q1 2024 Marketing Budget",
      "periodStart": "2024-01-01",
      "periodEnd": "2024-03-31",
      "budgetAmount": 50000,
      "currency": "USD"
    }
    ```
  * **Returns:** 201 Created / 409 Conflict (if budget ID already exists)

* **`GET /api/v1/finance/budgets`**
  * **Description:** Retrieve a paginated list of budgets.
  * **Query Parameters:**
    * `page` (Integer, default `1`)
    * `size` (Integer, default `10`)
    * `searchQuery` (String, optional)
    * `year` (String, optional) — e.g. `2024`
  * **Returns:** 200 OK
    ```json
    {
      "currentPage": 1,
      "totalPages": 3,
      "pageSize": 10,
      "totalElement": 25,
      "data": [
        {
          "id": "BDG-2024-001",
          "description": "Q1 2024 Marketing Budget",
          "periodStart": "2024-01-01",
          "periodEnd": "2024-03-31",
          "approvedAmount": 50000,
          "remainingAmount": 45000,
          "currency": "USD",
          "createdAt": "2024-01-01"
        }
      ]
    }
    ```

* **`GET /api/v1/finance/budgets/id-currency`**
  * **Description:** Get an abbreviated list of all budgets containing just their IDs and currencies. Useful for populating dropdowns.
  * **Returns:** 200 OK
    ```json
    [
      {
        "id": "BDG-2024-001",
        "currency": "USD"
      },
      {
        "id": "BDG-2024-002",
        "currency": "EUR"
      }
    ]
    ```

* **`DELETE /api/v1/finance/budgets/{id}`**
  * **Description:** Delete a specific budget by its ID.
  * **Path Variable:** `id`
  * **Returns:** 200 OK

---

## Projects Service
**Base Path:** `/api/v1/tasks`
**Default Port:** 8070

### Tasks

* **`POST /api/v1/tasks`**
  * **Description:** Create a new task and assign it to an employee.
  * **Request Body (JSON):**
    ```json
    {
      "id": "TASK-2024-001",
      "taskName": "Prepare Q2 financial report",
      "managerId": "USR-001",
      "employeeId": "USR-042",
      "deadline": "2024-06-30T23:59:59.000+00:00"
    }
    ```
  * **Returns:** 201 Created

* **`GET /api/v1/tasks/{employee_id}`**
  * **Description:** Get paginated tasks assigned to a specific employee.
  * **Path Variable:** `employee_id`
  * **Query Parameters:**
    * `page` (Integer, default `1`)
    * `size` (Integer, default `10`)
  * **Returns:** 200 OK
    ```json
    {
      "currentPage": 1,
      "totalPages": 2,
      "pageSize": 10,
      "totalElement": 14,
      "data": [
        {
          "id": "TASK-2024-001",
          "taskName": "Prepare Q2 financial report",
          "managerId": "USR-001",
          "employeeId": "USR-042",
          "deadline": "2024-06-30T23:59:59.000+00:00",
          "status": false
        }
      ]
    }
    ```

* **`PUT /api/v1/tasks/{id}/status`**
  * **Description:** Mark a task as completed (`status = true`).
  * **Path Variable:** `id`
  * **Returns:** 200 OK

* **`DELETE /api/v1/tasks/{id}`**
  * **Description:** Delete a specific task by its ID.
  * **Path Variable:** `id`
  * **Returns:** 200 OK

---

## Users-Auth Service
**Base Paths:** `/api/v1/auth`, `/api/v1/department`, `/api/v1/users`
**Default Port:** 8090

### Authentication

* **`POST /api/v1/auth/login`**
  * **Description:** Authenticate a user with user ID and password.
  * **Request Body (JSON):**
    ```json
    {
      "user_id": "USR-001",
      "password": "demo-password"
    }
    ```
  * **Returns:** 200 OK
    ```json
    {
      "authenticated": true,
      "message": "Login successful",
      "accessToken": "eyJhbGciOiJIUzI1NiJ9..."
    }
    ```

* **`POST /api/v1/auth/profile`**
  * **Description:** Resolve a user ID from an access token.
  * **Request Body (JSON):**
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
    ```
  * **Returns:** 200 OK (String user ID)
    ```json
    "USR-001"
    ```

---

### Departments

* **`POST /api/v1/department`**
  * **Description:** Create a new department.
  * **Request Body (JSON):**
    ```json
    {
      "id": "DEPT-001",
      "department_name": "Finance",
      "head_department": "USR-001"
    }
    ```
    > `id` is optional — if omitted, the server will generate one.
  * **Returns:** 200 OK (String — the created department's ID)

* **`GET /api/v1/department`**
  * **Description:** Get all available departments.
  * **Returns:** 200 OK
    ```json
    [
      {
        "id": "DEPT-001",
        "department_name": "Finance",
        "department_head": "USR-001",
        "head_name": "John Doe"
      }
    ]
    ```

* **`GET /api/v1/department/{department_id}`**
  * **Description:** Get a single department's details by ID.
  * **Path Variable:** `department_id`
  * **Returns:** 200 OK
    ```json
    {
      "id": "DEPT-001",
      "department_name": "Finance",
      "department_head": "USR-001",
      "head_name": "John Doe"
    }
    ```

---

### Users

* **`POST /api/v1/users`**
  * **Description:** Register a new user.
  * **Request Body (JSON):**
    ```json
    {
      "id": "USR-001",
      "full_name": "Jane Smith",
      "dob": "1990-05-15",
      "department_id": "DEPT-001",
      "yoe": 5,
      "approved": false,
      "role": "EMPLOYEE",
      "password": "demo-password"
    }
    ```
    > `id` is optional — if omitted, the server will generate one.  
    > `approved` and `role` are optional; defaults apply if not provided.
  * **Returns:** 200 OK

* **`GET /api/v1/users`**
  * **Description:** Retrieve a list of all users.
  * **Headers:** `Authorization: Bearer <accessToken>`
  * **Returns:** 200 OK
    ```json
    [
      {
        "id": "USR-001",
        "full_name": "Jane Smith",
        "dob": "1990-05-15",
        "department_id": "DEPT-001",
        "department_name": "Finance",
        "yoe": 5
      }
    ]
    ```

* **`GET /api/v1/users/{user_id}`**
  * **Description:** Get a specific user by their ID.
  * **Path Variable:** `user_id`
  * **Headers:** `Authorization: Bearer <accessToken>`
  * **Returns:** 200 OK
    ```json
    {
      "id": "USR-001",
      "full_name": "Jane Smith",
      "dob": "1990-05-15",
      "department_id": "DEPT-001",
      "department_name": "Finance",
      "yoe": 5
    }
    ```

* **`PUT /api/v1/users/{user_id}`**
  * **Description:** Update an existing user's details.
  * **Path Variable:** `user_id`
  * **Headers:** `Authorization: Bearer <accessToken>`
  * **Request Body (JSON):**
    ```json
    {
      "id": "USR-001",
      "full_name": "Jane Smith",
      "dob": "1990-05-15",
      "department_id": "DEPT-002",
      "yoe": 6,
      "approved": true,
      "role": "MANAGER",
      "password": "new-demo-password"
    }
    ```
  * **Returns:** 200 OK
