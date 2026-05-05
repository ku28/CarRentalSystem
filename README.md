# Car Rental Management System

A full-stack car rental management system built with **Spring Boot** (backend) and **React + Vite** (frontend).

---

## Tech Stack

| Layer    | Technology                                              |
|----------|---------------------------------------------------------|
| Backend  | Java 21, Spring Boot 4, Spring Data JPA, Spring Security, JWT, OAuth2 (GitHub) |
| Frontend | React 19, Vite, Axios, React Router                    |
| Database | SQL Server (MSSQL)                                      |
| Messaging| RabbitMQ (optional, for async notifications)            |

---

## Prerequisites

Before running the application, ensure you have:

1. **Java 21** (JDK) — [Download Adoptium](https://adoptium.net/)
2. **Node.js 18+** — [Download Node.js](https://nodejs.org/)
3. **SQL Server** running on `localhost:1433` with a database named `car_rental_db`
4. **RabbitMQ** (optional) — running on `localhost:5672` for async notifications

### Database Setup

1. Start SQL Server
2. Create a database called `car_rental_db`
3. Update credentials in `Car Rental System Backend/src/main/resources/application.properties` if different:
   ```properties
   spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=car_rental_db;encrypt=true;trustServerCertificate=true
   spring.datasource.username=testuser
   spring.datasource.password=kush2811
   ```

---

## How to Run

### 1. Start the Backend

```bash
cd "Car Rental System Backend"

# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

The backend starts on **http://localhost:8080**

> JPA is configured with `ddl-auto=update`, so tables are auto-created on first run.

### 2. Start the Frontend

```bash
cd "Car Rental System Frontend"

# Install dependencies (first time only)
npm install

# Start dev server
npm run dev
```

The frontend starts on **http://localhost:5173**

---

## Business Logic Flow

```
User Registers → Logs In → Searches Cars → Books Car → System Reserves Vehicle
→ Admin Processes Pickup → User Uses Car → Admin Processes Return
→ System Calculates Cost → Payment Processed → Reports Generated
```

### Step-by-Step

| Step | Action              | Who     | What Happens                                                   |
|------|---------------------|---------|----------------------------------------------------------------|
| 1    | Register            | User    | Creates account with email/password or GitHub OAuth             |
| 2    | Login               | User    | Gets JWT token for API access                                   |
| 3    | Browse Cars         | User    | Sees all available cars with search/filter                      |
| 4    | Request Booking     | User    | Selects dates, sends booking request. Car status → `BOOKED`    |
| 5    | Process Pickup      | Admin   | Selects pending booking from dropdown. Car status → `RENTED`   |
| 6    | Return Car          | Admin   | Selects active rental from dropdown. Cost auto-calculated       |
| 7    | Process Payment     | Admin   | Enters rental ID to process simulated payment                   |
| 8    | View Reports        | Admin   | Revenue, bookings, and car usage statistics                     |

### Pricing Calculation

```
totalCost = rentalDays × pricePerDay
```

Where `rentalDays = ceil(hours between pickup and return / 24)` (minimum 1 day).

---

## Core Modules

| Module                | Description                                          |
|-----------------------|------------------------------------------------------|
| **User Management**   | Register/Login, JWT + GitHub OAuth2, Roles: ADMIN/USER |
| **Car Management**    | CRUD for cars with category, pricing, availability    |
| **Booking Module**    | Search cars, send booking requests, cancel bookings   |
| **Rental Management** | Admin processes pickups and returns                   |
| **Payment Module**    | Simulated payment processing                          |
| **Availability**      | Real-time car status tracking (AVAILABLE/BOOKED/RENTED/MAINTENANCE) |
| **Notifications**     | Booking confirmations (RabbitMQ + email)              |
| **Reporting**         | Revenue, bookings, and car usage reports              |

---

## API Endpoints

### Auth
| Method | Endpoint               | Description                |
|--------|------------------------|----------------------------|
| POST   | `/api/auth/register`   | Register new user          |
| POST   | `/api/auth/login`      | Login, returns JWT         |
| GET    | `/oauth2/authorization/github` | GitHub OAuth login  |

### Cars
| Method | Endpoint               | Description                |
|--------|------------------------|----------------------------|
| GET    | `/api/cars`            | List all cars (paginated)  |
| GET    | `/api/cars/search`     | Search/filter cars         |
| POST   | `/api/cars`            | Add car (Admin)            |
| PUT    | `/api/cars/{id}`       | Update car (Admin)         |
| DELETE | `/api/cars/{id}`       | Delete car (Admin)         |

### Bookings
| Method | Endpoint                      | Description                    |
|--------|-------------------------------|--------------------------------|
| POST   | `/api/bookings`               | Create booking request         |
| GET    | `/api/bookings/me`            | Get my bookings                |
| DELETE | `/api/bookings/{id}`          | Cancel booking                 |
| GET    | `/api/bookings/all`           | All bookings (Admin)           |
| GET    | `/api/bookings/status/{status}` | Filter by status (Admin)     |

### Rentals
| Method | Endpoint                       | Description                   |
|--------|--------------------------------|-------------------------------|
| POST   | `/api/rentals/pickup/{bookingId}` | Process pickup (Admin)     |
| POST   | `/api/rentals/return/{rentalId}`  | Process return (Admin)     |
| GET    | `/api/rentals/active`          | Active rentals (Admin)        |
| GET    | `/api/rentals/booking/{bookingId}` | Get rental by booking     |

### Payments
| Method | Endpoint                       | Description                   |
|--------|--------------------------------|-------------------------------|
| POST   | `/api/payments`                | Process payment (Admin)       |
| GET    | `/api/payments/rental/{id}`    | Get payment by rental         |

### Reports
| Method | Endpoint               | Description                    |
|--------|------------------------|--------------------------------|
| GET    | `/api/reports/revenue`  | Revenue report (date range)   |
| GET    | `/api/reports/bookings` | Bookings report (date range)  |
| GET    | `/api/reports/usage`    | Car usage statistics           |

---

## Roles

| Role       | Permissions                                                  |
|------------|--------------------------------------------------------------|
| `ROLE_USER`  | Browse cars, request bookings, cancel bookings, view notifications |
| `ROLE_ADMIN` | All user permissions + manage cars, process pickups/returns, payments, reports, view users |

---

## Project Structure

```
Car Rental System/
├── Car Rental System Backend/        # Spring Boot API
│   ├── src/main/java/.../
│   │   ├── config/                   # Security, CORS, RabbitMQ config
│   │   ├── controller/               # REST controllers
│   │   ├── dto/                      # Request/Response DTOs
│   │   ├── entity/                   # JPA entities
│   │   ├── enums/                    # Status enums
│   │   ├── exception/                # Custom exceptions
│   │   ├── messaging/                # RabbitMQ producers/consumers
│   │   ├── repository/               # Spring Data repositories
│   │   ├── security/                 # JWT, OAuth2 handlers
│   │   ├── service/                  # Business logic
│   │   └── util/                     # Entity-DTO mappers
│   └── src/main/resources/
│       └── application.properties    # Database, JWT, OAuth2 config
│
├── Car Rental System Frontend/       # React + Vite SPA
│   ├── src/
│   │   ├── api/axios.js              # Axios with JWT interceptor
│   │   ├── context/AuthContext.jsx    # Auth state management
│   │   ├── components/               # Navbar, ProtectedRoute
│   │   └── pages/                    # All page components
│   └── index.html
│
└── README.md
```
