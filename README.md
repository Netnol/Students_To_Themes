```markdown
# Student-Themes Matching System

Полная система для сопоставления студентов с темами проектов с использованием машинного обучения. Состоит из Spring Boot бэкенда на Kotlin и ML микросервиса на Python.

## 📋 Содержание

- [Обзор системы](#обзор-системы)
- [Технологии](#технологии)
- [Архитектура](#архитектура)
- [Быстрый старт](#быстрый-старт)
- [Конфигурация](#конфигурация)
- [API Endpoints](#api-endpoints)
- [ML Сервис](#ml-сервис)
- [База данных](#база-данных)
- [Разработка](#разработка)
- [Деплоймент](#деплоймент)
- [Полная функциональность](#полная-функциональность)

## 🎯 Обзор системы

Система предоставляет полную функциональность для:
- **Управления студентами** - CRUD операции, поиск, фильтрация, управление активностью
- **Управления темами** - создание, редактирование, удаление тем проектов
- **Системы приоритетов** - ручная и автоматическая расстановка приоритетов
- **Специализаций** - многовариантное распределение по направлениям
- **ML сопоставления** - интеллектуальное распределение на основе семантического анализа
- **Управления активностью** - массовые операции со статусами студентов

## 🛠 Технологии

### Backend (Spring Boot Kotlin)
- **Java 17+**
- **Spring Boot 3.x** (Web, Data JPA, Validation)
- **Kotlin** с корутинами
- **PostgreSQL 12+** - реляционная база данных
- **JPA/Hibernate** - ORM с lazy loading
- **Flyway** - управление миграциями (production)
- **Gradle** - система сборки с Kotlin DSL
- **Spring Data JPA Specifications** - динамические запросы

### ML Сервис (Python FastAPI)
- **Python 3.8+**
- **FastAPI** с автоматической документацией Swagger
- **SentenceTransformers** - multilingual текстовые эмбеддинги
- **scikit-learn** - cosine similarity и метрики
- **pandas/numpy** - обработка и анализ данных
- **uvicorn** - высокопроизводительный ASGI сервер
- **pydantic** - валидация данных

## 🏗 Архитектура

```
┌─────────────────┐    REST API    ┌──────────────────┐
│   Frontend      │◄──────────────►│  Spring Boot     │
│   (клиент)      │                │  Backend (Kotlin)│
└─────────────────┘                └─────────┬────────┘
        │                                    │
        │                              ┌─────┴─────┐
        │                              │ PostgreSQL│
        │                              │  Database │
        │                              └─────┬─────┘
        │                                    │
        │                            REST API│ ML Calls
        │                          ┌─────────┴─────────┐
        └─────────────────────────►│   ML Service      │
                                   │ (Python/FastAPI)  │
                                   └───────────────────┘
```

## 🚀 Быстрый старт

### Предварительные требования

- Java 17 или выше
- PostgreSQL 12+ с включенным UUID расширением
- Python 3.8+ с pip
- Gradle 7.4+

### 1. Клонирование и настройка

```bash
git clone <repository-url>
cd spring-boot-kotlin-STT
```

### 2. Настройка базы данных

```sql
-- Создание базы данных
CREATE DATABASE student_themes;

-- Включение расширения для UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Создание пользователя (опционально)
CREATE USER stt_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE student_themes TO stt_user;
```

### 3. Конфигурация окружения

Создайте `.env` файл:

```properties
DATABASE_URL=jdbc:postgresql://localhost:5432/student_themes
POSTGRES_PASSWORD=your_password
PORT=8080
ML_SERVICE_URL=http://localhost:8000
SPRING_PROFILES_ACTIVE=dev
```

### 4. Запуск системы

**Backend:**
```bash
# Режим разработки
./gradlew bootRun --args='--spring.profiles.active=dev'

# Или production сборка
./gradlew clean build
java -jar build/libs/spring-boot-kotlin-STT-1.0.0.jar
```

**ML Service:**
```bash
# Установка зависимостей
pip install fastapi uvicorn sentence-transformers scikit-learn pandas numpy pydantic

# Запуск сервиса
python main.py
```

## ⚙️ Конфигурация

### Spring Boot профили

**application.yml** (основной):
```yaml
spring:
  datasource:
    url: ${DATABASE_URL}
    username: postgres
    password: ${POSTGRES_PASSWORD}
    driver-class-name: org.postgresql.Driver
  jpa:
    open-in-view: false

server:
  port: ${PORT:8080}
```

**application-dev.yml** (разработка):
```yaml
spring:
  jpa:
    hibernate.ddl-auto: update
    show-sql: true
    properties.hibernate.format_sql: true
  flyway.enabled: false

