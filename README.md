# Corporate Settlement Service

REST-сервис для управления корпоративными расчётными счетами и экземплярами продуктов.

## Описание

Сервис реализует два бизнес-процесса:

- **CSA (Corporate Settlement Account)** — создание продуктового регистра (расчётного счёта) для существующего экземпляра продукта.
- **CSI (Corporate Settlement Instance)** — создание нового экземпляра продукта с автоматическим открытием регистров, либо добавление дополнительных соглашений к существующему экземпляру.

## Технологии

- Java 11
- Spring Boot 2.7.9
- Spring Data JPA / Hibernate
- PostgreSQL (продуктив), H2 (тесты)
- Maven
- Lombok
- Docker / Docker Compose

## Запуск

### С Docker Compose

```bash
docker-compose up --build
```

Сервис поднимается на порту `8080`, база данных PostgreSQL — на `6541`.

### Локально

1. Запустить PostgreSQL (или использовать `docker-compose up db`).
2. Убедиться, что в `application.properties` указаны корректные параметры подключения.
3. Запустить сервис:

```bash
./mvnw spring-boot:run -pl service
```

## API

### POST `/corporate-settlement-account/create`

Создаёт продуктовый регистр (счёт) для существующего экземпляра продукта.

**Тело запроса:**

| Поле              | Тип    | Обязательное | Описание                       |
|-------------------|--------|:------------:|-------------------------------|
| `instanceId`      | Long   | ✓            | ИД экземпляра продукта        |
| `registryTypeCode`| String | ✓            | Код типа регистра             |
| `currencyCode`    | String |              | Код валюты                    |
| `branchCode`      | String |              | Код отделения                 |
| `priorityCode`    | String |              | Код приоритета                |
| `mdmCode`         | String |              | МДМ-код клиента               |
| `accountType`     | String |              | Тип счёта                     |
| `clientCode`      | String |              | Код клиента                   |
| `trainRegion`     | String |              | Регион обучения               |
| `counter`         | String |              | Счётчик                       |
| `salesCode`       | String |              | Код продаж                    |

**Пример ответа:**

```json
{
  "data": {
    "accountId": "42"
  }
}
```

---

### POST `/corporate-settlement-instance/create`

Создаёт новый экземпляр продукта или добавляет дополнительные соглашения к существующему.

**Тело запроса:**

| Поле                       | Тип           | Обязательное | Описание                              |
|----------------------------|---------------|:------------:|--------------------------------------|
| `instanceId`               | Integer       |              | ИД существующего экземпляра (если указан — режим обновления) |
| `productType`              | String        | ✓            | Тип продукта                         |
| `productCode`              | String        | ✓            | Код продукта из каталога             |
| `registerType`             | String        | ✓            | Тип регистра                         |
| `mdmCode`                  | String        | ✓            | МДМ-код клиента                      |
| `contractNumber`           | String        | ✓            | Номер договора                       |
| `contractDate`             | LocalDateTime | ✓            | Дата договора                        |
| `priority`                 | Integer       | ✓            | Приоритет                            |
| `contractId`               | Integer       | ✓            | ИД договора                          |
| `branchCode`               | String        | ✓            | Код отделения                        |
| `isoCurrencyCode`          | String        | ✓            | ISO-код валюты                       |
| `urgencyCode`              | String        | ✓            | Код срочности                        |
| `interestRatePenalty`      | Float         |              | Процентная ставка пени               |
| `thresholdAmount`          | Float         |              | Пороговая сумма                      |
| `taxPercentageRate`        | Float         |              | Налоговая ставка (%)                 |
| `instanceArrangementDto`   | Array         |              | Список дополнительных соглашений     |

**Пример ответа (создание):**

```json
{
  "data": {
    "instanceId": "15"
  },
  "registerId": ["23", "24"],
  "supplementaryAgreementId": null
}
```

**Пример ответа (обновление):**

```json
{
  "data": {
    "instanceId": "15"
  },
  "registerId": null,
  "supplementaryAgreementId": ["7"]
}
```

## Коды ошибок

| HTTP-код | Причина                                                            |
|----------|--------------------------------------------------------------------|
| 400      | Не заполнено обязательное поле, или запись с такими данными уже существует |
| 404      | Не найден тип регистра, пул счетов, или экземпляр продукта        |

## Структура базы данных

| Таблица                          | Описание                                  |
|----------------------------------|-------------------------------------------|
| `tpp_product`                    | Экземпляры продуктов                      |
| `tpp_product_register`           | Продуктовые регистры (счета)              |
| `agreement`                      | Дополнительные соглашения                 |
| `account_pool`                   | Пулы свободных счетов                     |
| `account`                        | Счёта в пулах                            |
| `tpp_ref_product_class`          | Справочник классов продуктов              |
| `tpp_ref_product_register_type`  | Справочник типов регистров                |
| `tpp_ref_account_type`           | Справочник типов счетов                   |
| `tpp_template_register_balance`  | Шаблоны балансов регистров                |

## Тесты

```bash
./mvnw test -pl service
```

Тесты используют встроенную базу данных H2 в режиме совместимости с PostgreSQL. Покрытие контролируется JaCoCo (профиль `coverage`):

```bash
./mvnw verify -pl service -P coverage
```
