# Employee Management System

A Java and Spring Boot REST backend for employee administration, organizational roles, attendance, leave requests, and company assets. The application uses PostgreSQL for persistence, JWT authentication with Spring Security, and Redis caching for department and role lookups.

Developed by [Faraz Ali](https://github.com/FarazAli-1) as an internship project.

## Contents

- [Features](#features)
- [Technology stack](#technology-stack)
- [Architecture and data model](#architecture-and-data-model)
- [Getting started](#getting-started)
- [Initial administrator setup](#initial-administrator-setup)
- [Authentication](#authentication)
- [API reference](#api-reference)
- [Example requests](#example-requests)
- [Pagination, caching, and errors](#pagination-caching-and-errors)
- [Tests](#tests)
- [Implementation notes](#implementation-notes)

## Features

### Employees and organization

- Create, list, retrieve, update, and soft-delete employees.
- Assign roles and reporting managers.
- Manage departments and roles, with `ADMIN`, `MANAGER`, and `EMPLOYEE` authority levels.
- Store document URLs for CNIC, contract, and certificate records.
- Support full-time, part-time, contract, and intern employment types.

### Attendance

- Record check-in for the authenticated employee.
- Classify check-ins after `09:00` as late.
- Record checkout with an ownership check.
- Classify checkout before `17:30` as early, including a combined late/early status.
- List attendance with pagination and sorting.

### Leave management

- Submit annual, sick, and casual leave requests.
- Check date order and available balance during submission.
- Approve leave through the employee's assigned manager, subject to role authorization.
- Deduct the requested days from the balance on approval.
- List the current employee's requests and approved team leave.
- Initialize balances through administrator-created employees: 20 annual, 10 sick, and 5 casual days.

### Asset management

- Create asset records, approve requests, and return assigned assets.
- Track approval and return history.
- Restrict approval and maintenance operations using role checks.

### Shared backend capabilities

- Stateless JWT authentication and BCrypt password hashing.
- Access tokens and database-backed refresh tokens.
- Jakarta Bean Validation on business request DTOs.
- Centralized exception handling.
- DTO-based API responses and handwritten mapper classes.
- Redis caching for department and role reads.
- Development and production configuration profiles.

## Technology stack

| Technology | Usage |
| --- | --- |
| Java 17 | Application language level |
| Spring Boot `4.0.3` | Application framework |
| Spring Web MVC | REST controllers |
| Spring Security | JWT filter, authentication provider, and method authorization |
| Spring Data JPA / Hibernate | Persistence, relationships, and soft deletes |
| PostgreSQL | Relational database |
| Redis / Spring Cache | Department and role caches |
| JJWT `0.12.5` | JWT creation and verification |
| Jakarta Bean Validation | Request and entity constraints |
| Lombok `1.18.44` | Accessors and constructors |
| Maven / Maven Wrapper | Build and dependency management |
| JUnit 5 / Spring Boot Test | Application context-load test |

## Architecture and data model

The repository contains one Spring Boot application under `ems/`. Controllers handle HTTP requests, services implement business rules, repositories access PostgreSQL, and mappers convert between entities and DTOs. Spring Security authenticates requests before protected controllers are reached.

| Component | Location relative to `ems/src/main/java/com/centegy/ems/` |
| --- | --- |
| Entry point | `EmsApplication.java` |
| REST endpoints | `controller/` |
| Business logic | `service/` |
| JPA repositories | `repository/` |
| Entities and enums | `model/` |
| Request and response DTOs | `dto/` |
| Handwritten mapping classes | `mapper/` |
| JWT and authentication components | `security/` |
| Redis cache configuration | `config/` |
| Exceptions and handlers | `exceptions/` |

| Entity | Main relationships |
| --- | --- |
| `Department` | Contains roles |
| `Role` | Belongs to a department and carries an authority level |
| `Employee` | Has a role and an optional manager who is another employee |
| `Attendance` | Belongs to an employee |
| `Leave` | Belongs to an employee and may reference an approver |
| `LeaveBalance` | Tracks remaining days per employee and leave type |
| `Asset` | References an assigned employee and an approver |
| `AssetHistory` | Records the asset, employee, approver, approval date, and return date |
| `RefreshToken` | Stores a token and expiry linked to an employee |

Employees and departments use Hibernate soft-delete restrictions. Roles have soft-delete SQL as well, but their active-row restriction is currently commented out.

## Getting started

### Prerequisites

- JDK 17.
- PostgreSQL and a database the application user can access.
- Redis, reachable at `localhost:6379` by default.
- Maven, or the Maven wrapper included in `ems/`.

### 1. Clone and enter the application directory

```bash
git clone https://github.com/FarazAli-1/employee-management-system.git
cd employee-management-system/ems
```

The Maven project is inside `ems/`; there is no root-level `pom.xml`.

### 2. Configure the development database

Create a PostgreSQL database, for example `ems_db`, using your PostgreSQL client. Set these environment variables for the application process:

| Variable | Example |
| --- | --- |
| `DEV_DB_URL` | `jdbc:postgresql://localhost:5432/ems_db` |
| `DEV_DB_USERNAME` | Your PostgreSQL username |
| `DEV_DB_PASSWORD` | Your PostgreSQL password |

Bash example:

```bash
export DEV_DB_URL='jdbc:postgresql://localhost:5432/ems_db'
export DEV_DB_USERNAME='your_db_user'
export DEV_DB_PASSWORD='your_db_password'
```

PowerShell example:

```powershell
$env:DEV_DB_URL = 'jdbc:postgresql://localhost:5432/ems_db'
$env:DEV_DB_USERNAME = 'your_db_user'
$env:DEV_DB_PASSWORD = 'your_db_password'
```

For IntelliJ, add the same values under the application's run configuration environment variables.

### 3. Configure JWT signing and Redis

For your local setup, replace the existing JWT property in `src/main/resources/application.properties` with an environment placeholder:

```properties
jwt.secretKey=${JWT_SECRET_KEY}
```

Then set `JWT_SECRET_KEY` in the application environment to your own random key of at least 32 bytes. The JWT service uses the string's raw bytes; it does not Base64-decode the property.

Redis defaults are configured in the same file:

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

Change them for your environment, or override them through `SPRING_DATA_REDIS_HOST` and `SPRING_DATA_REDIS_PORT`.

### 4. Run the application

The `dev` profile is active by default. Its `ddl-auto=update` setting creates/updates tables in the existing database.

Linux/macOS:

```bash
bash mvnw spring-boot:run
```

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

With installed Maven:

```bash
mvn spring-boot:run
```

The application uses Spring Boot's default HTTP port, `8080`.

### 5. Build an executable JAR

```bash
bash mvnw clean package
java -jar target/ems-0.0.1-SNAPSHOT.jar
```

The package command runs the included context test, so provide the same configuration and database access for the build process.

### Production profile

`application-prod.properties` reads `PROD_DB_URL`, `PROD_DB_USERNAME`, and `PROD_DB_PASSWORD`. Override the active profile with `SPRING_PROFILES_ACTIVE=prod`. The production profile uses `ddl-auto=validate`, so the schema must already exist. Replace its placeholder database defaults with real configuration.

## Initial administrator setup

The repository does not include a database seed script or startup initializer. Department and role APIs require an administrator, while sign-up requires an existing role.

For a fresh local database:

1. Start the application once so Hibernate can create the tables.
2. Use your PostgreSQL client to insert an initial department and an `ADMIN` role.
3. Sign up the first administrator using the returned role ID.

Example SQL, run against the application's database:

```sql
INSERT INTO department (name, is_active)
VALUES ('Administration', true)
RETURNING id;
```

Use that returned department ID in the following statement. The value `1` is an example:

```sql
INSERT INTO role (title, role_level, department_id, is_active)
VALUES ('Administrator', 'ADMIN', 1, true)
RETURNING id;
```

Use the returned role ID in the sign-up request below. IDs are generated by the database and are not fixed.

## Authentication

### Sign up

`POST http://localhost:8080/auth/signUp`

```json
{
  "firstName": "Local",
  "lastName": "Admin",
  "email": "admin@example.com",
  "password": "ChooseYourOwnPassword123!",
  "employmentType": "FULL_TIME",
  "roleId": 1
}
```

Both sign-up and login return `accessToken`, `refreshToken`, and the employee `id`.

### Login

`POST http://localhost:8080/auth/login`

```json
{
  "email": "admin@example.com",
  "password": "ChooseYourOwnPassword123!"
}
```

Use the returned access token on protected requests:

```http
Authorization: Bearer <accessToken>
```

The three `/auth` endpoints are public. All other requests require authentication, with additional controller/service checks on selected operations. Authorities come from the role's `roleLevel` enum (`ADMIN`, `MANAGER`, or `EMPLOYEE`), not its display title.

### Refresh tokens

The current controller uses **GET with a JSON body**:

`GET http://localhost:8080/auth/refresh`

```json
{
  "refreshToken": "<refreshToken>"
}
```

Use a client that supports this request shape, such as Postman. Access JWTs are issued for one hour and refresh JWTs for one day; database refresh-token records currently use a separate seven-day expiry.

## API reference

Base URL: `http://localhost:8080`. There is no `/api` prefix.

The access column describes the implemented checks. `Authenticated` means the controller adds no narrower role restriction; additional service checks are noted where present.

### Departments and roles

| Method | Path | Action | Access |
| --- | --- | --- | --- |
| POST | `/departments` | Create department | ADMIN |
| GET | `/departments` | List departments | ADMIN |
| GET | `/departments/{id}` | Get department | ADMIN |
| DELETE | `/departments/{id}` | Soft-delete department | ADMIN |
| POST | `/roles` | Create role | ADMIN |
| GET | `/roles` | List roles | ADMIN |
| GET | `/roles/{id}` | Get role | ADMIN |
| DELETE | `/roles/{id}` | Soft-delete role | ADMIN |

### Employees

| Method | Path | Action | Access |
| --- | --- | --- | --- |
| POST | `/employees` | Create employee and leave balances | ADMIN |
| GET | `/employees` | Paginated employee list | ADMIN |
| GET | `/employees/{id}` | Get employee | Authenticated |
| PUT | `/employees/{id}` | Update employee details, role, and manager | ADMIN |
| DELETE | `/employees/{id}` | Soft-delete employee | ADMIN |
| PUT | `/employees/{id}/documents` | Store a document URL | Authenticated |
| GET | `/employees/admin/fix-balances` | Initialize missing legacy leave balances | ADMIN |

Document requests use query parameters `documentType` and `documentUrl`. Supported types are `CNIC`, `CONTRACT`, and `CERTIFICATE`. This stores URL references; it is not a binary file-upload endpoint.

### Attendance

| Method | Path | Action | Access |
| --- | --- | --- | --- |
| POST | `/attendances` | Check in as the current employee | Authenticated |
| GET | `/attendances` | Paginated attendance list | ADMIN or MANAGER |
| GET | `/attendances/{id}` | Get attendance record | Authenticated |
| PUT | `/attendances/{id}` | Update attendance fields | Authenticated |
| PUT | `/attendances/checkout/{id}` | Check out using `checkOutTime` query parameter | Record owner |
| DELETE | `/attendances/{id}` | Delete attendance record | ADMIN |

### Leaves

| Method | Path | Action | Access |
| --- | --- | --- | --- |
| POST | `/leaves` | Apply for leave as the current employee | Authenticated |
| GET | `/leaves` | Paginated leave list | ADMIN or MANAGER |
| GET | `/leaves/{id}` | Get leave record | Authenticated |
| GET | `/leaves/employee` | List the current employee's leave requests | Authenticated |
| GET | `/leaves/calender` | List approved team leave | ADMIN or MANAGER |
| PUT | `/leaves/{id}` | Update leave request fields | Authenticated |
| DELETE | `/leaves/{id}` | Delete an owned, pending leave request | Request owner |
| PUT | `/leaves/{id}/approve` | Approve and deduct leave balance | ADMIN or MANAGER who is the assigned manager |

The path is spelled `/leaves/calender` in the source. Leave duration counts both start and end dates, including weekends. Although `REJECTED` exists in the enum, there is no rejection endpoint.

### Assets

| Method | Path | Action | Access |
| --- | --- | --- | --- |
| POST | `/assets` | Create a requested asset linked to the caller | ADMIN |
| GET | `/assets` | Paginated asset list | ADMIN or MANAGER |
| GET | `/assets/{id}` | Get asset | Authenticated |
| GET | `/assets/{id}/history` | Get approval/return history | Authenticated |
| PUT | `/assets/{id}` | Update asset name/type | ADMIN or MANAGER through a service check |
| PUT | `/assets/{id}/approve` | Approve a requested asset and create history | ADMIN or MANAGER |
| PUT | `/assets/{id}/return` | Return an assigned asset and close its history | Assigned employee |
| DELETE | `/assets/{id}` | Delete asset | ADMIN |

## Example requests

Send `Content-Type: application/json` and a suitable Bearer token. Substitute the IDs returned by your own requests.

### Create a department and role

`POST /departments` as an administrator:

```json
{
  "name": "Engineering",
  "headOfDepartmentId": null
}
```

`POST /roles` as an administrator:

```json
{
  "title": "Backend Developer",
  "departmentId": 2,
  "roleRank": "EMPLOYEE"
}
```

Create a separate role with `roleRank: "MANAGER"` for a reporting manager.

### Create an employee

`POST /employees` as an administrator:

```json
{
  "firstName": "Demo",
  "lastName": "Employee",
  "email": "employee@example.com",
  "phone": "03001234567",
  "password": "ChooseAnotherPassword123!",
  "employmentType": "FULL_TIME",
  "roleId": 3,
  "managerId": 2
}
```

The manager must already exist. This creation path also generates leave balances. Public sign-up does not generate those balances; an administrator can initialize them afterward with `GET /employees/admin/fix-balances`.

### Record attendance

`POST /attendances` using the employee's token:

```json
{
  "attendanceDate": "2026-10-16",
  "checkInTime": "09:10:00"
}
```

The DTO requires `attendanceDate`, but check-in replaces it with the server's current date. The employee ID comes from authentication. Checkout example:

```http
PUT /attendances/checkout/{attendanceId}?checkOutTime=17:30:00
```

### Submit and approve leave

`POST /leaves` using the employee's token:

```json
{
  "leaveType": "ANNUAL",
  "startDate": "2026-11-02",
  "endDate": "2026-11-04"
}
```

The request starts as `PENDING`. Log in as the employee's assigned manager and call:

```http
PUT /leaves/{leaveId}/approve
```

The manager also needs `ADMIN` or `MANAGER` authority. Approval deducts three days for the sample date range.

### Create an asset

`POST /assets` using an administrator's token:

```json
{
  "assetName": "Development Laptop",
  "assetType": "Laptop"
}
```

The current creation flow assigns the asset to the authenticated administrator and marks it `REQUESTED`. The request DTO does not accept an employee assignment ID.

## Pagination, caching, and errors

### Pagination and sorting

Employee, attendance, leave, and asset lists accept Spring Data pagination parameters:

```http
GET /employees?page=0&size=10&sort=id,asc
```

Defaults are page `0`, size `10`, and ascending `id`. The response structure is:

```json
{
  "content": [],
  "pageNo": 0,
  "pageSize": 10,
  "totalElements": 0,
  "totalPages": 0,
  "last": true
}
```

Other successful endpoints return their response DTOs or lists directly, without a shared `success/message/data` envelope.

### Redis caching

The cache configuration uses a **one-minute TTL**, string keys, JSON values, and disables null-value caching. Department and role list/detail reads are cached. Create/delete methods evict the corresponding list cache; detail entries are not explicitly evicted and may remain until TTL expiry.

### Errors

The global exception handler maps:

- Missing resources to HTTP `404`.
- `UnauthorizedActionException` to HTTP `403`.
- Other handled runtime exceptions to HTTP `400`.
- Request validation failures to HTTP `400` with a field-to-message map.

Resource/runtime error bodies contain `timestamp`, `status`, and `error`. Security-filter failures are outside the controller exception handler's scope.


## Implementation notes

- **Initial data:** fresh installations require a department/admin-role bootstrap. No automatic seed or schema migration scripts are included.
- **Authorization:** restrictions vary by endpoint. Several record reads/updates require authentication without checking record ownership. Public sign-up accepts an existing `roleId`; privileged-role assignment needs tightening before a public deployment.
- **Leave approval:** the current approval implementation does not guard against repeat approval or recheck the balance. Leave updates also do not repeat all submission checks.
- **Refresh tokens:** token replacement currently deletes by refresh-token primary key using an employee ID. JWT and database expiry durations differ. These details need alignment for a robust refresh flow.
- **Updates and responses:** employee updates do not apply the submitted password or employment type. Attendance responses omit the record ID needed by update/checkout paths. Document URLs are stored but omitted from the employee response DTO.
- **Configuration:** the Redis debug logging block uses YAML-style lines inside a `.properties` file; use proper keys such as `logging.level.org.springframework.data.redis=DEBUG` if enabling that logging.