logging:
  level.com.StudentsToThemes: DEBUG
```

**application-prod.yml** (продакшен):
```yaml
spring:
  jpa:
    hibernate.ddl-auto: validate
    show-sql: false
  flyway.enabled: true

logging:
  level.com.StudentsToThemes: INFO
```

## 📡 API Endpoints

### 🎓 Управление студентами (`/students`)

#### Полное CRUD с расширенными операциями:

**Поиск и фильтрация:**
```http
GET /students?name=Иван&hardSkill=ML&background=Python&interests=NLP&timeInWeek=20
```
Ответ: `List<StudentResponseDto>`

**Получение по ID:**
```http
GET /students/{id}
```
Ответ: `StudentResponseDto`

**Массовое получение:**
```http
GET /students/by-ids
Body: ["uuid1", "uuid2", ...]
```
Ответ: `List<StudentResponseDto>`

**Создание студента:**
```http
POST /students
Body: CreateStudentRequest
```
Ответ: `StudentResponseDto`

**Массовое создание:**
```http
POST /students/by-ids
Body: List<CreateStudentRequest>
```
Ответ: `List<StudentResponseDto>`

**Обновление:**
```http
PUT /students/{id}
Body: UpdateStudentRequest
```
Ответ: `StudentResponseDto`

**Управление активностью:**
```http
PUT /students/{id}/change-activity
Body: {"active": true/false}

