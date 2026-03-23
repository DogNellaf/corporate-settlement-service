# Corporate Settlement Service

Сервис для операций с корпоративными расчетными продуктами:
- создание корпоративного расчетного экземпляра продукта;
- создание корпоративного расчетного счета (регистра) для продукта.

Проект реализован на Java 11 и Spring Boot 2.7.9, использует PostgreSQL и JPA.

## Технологии

- Java 11
- Spring Boot (Web, Data JPA, Actuator)
- PostgreSQL 15
- Maven (multi-module)
- Lombok
- JUnit 5
- Docker / Docker Compose

## Структура проекта

- `service/` - основной Spring Boot модуль
- `service/src/main/java/ru/practice5/main/controllers/` - REST контроллеры
- `service/src/main/java/ru/practice5/main/services/` - бизнес-логика
- `service/src/main/resources/schema.sql` - схема БД
- `service/src/main/resources/data.sql` - тестовые/стартовые данные
- `docker-compose.yml` - запуск сервиса и PostgreSQL

## API

Базовый URL локально: `http://localhost:8080`

### 1. Создание расчетного счета

`POST /corporate-settlement-account/create`

Создает новый регистр продукта (`tpp_product_register`) и возвращает его идентификатор.

Пример запроса:

```json
{
	"instanceId": 1,
	"registryTypeCode": "03.012.002_47533_ComSoLd",
	"currencyCode": "800",
	"branchCode": "0022",
	"priorityCode": "00",
	"mdmCode": "15"
}
```

Пример успешного ответа:

```json
{
	"data": {
		"accountId": "3"
	}
}
```

Проверки в сервисе:

- `instanceId` обязателен;
- для `instanceId + registryTypeCode` не должен существовать регистр;
- `registryTypeCode` должен существовать в справочнике `tpp_ref_product_register_type`;
- подбирается счет из `account_pool` по набору параметров (`branchCode`, `currencyCode`, `mdmCode`, `priorityCode`, `registryTypeCode`).

### 2. Создание расчетного экземпляра

`POST /corporate-settlement-instance/create`

Сценарии:

- если `instanceId == null`: создается новый продукт и набор регистров;
- если `instanceId != null`: добавляются дополнительные соглашения к существующему продукту.

Минимальный рабочий пример запроса для сценария создания нового продукта:

```json
{
	"productType": "NSO",
	"productCode": "03.012.002",
	"registerType": "03.012.002_47533_ComSoLd",
	"mdmCode": "15",
	"contractNumber": "CN-100500",
	"contractDate": "2026-03-23T10:15:30",
	"priority": 0,
	"contractId": 100500,
	"BranchCode": "0022",
	"IsoCurrencyCode": "800",
	"urgencyCode": "NORMAL"
}
```

Пример успешного ответа:

```json
{
	"data": {
		"instanceId": "4"
	},
	"registerId": [
		"5"
	],
	"supplementaryAgreementId": null
}
```

### Формат ошибок

При бизнес-ошибках используется единый формат:

```json
{
	"error": "Текст ошибки"
}
```

Коды:

- `400 Bad Request` - валидационные ошибки (`ValidationException`)
- `404 Not Found` - сущность не найдена (`NotFoundException`)

## Быстрый старт

### Вариант 1. Локально (Maven + локальный PostgreSQL)

1. Поднимите PostgreSQL и создайте БД `postgres`.
2. Убедитесь, что параметры подключения соответствуют `service/src/main/resources/application.properties`:
	 - `jdbc:postgresql://localhost:5432/postgres`
	 - username: `postgres`
	 - password: `postgres`
3. Из корня проекта выполните:

```bash
mvn clean package
mvn -pl service spring-boot:run
```

После запуска сервис доступен на порту `8080`.

### Вариант 2. Docker Compose

Важно: текущий `service/Dockerfile` копирует уже собранный JAR (`target/*.jar`), поэтому перед `docker compose up` нужно собрать проект.

```bash
mvn clean package
docker compose up --build
```

Контейнеры:

- сервис: `localhost:8080`
- PostgreSQL: `localhost:6541` (внутри compose - `db:5432`)

## Примеры cURL

Создать регистр:

```bash
curl -X POST "http://localhost:8080/corporate-settlement-account/create" \
	-H "Content-Type: application/json" \
	-d '{
		"instanceId": 1,
		"registryTypeCode": "03.012.002_47533_ComSoLd",
		"currencyCode": "800",
		"branchCode": "0022",
		"priorityCode": "00",
		"mdmCode": "15"
	}'
```

Создать экземпляр:

```bash
curl -X POST "http://localhost:8080/corporate-settlement-instance/create" \
	-H "Content-Type: application/json" \
	-d '{
		"productType": "NSO",
		"productCode": "03.012.002",
		"registerType": "03.012.002_47533_ComSoLd",
		"mdmCode": "15",
		"contractNumber": "CN-100500",
		"contractDate": "2026-03-23T10:15:30",
		"priority": 0,
		"contractId": 100500,
		"BranchCode": "0022",
		"IsoCurrencyCode": "800",
		"urgencyCode": "NORMAL"
	}'
```

## Тесты и качество

Запуск unit/integration тестов:

```bash
mvn test
```

В проекте настроены:

- Checkstyle
- SpotBugs
- JaCoCo (пороговые значения покрытия)

## Известные ограничения

- В `service/src/main/java/ru/practice5/main/services/CsaService.java` присутствуют merge conflict markers (`<<<<<<<`, `=======`, `>>>>>>>`).
- Пока этот файл не приведен в валидное состояние, сборка/запуск приложения могут падать на этапе компиляции.

## Дополнительно

- Схема и начальные данные инициализируются через `schema.sql` и `data.sql` при старте приложения (`spring.sql.init.mode=always`).
- Главный класс приложения: `ru.practice5.main.MainServer`.

## Лицензия

Apache License 2.0

## Статус проекта

Завершен
