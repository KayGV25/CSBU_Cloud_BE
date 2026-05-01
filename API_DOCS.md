# CSBU Microservices Backend - API Documentation

This document outlines the available REST API endpoints exposed by the various microservices in the system.

---

## Finance Service
**Base Path:** `/api/v1/finance`
**Default Port:** 8050

### Transactions
* **`POST /api/v1/finance/transactions`**
  * **Description:** Create a new transaction.
  * **Request Body:** `AddTransactionRequest` (JSON)
  * **Returns:** 201 Created

* **`GET /api/v1/finance/transactions`**
  * **Description:** Retrieve paginated transactions with optional filters.
  * **Query Parameters:**
    * `searchQuery` (String, optional)
    * `startDate` (ISO Date, optional)
    * `endDate` (ISO Date, optional)
    * `currency` (String, optional)
    * `status` (String, optional)
    * `page` (Integer, default `1`)
    * `size` (Integer, default `10`)
  * **Returns:** 200 OK - `PageResponse<TransactionDto>`

* **`DELETE /api/v1/finance/transactions/{id}`**
  * **Description:** Delete a specific transaction by its ID.
  * **Path Variable:** `id`
  * **Returns:** 200 OK

* **`PUT /api/v1/finance/transactions/{id}/status`**
  * **Description:** Upload an image for a transaction and update its status. Also reduces the remaining budget based on the transaction amount.
  * **Path Variable:** `id`
  * **Request Param:** `file` (Multipart Image File)
  * **Returns:** 200 OK (Success) / 400 Bad Request (Exceeds budget or invalid file format)

### Budgets
* **`POST /api/v1/finance/budgets`**
  * **Description:** Create a new budget.
  * **Request Body:** `AddBudgetRequest` (JSON)
  * **Returns:** 201 Created / 409 Conflict (if budget ID already exists)

* **`GET /api/v1/finance/budgets`**
  * **Description:** Retrieve a paginated list of budgets.
  * **Query Parameters:**
    * `page` (Integer, default `1`)
    * `size` (Integer, default `10`)
    * `searchQuery` (String, optional)
    * `year` (String, optional)
  * **Returns:** 200 OK - `PageResponse<BudgetDto>`

* **`GET /api/v1/finance/budgets/id-currency`**
  * **Description:** Get an abbreviated list of all budgets containing just their IDs and Currencies.
  * **Returns:** 200 OK - `List<BudgetIdAndCurrencyDto>`

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
  * **Description:** Create a new task.
  * **Request Body:** `AddTaskRequest` (JSON)
  * **Returns:** 201 Created

* **`GET /api/v1/tasks/{employee_id}`**
  * **Description:** Get paginated tasks assigned to a specific employee.
  * **Path Variable:** `employee_id`
  * **Query Parameters:**
    * `page` (Integer, default `1`)
    * `size` (Integer, default `10`)
  * **Returns:** 200 OK - `PageResponse<TaskDto>`

* **`PUT /api/v1/tasks/{id}/status`**
  * **Description:** Update a task's status to completed (`true`).
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
* **`GET /api/v1/auth/login`**
  * **Description:** Endpoint to authenticate/login a user.
  * **Returns:** 200 OK (String)

### Departments
* **`POST /api/v1/department`**
  * **Description:** Create a new department.
  * **Request Body:** `DepartmentRequest` (JSON)
  * **Returns:** 200 OK (String ID or success message)

* **`GET /api/v1/department`**
  * **Description:** Get all available departments.
  * **Returns:** 200 OK - `List<DepartmentResponse>`

* **`GET /api/v1/department/{department_id}`**
  * **Description:** Get a single department's details by ID.
  * **Path Variable:** `department_id`
  * **Returns:** 200 OK - `DepartmentResponse`

### Users
* **`POST /api/v1/users`**
  * **Description:** Register a new user.
  * **Request Body:** `UserRequest` (JSON)
  * **Returns:** 200 OK

* **`GET /api/v1/users`**
  * **Description:** Retrieve a list of all users.
  * **Returns:** 200 OK - `List<UserResponse>`

* **`GET /api/v1/users/{user_id}`**
  * **Description:** Get a specific user by their ID.
  * **Path Variable:** `user_id`
  * **Returns:** 200 OK - `UserResponse`

* **`PUT /api/v1/users/{user_id}`**
  * **Description:** Update an existing user's details.
  * **Path Variable:** `user_id`
  * **Request Body:** `UserRequest` (JSON)
  * **Returns:** 200 OK
