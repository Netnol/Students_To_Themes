## 🚀 Деплоймент

- [Docker развертывание](#docker-развертывание)
  - [Dockerfile для Backend](#dockerfile-для-backend)
  - [Dockerfile для ML Service](#dockerfile-для-ml-service)
  - [docker-compose.yml](#docker-compose-yml)
  - [init-db.sql](#init-db-sql)
- [Production настройки](#production-настройки)
  - [Переменные окружения для production](#переменные-окружения-для-production)
  - [Запуск в production](#запуск-в-production)
- [Мониторинг и логи](#мониторинг-и-логи)
  - [Настройка логирования](#настройка-логирования)
  - [Health checks](#health-checks)

### <a id="docker-развертывание">Docker развертывание</a>


**<a id="dockerfile-для-backend">Dockerfile для Backend:</a>**
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

**<a id="dockerfile-для-ml-service">Dockerfile для ML Service:</a>**
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

**<a id="docker-compose-yml">docker-compose.yml:</a>**
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

**<a id="init-db-sql">init-db.sql:</a>**
```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

### <a id="production-настройки">Production настройки</a>

**<a id="переменные-окружения-для-production">Переменные окружения для production:</a>**
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

**<a id="запуск-в-production">Запуск в production:</a>**
```bash
# С использованием Docker Compose
docker-compose up -d

# Или напрямую с JAR
java -jar spring-boot-kotlin-STT-1.0.0.jar --spring.profiles.active=prod
```

### <a id="мониторинг-и-логи">Мониторинг и логи</a>

**<a id="настройка-логирования">Настройка логирования:</a>**
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

**<a id="health-checks">Health checks:</a>**
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