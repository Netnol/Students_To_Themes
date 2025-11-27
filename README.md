# Students to Themes Management System

## Оглавление
- [Описание](#описание)
- [Технологии](#технологии)
- [Функциональность](#функциональность)
- [Быстрый старт](#быстрый-старт)
- [API Документация](#api-документация)
- [Структура данных](#структура-данных)
- [Миграции базы данных](#миграции-базы-данных)
- [Профили приложения](#профили-приложения)
- [Примеры использования](#примеры-использования)
- [Разработка](#разработка)

## Описание

REST API сервис для управления темами, студентами и их распределением по специализациям. Система позволяет:

- Создавать и управлять темами с различными специализациями
- Регистрировать студентов с подробной информацией о навыках
- Распределять студентов по темам и специализациям с системой приоритетов
- Гибко управлять списками студентов в основных темах и специализациях

## Технологии

- **Backend**: Kotlin, Spring Boot 3.5.7
- **Database**: PostgreSQL с Flyway для миграций
- **ORM**: Hibernate/JPA
- **Validation**: Bean Validation
- **Build Tool**: Gradle
- **Java**: JDK 21

## Функциональность

### Управление темами
- ✅ Создание, чтение, обновление, удаление тем
- ✅ Динамический поиск тем по названию, описанию, автору
- ✅ Управление списком студентов в теме с приоритетами
- ✅ Добавление/удаление студентов из тем

### Управление студентами
- ✅ Полный CRUD для студентов
- ✅ Поиск студентов по имени, навыкам, интересам
- ✅ Управление активностью студентов
- ✅ Массовые операции со студентами

### Специализации
- ✅ Каждая тема может иметь собственные специализации
- ✅ Независимые списки студентов для каждой специализации
- ✅ Система приоритетов внутри специализаций
- ✅ Копирование основного списка темы в специализацию

### Особенности
- ✅ Валидация данных на всех уровнях
- ✅ Подробное логирование
- ✅ Глобальная обработка ошибок
- ✅ Оптимизированные запросы с индексами
- ✅ Поддержка dev/prod профилей

## Быстрый старт

### Предварительные требования
- JDK 21+
- PostgreSQL 12+
- Gradle 7+

### Настройка базы данных
```sql
-- Создайте базу данных
CREATE DATABASE students_themes;

-- Или используйте переменные окружения для подключения
```

### Настройка окружения
```bash
# Скопируйте и настройте переменные окружения
export DATABASE_URL="jdbc:postgresql://localhost:5432/students_themes"
export POSTGRES_PASSWORD="your_password"
export PORT=8080
```

### Запуск приложения
```bash
# Клонируйте репозиторий
git clone <repository-url>
cd spring-boot-kotlin-STT

# Запуск в dev режиме (по умолчанию)
./gradlew bootRun

# Или соберите и запустите JAR
./gradlew build
java -jar build/libs/spring-boot-kotlin-STT-1.0.0.jar
```

## API Документация

### Базовый URL
```
http://localhost:8080
```

### Темы

#### Получить все темы
```http
GET /themes?name={name}&description={description}&author={author}
```

#### Создать тему
```http
POST /themes
Content-Type: application/json

{
  "name": "Web Development",
  "description": "Modern web technologies",
  "author": "John Doe",
  "specializations": ["Frontend", "Backend", "DevOps"],
  "priorityStudents": ["student-uuid-1", "student-uuid-2"]
}
```

#### Получить тему по ID
```http
GET /themes/{themeId}
```

#### Обновить тему
```http
PUT /themes/{themeId}
Content-Type: application/json

{
  "name": "Updated Name",
  "description": "Updated description",
  "author": "New Author",
  "specializations": ["Frontend", "Mobile"],
  "priorityStudents": ["student-uuid-1", "student-uuid-3"]
}
```

#### Управление специализациями темы
```http
# Добавить специализацию
POST /themes/{themeId}/specializations
Content-Type: application/json
{"name": "Mobile Development"}

# Удалить специализацию
DELETE /themes/{themeId}/specializations/{specializationName}

# Обновить список специализаций
PUT /themes/{themeId}/specializations
Content-Type: application/json
["Frontend", "Backend", "Mobile"]
```

#### Управление студентами в специализациях
```http
# Добавить студента в специализацию
POST /themes/{themeId}/specializations/{specializationName}/students/{studentId}

# Добавить нескольких студентов
POST /themes/{themeId}/specializations/{specializationName}/students
Content-Type: application/json
["student-uuid-1", "student-uuid-2"]

# Обновить список студентов специализации
PUT /themes/{themeId}/specializations/{specializationName}/students
Content-Type: application/json
["student-uuid-1", "student-uuid-2", "student-uuid-3"]

# Скопировать основной список темы в специализацию
POST /themes/{themeId}/specializations/{specializationName}/copy-from-theme

# Получить студентов специализации
GET /themes/{themeId}/specializations/{specializationName}/students?limit=5
```

### Студенты

#### Получить всех студентов
```http
GET /students?name={name}&hardSkill={skill}&background={bg}&interests={int}&timeInWeek={time}
```

#### Создать студента
```http
POST /students
Content-Type: application/json

{
  "name": "Alice Smith",
  "hardSkill": "React, TypeScript",
  "background": "Frontend developer with 2 years experience",
  "interests": "UI/UX, Accessibility",
  "timeInWeek": "20 hours"
}
```

#### Получить студента по ID
```http
GET /students/{studentId}
```

#### Получить детальную информацию о студенте
```http
GET /students/{studentId}/detailed
```

#### Обновить студента
```http
PUT /students/{studentId}
Content-Type: application/json

{
  "name": "Updated Name",
  "hardSkill": "Updated Skills",
  "background": "Updated background",
  "interests": "Updated interests",
  "timeInWeek": "25 hours"
}
```

#### Управление активностью студентов
```http
# Изменить активность одного студента
PUT /students/{studentId}/change-activity
Content-Type: application/json
{"active": false}

# Изменить активность нескольких студентов
PUT /students/change-activities
Content-Type: application/json
{
  "ids": ["uuid-1", "uuid-2"],
  "active": true
}
```

#### Массовые операции
```http
# Получить студентов по IDs
GET /students/by-ids
Content-Type: application/json
["uuid-1", "uuid-2", "uuid-3"]

# Создать нескольких студентов
POST /students/by-ids
Content-Type: application/json
[
  {
    "name": "Student 1",
    "hardSkill": "Skill 1",
    "background": "Background 1",
    "interests": "Interests 1",
    "timeInWeek": "20 hours"
  },
  {
    "name": "Student 2",
    "hardSkill": "Skill 2",
    "background": "Background 2",
    "interests": "Interests 2",
    "timeInWeek": "25 hours"
  }
]

# Удалить нескольких студентов
DELETE /students/by-ids
Content-Type: application/json
["uuid-1", "uuid-2"]
```

### Дополнительные endpoints

#### Получить темы студента
```http
GET /themes/students/{studentId}/themes
```

#### Получить специализации студента
```http
GET /themes/students/{studentId}/specializations
```

#### Получить активных студентов
```http
GET /students/active
```

#### Получить неактивных студентов
```http
GET /students/unactive
```

## Структура данных

### Основные сущности

#### ThemeEntity
- `id` (UUID) - уникальный идентификатор
- `name` - название темы
- `description` - описание темы
- `author` - автор темы
- `specializations` - список специализаций (String)
- `priorityStudents` - основной список студентов с приоритетами
- `specializationStudents` - студенты специализаций

#### StudentEntity
- `id` (UUID) - уникальный идентификатор
- `name` - имя студента
- `hardSkill` - технические навыки
- `background` - опыт и образование
- `interests` - интересы
- `timeInWeek` - доступное время
- `active` - статус активности
- `themes` - темы, в которых состоит студент
- `specializationThemes` - специализации студента

#### ThemeSpecializationStudent
- Связующая сущность между темами, специализациями и студентами
- Содержит поле `priorityOrder` для управления приоритетом

## Миграции базы данных

### Автоматическое создание таблиц
В dev-профиле Hibernate автоматически создает таблицы через `ddl-auto: update`

### Ручные миграции (Flyway)
```sql
-- V2__Add_performance_indexes.sql
CREATE INDEX idx_theme_specialization_students_theme_spec ON theme_specialization_students(theme_id, specialization_name);
CREATE INDEX idx_theme_specialization_students_student ON theme_specialization_students(student_id);
CREATE INDEX idx_theme_specialization_students_priority ON theme_specialization_students(priority_order);
CREATE INDEX idx_theme_student_priority_theme ON theme_student_priority(theme_id);
CREATE INDEX idx_theme_student_priority_student ON theme_student_priority(student_id);
CREATE INDEX idx_theme_specializations_theme ON theme_specializations(theme_id);
```

## Профили приложения

### Dev профиль (`application-dev.yml`)
- Автоматическое создание/обновление схемы БД
- Подробное логирование SQL
- Flyway отключен

### Prod профиль (`application-prod.yml`)
- Валидация схемы БД
- Flyway включен для управления миграциями
- Минимальное логирование

### Активация профилей
```bash
# Dev (по умолчанию)
./gradlew bootRun

# Prod
./gradlew bootRun --args='--spring.profiles.active=prod'

# Через переменные окружения
SPRING_PROFILES_ACTIVE=prod ./gradlew bootRun
```

## Примеры использования

### Сценарий 1: Создание темы со специализациями
```bash
# 1. Создаем тему
curl -X POST http://localhost:8080/themes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Full Stack Development",
    "description": "Complete web development course",
    "author": "Tech University",
    "specializations": ["Frontend", "Backend", "DevOps"],
    "priorityStudents": []
  }'

# 2. Добавляем специализацию
curl -X POST http://localhost:8080/themes/{themeId}/specializations \
  -H "Content-Type: application/json" \
  -d '{"name": "Mobile"}'

# 3. Создаем студентов
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Developer",
    "hardSkill": "JavaScript, React, Node.js",
    "background": "2 years web development",
    "interests": "SPA, REST APIs",
    "timeInWeek": "25 hours"
  }'

# 4. Добавляем студентов в специализацию
curl -X POST http://localhost:8080/themes/{themeId}/specializations/Frontend/students/{studentId}
```

### Сценарий 2: Управление приоритетами
```bash
# Обновить приоритеты студентов в теме
curl -X PUT http://localhost:8080/themes/{themeId}/priority \
  -H "Content-Type: application/json" \
  -d '{
    "studentIds": ["student-uuid-3", "student-uuid-1", "student-uuid-2"]
  }'

# Скопировать основной список в специализацию
curl -X POST http://localhost:8080/themes/{themeId}/specializations/Backend/copy-from-theme
```

## Разработка

### Структура проекта
```
src/main/kotlin/com/StudentsToThemes/spring_boot_kotlin_STT/
├── controller/          # REST контроллеры
├── service/            # Бизнес-логика
├── repository/         # JPA репозитории
├── entity/            # Сущности БД
├── config/            # Конфигурации
└── exception/         # Обработка ошибок

src/main/resources/
├── application.yml           # Основная конфигурация
├── application-dev.yml       # Dev профиль
├── application-prod.yml      # Prod профиль
└── db/migration/            # Миграции БД
```

### Локальная разработка
1. Убедитесь, что PostgreSQL запущен
2. Настройте переменные окружения для БД
3. Запустите приложение в dev-профиле
4. Используйте Postman или curl для тестирования API

### Тестирование
```bash
# Запуск тестов
./gradlew test

# Запуск с покрытием
./gradlew jacocoTestReport
```

### Сборка для продакшена
```bash
# Сборка JAR
./gradlew clean build

# Запуск в prod режиме
java -jar build/libs/spring-boot-kotlin-STT-1.0.0.jar --spring.profiles.active=prod
```

## Поддержка

Для вопросов и предложений обращайтесь к разработчикам проекта.
