# Lidar Backend Delivery

## Stack

- Java 17
- Spring Boot 3.2.x
- Maven 3.6+
- MySQL 8.x

## Run

### Default mode

Starts with built-in sample data and does not require MySQL:

```powershell
mvn spring-boot:run
```

In VS Code, use `Run Lidar Backend`.

### MySQL mode

1. Create a local database:

```sql
CREATE DATABASE lidar_backend DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Set environment variables for your local MySQL account:

```powershell
$env:MYSQL_HOST='localhost'
$env:MYSQL_PORT='3306'
$env:MYSQL_DB='lidar_backend'
$env:MYSQL_USER='root'
$env:MYSQL_PASSWORD='your_password'
```

3. Start the MySQL profile:

```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

In VS Code, use `Run Lidar Backend (MySQL)`.

This project uses the Java 17 environment already installed on the local machine. In MySQL mode it initializes tables from `src/main/resources/schema.sql` and demo records from `src/main/resources/data.sql`.

## Auth

This standalone lidar module only checks whether the request contains:

```http
Authorization: Bearer demo-token
```

Real JWT validation belongs to the common backend module maintained by member 1.

## Implemented APIs

### Realtime

- `GET /api/v1/lidar/realtime/summary`
- `GET /api/v1/lidar/realtime/targets`
- `GET /api/v1/lidar/realtime/vehicle-list`
- `GET /api/v1/lidar/realtime/events`

### History

- `GET /api/v1/lidar/history/tasks`
- `GET /api/v1/lidar/history/detail?taskId=...`
- `GET /api/v1/lidar/history/events`
- `GET /api/v1/lidar/history/analysis?taskId=...`

### Video

- `GET /api/v1/lidar/video/summary`
- `GET /api/v1/lidar/video/playback-list`
- `GET /api/v1/lidar/video/download-list`

### Analysis

- `GET /api/v1/lidar/analysis/summary`
- `GET /api/v1/lidar/analysis/distance-distribution`
- `GET /api/v1/lidar/analysis/device-stats`

## Query rules

- API prefix: `/api/v1`
- Time fields: millisecond timestamp
- Geo fields: `lng`, `lat`
- Pagination: `page`, `pageSize`
- Sort: `sortBy`, `sortOrder`

## Sample curl

```bash
curl -H "Authorization: Bearer demo-token" "http://localhost:8080/api/v1/lidar/realtime/summary"
```

```bash
curl -H "Authorization: Bearer demo-token" "http://localhost:8080/api/v1/lidar/history/tasks?page=1&pageSize=10&sortBy=startTimeMs&sortOrder=desc"
```

## Notes

- The backend supports both sample-data mode and MySQL mode.
- Initial demo data is inserted by `data.sql`, so the frontend can start integration immediately after database startup.
- Sample coordinates come from the provided GNSS text sample, and lidar metrics come from the parsed lidar summaries in the stage-2 data package.
