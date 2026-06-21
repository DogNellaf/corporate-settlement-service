# Corporate Settlement Service

**English** | [Русский](README.ru.md)

REST service for managing corporate settlement accounts and product instances.

## Overview

The service implements two business processes:

- **CSA (Corporate Settlement Account)** — creates a product register (settlement account) for an existing product instance.
- **CSI (Corporate Settlement Instance)** — creates a new product instance with automatic register opening, or adds supplementary agreements to an existing instance.

## Tech Stack

- Java 11
- Spring Boot 2.7.9
- Spring Data JPA / Hibernate
- PostgreSQL (production), H2 (tests)
- Maven
- Lombok
- Docker / Docker Compose

## Running

### With Docker Compose

```bash
docker-compose up --build
```

The service starts on port `8080`, PostgreSQL on `6541`.

### Locally

1. Start PostgreSQL (or use `docker-compose up db`).
2. Verify the connection settings in `application.properties`.
3. Run the service:

```bash
./mvnw spring-boot:run -pl service
```

## API

### POST `/corporate-settlement-account/create`

Creates a product register (account) for an existing product instance.

**Request body:**

| Field             | Type   | Required | Description                  |
|-------------------|--------|:--------:|------------------------------|
| `instanceId`      | Long   | ✓        | Product instance ID          |
| `registryTypeCode`| String | ✓        | Register type code           |
| `currencyCode`    | String |          | Currency code                |
| `branchCode`      | String |          | Branch code                  |
| `priorityCode`    | String |          | Priority code                |
| `mdmCode`         | String |          | Client MDM code              |
| `accountType`     | String |          | Account type                 |
| `clientCode`      | String |          | Client code                  |
| `trainRegion`     | String |          | Training region              |
| `counter`         | String |          | Counter                      |
| `salesCode`       | String |          | Sales code                   |

**Response:**

```json
{
  "data": {
    "accountId": "42"
  }
}
```

---

### POST `/corporate-settlement-instance/create`

Creates a new product instance or adds supplementary agreements to an existing one.

**Request body:**

| Field                      | Type          | Required | Description                                                     |
|----------------------------|---------------|:--------:|-----------------------------------------------------------------|
| `instanceId`               | Integer       |          | Existing instance ID (if provided — update mode)               |
| `productType`              | String        | ✓        | Product type                                                    |
| `productCode`              | String        | ✓        | Product code from the catalog                                   |
| `registerType`             | String        | ✓        | Register type                                                   |
| `mdmCode`                  | String        | ✓        | Client MDM code                                                 |
| `contractNumber`           | String        | ✓        | Contract number                                                 |
| `contractDate`             | LocalDateTime | ✓        | Contract date                                                   |
| `priority`                 | Integer       | ✓        | Priority                                                        |
| `contractId`               | Integer       | ✓        | Contract ID                                                     |
| `branchCode`               | String        | ✓        | Branch code                                                     |
| `isoCurrencyCode`          | String        | ✓        | ISO currency code                                               |
| `urgencyCode`              | String        | ✓        | Urgency code                                                    |
| `interestRatePenalty`      | Float         |          | Penalty interest rate                                           |
| `thresholdAmount`          | Float         |          | Threshold amount                                                |
| `taxPercentageRate`        | Float         |          | Tax rate (%)                                                    |
| `instanceArrangementDto`   | Array         |          | List of supplementary agreements                                |

**Response (creation):**

```json
{
  "data": {
    "instanceId": "15"
  },
  "registerId": ["23", "24"],
  "supplementaryAgreementId": null
}
```

**Response (update):**

```json
{
  "data": {
    "instanceId": "15"
  },
  "registerId": null,
  "supplementaryAgreementId": ["7"]
}
```

## Error Codes

| HTTP code | Reason                                                                    |
|-----------|---------------------------------------------------------------------------|
| 400       | Required field missing, or a record with the given data already exists    |
| 404       | Register type, account pool, or product instance not found                |

## Database Schema

| Table                            | Description                        |
|----------------------------------|------------------------------------|
| `tpp_product`                    | Product instances                  |
| `tpp_product_register`           | Product registers (accounts)       |
| `agreement`                      | Supplementary agreements           |
| `account_pool`                   | Free account pools                 |
| `account`                        | Accounts within pools              |
| `tpp_ref_product_class`          | Product class reference            |
| `tpp_ref_product_register_type`  | Register type reference            |
| `tpp_ref_account_type`           | Account type reference             |
| `tpp_template_register_balance`  | Register balance templates         |

## Tests

```bash
./mvnw test -pl service
```

Tests use an in-memory H2 database in PostgreSQL-compatibility mode. Coverage is enforced by JaCoCo (profile `coverage`):

```bash
./mvnw verify -pl service -P coverage
```