PUT /students/change-activities
Body: {"ids": ["uuid1", ...], "active": true/false}
```

**Удаление:**
```http
DELETE /students/{id}
DELETE /students/by-ids
DELETE /students/all
DELETE /students/unactive
```

**Фильтрация по активности:**
```http
GET /students/active
GET /students/unactive
```

### 🎯 Управление темами (`/themes`)

#### Основные операции:

**Поиск тем:**
```http
GET /themes?name=ML&description=анализ&author=Петров
```
Ответ: `List<ThemeResponseDto>`

**Создание темы:**
```http
POST /themes
Body: {
  "name": "Название",
  "description": "Описание", 
  "author": "Автор",
  "specializations": ["ML", "Backend"],
  "priorityStudents": ["studentUuid1", ...]
}
```

**Управление приоритетами:**
```http
PUT /themes/{themeId}/priority
Body: {"studentIds": ["uuid1", "uuid2", ...]} // Новый порядок
```

#### Управление студентами в теме:

**Добавление студентов:**
```http
POST /themes/{themeId}/students/{studentId}
POST /themes/{themeId}/students
Body: ["studentUuid1", "studentUuid2"]
```

**Удаление студентов:**
```http
DELETE /themes/{themeId}/students/{studentId}
DELETE /themes/{themeId}/students
Body: ["studentUuid1", ...]
```

**Получение студентов темы:**
```http
GET /themes/{themeId}/students?limit=10
```
Ответ: `List<StudentWithPriorityDto>`

**Управление активностью:**
```http
PUT /themes/{themeId}/students/active
Body: {"active": true/false}
```

### 🔧 Специализации

#### Управление специализациями тем:

**Добавление специализации:**
```http
POST /themes/{themeId}/specializations
Body: {"name": "Machine Learning"}
```

**Удаление специализации:**
```http
DELETE /themes/{themeId}/specializations/{specializationName}
```

**Обновление списка:**
```http
PUT /themes/{themeId}/specializations
Body: ["ML", "Data Science", "Backend"]
```

#### Управление студентами в специализациях:

**Обновление студентов специализации:**
```http
PUT /themes/{themeId}/specializations/{specName}/students
Body: ["studentUuid1", "studentUuid2", ...] // Новый порядок
```

**Добавление студента:**
```http
POST /themes/{themeId}/specializations/{specName}/students/{studentId}
```

**Удаление студента:**
```http
DELETE /themes/{themeId}/specializations/{specName}/students/{studentId}
```

**Получение студентов специализации:**
```http
GET /themes/{themeId}/specializations/{specName}/students?limit=5&useMLSorting=true&onlyActive=true
```
Параметры:
- `limit` - ограничение количества
- `useMLSorting` - использовать ML сортировку
- `onlyActive` - только активные студенты

### 🤖 ML Функциональность

**ML сортировка специализации:**
```http
POST /themes/{themeId}/specializations/{specName}/ml-sort
```
Применяет ML алгоритм для пересортировки студентов

**Массовая ML сортировка:**
```http
POST /themes/{themeId}/ml-sort-all
```
Применяет ML сортировку ко всем специализациям темы

**Проверка здоровья ML сервиса:**
```http
GET /themes/ml-health
```
Ответ: `{"status": "healthy", "service": "ML Matching Service"}`

### 📊 Дополнительные endpoints

**Темы студента:**
```http
GET /themes/students/{studentId}/themes
```
Ответ: `List<ThemeWithPriorityDto>`

**Специализации студента:**
```http
GET /themes/students/{studentId}/specializations
```
Ответ: `Map<String, Map<UUID, Int>>` // specialization -> (themeId -> priority)

**Копирование студентов:**
```http
POST /themes/{themeId}/copy-to-specializations
POST /themes/{themeId}/add-to-specializations
POST /themes/{themeId}/specializations/{specName}/copy-from-theme
POST /themes/{themeId}/specializations/{specName}/add-from-theme
```

## 🧠 ML Сервис

### Алгоритм сопоставления

#### Компоненты оценки:

1. **Семантическое сходство (40%)**
   - Эмбеддинги текстов студентов и тем
   - Мультиязычная модель: `paraphrase-multilingual-MiniLM-L12-v2`
   - Cosine similarity между эмбеддингами

2. **Совпадение специализации (30%)**
   - Точное совпадение: 1.0
   - Родственные специализации: 0.7
   - Нет совпадения: 0.0

3. **Совпадение навыков (20%)**
   - Автоматическое извлечение навыков из background
   - Сопоставление с ключевыми словами темы

4. **Доступность времени (10%)**
   - Нормализация времени в неделю:
     - ≤10ч: 0.3
     - ≤15ч: 0.6  
     - ≤20ч: 0.8
     - >20ч: 1.0

### Поддерживаемые специализации

Система автоматически нормализует специализации:

```python
specialization_mapping = {
    'Machine Learning': ['Machine Learning', 'ML', 'AI', 'машинное обучение'],
    'Data Science': ['Data Science', 'Data Analytics', 'анализ данных'],
    'NLP': ['NLP', 'Natural Language Processing', 'обработка текста'],
    'Computer Vision': ['Computer Vision', 'CV', 'компьютерное зрение'],
    'Backend': ['Backend', 'API', 'Microservices', 'бэкенд'],
    'Frontend': ['Frontend', 'UI', 'UX', 'Web', 'фронтенд'],
    'Android': ['Android', 'Mobile', 'Kotlin', 'мобильная разработка'],
    'DevOps': ['DevOps', 'Cloud', 'CI/CD', 'Infrastructure', 'девопс'],
    # ... и другие
}
```

### API ML сервиса

**Эндпоинт сортировки:**
```http
POST /sort-specialization
Body: {
  "students": [
    {
      "id": "uuid",
      "name": "Имя", 
      "hardSkill": "ML",
      "background": "Опыт Python...",
      "interests": "NLP, CV",
      "timeInWeek": "20"
    }
  ],
  "theme": {
    "id": "themeUuid",
    "name": "Название темы",
    "description": "Описание темы",
    "author": "Автор",
    "specializations": ["ML", "Data Science"]
  },
  "targetSpecialization": "Machine Learning"
}
```

Ответ:
```json
{
  "sortedStudentIds": ["uuid1", "uuid2", "uuid3", ...]
}
```

**Health checks:**
```http
GET /health
GET /
```

## 🗄 База данных

### Схема данных

#### Основные сущности:

**students:**
- `id` UUID (primary key)
- `name` VARCHAR(100)
- `hard_skill` VARCHAR(100)
- `background` TEXT
- `interests` TEXT  
- `time_in_week` VARCHAR(100)
- `active` BOOLEAN
- `created_at` TIMESTAMP
- `updated_at` TIMESTAMP

**themes:**
- `id` UUID (primary key) 
- `name` VARCHAR
- `description` TEXT
- `author` VARCHAR
- `created_at` TIMESTAMP
- `updated_at` TIMESTAMP

**theme_specializations:**
- `theme_id` UUID (foreign key)
- `specialization_name` VARCHAR(100)

**theme_student_priority:**
- `theme_id` UUID (foreign key)
- `student_id` UUID (foreign key) 
- `priority_order` INTEGER

**theme_specialization_students:**
- `id` UUID (primary key)
- `theme_id` UUID (foreign key)
- `specialization_name` VARCHAR(100)
- `student_id` UUID (foreign key)
- `priority_order` INTEGER
- `created_at` TIMESTAMP
- `updated_at` TIMESTAMP

### Индексы для производительности

```sql
-- Для быстрого поиска студентов по теме и специализации
CREATE INDEX idx_theme_specialization_students_theme_spec 
ON theme_specialization_students(theme_id, specialization_name);

