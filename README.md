# Quarkus Order Service

Aplikasi REST API untuk manajemen transaksi order dengan Quarkus Framework yang mendukung blocking dan reactive programming.

## Fitur

- **Entity Management**: Order dan OrderItem dengan relasi one-to-many
- **REST API**: CRUD operations dengan pagination
- **Database Migration**: Menggunakan Flyway
- **Reactive Support**: Versi reactive dengan Reactive Panache
- **Validations**: Bean Validation untuk input
- **Query Analytics**: Total belanja per customer

## Database Setup

### Menggunakan Docker Compose

Jalankan PostgreSQL 16:

```bash
docker-compose up -d
```

Database akan tersedia di:
- Host: localhost:5432
- Database: order_db
- Username: quarkus
- Password: password

## API Endpoints

### Blocking REST API

#### Orders Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/orders` | Create new order with validation |
| GET | `/orders/{id}` | Get order by ID with full details |
| GET | `/orders` | Get all orders with pagination, sorting, and filtering |
| GET | `/orders?customerName={name}` | Get orders filtered by customer name |
| GET | `/orders/customers/spending` | Get total spending per customer with pagination |
| GET | `/orders/customers/{customerName}` | Get all orders for a specific customer with pagination |

#### Request Examples

**Create Order:**
```json
POST /orders
{
  "customerName": "John Doe",
  "orderDate": "2024-01-20",
  "items": [
    {
      "productName": "Laptop",
      "quantity": 1,
      "price": 1200000.00
    },
    {
      "productName": "Mouse",
      "quantity": 1,
      "price": 150000.00
    }
  ]
}
```

**Success Response:**
```json
{
  "code": 201,
  "status": "CREATED",
  "message": "order created successfully",
  "data": {
    "id": 1,
    "customerName": "John Doe",
    "orderDate": "2024-01-20",
    "totalAmount": 1350000.00,
    "items": [
      {
        "id": 1,
        "productName": "Laptop",
        "quantity": 1,
        "price": 1200000.00,
        "subTotal": 1200000.00
      },
      {
        "id": 2,
        "productName": "Mouse",
        "quantity": 1,
        "price": 150000.00,
        "subTotal": 150000.00
      }
    ],
    "createdAt": "2024-01-20T10:30:00",
    "updatedAt": "2024-01-20T10:30:00"
  }
}
```

**Paginated Response:**
```json
{
  "code": 200,
  "status": "SUCCESS",
  "message": "Orders retrieved successfully",
  "data": [
    {
      "id": 1,
      "customerName": "John Doe",
      "orderDate": "2024-01-20",
      "totalAmount": 1350000.00,
      "items": [...],
      "createdAt": "2024-01-20T10:30:00",
      "updatedAt": "2024-01-20T10:30:00"
    }
  ],
  "pagination": {
    "totalElements": 100,
    "currentPage": 1,
    "pageSize": 20,
    "totalPages": 5
  }
}
```

**Error Response:**
```json
{
  "code": 400,
  "status": "BAD_REQUEST",
  "message": "Validation failed",
  "errors": {
    "items": ["Order must have at least one item"],
    "quantity": ["Quantity is required and must be greater than 0"],
    "price": ["Price is required and must be greater than 0"],
    "productName": ["Product name is required"]
  }
}
```

### Reactive REST API

Semua endpoint blocking juga tersedia dalam versi reactive di:
- Base URL: `/reactive/orders`
- Tambahan: `/reactive/orders/stream` untuk streaming orders

#### Reactive Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/reactive/orders` | Create new order (reactive version) |
| GET | `/reactive/orders/{id}` | Get order by ID (reactive version) |
| GET | `/reactive/orders` | Get all orders with pagination (reactive version) |
| GET | `/reactive/orders?customerName={name}` | Get orders by customer name (reactive version) |
| DELETE | `/reactive/orders/{id}` | Delete order by ID (reactive version) |
| GET | `/reactive/orders/customers/spending` | Get total spending per customer (reactive version) |
| GET | `/reactive/orders/customers/{customerName}` | Get orders by customer (reactive version) |
| GET | `/reactive/orders/stream` | Stream orders (reactive streaming) |

