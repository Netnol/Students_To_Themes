# 📋 Быстрый старт

## Оглавление раздела "Быстрый старт"

- [Предварительные требования](#предварительные-требования)
- [Установка и настройка базы данных](#установка-и-настройка-базы-данных)
  - [Создание проекта в Supabase](#создание-проекта-в-supabase)
  - [Получение строки подключения JDBC](#получение-строки-подключения-jdbc)
  - [Альтернативные способы подключения](#альтернативные-способы-подключения)
- [Настройка переменных окружения](#настройка-переменных-окружения)
  - [Через IDE (IntelliJ IDEA)](#через-ide-intellij-idea)
  - [Через командную строку](#через-командную-строку)
  - [Через .env файл](#через-env-файл)
  - [Через Docker](#через-docker)
- [Запуск Backend приложения](#запуск-backend-приложения)
- [Запуск ML Сервиса](#запуск-ml-сервиса)
  - [Быстрый старт](#быстрый-старт)
  - [Детальные инструкции для разных ОС](#детальные-инструкции-для-разных-ос)
  - [Docker-способ](#docker-способ)
  - [Проверка работоспособности](#проверка-работоспособности)
- [Проверка интеграции](#проверка-интеграции)
- [Устранение проблем](#устранение-проблем)

<a id="предварительные-требования"></a>
## 📌 Предварительные требования

```bash
# Проверка установки Java
java -version  # Должна быть 21

# Проверка PostgreSQL
psql --version  # Должна быть 12 или выше

# Проверка Python
python --version  # Должна быть 3.11 или выше

# Проверка Gradle
gradle --version  # Должна быть 7.4 или выше
```

<a id="установка-и-настройка-базы-данных"></a>
## 🗄️ Установка и настройка базы данных

<a id="создание-проекта-в-supabase"></a>
### 1. Создание проекта в Supabase

Supabase - это платформа для разработки на основе Postgres, которая предоставляет базу данных, аутентификацию, API, Edge Functions, Realtime-подписки и другие функции.

![Supabase - Postgres development platform](https://supabase.com/images/index/products/database-dark.png)

1. Перейдите на [Supabase](https://supabase.com/)
   ![Supabase homepage](https://github.com/user-attachments/assets/af26ea5d-acf6-4b2d-bc60-713a25e2a8f5)

2. Нажмите кнопку "Start your project"

3. Нажмите "Sign Up Now" для регистрации
   ![Sign up button](https://github.com/user-attachments/assets/cb986342-17c0-4dd6-8690-8bff3cbe43f4)

4. Зарегистрируйтесь в системе
   ![Registration](https://github.com/user-attachments/assets/3fdecdf0-14ca-48af-ac75-f27825ca7a5b)

5. После входа создайте организацию (если у вас нет проекта, он будет предложен при входе)
   ![Create organization](https://github.com/user-attachments/assets/0b3b7a1b-817a-40da-bf42-92093fe109e2)

6. Создайте проект и запомните Database password, который понадобится для конфигурации POSTGRES_PASSWORD
   ![Create project](https://github.com/user-attachments/assets/1676a1d6-f33a-4801-954b-be0c3acfb14e)

7. Убедитесь, что проект создан
   ![Project dashboard](https://github.com/user-attachments/assets/796a6aa5-0c32-4820-9d8a-23c45f59fbd4)

<a id="получение-строки-подключения-jdbc"></a>
### 2. Получение строки подключения JDBC

1. Нажмите кнопку "Connect" в верхнем левом углу
   ![Connect button](https://github.com/user-attachments/assets/b1d77aa1-d497-4ee7-8df0-f057335b324e)

2. Откроется меню подключения
   ![Connect menu](https://github.com/user-attachments/assets/1c530f80-764e-40bb-93f7-3e85379679b5)

3. Нажмите на выбор "Type"
   ![Connection type](https://github.com/user-attachments/assets/4cbdcd38-b3a1-4c41-92f4-190fa5b9ec90)

4. Выберите "JDBC"
   ![JDBC option](https://github.com/user-attachments/assets/4007db8b-3974-42b4-9d55-43cf1092da5d)

5. Сохраните строку подключения
   ![JDBC connection string](https://github.com/user-attachments/assets/4bc9e1f5-c009-4479-9923-49e2ebe410c3)

<a id="альтернативные-способы-подключения"></a>
### 3. Альтернативные способы подключения

Если возникают проблемы с подключением по "Direct connection" (как на изображении ниже), попробуйте сменить на "Session Pooler":

![Direct connection issue](https://github.com/user-attachments/assets/1010940d-4447-4989-b03d-5c114c665b76)

После смены на Session Pooler сохраните новую строку JDBC, но уберите из нее только: `&password=[YOUR_PASSWORD]`

![Session Pooler connection](https://github.com/user-attachments/assets/708bb15e-409d-4697-847b-a0749d207e9c)

<a id="настройка-переменных-окружения"></a>
## ⚙️ Настройка переменных окружения

<a id="через-ide-intellij-idea"></a>
### 1. Через IDE (IntelliJ IDEA)

1. Откройте проект в IntelliJ IDEA
   ![Open project](https://github.com/user-attachments/assets/d5a4b0f1-5491-44b7-9b89-bb1eaed64564)

2. Нажмите на значок запуска в верхнем правом углу
   ![Run icon](https://github.com/user-attachments/assets/3631925f-cc21-459e-94dc-ed25160a020e)

3. Выберите "Edit Configurations..."
   ![Edit configurations](https://github.com/user-attachments/assets/edd6d6eb-e5e2-40ae-bb7a-3bc6074c58d6)

4. В открывшемся окне добавьте переменные окружения
   ![Configuration window](https://github.com/user-attachments/assets/107442ce-9682-45b6-bf32-b2748f08858c)

5. В поле "Environment variables" введите:
   ```
   DATABASE_URL="URL";POSTGRES_PASSWORD="Password";SPRING_PROFILES_ACTIVE="profile"
   ```
   где:
   - "URL" - строка из пункта 12 в разделе "Установка и настройка базы данных", но без `?user=postgres&password=[YOUR_PASSWORD]` в конце
   - "Password" - пароль из пункта 6
   - "profile" - "dev" или "prod" (рекомендуется начать с "dev")
   
   ![Environment variables](https://github.com/user-attachments/assets/929c72f5-d082-47a0-8149-20985ddeb8c6)

6. Если поле "Environment variables" отсутствует:
   - Нажмите "Modify options"
     ![Modify options](https://github.com/user-attachments/assets/7c572a89-4439-42a0-84dd-7c5345899253)
   - Выберите "Environment variables"
     ![Environment variables option](https://github.com/user-attachments/assets/369864df-467f-4fa3-bc12-ac790089e449)

<a id="через-командную-строку"></a>
### 2. Через командную строку

#### Linux/Mac
```bash
# Установка переменных окружения
export DATABASE_URL=jdbc:postgresql://localhost:5432/students_themes_db
export POSTGRES_PASSWORD=your_password
export SPRING_PROFILES_ACTIVE=dev
export PORT=8080

# Запуск приложения
./gradlew bootRun
```

#### Windows (CMD)
```cmd
set DATABASE_URL=jdbc:postgresql://localhost:5432/students_themes_db
set POSTGRES_PASSWORD=your_password
set SPRING_PROFILES_ACTIVE=dev
set PORT=8080

gradlew.bat bootRun
```

#### Windows (PowerShell)
```powershell
$env:DATABASE_URL="jdbc:postgresql://localhost:5432/students_themes_db"
$env:POSTGRES_PASSWORD="your_password"
$env:SPRING_PROFILES_ACTIVE="dev"
$env:PORT=8080

.\gradlew.bat bootRun
```

#### One-liner (Linux/Mac)
```bash
DATABASE_URL="jdbc:postgresql://localhost:5432/students_themes_db" \
POSTGRES_PASSWORD="your_password" \
SPRING_PROFILES_ACTIVE="dev" \
PORT=8080 \
./gradlew bootRun
```

<a id="через-env-файл"></a>
### 3. Через .env файл

Создайте файл `.env` в корне проекта:

```env
DATABASE_URL=jdbc:postgresql://localhost:5432/students_themes_db
POSTGRES_PASSWORD=your_password
SPRING_PROFILES_ACTIVE=dev
PORT=8080
ML_SERVICE_URL=http://localhost:8000
```

#### Запуск с использованием .env файла

**Linux/Mac (с direnv):**
```bash
# Установите direnv
brew install direnv

# Настройте direnv для проекта
echo 'export $(cat .env | xargs)' > .envrc
direnv allow

# Запустите приложение
./gradlew bootRun
```

**Альтернатива: bash-скрипт**
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

<a id="через-docker"></a>
### 4. Через Docker

#### Docker Compose
Создайте файл `docker-compose.yml`:

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
    build: ./ML
    ports:
      - "8000:8000"

volumes:
  postgres_data:
```

#### Запуск Docker Compose
```bash
# Создайте файл .env
echo "POSTGRES_PASSWORD=your_password" > .env
echo "SPRING_PROFILES_ACTIVE=prod" >> .env

# Запустите
docker-compose up --build
```

<a id="запуск-backend-приложения"></a>
## 🚀 Запуск Backend приложения

**Важно!** В первый раз запустите с конфигурацией `SPRING_PROFILES_ACTIVE=dev`, чтобы создать все таблицы и чтобы миграции Flyway работали. После этого можно запускать с `prod`.

```bash
# Клонирование репозитория (если нужно)
git clone <repository-url>
cd spring-boot-kotlin-STT

# Запуск в режиме разработки
./gradlew bootRun --args='--spring.profiles.active=dev'

# Или сборка и запуск JAR
./gradlew clean build
java -jar build/libs/spring-boot-kotlin-STT-1.0.0.jar --spring.profiles.active=dev

# Проверка связи с ML модулем
curl http://localhost:8080/themes/ml-health
```

<a id="запуск-ml-сервиса"></a>
## 🧠 Запуск ML Сервиса

<a id="быстрый-старт"></a>
### 1. Быстрый старт

```bash
# 1. Перейдите в директорию
cd ML

# 2. Создайте виртуальное окружение и установите зависимости
python -m venv venv
source venv/bin/activate  # или venv\Scripts\activate на Windows
pip install -r requirements.txt

# 3. Запустите сервис
python main.py

# 4. Проверьте в браузере: http://localhost:8000/health
```

<a id="детальные-инструкции-для-разных-ос"></a>
### 2. Детальные инструкции для разных ОС

#### Linux/Ubuntu/Debian
```bash
# Обновление pip и установка зависимостей
cd /path/to/ml-service
python3 -m venv venv
source venv/bin/activate
pip install --upgrade pip
pip install -r requirements.txt

# Запуск сервиса
python main.py

# Альтернативный запуск с uvicorn напрямую
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

#### Windows
```cmd
:: Убедитесь, что Python установлен
python --version

:: Создание виртуального окружения
cd C:\path\to\ml-service
python -m venv venv
venv\Scripts\activate

:: Установка зависимостей
pip install --upgrade pip
pip install -r requirements.txt

:: Запуск сервиса
python main.py
```

#### macOS
```bash
# Установка Homebrew если нет
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Установка Python
brew install python

# Создание и активация окружения
cd /path/to/ml-service
python3 -m venv venv
source venv/bin/activate

# Установка зависимостей
pip install --upgrade pip
pip install -r requirements.txt

# Запуск сервиса
python main.py
```

<a id="docker-способ"></a>
### 3. Docker-способ

#### Dockerfile
```dockerfile
FROM python:3.10-slim

WORKDIR /app

# Копируем файлы зависимостей
COPY requirements.txt .

# Устанавливаем зависимости
RUN pip install --no-cache-dir -r requirements.txt

# Копируем исходный код
COPY . .

# Открываем порт
EXPOSE 8000

# Команда запуска
CMD ["python", "main.py"]
```

#### Запуск в Docker
```bash
# Сборка образа
docker build -t ml-service .

# Запуск контейнера
docker run -p 8000:8000 --name ml-service-container ml-service

# Или с монтированием текущей директории (для разработки)
docker run -p 8000:8000 -v $(pwd):/app ml-service
```

<a id="проверка-работоспособности"></a>
### 4. Проверка работоспособности

После запуска проверьте:
```bash
# Проверка здоровья сервиса
curl http://localhost:8000/health

# Проверка главной страницы
curl http://localhost:8000/

# Или в браузере откройте:
# http://localhost:8000/docs - документация Swagger
# http://localhost:8000/redoc - альтернативная документация
```

<a id="проверка-интеграции"></a>
## 🔗 Проверка интеграции

```bash
# Проверка связи между сервисами
curl http://localhost:8080/themes/ml-health

# Ожидаемый ответ:
# {"status": "healthy", "service": "ML Matching Service"}
```

<a id="устранение-проблем"></a>
## 🛠️ Устранение проблем

Если при запуске возникает ошибка подключения к базе данных:

```
2025-12-02 22:55:15 - o.h.e.jdbc.spi.SqlExceptionHelper - SQL Error: 0, SQLState: 08001
2025-12-02 22:55:15 - o.h.e.jdbc.spi.SqlExceptionHelper - Ошибка при попытке подсоединения.
2025-12-02 22:55:15 - o.h.e.j.e.i.JdbcEnvironmentInitiator - HHH000342: Could not obtain connection to query metadata
org.hibernate.exception.JDBCConnectionException: unable to obtain isolated JDBC connection [Ошибка при попытке подсоединения.] [n/a]
Caused by: org.postgresql.util.PSQLException: Ошибка при попытке подсоединения.
Caused by: java.net.UnknownHostException: db.vkhuqmdbzoakdnxlibsx.supabase.co
```

**Решение:**
1. Проверьте, что вы используете правильную строку подключения из раздела [Получение строки подключения JDBC](#получение-строки-подключения-jdbc)
2. Если возникают проблемы с "Direct connection", переключитесь на "Session Pooler" как описано в разделе [Альтернативные способы подключения](#альтернативные-способы-подключения)
3. Убедитесь, что вы убрали `&password=[YOUR_PASSWORD]` из строки подключения при использовании Session Pooler

**Если переменные не загружаются:**
1. Проверьте правильность имен переменных (чувствительны к регистру в Linux/Mac)
2. Убедитесь, что переменные экспортированы в той же сессии терминала
3. Проверьте `.env` файл на наличие синтаксических ошибок
4. Для Windows: используйте `set` вместо `export`
5. Перезапустите терминал/IDE после установки переменных

**Для профиля `prod`:**
- Убедитесь, что миграции Flyway применены
- ML-сервис должен быть запущен отдельно, если используется ML-сортировка
- Для продакшена используйте сильные пароли и защищенные соединения (SSL)