-- Для поиска тем по студенту  
CREATE INDEX idx_theme_specialization_students_student 
ON theme_specialization_students(student_id);

-- Для сортировки по приоритету
CREATE INDEX idx_theme_specialization_students_priority 
ON theme_specialization_students(priority_order);

-- Для поиска студентов основной темы
CREATE INDEX idx_theme_student_priority_theme ON theme_student_priority(theme_id);
CREATE INDEX idx_theme_student_priority_student ON theme_student_priority(student_id);

-- Для поиска специализаций темы
CREATE INDEX idx_theme_specializations_theme ON theme_specializations(theme_id);
```

## 💻 Разработка

### Структура проекта Backend

```
src/main/kotlin/com/StudentsToThemes/spring_boot_kotlin_STT/
├── controller/
│   ├── StudentsController.kt      # 20+ endpoints для студентов
│   └── ThemesController.kt        # 30+ endpoints для тем
├── service/
│   ├── StudentsService.kt         # Бизнес-логика студентов
│   ├── ThemesService.kt           # Бизнес-логика тем (+ ML интеграция)
│   └── MLSortingService.kt        # Клиент для ML сервиса
├── repository/
│   ├── StudentsRepository.kt      # Spring Data JPA репозиторий
│   ├── ThemesRepository.kt        # Репозиторий тем
│   └── ThemeSpecializationStudentRepository.kt
├── entity/
│   ├── StudentEntity.kt           # JPA сущность студента
│   ├── ThemeEntity.kt             # JPA сущность темы
│   └── ThemeSpecializationStudent.kt # Связь студент-специализация
├── DTO/
│   ├── StudentResponseDto.kt      # Response DTO студента
│   ├── ThemeResponseDto.kt        # Response DTO темы
│   ├── CreateStudentRequest.kt    # Request DTO создания
│   ├── CreateThemeRequest.kt      # Request DTO темы
│   ├── StudentWithPriorityDto.kt  # DTO с приоритетом
│   ├── ThemeWithPriorityDto.kt    # DTO темы с приоритетом
│   └── ... (+10 других DTO)
├── exception/
│   ├── GlobalExceptionHandler.kt  # Обработчик исключений
│   ├── StudentNotFoundException.kt
│   └── ThemeNotFoundException.kt
├── queriesBuilder/
│   └── ThemeSpecifications.kt     # Динамические запросы
└── configuration/
    └── (конфигурационные классы)
```

### Ключевые особенности реализации

**Безопасность типов:**
- Kotlin null-safety
- Валидация через Bean Validation (@NotBlank, @Size)
- Кастомные исключения с обработкой

**Производительность:**
- Lazy loading связей
- @EntityGraph для eager loading когда нужно
- Пагинация и ограничения
- Индексы БД

**Масштабируемость:**
- Чистая архитектура с разделением слоев
- DI через Spring
- Микросервисная архитектура ML компонента

### ML Сервис структура

```python
ml-service/
├── main.py                      # FastAPI приложение
├── CSVStudentTopicMatcher       # Основной ML класс
│   ├── __init__()              # Инициализация модели
│   ├── _normalize_specialization() # Нормализация специализаций
│   ├── _extract_skills()       # Извлечение навыков
│   ├── calculate_semantic_similarity() # Семантическое сходство
│   ├── calculate_specialization_match() # Сопоставление специализаций
│   ├── calculate_skill_match() # Сопоставление навыков
│   ├── calculate_comprehensive_score() # Итоговая оценка
│   └── sort_students_for_specialization() # Основной метод
└── requirements.txt            # Зависимости Python
```

## 🚀 Деплоймент

### Docker развертывание

**Dockerfile Backend:**
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Dockerfile ML Service:**
```dockerfile
FROM python:3.9-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt
COPY main.py .
EXPOSE 8000
CMD ["python", "main.py"]
```

**docker-compose.yml:**
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:13
    environment:
      POSTGRES_DB: student_themes
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

  backend:
    build: .
    environment:
      DATABASE_URL: jdbc:postgresql://postgres:5432/student_themes
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      ML_SERVICE_URL: http://ml-service:8000
    ports:
      - "8080:8080"
    depends_on:
      - postgres
      - ml-service

  ml-service:
    build:
      context: .
      dockerfile: Dockerfile.ml
    ports:
      - "8000:8000"

volumes:
  postgres_data:
```

### Production настройки

**Переменные окружения:**
```bash
export DATABASE_URL=jdbc:postgresql://host:5432/student_themes
export POSTGRES_PASSWORD=secure_password
export ML_SERVICE_URL=http://ml-service:8000
export SPRING_PROFILES_ACTIVE=prod
```