## Query Parameters

### Pagination
- `page`: Halaman (default: 1, 1-based indexing)
- `size`: Jumlah data per halaman (default: 20, max: 100)
- `sort`: Field untuk sorting (default: id)
- `order`: Sort order: asc/desc (default: desc)
- `customerName`: Filter berdasarkan nama customer (exact match)

### Contoh:
```bash
GET /orders?page=1&size=10&sort=totalAmount&order=asc&customerName=John
```

### Available Sort Fields
- `id`: Sort by order ID
- `customerName`: Sort by customer name
- `orderDate`: Sort by order date
- `totalAmount`: Sort by total amount
- `createdAt`: Sort by creation timestamp
- `updatedAt`: Sort by update timestamp

## Struktur Proyek

```
src/main/java/com/must5/
├── entity/                 # JPA Entities
│   ├── Order.java         # Order entity dengan Panache
│   ├── OrderItem.java     # OrderItem entity dengan Panache
│   └── CustomerSpending.java # DTO untuk customer spending
├── dto/                    # Data Transfer Objects
│   ├── OrderRequest.java
│   ├── OrderResponse.java
│   ├── OrderItemRequest.java
│   └── OrderItemResponse.java
├── resource/               # REST Resources (Blocking)
│   └── OrderResource.java
└── reactive/               # Reactive Components
    ├── ReactiveOrder.java
    ├── ReactiveOrderItem.java
    ├── CustomerSpendingReactive.java
    └── ReactiveOrderResource.java
```

## Database Migration

Flyway migrations terletak di:
```
src/main/resources/db/migration/
├── V1__Create_orders_table.sql
├── V2__Create_order_items_table.sql
└── V3__Create_views_and_functions.sql
```

## Configuration

### Application Properties
- Konfigurasi database di `application.properties`
- Konfigurasi reactive di `application-reactive.properties`

### Menjalankan Versi Reactive

```bash
./mvnw quarkus:dev -Dquarkus.profile=reactive
```

## Running the Application

### Dev Mode
```bash
# Jalankan database
docker-compose up -d

# Jalankan aplikasi (blocking)
./mvnw quarkus:dev

# Jalankan aplikasi (reactive)
./mvnw quarkus:dev -Dquarkus.profile=reactive
```

Aplikasi akan berjalan di:
- Blocking API: http://localhost:8080/orders
- Reactive API: http://localhost:8080/reactive/orders
- Dev UI: http://localhost:8080/q/dev/


## Testing API

### Menggunakan cURL

```bash
# Create order
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Test Customer",
    "orderDate": "2024-01-20",
    "items": [
      {"productName": "Product 1", "quantity": 2, "price": 100000}
    ]
  }'

# Get all orders with pagination
curl "http://localhost:8080/orders?page=1&size=10"

# Get order by ID
curl http://localhost:8080/orders/1

# Get orders filtered by customer name
curl "http://localhost:8080/orders?customerName=John%20Doe"

# Get customer spending with pagination
curl "http://localhost:8080/orders/customers/spending?page=1&size=5"

# Get orders for specific customer
curl "http://localhost:8080/orders/customers/John%20Doe?page=1&size=10"

# Delete order
curl -X DELETE http://localhost:8080/orders/1
```

### Menggunakan HTTP

```bash
# Create order
http POST localhost:8080/orders customerName="Test Customer" orderDate="2024-01-20" items:='[{"productName":"Product 1","quantity":2,"price":100000}]'

# Get all orders with pagination
http GET localhost:8080/orders page==1 size==10

# Get orders with sorting
http GET localhost:8080/orders page==1 size==10 sort==totalAmount order==desc

# Get orders filtered by customer name
http GET localhost:8080/orders customerName=="John Doe"

# Get customer spending
http GET localhost:8080/orders/customers/spending

# Get orders for specific customer
http GET localhost:8080/orders/customers/John%20Doe page==1 size==5
```
