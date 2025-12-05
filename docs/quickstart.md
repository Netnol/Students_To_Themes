
## 📋 Оглавление раздела "Быстрый старт"

- [🚀 Быстрый старт](#быстрый-старт)
  - [Предварительные требования](#предварительные-требования)
  - [1. Установка и настройка базы данных](#1-установка-и-настройка-базы-данных)
  - [2. Настройка переменных окружения](#2-настройка-переменных-окружения)
  - [3. Запуск Backend приложения](#3-запуск-backend-приложения)
  - [4. Запуск ML Сервиса](#4-запуск-ml-сервиса)
  - [5. Проверка интеграции](#5-проверка-интеграции)

## <a id="быстрый-старт">🚀 Быстрый старт</a>

### <a id="предварительные-требования">Предварительные требования</a>

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

### <a id="1-установка-и-настройка-базы-данных">1. Установка и настройка базы данных</a>

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

### <a id="2-настройка-переменных-окружения">2. Настройка переменных окружения</a>

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

### <a id="3-запуск-backend-приложения">3. Запуск Backend приложения</a>

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

### <a id="4-запуск-ml-сервиса">4. Запуск ML Сервиса</a>

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

### <a id="5-проверка-интеграции">5. Проверка интеграции</a>

```bash
# Проверка связи между сервисами
curl http://localhost:8080/themes/ml-health

# Ожидаемый ответ:
# {"status": "healthy", "service": "ML Matching Service"}
```