**Миграции базы данных:**
```bash
# Flyway автоматически применяет миграции в production
./gradlew flywayMigrate -Dspring.profiles.active=prod
```

## 🔧 Мониторинг и диагностика

### Логирование

**Уровни логирования:**
- DEBUG - разработка (подробные SQL запросы)
- INFO - продакшен (основные операции)
- WARN - предупреждения
- ERROR - ошибки

**Файлы логов:**
```
logs/application.log          # Основной лог приложения
ml-service/logs/              # Логи ML сервиса (консоль)
```

### Health checks

```bash
# Проверка бэкенда
curl http://localhost:8080/actuator/health

# Проверка ML сервиса  
curl http://localhost:8000/health

# Проверка интеграции
curl http://localhost:8080/themes/ml-health
```

### Метрики производительности

- Время ответа API endpoints
- Количество запросов к ML сервису
- Процент успешных ML сортировок
- Использование памяти и CPU

## 📊 Полная функциональность

### 🎓 Студенты

**Управление профилями:**
- Полный CRUD с валидацией
- Поиск по всем текстовым полям (name, hardSkill, background, interests, timeInWeek)
- Массовые операции (создание, обновление, удаление)
- Управление статусом активности

**Приоритеты и распределение:**
- Приоритеты в основных темах
- Приоритеты в специализациях
- Автоматическое вычисление позиций

### 🎯 Темы проектов

**Базовое управление:**
- Создание тем с валидацией специализаций
- Динамический поиск (name, description, author)
- Полное обновление с сохранением связей

**Система приоритетов:**
- Ручная расстановка приоритетов
- Drag-and-drop упорядочивание
- Массовое управление студентами

### 🔧 Специализации

**Гибкая система направлений:**
- Динамическое добавление/удаление специализаций
- Валидация названий (только буквы, цифры, пробелы, дефисы)
- Автоматическое предотвращение дубликатов

**Распределение студентов:**
- Независимые списки для каждой специализации
- Копирование студентов между основной темой и специализациями
- Массовые операции добавления

### 🤖 Интеллектуальное сопоставление

**ML алгоритм:**
- Семантический анализ текстов
- Автоматическое определение навыков
- Учет доступности времени
- Взвешенная scoring система

**Гибкость использования:**
- On-demand сортировка отдельных специализаций
- Массовая сортировка всех специализаций темы
- Real-time сортировка при запросе
- Fallback на ручную сортировку при недоступности ML

### ⚡ Производительность

**Оптимизации БД:**
- Стратегические индексы для всех ключевых запросов
- Lazy loading для тяжелых связей
- Batch operations для массовых операций

**Кэширование:**
- Кэш эмбеддингов в ML сервисе
- Оптимизированные запросы с пагинацией

### 🛡 Безопасность и надежность

**Обработка ошибок:**
- Глобальный обработчик исключений
- Кастомные исключения для доменных ошибок
- Graceful degradation при недоступности ML сервиса

**Валидация:**
- Bean Validation на DTO
- Кастомная валидация бизнес-правил
- Валидация специализаций и приоритетов

### 🔄 Рабочие процессы

**Типичные сценарии использования:**

1. **Создание темы с распределением:**
   - Создать тему со специализациями
   - Добавить студентов в основную тему
   - Скопировать студентов в специализации
   - Применить ML сортировку

2. **Массовое управление:**
   - Изменить активность группы студентов
   - Обновить приоритеты для всей темы
   - Применить ML ко всем специализациям

3. **Поиск и анализ:**
   - Найти студентов по навыкам
   - Получить темы студента с приоритетами
   - Анализировать распределение по специализациям

## 📞 Поддержка и развитие

### Известные ограничения

- ML модель требует ~500MB памяти
- Большие списки студентов (>1000) могут требовать оптимизации
- Мультиязычность ограничена поддерживаемыми моделью языками

### Планы развития

- [ ] Кэширование результатов ML сортировки
- [ ] Расширенная аналитика распределения
- [ ] Интеграция с внешними системами
- [ ] Advanced ML модели с fine-tuning

### Поддержка

**Документация API:**
- Swagger UI: http://localhost:8080/swagger-ui.html
- ML Service Docs: http://localhost:8000/docs

**Мониторинг:**
- Application logs: `logs/application.log`
- Database metrics: PostgreSQL logs
- ML Service: console output

---

**Лицензия:** MIT  
**Версия:** 1.0.0  
**Поддержка:** [team@example.com]
```