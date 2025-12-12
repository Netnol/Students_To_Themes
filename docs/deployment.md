## 🚀 Деплоймент

- [Docker развертывание](#docker-развертывание)
- [Production настройки](#production-настройки)
  - [Переменные окружения для production](#переменные-окружения-для-production)
  - [Запуск в production](#запуск-в-production)
- [Мониторинг и логи](#мониторинг-и-логи)
  - [Настройка логирования](#настройка-логирования)
  - [Health checks](#health-checks)

### <a id="docker-развертывание">Docker развертывание</a>

#### Docker Compose
У нас есть 2 файла: docker-compose.yml с внутренней БД и docker-compose2.yml с внешней БД
Тот вариант, который вы хотите использовать назовите docker-compose.yml

Все настройки есть в них, а также в файлах .env и 2 файлах Dockerfile: 1 в папке ML, другой в spring-boot-kotlin-STT

Для успешной работы поменяйте в .env переменные, DATABASE_URL (если он вам нужен) и POSTGRES_PASSWORD как требуется

#### Запуск Docker Compose
```bash
# Запустите
docker-compose up --build

# Для очистки можете ввести: docker-compose down
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