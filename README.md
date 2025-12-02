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
- [Структура Проекта](#cтруктура_проекта)
- [Деплоймент](#деплоймент)
- [Примеры использования](#примеры-использования)
- [Устранение неисправностей](#устранение-неисправностей)
- [Поддержка](#поддержка)

## <a id = "обзор-системы">🎯 Обзор системы</a>

Полная система для управления студентами, темами проектов и интеллектуального распределения с использованием машинного обучения. Система поддерживает многовариантное распределение по специализациям, ручное и автоматическое упорядочивание приоритетов.

### Ключевые возможности:
- **Полное CRUD управление** студентами и темами
- **Система приоритетов** - ручное и автоматическое упорядочивание
- **Специализации** - многовариантное распределение по направлениям
- **ML-сопоставление** - интеллектуальное распределение на основе семантического анализа
- **Массовые операции** - управление группами студентов
- **Гибкий поиск** - фильтрация по всем полям
- **Управление активностью** - активация/деактивация студентов

## <a id = "технологии">🛠 Технологии</a>

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

## <a id = "архитектура">🏗 Архитектура </a>

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

## <a id = "быстрый-старт">🚀 Быстрый старт </a>

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

## 1. Установка и настройка базы данных

Вам нужна база данных, чтобы запустить Spring-Boot, без нее он не запуститься.
Расскажу как все настроить на примере Supabase:
### 1. Заходите на https://supabase.com/
<img width="1477" height="1232" alt="image" src="https://github.com/user-attachments/assets/af26ea5d-acf6-4b2d-bc60-713a25e2a8f5" />

### 2. Нажимайте кнопку "Start your project"
<img width="495" height="656" alt="image" src="https://github.com/user-attachments/assets/cb986342-17c0-4dd6-8690-8bff3cbe43f4" />

### 3. Нажимайте кнопку "Sign Up Now"
<img width="453" height="555" alt="image" src="https://github.com/user-attachments/assets/3fdecdf0-14ca-48af-ac75-f27825ca7a5b" />

### 4. Регистрируетесь

### 5. После входа в аккаунт создете организацию (если у вас нет проекта его сразу предлагают создать при входе)
<img width="700" height="478" alt="image" src="https://github.com/user-attachments/assets/0b3b7a1b-817a-40da-bf42-92093fe109e2" />

### 6. Создаете проект и запоминаете Database password, так как это как раз то поле, что нужно будет подать в конфигурацию POSTGRES_PASSWORD при запуске проекта Spring
<img width="731" height="638" alt="image" src="https://github.com/user-attachments/assets/1676a1d6-f33a-4801-954b-be0c3acfb14e" />

### 7. У вас должен был создаться проект
<img width="3440" height="1317" alt="image" src="https://github.com/user-attachments/assets/796a6aa5-0c32-4820-9d8a-23c45f59fbd4" />

### 8. Сверху слева есть кнопка "Connect", нажимаете её
<img width="1415" height="265" alt="image" src="https://github.com/user-attachments/assets/b1d77aa1-d497-4ee7-8df0-f057335b324e" />

### 9. У вас откроется такое меню
<img width="1063" height="601" alt="image" src="https://github.com/user-attachments/assets/1c530f80-764e-40bb-93f7-3e85379679b5" />

### 10. Нажимаете на выбор "Type"
<img width="1037" height="733" alt="image" src="https://github.com/user-attachments/assets/4cbdcd38-b3a1-4c41-92f4-190fa5b9ec90" />

### 11. Выбираете JDBC
<img width="1077" height="640" alt="image" src="https://github.com/user-attachments/assets/4007db8b-3974-42b4-9d55-43cf1092da5d" />

###12. Сохраняете эту строку
<img width="1030" height="597" alt="image" src="https://github.com/user-attachments/assets/4bc9e1f5-c009-4479-9923-49e2ebe410c3" />

###13. Также у вас могут быть проблемы с подключением по "Dirrect connection", тогда смените его на Session Pooler и сохраняете новую строку JDBC как в пункте 12
<img width="1049" height="590" alt="image" src="https://github.com/user-attachments/assets/708bb15e-409d-4697-847b-a0749d207e9c" />

## 2. Настройка переменных окружения

## 1. Если у вас есть IDEA, то:
### 1. Открываете в ней проект
<img width="3440" height="921" alt="image" src="https://github.com/user-attachments/assets/d5a4b0f1-5491-44b7-9b89-bb1eaed64564" />

### 2. Нажимаете сверху справа на этот значок
<img width="856" height="610" alt="image" src="https://github.com/user-attachments/assets/3631925f-cc21-459e-94dc-ed25160a020e" />

### 3. Нажимаете "Edit Configurations..."
<img width="315" height="167" alt="image" src="https://github.com/user-attachments/assets/edd6d6eb-e5e2-40ae-bb7a-3bc6074c58d6" />

### 4. У вас открывается такое окно
<img width="824" height="705" alt="image" src="https://github.com/user-attachments/assets/107442ce-9682-45b6-bf32-b2748f08858c" />

### 5. Вводите в поле "Enviroment variables:" строку:
DATABASE_URL=<URL>;POSTGRES_PASSWORD=<Password>;SPRING_PROFILES_ACTIVE=<profile>
где вместо <URL> вы подставляете строку из 12 пункта в "1. Установка и настройка базы данных", но убираете "?user=postgres&password=[YOUR_PASSWORD]" с конца строки
вместо <Password> вставляете пароль из 6 пункта
вместо <profile> вставляете "dev" или "prod" в зависимости от конфигурации, которую хотите использовать (в первый раз запускайте с "dev")
<img width="571" height="76" alt="image" src="https://github.com/user-attachments/assets/929c72f5-d082-47a0-8149-20985ddeb8c6" />

### 5-1. Если "Enviroment variables:" нету, то:
Нажимаете "Modify options"
<img width="570" height="180" alt="image" src="https://github.com/user-attachments/assets/7c572a89-4439-42a0-84dd-7c5345899253" />

### 5-2. И ваыбираете здесь "Enviroment variables"
<img width="457" height="591" alt="image" src="https://github.com/user-attachments/assets/369864df-467f-4fa3-bc12-ac790089e449" />

### 2. Если у вас нет IDEA, то ориентируясь на то как было в IDEA, сделаете нужным вам способом:
#### 1. **Подготовка переменных окружения**
#### Требуемые переменные:
```bash
# Обязательные
DATABASE_URL=jdbc:postgresql://localhost:5432/students_themes_db
POSTGRES_PASSWORD=your_password

# Опциональные (значения по умолчанию)
SPRING_PROFILES_ACTIVE=dev  # или prod
PORT=8080
ML_SERVICE_URL=http://localhost:8000
```

## 2. **Способы запуска**

### Способ 1: Командная строка (Linux/Mac)
```bash
# Установка переменных окружения
export DATABASE_URL=jdbc:postgresql://localhost:5432/students_themes_db
export POSTGRES_PASSWORD=your_password
export SPRING_PROFILES_ACTIVE=dev
export PORT=8080

# Запуск приложения
./gradlew bootRun
```

### Способ 2: Командная строка (Windows)
```cmd
# Установка переменных окружения
set DATABASE_URL=jdbc:postgresql://localhost:5432/students_themes_db
set POSTGRES_PASSWORD=your_password
set SPRING_PROFILES_ACTIVE=dev
set PORT=8080

# Запуск приложения
gradlew.bat bootRun
```

### Способ 3: One-liner (Linux/Mac)
```bash
DATABASE_URL="jdbc:postgresql://localhost:5432/students_themes_db" \
POSTGRES_PASSWORD="your_password" \
SPRING_PROFILES_ACTIVE="dev" \
PORT=8080 \
./gradlew bootRun
```

### Способ 4: One-liner (Windows PowerShell)
```powershell
$env:DATABASE_URL="jdbc:postgresql://localhost:5432/students_themes_db"; `
$env:POSTGRES_PASSWORD="your_password"; `
$env:SPRING_PROFILES_ACTIVE="dev"; `
$env:PORT=8080; `
.\gradlew.bat bootRun
```

## 3. **Запуск с помощью .env файла**

### Создайте файл `.env` в корне проекта:
```env
DATABASE_URL=jdbc:postgresql://localhost:5432/students_themes_db
POSTGRES_PASSWORD=your_password
SPRING_PROFILES_ACTIVE=dev
PORT=8080
ML_SERVICE_URL=http://localhost:8000
```

### Запуск с использованием .env файла:

#### Linux/Mac (с установкой `direnv` или `dotenv`):
```bash
# Установите direnv
brew install direnv

# Настройте direnv для проекта
echo 'export $(cat .env | xargs)' > .envrc
direnv allow

# Запустите приложение
./gradlew bootRun
```

#### Альтернатива: использование bash-скрипта
Создайте файл `run.sh`:
```bash
#!/bin/bash

# Загружаем переменные из .env файла
set -a
source .env
set +a

# Запускаем приложение
./gradlew bootRun
```

Сделайте скрипт исполняемым и запустите:
```bash
chmod +x run.sh
./run.sh
```

### 4. **Запуск в Docker с переменными окружения**

#### Dockerfile:
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Docker Compose (рекомендуется):
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: students_themes_db
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_USER: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  app:
    build: .
    environment:
      DATABASE_URL: jdbc:postgresql://postgres:5432/students_themes_db
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE:-dev}
      PORT: 8080
    ports:
      - "8080:8080"
    depends_on:
      - postgres

  ml-service:
    build: ./ml-service
    ports:
      - "8000:8000"

volumes:
  postgres_data:
```

#### Запуск Docker Compose:
```bash
# Создайте файл .env
echo "POSTGRES_PASSWORD=your_password" > .env
echo "SPRING_PROFILES_ACTIVE=prod" >> .env

# Запустите
docker-compose up --build
```

### 6. **Запуск готового JAR файла**

```bash
# Сборка проекта
./gradlew build

# Запуск с переменными окружения
DATABASE_URL=jdbc:postgresql://localhost:5432/students_themes_db \
POSTGRES_PASSWORD=your_password \
SPRING_PROFILES_ACTIVE=prod \
java -jar build/libs/spring-boot-kotlin-STT-*.jar
```

### 7. **Проверка переменных окружения**

Если вы хотте проверить, то добавьте в код для отладки (временный):

```kotlin
@SpringBootApplication
class SpringBootKotlinSttApplication {
    @PostConstruct
    fun logEnvVars() {
        println("=== Environment Variables ===")
        println("DATABASE_URL: ${System.getenv("DATABASE_URL")}")
        println("SPRING_PROFILES_ACTIVE: ${System.getenv("SPRING_PROFILES_ACTIVE")}")
        println("PORT: ${System.getenv("PORT")}")
    }
}
```

### 8. **Примеры для разных окружений**

# Локальная разработка (dev):
```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/students_themes_dev
export POSTGRES_PASSWORD=dev_password
export SPRING_PROFILES_ACTIVE=dev
export ML_SERVICE_URL=http://localhost:8000
./gradlew bootRun
```

#### Продакшн (prod):
```bash
export DATABASE_URL=jdbc:postgresql://prod-db.example.com:5432/students_themes_prod
export POSTGRES_PASSWORD=secure_prod_password
export SPRING_PROFILES_ACTIVE=prod
export ML_SERVICE_URL=http://ml-service.prod.svc.cluster.local:8000
java -jar app.jar
```

#### Docker-контейнер (Kubernetes):
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: spring-app
spec:
  containers:
  - name: app
    image: your-registry/app:latest
    env:
    - name: DATABASE_URL
      valueFrom:
        secretKeyRef:
          name: db-secret
          key: url
    - name: POSTGRES_PASSWORD
      valueFrom:
        secretKeyRef:
          name: db-secret
          key: password
    - name: SPRING_PROFILES_ACTIVE
      value: "prod"
```

### 9. **Устранение проблем**

Если переменные не загружаются:

1. Проверьте правильность имен переменных (чувствительны к регистру в Linux/Mac)
2. Убедитесь, что переменные экспортированы в той же сессии терминала
3. Проверьте `.env` файл на наличие синтаксических ошибок
4. Для Windows: используйте `set` вместо `export`
5. Перезапустите терминал/IDE после установки переменных

### 10. **Быстрый старт (TL;DR)**

```bash
# 1. Клонируйте репозиторий
git clone <your-repo>
cd spring-boot-kotlin-STT

# 2. Создайте базу данных PostgreSQL
createdb students_themes_db

# 3. Запустите с переменными окружения
DATABASE_URL=jdbc:postgresql://localhost:5432/students_themes_db \
POSTGRES_PASSWORD=postgres \
SPRING_PROFILES_ACTIVE=dev \
./gradlew bootRun

# 4. Приложение будет доступно по http://localhost:8080
```

#### Примечания:
- Для профиля `prod` убедитесь, что миграции Flyway применены
- ML-сервис должен быть запущен отдельно, если используется ML-сортировка
- Для продакшена используйте сильные пароли и защищенные соединения (SSL)

#### 3. Запуск Backend приложения
Пример:
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

Если у вас при запуске возникла примерно такая проблема, то смотрите пункт 13 в "1. Установка и настройка базы данных":
```
2025-12-02 22:55:12 - c.S.s.SpringBootKotlinSttApplicationKt - Starting SpringBootKotlinSttApplicationKt v1.0.0-dev using Java 21.0.4 with PID 44384 (C:\Spring\spring-boot-kotlin-STT\build\classes\kotlin\main started by Netnol in C:\Spring\spring-boot-kotlin-STT)
2025-12-02 22:55:12 - c.S.s.SpringBootKotlinSttApplicationKt - Running with Spring Boot v3.5.7, Spring v6.2.12
2025-12-02 22:55:12 - c.S.s.SpringBootKotlinSttApplicationKt - The following 1 profile is active: "dev"
2025-12-02 22:55:13 - o.s.d.r.c.RepositoryConfigurationDelegate - Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2025-12-02 22:55:13 - o.s.d.r.c.RepositoryConfigurationDelegate - Finished Spring Data repository scanning in 45 ms. Found 3 JPA repository interfaces.
2025-12-02 22:55:13 - o.s.b.w.e.tomcat.TomcatWebServer - Tomcat initialized with port 8080 (http)
2025-12-02 22:55:13 - o.a.catalina.core.StandardService - Starting service [Tomcat]
2025-12-02 22:55:13 - o.a.catalina.core.StandardEngine - Starting Servlet engine: [Apache Tomcat/10.1.48]
2025-12-02 22:55:13 - o.a.c.c.C.[Tomcat].[localhost].[/] - Initializing Spring embedded WebApplicationContext
2025-12-02 22:55:13 - o.s.b.w.s.c.ServletWebServerApplicationContext - Root WebApplicationContext: initialization completed in 874 ms
2025-12-02 22:55:14 - o.s.o.j.p.SpringPersistenceUnitInfo - No LoadTimeWeaver setup: ignoring JPA class transformer
2025-12-02 22:55:14 - com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Starting...
2025-12-02 22:55:15 - o.h.e.jdbc.spi.SqlExceptionHelper - SQL Error: 0, SQLState: 08001
2025-12-02 22:55:15 - o.h.e.jdbc.spi.SqlExceptionHelper - Ошибка при попытке подсоединения.
2025-12-02 22:55:15 - o.h.e.j.e.i.JdbcEnvironmentInitiator - HHH000342: Could not obtain connection to query metadata
org.hibernate.exception.JDBCConnectionException: unable to obtain isolated JDBC connection [Ошибка при попытке подсоединения.] [n/a]
Caused by: org.postgresql.util.PSQLException: Ошибка при попытке подсоединения.
Caused by: java.net.UnknownHostException: db.vkhuqmdbzoakdnxlibsx.supabase.co
2025-12-02 22:55:15 - o.s.o.j.LocalContainerEntityManagerFactoryBean - Failed to initialize JPA EntityManagerFactory: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
2025-12-02 22:55:15 - o.s.b.w.s.c.AnnotationConfigServletWebServerApplicationContext - Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
2025-12-02 22:55:15 - o.a.catalina.core.StandardService - Stopping service [Tomcat]
2025-12-02 22:55:15 - o.s.b.a.l.ConditionEvaluationReportLogger - 

Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2025-12-02 22:55:15 - o.s.boot.SpringApplication - Application run failed
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in class path resource [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
Caused by: org.hibernate.service.spi.ServiceException: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
Caused by: org.hibernate.HibernateException: Unable to determine Dialect without JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)

Process finished with exit code 1
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

## <a id = "конфигурация">⚙️ Конфигурация </a>
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

### Сборка и запуск

**Локальная разработка:**
```bash
# Запуск с профилем разработки
./gradlew bootRun --args='--spring.profiles.active=dev'

# Или через IDE:
# Установите активный профиль: dev
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

## <a id = "полное-описание-api">📡 Полное описание API </a>

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

## <a id = "ml-сервис">🧠 ML Сервис </a>

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

## <a id = "база-данных">🗄 База данных </a>

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

## <a id = "cтруктура_проекта">💻 Структура проекта</a>

## Архитектура проекта

```
spring-boot-kotlin-STT/
├── src/main/kotlin/com/StudentsToThemes/spring_boot_kotlin_STT/
│   ├── controller/                    # REST контроллеры
│   │   ├── StudentsController.kt     # Контроллер для работы со студентами
│   │   └── ThemesController.kt       # Контроллер для работы с темами
│   ├── service/                      # Сервисный слой
│   │   ├── StudentsService.kt        # Сервис для студентов
│   │   ├── ThemesService.kt          # Сервис для тем
│   │   ├── MLSortingService.kt       # Сервис интеграции с ML
│   │   ├── StudentMappers.kt         # Мапперы для студентов
│   │   └── ThemeMappers.kt           # Мапперы для тем
│   ├── repository/                   # Репозитории (Data Access Layer)
│   │   ├── StudentsRepository.kt     # Репозиторий студентов
│   │   ├── ThemesRepository.kt       # Репозиторий тем
│   │   └── ThemeSpecializationStudentRepository.kt # Репозиторий связей
│   ├── entity/                       # JPA сущности
│   │   ├── StudentEntity.kt          # Сущность студента
│   │   ├── ThemeEntity.kt            # Сущность темы
│   │   └── ThemeSpecializationStudent.kt # Связь студента и специализации
│   ├── DTO/                         # Data Transfer Objects
│   │   ├── StudentResponseDto.kt     # DTO ответа студента
│   │   ├── ThemeResponseDto.kt       # DTO ответа темы
│   │   ├── CreateStudentRequest.kt   # DTO создания студента
│   │   ├── UpdateStudentRequest.kt   # DTO обновления студента
│   │   ├── CreateThemeRequest.kt     # DTO создания темы
│   │   ├── UpdateThemeRequest.kt     # DTO обновления темы
│   │   ├── ActiveRequest.kt          # DTO активности
│   │   ├── ChangeActivitiesRequest.kt # DTO изменения активности
│   │   ├── StudentWithPriorityDto.kt # DTO студента с приоритетом
│   │   ├── ThemeWithPriorityDto.kt   # DTO темы с приоритетом
│   │   ├── SpecializationRequest.kt  # DTO специализации
│   │   └── UpdateThemePriorityRequest.kt # DTO обновления приоритета
│   ├── exception/                    # Обработка исключений
│   │   ├── StudentNotFoundException.kt
│   │   ├── ThemeNotFoundException.kt
│   │   └── GlobalExceptionHandler.kt
│   ├── queriesBuilder/              # Построители запросов и спецификации
│   │   ├── StudentSpecifications.kt # Спецификации для динамического поиска студентов
│   │   └── ThemeSpecifications.kt   # Спецификации для динамического поиска тем
│   └── SpringBootKotlinSttApplication.kt # Главный класс приложения
├── src/main/resources/
│   ├── application.yml              # Основная конфигурация
│   ├── application-dev.yml          # Конфигурация разработки
│   ├── application-prod.yml         # Конфигурация продакшена
│   └── db/migration/               # Миграции базы данных
│       └── V2__Add_performance_indexes.sql
├── ml-service/                      # ML-сервис (отдельный проект)
│   └── main.py                     # Python ML-сервис
└── build.gradle.kts                 # Конфигурация сборки
```

## Модули и их ответственность

### 1. **Слой контроллеров (`controller/`)**
- **StudentsController**: Управление REST эндпоинтами для студентов (CRUD операции, фильтрация, изменение активности)
- **ThemesController**: Управление REST эндпоинтами для тем (CRUD, управление студентами в темах, специализации, ML-сортировка)

### 2. **Сервисный слой (`service/`)**
- **StudentsService**: Бизнес-логика для операций со студентами
- **ThemesService**: Бизнес-логика для операций с темами и специализациями
- **MLSortingService**: Интеграция с внешним ML-сервисом для сортировки студентов
- **Мапперы**: Преобразование между сущностями и DTO (StudentMappers.kt, ThemeMappers.kt)

### 3. **Слой доступа к данным (`repository/`)**
- **StudentsRepository**: CRUD операции для сущности StudentEntity
- **ThemesRepository**: CRUD операции для сущности ThemeEntity
- **ThemeSpecializationStudentRepository**: Управление связями студентов и специализаций

### 4. **Сущности (`entity/`)**
- **StudentEntity**: Представляет студента в системе
- **ThemeEntity**: Представляет тему (проект) с возможностью специализаций
- **ThemeSpecializationStudent**: Связь многие-ко-многим между студентами и специализациями тем

### 5. **DTO слои (`DTO/`)**
- **Запросы (Request DTOs)**: Валидация входных данных
- **Ответы (Response DTOs)**: Структурированные ответы API
- **Специализированные DTOs**: Для конкретных операций (сортировка, приоритеты)

### 6. **Построители запросов (`queriesBuilder/`)**
- **StudentSpecifications**: Реализация спецификаций для динамической фильтрации студентов по различным полям
- **ThemeSpecifications**: Реализация спецификаций для динамической фильтрации тем

### 7. **Обработка исключений (`exception/`)**
- **GlobalExceptionHandler**: Централизованная обработка исключений
- **StudentNotFoundException**, **ThemeNotFoundException**: Специализированные исключения

### 8. **Инфраструктура**
- **Конфигурационные файлы**: Поддержка разных окружений (dev/prod)
- **Миграции базы данных**: Управление схемой БД через Flyway

## Основные сущности базы данных

### Student
- `id` (UUID): Уникальный идентификатор
- `name`, `hardSkill`, `background`, `interests`: Основная информация
- `active`: Статус активности
- `createdAt`, `updatedAt`: Таймстампы

### Theme
- `id` (UUID): Уникальный идентификатор
- `name`, `description`, `author`: Описание темы
- `specializations`: Список специализаций (массив)
- `priorityStudents`: Основной список студентов с приоритетами
- `mlSortedSpecializations`: Множество специализаций, отсортированных с помощью ML

### ThemeSpecializationStudent (Связующая таблица)
- Связывает студентов со специализациями тем
- Содержит `priorityOrder` для сортировки внутри специализации
- Гарантирует уникальность комбинации `theme_id`, `specialization_name`, `student_id`

## Взаимодействие с ML-сервисом

Проект интегрирован с внешним Python ML-сервисом, который:
1. Принимает данные студентов и темы
2. Использует эмбеддинги для семантического анализа
3. Возвращает отсортированный список студентов для специализации
4. Доступен по адресу: `http://localhost:8000`

## Конфигурационные профили

### Dev (`application-dev.yml`)
- Автообновление схемы БД (`ddl-auto: update`)
- Подробное логирование SQL запросов
- Отключен Flyway

### Prod (`application-prod.yml`)
- Валидация схемы БД (`ddl-auto: validate`)
- Включен Flyway для управления миграциями
- Оптимизированное логирование

## Поток данных

```
HTTP Request → Controller → Service → Repository → Database
                                    ↓
                               ML Service (при необходимости)
                                    ↓
HTTP Response ← Controller ← Service ← Repository ← Database
```

## Безопасность и валидация

- Валидация входных данных через аннотации Jakarta Validation
- Централизованная обработка исключений
- Логирование всех операций на разных уровнях
- Поддержка транзакций через Spring Data JPA

## <a id = "деплоймент">🚀 Деплоймент </a>

### Docker развертывание

**Dockerfile для Backend:**
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

# Копирование JAR файла
COPY build/libs/*.jar app.jar

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

## <a id = "примеры-использования">📊 Примеры использования </a>

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

## <a id = "устранение-неисправностей">🔧 Устранение неисправностей </a>

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

## <a id = "поддержка">📞 Поддержка </a>

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

- **Backend вопросы**: 
- **ML вопросы**:  
- **Базы данных**: 
- **Экстренные случаи**: 

### Полезные ссылки

- **Документация API**: 
- **ML Service Docs**:
- **База данных**: 
- **Мониторинг**:

---

**Лицензия**: MIT  
**Версия**: 1.0.0  
**Дата последнего обновления**: 2025-11-28

Для получения дополнительной помощи обращайтесь к документации или создавайте issue в репозитории проекта.
