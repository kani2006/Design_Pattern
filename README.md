# Design_Pattern
# Smart Restaurant Queue and Table Management

A small Spring Boot backend that demonstrates design patterns through a realistic restaurant workflow:

`customer arrives -> queue -> allocation strategy -> table state transition -> notification`

## Requirements

- Java 17+
- Maven 3.8+
- MySQL 8+

The application now uses Spring Data JPA with MySQL. Hibernate creates or updates the `customers`, `restaurant_table`, and `queue_entry` tables when the application starts.

## MySQL setup

The repository includes [database/setup.sql](database/setup.sql). Run it from a terminal where you can authenticate as a MySQL administrator:

```bash
mysql -u root -p < database/setup.sql
```

The script creates database `restaurant_queue` and application user `restaurant_app` with password `restaurant_app_password`. For a real deployment, change that password in the script before running it.

The default application configuration already uses that database user. You can override it without editing source files:

```bash
export DB_USERNAME=restaurant_app
export DB_PASSWORD=restaurant_app_password
```

## Run

```bash
mvn spring-boot:run
```

The API runs at `http://localhost:8080`.

The operations dashboard is available at `http://localhost:8080/`.

## Example flow

Create a customer:

```bash
curl -X POST http://localhost:8080/api/customers \
  -H 'Content-Type: application/json' \
  -d '{"name":"Amina Khan","phone":"+1234567890","partySize":2,"vip":true}'
```

Create a table and join the queue:

```bash
curl -X POST http://localhost:8080/api/tables \
  -H 'Content-Type: application/json' \
  -d '{"tableNumber":5,"type":"FOUR_SEATER"}'

curl -X POST http://localhost:8080/api/queue \
  -H 'Content-Type: application/json' \
  -d '{"customerId":1}'
```

Allocate the next customer using a strategy:

```bash
curl -X POST http://localhost:8080/api/queue/allocate \
  -H 'Content-Type: application/json' \
  -d '{"tableId":1,"strategy":"VIP_PRIORITY"}'
```

Available strategies are `FCFS`, `VIP_PRIORITY`, and `SMALLEST_SUITABLE`.

## Endpoints

| Method | Endpoint | Purpose |
| --- | --- | --- |
| POST | `/api/customers` | Register a customer |
| GET | `/api/customers/{id}` | Find a customer |
| POST | `/api/tables` | Create a table using the Factory |
| GET | `/api/tables/available` | List available tables |
| PUT | `/api/tables/{id}/status?status=CLEANING` | Transition table state |
| POST | `/api/queue` | Add a customer to the queue |
| GET | `/api/queue` | View queue entries |
| DELETE | `/api/queue/{id}` | Cancel a waiting entry |
| POST | `/api/queue/allocate` | Allocate a table using a Strategy |
| POST | `/api/reservations` | Book an available table for a customer |
| GET | `/api/reservations` | View reservations |
| DELETE | `/api/reservations/{id}` | Cancel a reservation and release its table |

## Booking protection

Book a table with:

```bash
curl -X POST http://localhost:8080/api/reservations \
  -H 'Content-Type: application/json' \
  -d '{"customerId":1,"tableId":1,"reservationTime":"2026-09-07T20:00:00Z"}'
```

Booking locks the table row in MySQL, changes its status to `RESERVED`, and stores the customer as the owner. A second booking attempt for that table receives HTTP `409 Conflict`; it cannot overwrite the first customer. Cancellation changes the reservation to `CANCELLED` and makes the table available again.

The booking and queue allocation operations are transactional. The table lock prevents two simultaneous requests from booking or allocating the same table.

## Pattern map

- **Factory:** `TableFactory` centralizes table creation and capacity rules.
- **Strategy:** `TableAllocationStrategy` allows FCFS, VIP, or suitability rules to be selected at runtime.
- **Observer:** `NotificationSubject` broadcasts allocation events to SMS and email observers.
- **State:** `TableStateContext` controls legal transitions between available, reserved, occupied, and cleaning.

## Build and test

```bash
mvn test
```

## Postman

Import the endpoints from the table above into Postman. Start with customer and table creation, add the customer to the queue, then call `/api/queue/allocate` with a table id and one of `FCFS`, `VIP_PRIORITY`, or `SMALLEST_SUITABLE`.
