# lidar-backend

Java 17 Spring Boot backend for the lidar training module. The project supports two startup modes:

- Default mode: starts directly with built-in sample data, no database required.
- MySQL mode: connects to your local MySQL database and auto-initializes tables plus demo records.

## Quick start

If your machine default `java -version` is already Java 17, run directly in the `lidar-backend` directory:

```powershell
mvn spring-boot:run
```

This starts the service on port `8080` with sample data.

## Start with MySQL

1. Create the database locally:

```sql
CREATE DATABASE lidar_backend DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Set your local MySQL connection info before startup:

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

Tables and demo records will be initialized automatically from `src/main/resources/schema.sql` and `src/main/resources/data.sql`.

## VS Code

Open the `lidar-backend` folder directly in VS Code, then use one of these launch items:

- `Run Lidar Backend`: direct startup with sample data
- `Run Lidar Backend (MySQL)`: startup with local MySQL

You can also run these tasks:

- `mvn spring-boot:run`
- `mvn spring-boot:run (mysql)`

## Request header

```http
Authorization: Bearer demo-token
```

## Docs

- `docs/lidar_api.md`
- `docs/lidar_schema.sql`
