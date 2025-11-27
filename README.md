# Student-Themes Matching System - Полная документация

## 📋 Содержание

- [Обзор системы](#обзор-системы)
- [Технологии](#технологии)
- [Архитектура](#архитектура)
- [Быстрый старт](#быстрый-старт)
- [Конфигурация](#конфигурация)
- [Полное описание API](#полное-описание-api)
- [ML Сервис](#ml-сервис)
- [База данных](#база-данных)
- [Разработка](#разработка)
- [Деплоймент](#деплоймент)
- [Примеры использования](#примеры-использования)

## 🎯 Обзор системы

Полная система для управления студентами, темами проектов и интеллектуального распределения с использованием машинного обучения. Система поддерживает многовариантное распределение по специализациям, ручное и автоматическое упорядочивание приоритетов.

### Ключевые возможности:
- **Полное CRUD управление** студентами и темами
- **Система приоритетов** - ручное и автоматическое упорядочивание
- **Специализации** - многовариантное распределение по направлениям
- **ML-сопоставление** - интеллектуальное распределение на основе семантического анализа
- **Массовые операции** - управление группами студентов
- **Гибкий поиск** - фильтрация по всем полям
- **Управление активностью** - активация/деактивация студентов

## 🛠 Технологии

### Backend (Spring Boot Kotlin)
- **Java 17+** с модульностью
- **Spring Boot 3.2.x** (Web, Data JPA, Validation, Actuator)
- **Kotlin 1.9+** с корутинами и null-safety
- **PostgreSQL 14+** с расширением UUID
- **JPA/Hibernate 6.x** - ORM с lazy loading
- **Flyway** - миграции базы данных
- **Gradle 8.x** - система сборки с Kotlin DSL
- **Spring Data JPA** - репозитории и спецификации

### ML Сервис (Python FastAPI)
- **Python 3.9+** с type hints
- **FastAPI 0.104+** - современный async фреймворк
- **SentenceTransformers** - multilingual текстовые эмбеддинги
- **scikit-learn 1.3+** - cosine similarity и метрики
- **pandas 2.1+** - обработка табличных данных
- **uvicorn** - высокопроизводительный ASGI сервер
- **pydantic 2.0+** - валидация и сериализация

## 🏗 Архитектура

```
┌─────────────────┐    REST API    ┌──────────────────┐    JPA/Hibernate   ┌──────────────┐
│   Frontend      │◄──────────────►│  Spring Boot     │◄──────────────────►│  PostgreSQL  │
│   (клиент)      │                │  Backend (Kotlin)│                    │  Database    │
└─────────────────┘                └─────────┬────────┘                    └──────────────┘
        │                                    │
        │                              REST API│ ML Calls
        │                          ┌──────────┴──────────┐
        └─────────────────────────►│    ML Service       │
                                   │  (Python/FastAPI)   │
                                   └─────────────────────┘
```

### Поток данных:
1. **Клиент** взаимодействует с Spring Boot через REST API
2. **Spring Boot** сохраняет данные в PostgreSQL через JPA
3. **ML Service** предоставляет API для интеллектуальной сортировки
4. **Spring Boot** вызывает ML Service для автоматического распределения

## 🚀 Быстрый старт

### Предварительные требования

```bash
# Проверка установки Java
java -version  # Должна быть 17 или выше

# Проверка PostgreSQL
psql --version  # Должна быть 12 или выше

# Проверка Python
python --version  # Должна быть 3.8 или выше

# Проверка Gradle
gradle --version  # Должна быть 7.4 или выше
```

### 1. Установка и настройка базы данных

```sql
-- Подключение к PostgreSQL как superuser
sudo -u postgres psql

-- Создание базы данных
CREATE DATABASE student_themes;

-- Включение расширения для UUID (обязательно)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Создание пользователя (опционально)
CREATE USER stt_user WITH PASSWORD 'secure_password_123';
GRANT ALL PRIVILEGES ON DATABASE student_themes TO stt_user;
GRANT pg_read_all_data TO stt_user;
GRANT pg_write_all_data TO stt_user;

-- Проверка
\c student_themes
SELECT version();
```

### 2. Настройка переменных окружения

Создайте файл `.env` в корне проекта:

```properties
# Database Configuration
DATABASE_URL=jdbc:postgresql://localhost:5432/student_themes
POSTGRES_PASSWORD=secure_password_123

# Application Configuration
PORT=8080
ML_SERVICE_URL=http://localhost:8000
SPRING_PROFILES_ACTIVE=dev

# Logging
LOG_LEVEL=DEBUG
LOG_FILE=logs/application.log
```

### 3. Запуск Backend приложения

```bash
# Клонирование репозитория (если нужно)
git clone <repository-url>
cd spring-boot-kotlin-STT

# Запуск в режиме разработки
./gradlew bootRun --args='--spring.profiles.active=dev'

# Или сборка и запуск JAR
./gradlew clean build
java -jar build/libs/spring-boot-kotlin-STT-1.0.0.jar --spring.profiles.active=dev

# Проверка работы
curl http://localhost:8080/actuator/health
```

### 4. Запуск ML Сервиса

```bash
# Создание виртуального окружения (рекомендуется)
python -m venv venv
source venv/bin/activate  # Linux/Mac
# venv\Scripts\activate  # Windows

# Установка зависимостей
pip install fastapi uvicorn sentence-transformers scikit-learn pandas numpy pydantic requests

# Запуск ML сервиса
python main.py

# Проверка работы в отдельном терминале
curl http://localhost:8000/health
```

### 5. Проверка интеграции

```bash
# Проверка связи между сервисами
curl http://localhost:8080/themes/ml-health

# Ожидаемый ответ:
# {"status": "healthy", "service": "ML Matching Service"}
```

## ⚙️ Конфигурация

### Файлы конфигурации Spring Boot

**application.yml** (основная конфигурация):
```yaml
spring:
  application:
    name: spring-boot-kotlin-STT
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/student_themes}
    username: ${DATABASE_USERNAME:postgres}
    password: ${POSTGRES_PASSWORD}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
  jpa:
    open-in-view: false
    properties:
      hibernate:
        jdbc.batch_size: 20
        order_inserts: true
        order_updates: true

logging:
  level:
    com.StudentsToThemes.spring_boot_kotlin_STT: DEBUG
    org.springframework.web: INFO
    org.hibernate: WARN
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %logger{36} - %msg%n"
  file:
    name: "logs/application.log"

server:
  port: ${PORT:8080}
  servlet:
    context-path: /api

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```

**application-dev.yml** (разработка):
```yaml
spring:
  application:
    version: 1.0.0-dev
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        show_sql: true
        format_sql: true
        use_sql_comments: true
  # В DEV отключаем Flyway - пусть Hibernate управляет схемой
  flyway:
    enabled: false

logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

**application-prod.yml** (продакшен):
```yaml
spring:
  application:
    version: 1.0.0-prod
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        show_sql: false
        format_sql: false
  # В PROD включаем Flyway для управления миграциями
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true

logging:
  level:
    com.StudentsToThemes.spring_boot_kotlin_STT: INFO
    org.springframework: WARN
```

### Конфигурация ML сервиса

ML сервис можно кастомизировать через параметры:

```python
# В файле main.py можно изменить:
matcher = CSVStudentTopicMatcher(
    model_name='sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2'  # Модель по умолчанию
)

# Доступные модели:
# - 'sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2' (рекомендуется)
# - 'sentence-transformers/paraphrase-multilingual-mpnet-base-v2' (больше точность, больше памяти)
# - 'sentence-transformers/all-MiniLM-L6-v2' (быстрее, меньше памяти)
```

## 📡 Полное описание API

### 🎓 Управление студентами (`/students`)

#### 🔍 Поиск и получение студентов

**1. Получить всех студентов с фильтрацией**
```http
GET /api/students?name=Иван&hardSkill=ML&background=Python&interests=NLP&timeInWeek=20
```
**Параметры:**
- `name` (опционально) - поиск по имени (частичное совпадение)
- `hardSkill` (опционально) - поиск по основному навыку
- `background` (опционально) - поиск по опыту работы
- `interests` (опционально) - поиск по интересам
- `timeInWeek` (опционально) - поиск по доступному времени

**Пример запроса:**
```bash
curl "http://localhost:8080/api/students?name=Иван&hardSkill=Machine%20Learning" \
  -H "Content-Type: application/json"
```

**Пример ответа:**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Иван Петров",
    "hardSkill": "Machine Learning",
    "background": "Опыт работы с Python, ML, глубокое обучение 2 года",
    "interests": "NLP, компьютерное зрение, анализ данных",
    "timeInWeek": "20 часов",
    "themePriorities": {
      "660e8400-e29b-41d4-a716-446655440001": 0,
      "660e8400-e29b-41d4-a716-446655440002": 1
    },
    "specializationPriorities": {
      "Machine Learning": {
        "660e8400-e29b-41d4-a716-446655440001": 0
      }
    },
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T10:30:00Z"
  }
]
```

**2. Получить студента по ID**
```http
GET /api/students/{id}
```
**Пример:**
```bash
curl "http://localhost:8080/api/students/550e8400-e29b-41d4-a716-446655440000" \
  -H "Content-Type: application/json"
```

**3. Получить студентов по списку ID**
```http
GET /api/students/by-ids
Content-Type: application/json
```
**Тело запроса:**
```json
[
  "550e8400-e29b-41d4-a716-446655440000",
  "550e8400-e29b-41d4-a716-446655440001",
  "550e8400-e29b-41d4-a716-446655440002"
]
```
**Пример:**
```bash
curl -X GET "http://localhost:8080/api/students/by-ids" \
  -H "Content-Type: application/json" \
  -d '["550e8400-e29b-41d4-a716-446655440000", "550e8400-e29b-41d4-a716-446655440001"]'
```

**4. Получить активных студентов**
```http
GET /api/students/active
```
**Пример:**
```bash
curl "http://localhost:8080/api/students/active" \
  -H "Content-Type: application/json"
```

**5. Получить неактивных студентов**
```http
GET /api/students/unactive
```
**Пример:**
```bash
curl "http://localhost:8080/api/students/unactive" \
  -H "Content-Type: application/json"
```

#### ➕ Создание студентов

**6. Создать нового студента**
```http
POST /api/students
Content-Type: application/json
```
**Тело запроса:**
```json
{
  "name": "Анна Сидорова",
  "hardSkill": "Data Science",
  "background": "Стажировка в Яндекс, опыт Pandas, NumPy, SQL 1 год",
  "interests": "Анализ данных, визуализация, ML",
  "timeInWeek": "15 часов"
}
```
**Пример:**
```bash
curl -X POST "http://localhost:8080/api/students" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Анна Сидорова",
    "hardSkill": "Data Science",
    "background": "Стажировка в Яндекс, опыт Pandas, NumPy, SQL 1 год",
    "interests": "Анализ данных, визуализация, ML",
    "timeInWeek": "15 часов"
  }'
```

**7. Создать нескольких студентов**
```http
POST /api/students/by-ids
Content-Type: application/json
```
**Тело запроса:**
```json
[
  {
    "name": "Петр Иванов",
    "hardSkill": "Backend Development",
    "background": "Java, Spring Boot, PostgreSQL 3 года",
    "interests": "Микросервисы, облачные технологии",
    "timeInWeek": "25 часов"
  },
  {
    "name": "Мария Козлова", 
    "hardSkill": "Frontend Development",
    "background": "React, TypeScript, CSS 2 года",
    "interests": "UI/UX, мобильная разработка",
    "timeInWeek": "20 часов"
  }
]
```
**Пример:**
```bash
curl -X POST "http://localhost:8080/api/students/by-ids" \
  -H "Content-Type: application/json" \
  -d '[
    {
      "name": "Петр Иванов",
      "hardSkill": "Backend Development",
      "background": "Java, Spring Boot, PostgreSQL 3 года",
      "interests": "Микросервисы, облачные технологии",
      "timeInWeek": "25 часов"
    }
  ]'
```

#### ✏️ Обновление студентов

**8. Обновить данные студента**
```http
PUT /api/students/{id}
Content-Type: application/json
```
**Тело запроса:**
```json
{
  "name": "Анна Сидорова (обновлено)",
  "hardSkill": "Senior Data Science",
  "background": "Стажировка в Яндекс, опыт Pandas, NumPy, SQL 2 года",
  "interests": "Анализ данных, визуализация, ML, Big Data",
  "timeInWeek": "20 часов"
}
```
**Пример:**
```bash
curl -X PUT "http://localhost:8080/api/students/550e8400-e29b-41d4-a716-446655440000" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Анна Сидорова (обновлено)",
    "hardSkill": "Senior Data Science", 
    "background": "Стажировка в Яндекс, опыт Pandas, NumPy, SQL 2 года",
    "interests": "Анализ данных, визуализация, ML, Big Data",
    "timeInWeek": "20 часов"
  }'
```

**9. Изменить активность студента**
```http
PUT /api/students/{id}/change-activity
Content-Type: application/json
```
**Тело запроса:**
```json
{
  "active": false
}
```
**Пример:**
```bash
curl -X PUT "http://localhost:8080/api/students/550e8400-e29b-41d4-a716-446655440000/change-activity" \
  -H "Content-Type: application/json" \
  -d '{"active": false}'
```

**10. Изменить активность нескольких студентов**
```http
PUT /api/students/change-activities
Content-Type: application/json
```
**Тело запроса:**
```json
{
  "ids": [
    "550e8400-e29b-41d4-a716-446655440000",
    "550e8400-e29b-41d4-a716-446655440001"
  ],
  "active": true
}
```
**Пример:**
```bash
curl -X PUT "http://localhost:8080/api/students/change-activities" \
  -H "Content-Type: application/json" \
  -d '{
    "ids": ["550e8400-e29b-41d4-a716-446655440000", "550e8400-e29b-41d4-a716-446655440001"],
    "active": true
  }'
```

#### 🗑️ Удаление студентов

**11. Удалить студента по ID**
```http
DELETE /api/students/{id}
```
**Пример:**
```bash
curl -X DELETE "http://localhost:8080/api/students/550e8400-e29b-41d4-a716-446655440000"
```

**12. Удалить нескольких студентов**
```http
DELETE /api/students/by-ids
Content-Type: application/json
```
**Тело запроса:**
```json
[
  "550e8400-e29b-41d4-a716-446655440000",
  "550e8400-e29b-41d4-a716-446655440001"
]
```
**Пример:**
```bash
curl -X DELETE "http://localhost:8080/api/students/by-ids" \
  -H "Content-Type: application/json" \
  -d '["550e8400-e29b-41d4-a716-446655440000", "550e8400-e29b-41d4-a716-446655440001"]'
```

**13. Удалить всех студентов**
```http
DELETE /api/students/all
```
**Пример:**
```bash
curl -X DELETE "http://localhost:8080/api/students/all"
```

**14. Удалить неактивных студентов**
```http
DELETE /api/students/unactive
```
**Пример:**
```bash
curl -X DELETE "http://localhost:8080/api/students/unactive"
```

### 🎯 Управление темами (`/themes`)

#### 🔍 Поиск и получение тем

**15. Получить все темы с фильтрацией**
```http
GET /api/themes?name=ML&description=анализ&author=Петров
```
**Параметры:**
- `name` (опционально) - поиск по названию темы
- `description` (опционально) - поиск по описанию
- `author` (опционально) - поиск по автору

**Пример:**
```bash
curl "http://localhost:8080/api/themes?name=Machine%20Learning&author=Петров" \
  -H "Content-Type: application/json"
```

**Пример ответа:**
```json
[
  {
    "id": "660e8400-e29b-41d4-a716-446655440000",
    "name": "Разработка ML модели для классификации текстов",
    "description": "Создание и обучение модели для автоматической классификации customer reviews",
    "author": "Др. Петров",
    "specializations": ["Machine Learning", "NLP"],
    "priorityStudents": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "name": "Иван Петров",
        "hardSkill": "Machine Learning",
        "background": "Опыт работы с Python, ML, глубокое обучение 2 года",
        "interests": "NLP, компьютерное зрение, анализ данных",
        "timeInWeek": "20 часов",
        "themePriorities": {},
        "specializationPriorities": {},
        "createdAt": "2024-01-15T10:30:00Z",
        "updatedAt": "2024-01-15T10:30:00Z"
      }
    ],
    "studentPriorities": {
      "550e8400-e29b-41d4-a716-446655440000": 0
    },
    "specializationStudents": {
      "Machine Learning": [
        {
          "studentId": "550e8400-e29b-41d4-a716-446655440000",
          "studentName": "Иван Петров",
          "priority": 0,
          "hardSkill": "Machine Learning",
          "background": "Опыт работы с Python, ML, глубокое обучение 2 года",
          "active": true
        }
      ]
    },
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T10:30:00Z"
  }
]
```

**16. Получить тему по ID**
```http
GET /api/themes/{id}
```
**Пример:**
```bash
curl "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000" \
  -H "Content-Type: application/json"
```

#### ➕ Создание тем

**17. Создать новую тему**
```http
POST /api/themes
Content-Type: application/json
```
**Тело запроса:**
```json
{
  "name": "Разработка системы рекомендаций",
  "description": "Создание системы рекомендаций на основе коллаборативной фильтрации и контентного анализа",
  "author": "Проф. Иванова",
  "specializations": ["Machine Learning", "Data Science", "Backend"],
  "priorityStudents": [
    "550e8400-e29b-41d4-a716-446655440000",
    "550e8400-e29b-41d4-a716-446655440001"
  ]
}
```
**Пример:**
```bash
curl -X POST "http://localhost:8080/api/themes" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Разработка системы рекомендаций",
    "description": "Создание системы рекомендаций на основе коллаборативной фильтрации и контентного анализа",
    "author": "Проф. Иванова", 
    "specializations": ["Machine Learning", "Data Science", "Backend"],
    "priorityStudents": ["550e8400-e29b-41d4-a716-446655440000"]
  }'
```

#### ✏️ Обновление тем

**18. Обновить данные темы**
```http
PUT /api/themes/{themeId}
Content-Type: application/json
```
**Тело запроса:**
```json
{
  "name": "Разработка системы рекомендаций (обновлено)",
  "description": "Создание системы рекомендаций с использованием ML алгоритмов и веб-интерфейса",
  "author": "Проф. Иванова",
  "specializations": ["Machine Learning", "Data Science", "Backend", "Frontend"],
  "priorityStudents": [
    "550e8400-e29b-41d4-a716-446655440000",
    "550e8400-e29b-41d4-a716-446655440002"
  ]
}
```
**Пример:**
```bash
curl -X PUT "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Разработка системы рекомендаций (обновлено)",
    "description": "Создание системы рекомендаций с использованием ML алгоритмов и веб-интерфейса",
    "author": "Проф. Иванова",
    "specializations": ["Machine Learning", "Data Science", "Backend", "Frontend"],
    "priorityStudents": ["550e8400-e29b-41d4-a716-446655440000", "550e8400-e29b-41d4-a716-446655440002"]
  }'
```

**19. Обновить приоритеты студентов в теме**
```http
PUT /api/themes/{themeId}/priority
Content-Type: application/json
```
**Тело запроса:**
```json
{
  "studentIds": [
    "550e8400-e29b-41d4-a716-446655440002",
    "550e8400-e29b-41d4-a716-446655440000", 
    "550e8400-e29b-41d4-a716-446655440001"
  ]
}
```
**Пример:**
```bash
curl -X PUT "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/priority" \
  -H "Content-Type: application/json" \
  -d '{
    "studentIds": [
      "550e8400-e29b-41d4-a716-446655440002",
      "550e8400-e29b-41d4-a716-446655440000",
      "550e8400-e29b-41d4-a716-446655440001"
    ]
  }'
```

#### 👥 Управление студентами в темах

**20. Добавить студента к теме**
```http
POST /api/themes/{themeId}/students/{studentId}
```
**Пример:**
```bash
curl -X POST "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/students/550e8400-e29b-41d4-a716-446655440003"
```

**21. Добавить нескольких студентов к теме**
```http
POST /api/themes/{themeId}/students
Content-Type: application/json
```
**Тело запроса:**
```json
[
  "550e8400-e29b-41d4-a716-446655440003",
  "550e8400-e29b-41d4-a716-446655440004",
  "550e8400-e29b-41d4-a716-446655440005"
]
```
**Пример:**
```bash
curl -X POST "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/students" \
  -H "Content-Type: application/json" \
  -d '["550e8400-e29b-41d4-a716-446655440003", "550e8400-e29b-41d4-a716-446655440004"]'
```

**22. Удалить студента из темы**
```http
DELETE /api/themes/{themeId}/students/{studentId}
```
**Пример:**
```bash
curl -X DELETE "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/students/550e8400-e29b-41d4-a716-446655440003"
```

**23. Удалить нескольких студентов из темы**
```http
DELETE /api/themes/{themeId}/students
Content-Type: application/json
```
**Тело запроса:**
```json
[
  "550e8400-e29b-41d4-a716-446655440003",
  "550e8400-e29b-41d4-a716-446655440004"
]
```
**Пример:**
```bash
curl -X DELETE "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/students" \
  -H "Content-Type: application/json" \
  -d '["550e8400-e29b-41d4-a716-446655440003", "550e8400-e29b-41d4-a716-446655440004"]'
```

**24. Изменить активность студентов в теме**
```http
PUT /api/themes/{themeId}/students/active
Content-Type: application/json
```
**Тело запроса:**
```json
{
  "active": false
}
```
**Пример:**
```bash
curl -X PUT "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/students/active" \
  -H "Content-Type: application/json" \
  -d '{"active": false}'
```

#### 📊 Получение данных о студентах в темах

**25. Получить студентов темы**
```http
GET /api/themes/{themeId}/students?limit=10
```
**Параметры:**
- `limit` (опционально) - ограничение количества возвращаемых студентов

**Пример:**
```bash
curl "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/students?limit=5" \
  -H "Content-Type: application/json"
```

**Пример ответа:**
```json
[
  {
    "studentId": "550e8400-e29b-41d4-a716-446655440000",
    "studentName": "Иван Петров",
    "priority": 0,
    "hardSkill": "Machine Learning",
    "background": "Опыт работы с Python, ML, глубокое обучение 2 года",
    "active": true
  },
  {
    "studentId": "550e8400-e29b-41d4-a716-446655440001",
    "studentName": "Анна Сидорова",
    "priority": 1,
    "hardSkill": "Data Science",
    "background": "Стажировка в Яндекс, опыт Pandas, NumPy, SQL 1 год",
    "active": true
  }
]
```

**26. Получить темы студента**
```http
GET /api/themes/students/{studentId}/themes
```
**Пример:**
```bash
curl "http://localhost:8080/api/themes/students/550e8400-e29b-41d4-a716-446655440000/themes" \
  -H "Content-Type: application/json"
```

**Пример ответа:**
```json
[
  {
    "themeId": "660e8400-e29b-41d4-a716-446655440000",
    "themeName": "Разработка ML модели для классификации текстов",
    "priority": 0,
    "description": "Создание и обучение модели для автоматической классификации customer reviews",
    "author": "Др. Петров"
  },
  {
    "themeId": "660e8400-e29b-41d4-a716-446655440001", 
    "themeName": "Разработка системы рекомендаций",
    "priority": 1,
    "description": "Создание системы рекомендаций на основе коллаборативной фильтрации",
    "author": "Проф. Иванова"
  }
]
```

#### 🗑️ Удаление тем

**27. Удалить тему**
```http
DELETE /api/themes/{themeId}
```
**Пример:**
```bash
curl -X DELETE "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000"
```

### 🔧 Управление специализациями

#### ➕ Создание и управление специализациями

**28. Добавить специализацию к теме**
```http
POST /api/themes/{themeId}/specializations
Content-Type: application/json
```
**Тело запроса:**
```json
{
  "name": "Computer Vision"
}
```
**Пример:**
```bash
curl -X POST "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/specializations" \
  -H "Content-Type: application/json" \
  -d '{"name": "Computer Vision"}'
```

**29. Удалить специализацию из темы**
```http
DELETE /api/themes/{themeId}/specializations/{specializationName}
```
**Пример:**
```bash
curl -X DELETE "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/specializations/Computer%20Vision"
```

**30. Обновить список специализаций темы**
```http
PUT /api/themes/{themeId}/specializations
Content-Type: application/json
```
**Тело запроса:**
```json
[
  "Machine Learning",
  "Data Science", 
  "Computer Vision",
  "NLP"
]
```
**Пример:**
```bash
curl -X PUT "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/specializations" \
  -H "Content-Type: application/json" \
  -d '["Machine Learning", "Data Science", "Computer Vision", "NLP"]'
```

#### 👥 Управление студентами в специализациях

**31. Обновить студентов в специализации**
```http
PUT /api/themes/{themeId}/specializations/{specializationName}/students
Content-Type: application/json
```
**Тело запроса:**
```json
[
  "550e8400-e29b-41d4-a716-446655440000",
  "550e8400-e29b-41d4-a716-446655440001",
  "550e8400-e29b-41d4-a716-446655440002"
]
```
**Пример:**
```bash
curl -X PUT "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/specializations/Machine%20Learning/students" \
  -H "Content-Type: application/json" \
  -d '["550e8400-e29b-41d4-a716-446655440000", "550e8400-e29b-41d4-a716-446655440001", "550e8400-e29b-41d4-a716-446655440002"]'
```

**32. Добавить студента в специализацию**
```http
POST /api/themes/{themeId}/specializations/{specializationName}/students/{studentId}
```
**Пример:**
```bash
curl -X POST "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/specializations/Machine%20Learning/students/550e8400-e29b-41d4-a716-446655440003"
```

**33. Удалить студента из специализации**
```http
DELETE /api/themes/{themeId}/specializations/{specializationName}/students/{studentId}
```
**Пример:**
```bash
curl -X DELETE "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/specializations/Machine%20Learning/students/550e8400-e29b-41d4-a716-446655440003"
```

**34. Получить студентов специализации**
```http
GET /api/themes/{themeId}/specializations/{specializationName}/students?limit=5&useMLSorting=true&onlyActive=true
```
**Параметры:**
- `limit` (опционально) - ограничение количества
- `useMLSorting` (опционально, по умолчанию false) - использовать ML сортировку
- `onlyActive` (опционально, по умолчанию false) - только активные студенты

**Пример:**
```bash
curl "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/specializations/Machine%20Learning/students?limit=10&useMLSorting=true&onlyActive=true" \
  -H "Content-Type: application/json"
```

#### 📋 Получение данных о специализациях

**35. Получить специализации студента**
```http
GET /api/themes/students/{studentId}/specializations
```
**Пример:**
```bash
curl "http://localhost:8080/api/themes/students/550e8400-e29b-41d4-a716-446655440000/specializations" \
  -H "Content-Type: application/json"
```

**Пример ответа:**
```json
{
  "Machine Learning": {
    "660e8400-e29b-41d4-a716-446655440000": 0,
    "660e8400-e29b-41d4-a716-446655440001": 1
  },
  "Data Science": {
    "660e8400-e29b-41d4-a716-446655440000": 2
  }
}
```

#### 🔄 Копирование данных между темами и специализациями

**36. Скопировать студентов темы в специализацию (с заменой)**
```http
POST /api/themes/{themeId}/specializations/{specializationName}/copy-from-theme
```
**Пример:**
```bash
curl -X POST "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/specializations/Machine%20Learning/copy-from-theme"
```

**37. Скопировать студентов темы во все специализации (с заменой)**
```http
POST /api/themes/{themeId}/copy-to-specializations
```
**Пример:**
```bash
curl -X POST "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/copy-to-specializations"
```

**38. Добавить студентов темы в специализацию (без удаления существующих)**
```http
POST /api/themes/{themeId}/specializations/{specializationName}/add-from-theme
```
**Пример:**
```bash
curl -X POST "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/specializations/Machine%20Learning/add-from-theme"
```

**39. Добавить студентов темы во все специализации (без удаления существующих)**
```http
POST /api/themes/{themeId}/add-to-specializations
```
**Пример:**
```bash
curl -X POST "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/add-to-specializations"
```

#### ⚡ Управление активностью в специализациях

**40. Изменить активность студентов в специализации**
```http
PUT /api/themes/{themeId}/specializations/{specializationName}/activity
Content-Type: application/json
```
**Тело запроса:**
```json
{
  "active": false
}
```
**Пример:**
```bash
curl -X PUT "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/specializations/Machine%20Learning/activity" \
  -H "Content-Type: application/json" \
  -d '{"active": false}'
```

### 🤖 ML Функциональность

**41. Применить ML сортировку к специализации**
```http
POST /api/themes/{themeId}/specializations/{specializationName}/ml-sort
```
**Пример:**
```bash
curl -X POST "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/specializations/Machine%20Learning/ml-sort"
```

**42. Применить ML сортировку ко всем специализациям темы**
```http
POST /api/themes/{themeId}/ml-sort-all
```
**Пример:**
```bash
curl -X POST "http://localhost:8080/api/themes/660e8400-e29b-41d4-a716-446655440000/ml-sort-all"
```

**43. Проверить статус ML сервиса**
```http
GET /api/themes/ml-health
```
**Пример:**
```bash
curl "http://localhost:8080/api/themes/ml-health" \
  -H "Content-Type: application/json"
```

**Пример ответа:**
```json
{
  "status": "healthy",
  "service": "ML Matching Service"
}
```

## 🧠 ML Сервис

### API ML сервиса

ML сервис предоставляет следующие endpoints:

**44. Сортировка студентов для специализации**
```http
POST http://localhost:8000/sort-specialization
Content-Type: application/json
```
**Тело запроса:**
```json
{
  "students": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "Иван Петров",
      "hardSkill": "Machine Learning",
      "background": "Опыт работы с Python, ML, глубокое обучение 2 года",
      "interests": "NLP, компьютерное зрение, анализ данных",
      "timeInWeek": "20 часов"
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "name": "Анна Сидорова",
      "hardSkill": "Data Science", 
      "background": "Стажировка в Яндекс, опыт Pandas, NumPy, SQL 1 год",
      "interests": "Анализ данных, визуализация, ML",
      "timeInWeek": "15 часов"
    }
  ],
  "theme": {
    "id": "660e8400-e29b-41d4-a716-446655440000",
    "name": "Разработка ML модели для классификации текстов",
    "description": "Создание и обучение модели для автоматической классификации customer reviews",
    "author": "Др. Петров",
    "specializations": ["Machine Learning", "NLP"]
  },
  "targetSpecialization": "Machine Learning"
}
```
**Пример:**
```bash
curl -X POST "http://localhost:8000/sort-specialization" \
  -H "Content-Type: application/json" \
  -d '{
    "students": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "name": "Иван Петров",
        "hardSkill": "Machine Learning",
        "background": "Опыт работы с Python, ML, глубокое обучение 2 года",
        "interests": "NLP, компьютерное зрение, анализ данных",
        "timeInWeek": "20 часов"
      }
    ],
    "theme": {
      "id": "660e8400-e29b-41d4-a716-446655440000",
      "name": "Разработка ML модели для классификации текстов",
      "description": "Создание и обучение модели для автоматической классификации customer reviews",
      "author": "Др. Петров",
      "specializations": ["Machine Learning", "NLP"]
    },
    "targetSpecialization": "Machine Learning"
  }'
```

**Пример ответа:**
```json
{
  "sortedStudentIds": [
    "550e8400-e29b-41d4-a716-446655440000",
    "550e8400-e29b-41d4-a716-446655440001"
  ]
}
```

**45. Health check ML сервиса**
```http
GET http://localhost:8000/health
```
**Пример:**
```bash
curl "http://localhost:8000/health"
```

**46. Корневой endpoint ML сервиса**
```http
GET http://localhost:8000/
```
**Пример:**
```bash
curl "http://localhost:8000/"
```

### Алгоритм ML сопоставления

#### Детальный процесс:

1. **Предобработка данных:**
   - Нормализация специализаций (поддержка русского и английского)
   - Извлечение навыков из поля background
   - Нормализация времени доступности

2. **Создание текстов для эмбеддингов:**
   - Для студента: `hardSkill + background + interests`
   - Для темы: `name + description + specializations`

3. **Расчет семантического сходства:**
   - Использование multilingual SentenceTransformer модели
   - Cosine similarity между эмбеддингами студентов и темы

4. **Совпадение специализации:**
   - Точное совпадение: 1.0 балл
   - Родственные специализации: 0.7 балла
   - Нет совпадения: 0.0 баллов

5. **Совпадение навыков:**
   - Автоматическое извлечение ключевых навыков
   - Сопоставление с ключевыми словами темы
   - Расчет процента совпадения

6. **Формирование итоговой оценки:**
   ```python
   final_score = (semantic_similarity * 0.4) + 
                 (spec_match * 0.3) + 
                 (skill_match * 0.2) + 
                 (hours_score * 0.1)
   ```

#### Поддерживаемые специализации:

```python
specialization_mapping = {
    'Machine Learning': ['Machine Learning', 'ML', 'AI', 'машинное обучение', 'ml', 'ai', 'machine learning'],
    'Data Science': ['Data Science', 'Data Analytics', 'анализ данных', 'data science', 'data analytics'],
    'NLP': ['NLP', 'Natural Language Processing', 'обработка текста', 'nlp', 'natural language processing'],
    'Computer Vision': ['Computer Vision', 'CV', 'компьютерное зрение', 'computer vision', 'cv'],
    'Data Engineering': ['Data Engineering', 'ETL', 'Big Data', 'инженерия данных', 'data engineering'],
    'Backend': ['Backend', 'API', 'Microservices', 'Server-side', 'бэкенд', 'backend', 'back-end'],
    'Frontend': ['Frontend', 'UI', 'UX', 'Web', 'React', 'Vue', 'фронтенд', 'frontend', 'front-end'],
    'Android': ['Android', 'Mobile', 'Kotlin', 'мобильная разработка', 'android', 'mobile development'],
    'DevOps': ['DevOps', 'Cloud', 'CI/CD', 'Infrastructure', 'девопс', 'devops'],
    'QA': ['QA', 'Testing', 'Test Automation', 'Quality Assurance', 'тестирование', 'qa', 'quality assurance'],
    'UI/UX': ['UI/UX', 'Design', 'User Experience', 'Interface', 'дизайн', 'ui/ux', 'ui', 'ux'],
    'GameDev': ['GameDev', 'Game Development', 'VR', 'AR', 'геймдев', 'game development'],
    'Биоинформатика': ['Биоинформатика', 'Bioinformatics', 'Genomics', 'Biology', 'геномика'],
    'Cybersecurity': ['Cybersecurity', 'Security', 'InfoSec', 'кибербезопасность', 'cybersecurity'],
    'Robotics': ['Robotics', 'Robots', 'Automation', 'робототехника'],
    'Product Analytics': ['Product Analytics', 'Analytics', 'BI', 'Business Intelligence', 'аналитика'],
    'Other': ['Other', 'Другое', 'Прочее', 'Разное']
}
```

#### Связанные специализации:

```python
related_specs = {
    'Machine Learning': ['Data Science', 'NLP', 'Computer Vision', 'Data Analytics'],
    'Data Science': ['Machine Learning', 'Data Engineering', 'NLP', 'Product Analytics'],
    'NLP': ['Machine Learning', 'Data Science'],
    'Computer Vision': ['Machine Learning', 'Data Science'],
    'Backend': ['DevOps', 'Data Engineering'],
    'Frontend': ['UI/UX', 'Android'],
    'Android': ['Frontend', 'UI/UX'],
    'DevOps': ['Backend', 'Data Engineering'],
    'Data Engineering': ['Data Science', 'Backend', 'DevOps'],
    'UI/UX': ['Frontend', 'Android']
}
```

## 🗄 База данных

### Полная схема данных

#### Таблица: students
```sql
CREATE TABLE students (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    hard_skill VARCHAR(100) NOT NULL,
    background TEXT NOT NULL,
    interests TEXT NOT NULL,
    time_in_week VARCHAR(100),
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Таблица: themes
```sql
CREATE TABLE themes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    author VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Таблица: theme_specializations
```sql
CREATE TABLE theme_specializations (
    theme_id UUID NOT NULL REFERENCES themes(id) ON DELETE CASCADE,
    specialization_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (theme_id, specialization_name)
);
```

#### Таблица: theme_student_priority
```sql
CREATE TABLE theme_student_priority (
    theme_id UUID NOT NULL REFERENCES themes(id) ON DELETE CASCADE,
    student_id UUID NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    priority_order INTEGER NOT NULL,
    PRIMARY KEY (theme_id, student_id)
);
```

#### Таблица: theme_specialization_students
```sql
CREATE TABLE theme_specialization_students (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    theme_id UUID NOT NULL REFERENCES themes(id) ON DELETE CASCADE,
    specialization_name VARCHAR(100) NOT NULL,
    student_id UUID NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    priority_order INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(theme_id, specialization_name, student_id)
);
```

#### Таблица: theme_ml_sorted_specializations
```sql
CREATE TABLE theme_ml_sorted_specializations (
    theme_id UUID NOT NULL REFERENCES themes(id) ON DELETE CASCADE,
    specialization_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (theme_id, specialization_name)
);
```

### Индексы для оптимизации

```sql
-- Индексы для theme_specialization_students
CREATE INDEX idx_theme_specialization_students_theme_spec ON theme_specialization_students(theme_id, specialization_name);
CREATE INDEX idx_theme_specialization_students_student ON theme_specialization_students(student_id);
CREATE INDEX idx_theme_specialization_students_priority ON theme_specialization_students(priority_order);

-- Индексы для theme_student_priority
CREATE INDEX idx_theme_student_priority_theme ON theme_student_priority(theme_id);
CREATE INDEX idx_theme_student_priority_student ON theme_student_priority(student_id);

-- Индексы для theme_specializations
CREATE INDEX idx_theme_specializations_theme ON theme_specializations(theme_id);

-- Дополнительные индексы для поиска
CREATE INDEX idx_students_name ON students(name);
CREATE INDEX idx_students_active ON students(active);
CREATE INDEX idx_themes_name ON themes(name);
CREATE INDEX idx_themes_author ON themes(author);
```

### Миграции

Система использует Flyway для управления миграциями в production. Миграции находятся в `src/main/resources/db/migration/`.

**Пример миграции:**
```sql
-- V2__Add_performance_indexes.sql
CREATE INDEX idx_theme_specialization_students_theme_spec ON theme_specialization_students(theme_id, specialization_name);
CREATE INDEX idx_theme_specialization_students_student ON theme_specialization_students(student_id);
CREATE INDEX idx_theme_specialization_students_priority ON theme_specialization_students(priority_order);
CREATE INDEX idx_theme_student_priority_theme ON theme_student_priority(theme_id);
CREATE INDEX idx_theme_student_priority_student ON theme_student_priority(student_id);
CREATE INDEX idx_theme_specializations_theme ON theme_specializations(theme_id);
```

## 💻 Разработка

### Структура проекта

```
spring-boot-kotlin-STT/
├── src/main/kotlin/com/StudentsToThemes/spring_boot_kotlin_STT/
│   ├── controller/
│   │   ├── StudentsController.kt          # 14 endpoints
│   │   └── ThemesController.kt            # 29 endpoints
│   ├── service/
│   │   ├── StudentsService.kt             # Бизнес-логика студентов
│   │   ├── ThemesService.kt               # Бизнес-логика тем
│   │   └── MLSortingService.kt            # Интеграция с ML сервисом
│   ├── repository/
│   │   ├── StudentsRepository.kt          # Spring Data JPA
│   │   ├── ThemesRepository.kt            # Репозиторий тем
│   │   └── ThemeSpecializationStudentRepository.kt
│   ├── entity/
│   │   ├── StudentEntity.kt               # JPA сущность студента
│   │   ├── ThemeEntity.kt                 # JPA сущность темы
│   │   └── ThemeSpecializationStudent.kt  # Связь студент-специализация
│   ├── DTO/
│   │   ├── StudentResponseDto.kt          # Response DTO
│   │   ├── ThemeResponseDto.kt            # Response DTO
│   │   ├── CreateStudentRequest.kt        # Request DTO
│   │   ├── CreateThemeRequest.kt          # Request DTO
│   │   ├── StudentWithPriorityDto.kt      # DTO с приоритетом
│   │   ├── ThemeWithPriorityDto.kt        # DTO темы с приоритетом
│   │   ├── UpdateStudentRequest.kt        # Request DTO
│   │   ├── UpdateThemeRequest.kt          # Request DTO
│   │   ├── UpdateThemePriorityRequest.kt  # Request DTO
│   │   ├── SpecializationRequest.kt       # Request DTO
│   │   ├── ActiveRequest.kt               # Request DTO
│   │   └── ChangeActivitiesRequest.kt     # Request DTO
│   ├── exception/
│   │   ├── GlobalExceptionHandler.kt      # Обработчик исключений
│   │   ├── StudentNotFoundException.kt    # Кастомное исключение
│   │   └── ThemeNotFoundException.kt      # Кастомное исключение
│   ├── queriesBuilder/
│   │   └── ThemeSpecifications.kt         # Динамические запросы
│   └── SpringBootKotlinSttApplication.kt  # Главный класс
├── src/main/resources/
│   ├── application.yml                    # Основная конфигурация
│   ├── application-dev.yml               # Конфигурация разработки
│   ├── application-prod.yml              # Конфигурация продакшена
│   └── db/migration/                     # Миграции базы данных
├── ml-service/
│   └── main.py                           # ML сервис на Python
├── build.gradle.kts                      # Конфигурация сборки
└── README.md                            # Документация
```

### Сборка и запуск

**Локальная разработка:**
```bash
# Запуск с профилем разработки
./gradlew bootRun --args='--spring.profiles.active=dev'

# Или через IDE:
# Установите активный профиль: dev
```

**Тестирование:**
```bash
# Запуск unit тестов
./gradlew test

# Запуск с генерацией отчета покрытия
./gradlew jacocoTestReport

# Проверка стиля кода
./gradlew ktlintCheck
```

**Производственная сборка:**
```bash
# Очистка и сборка
./gradlew clean build

# Пропуск тестов (для быстрой сборки)
./gradlew build -x test

# Сборка с зависимостями
./gradlew bootJar
```

### Модели данных

#### StudentEntity
```kotlin
@Entity
@Table(name = "students")
class StudentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var name: String = "",
    var hardSkill: String = "",
    var background: String = "",
    var interests: String = "",
    var timeInWeek: String? = null,
    var active: Boolean = true,
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
) {
    @ManyToMany(mappedBy = "priorityStudents")
    val themes: MutableList<ThemeEntity> = mutableListOf()

    @OneToMany(mappedBy = "student", cascade = [CascadeType.ALL], orphanRemoval = true)
    val specializationThemes: MutableList<ThemeSpecializationStudent> = mutableListOf()
}
```

#### ThemeEntity
```kotlin
@Entity
@Table(name = "themes")
class ThemeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var name: String = "",
    var description: String = "",
    var author: String = "",
    
    @ElementCollection
    var specializations: MutableList<String> = mutableListOf(),
    
    @ManyToMany
    @OrderColumn(name = "priority_order")
    var priorityStudents: MutableList<StudentEntity> = mutableListOf(),
    
    @OneToMany(mappedBy = "theme", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("priorityOrder ASC")
    var specializationStudents: MutableList<ThemeSpecializationStudent> = mutableListOf(),
    
    @ElementCollection
    val mlSortedSpecializations: MutableSet<String> = mutableSetOf(),
    
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
)
```

## 🚀 Деплоймент

### Docker развертывание

**Dockerfile для Backend:**
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

# Копирование JAR файла
COPY build/libs/spring-boot-kotlin-STT-1.0.0.jar app.jar

# Создание пользователя для безопасности
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Dockerfile для ML Service:**
```dockerfile
FROM python:3.9-slim

WORKDIR /app

# Копирование requirements и установка зависимостей
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Копирование исходного кода
COPY main.py .

# Создание пользователя для безопасности
RUN addgroup --system python && adduser --system --ingroup python python
USER python:python

EXPOSE 8000

CMD ["python", "main.py"]
```

**docker-compose.yml:**
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:14
    environment:
      POSTGRES_DB: student_themes
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_INITDB_ARGS: "--encoding=UTF8"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./init-db.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

  backend:
    build: 
      context: .
      dockerfile: Dockerfile.backend
    environment:
      DATABASE_URL: jdbc:postgresql://postgres:5432/student_themes
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      ML_SERVICE_URL: http://ml-service:8000
      SPRING_PROFILES_ACTIVE: prod
      JAVA_OPTS: "-Xmx512m -Xms256m"
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy
      ml-service:
        condition: service_started
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/api/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  ml-service:
    build:
      context: ./ml-service
      dockerfile: Dockerfile.ml
    environment:
      PYTHONUNBUFFERED: 1
    ports:
      - "8000:8000"
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8000/health"]
      interval: 30s
      timeout: 10s
      retries: 3

volumes:
  postgres_data:

networks:
  default:
    name: student-themes-network
```

**init-db.sql:**
```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

### Production настройки

**Переменные окружения для production:**
```bash
# Database
export DATABASE_URL=jdbc:postgresql://your-postgres-host:5432/student_themes
export POSTGRES_PASSWORD=your_secure_password_123

# Application
export SPRING_PROFILES_ACTIVE=prod
export SERVER_PORT=8080
export ML_SERVICE_URL=http://your-ml-service-host:8000

# Performance
export JAVA_OPTS="-Xmx1g -Xms512m -XX:+UseG1GC"
export SPRING_JPA_PROPERTIES_HIBERNATE_JDBC_BATCH_SIZE=20
```

**Запуск в production:**
```bash
# С использованием Docker Compose
docker-compose up -d

# Или напрямую с JAR
java -jar spring-boot-kotlin-STT-1.0.0.jar --spring.profiles.active=prod
```

### Мониторинг и логи

**Настройка логирования:**
```yaml
logging:
  level:
    com.StudentsToThemes.spring_boot_kotlin_STT: INFO
    org.springframework: WARN
    org.hibernate: ERROR
  file:
    name: /var/log/student-themes/application.log
    max-size: 10MB
    max-history: 30
  logback:
    rollingpolicy:
      max-file-size: 10MB
      total-size-cap: 1GB
```

**Health checks:**
```bash
# Проверка бэкенда
curl http://localhost:8080/api/actuator/health

# Проверка базы данных
psql -h localhost -U postgres -d student_themes -c "SELECT version();"

# Проверка ML сервиса
curl http://localhost:8000/health

# Проверка интеграции
curl http://localhost:8080/api/themes/ml-health
```

## 📊 Примеры использования

### Сценарий 1: Создание темы и распределение студентов

```bash
# 1. Создаем студентов
curl -X POST "http://localhost:8080/api/students" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Алексей ML разработчик",
    "hardSkill": "Machine Learning",
    "background": "Опыт Python, TensorFlow, PyTorch 3 года",
    "interests": "Глубокое обучение, компьютерное зрение",
    "timeInWeek": "25 часов"
  }'

curl -X POST "http://localhost:8080/api/students" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Мария Data Scientist", 
    "hardSkill": "Data Science",
    "background": "Pandas, NumPy, SQL, статистика 2 года",
    "interests": "Анализ данных, визуализация",
    "timeInWeek": "20 часов"
  }'

# 2. Создаем тему
curl -X POST "http://localhost:8080/api/themes" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Разработка системы предсказания цен недвижимости",
    "description": "Создание ML модели для предсказания цен на недвижимость на основе исторических данных и характеристик объектов",
    "author": "Проф. Смирнов",
    "specializations": ["Machine Learning", "Data Science", "Backend"],
    "priorityStudents": []
  }'

# 3. Добавляем студентов к теме
curl -X POST "http://localhost:8080/api/themes/THEME_ID/students" \
  -H "Content-Type: application/json" \
  -d '["STUDENT_1_ID", "STUDENT_2_ID"]'

# 4. Копируем студентов в специализации
curl -X POST "http://localhost:8080/api/themes/THEME_ID/copy-to-specializations"

# 5. Применяем ML сортировку для Machine Learning специализации
curl -X POST "http://localhost:8080/api/themes/THEME_ID/specializations/Machine%20Learning/ml-sort"

# 6. Проверяем результат
curl "http://localhost:8080/api/themes/THEME_ID/specializations/Machine%20Learning/students?useMLSorting=true"
```

### Сценарий 2: Массовое управление студентами

```bash
# 1. Создаем несколько студентов
curl -X POST "http://localhost:8080/api/students/by-ids" \
  -H "Content-Type: application/json" \
  -d '[
    {
      "name": "Дмитрий Backend",
      "hardSkill": "Backend Development",
      "background": "Java, Spring Boot, PostgreSQL 4 года",
      "interests": "Микросервисы, облачные технологии",
      "timeInWeek": "30 часов"
    },
    {
      "name": "Ольга Frontend",
      "hardSkill": "Frontend Development", 
      "background": "React, TypeScript, CSS 3 года",
      "interests": "UI/UX, мобильная разработка",
      "timeInWeek": "25 часов"
    }
  ]'

# 2. Деактивируем группу студентов
curl -X PUT "http://localhost:8080/api/students/change-activities" \
  -H "Content-Type: application/json" \
  -d '{
    "ids": ["STUDENT_1_ID", "STUDENT_2_ID"],
    "active": false
  }'

# 3. Удаляем неактивных студентов
curl -X DELETE "http://localhost:8080/api/students/unactive"
```

### Сценарий 3: Поиск и фильтрация

```bash
# Поиск студентов по навыкам
curl "http://localhost:8080/api/students?hardSkill=Machine%20Learning&background=Python"

# Поиск тем по автору и описанию
curl "http://localhost:8080/api/themes?author=Петров&description=анализ"

# Получение активных студентов с ограничением
curl "http://localhost:8080/api/students/active"

# Получение студентов специализации с ML сортировкой
curl "http://localhost:8080/api/themes/THEME_ID/specializations/Data%20Science/students?useMLSorting=true&onlyActive=true"
```

## 🔧 Устранение неисправностей

### Распространенные проблемы

**1. Ошибка подключения к базе данных:**
```bash
# Проверка подключения к PostgreSQL
psql -h localhost -U postgres -d student_themes

# Проверка расширения UUID
\c student_themes
SELECT * FROM pg_extension WHERE extname = 'uuid-ossp';
```

**2. ML сервис недоступен:**
```bash
# Проверка порта
netstat -tulpn | grep 8000

# Проверка зависимостей Python
python -c "import sentence_transformers; print('OK')"

# Перезапуск ML сервиса
pkill -f "python main.py"
python main.py
```

**3. Ошибки миграции Flyway:**
```bash
# Проверка состояния миграций
./gradlew flywayInfo -Dspring.profiles.active=prod

# Восстановление после ошибки
./gradlew flywayRepair -Dspring.profiles.active=prod
```

**4. Проблемы с памятью:**
```bash
# Увеличить память JVM
export JAVA_OPTS="-Xmx2g -Xms1g"

# Для ML сервиса (если большая модель)
export TRANSFORMERS_CACHE=/path/to/cache
```

### Логи и диагностика

**Просмотр логов:**
```bash
# Backend логи
tail -f logs/application.log

# ML сервис логи (консоль)
# Или перенаправление в файл:
python main.py > ml-service.log 2>&1

# Логи базы данных
sudo tail -f /var/lib/postgresql/*/log/postgresql-*.log
```

**Проверка здоровья системы:**
```bash
#!/bin/bash
# health-check.sh

echo "=== System Health Check ==="

# Backend
echo "Backend:"
curl -s http://localhost:8080/api/actuator/health | jq .

# Database
echo "Database:"
psql -h localhost -U postgres -d student_themes -c "SELECT version();" 2>/dev/null || echo "Database connection failed"

# ML Service
echo "ML Service:"
curl -s http://localhost:8000/health | jq . 2>/dev/null || echo "ML Service unavailable"

echo "=== Check Complete ==="
```

## 📞 Поддержка

### Полезные команды

**Быстрая проверка системы:**
```bash
# Однострочник для проверки всех компонентов
curl -s http://localhost:8080/api/themes/ml-health && \
curl -s http://localhost:8080/api/actuator/health && \
curl -s http://localhost:8000/health && \
echo "All systems operational"
```

**Сброс тестовых данных:**
```bash
# Удаление всех данных (осторожно!)
curl -X DELETE "http://localhost:8080/api/students/all"
curl -X DELETE "http://localhost:8080/api/themes/all"
```

### Контакты для поддержки

- **Backend вопросы**: backend-team@example.com
- **ML вопросы**: ml-team@example.com  
- **Базы данных**: dba@example.com
- **Экстренные случаи**: oncall@example.com

### Полезные ссылки

- **Документация API**: http://localhost:8080/api/swagger-ui.html
- **ML Service Docs**: http://localhost:8000/docs
- **База данных**: http://localhost:5432
- **Мониторинг**: http://localhost:8080/api/actuator

---

**Лицензия**: MIT  
**Версия**: 1.0.0  
**Дата последнего обновления**: 2024-01-15

Для получения дополнительной помощи обращайтесь к документации или создавайте issue в репозитории проекта.