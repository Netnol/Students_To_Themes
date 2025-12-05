## 💻 Разработка 

- [Структура проекта](#Структура-проекта)
- [Сборка и запуск](#Сборка-и-запуск)
  - [Локальная разработка](#dockerfile-для-ml-service)
  - [Тестирование](#docker-compose-yml)
  - [Производственная сборка](#init-db-sql)
- [Модели данных](#production-настройки)
  - [StudentEntity](#переменные-окружения-для-production)
  - [ThemeEntity](#запуск-в-production)


### <a id="Структура-проекта">Структура проекта</a>


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


### <a id="Сборка-и-запуск">Сборка и запуск</a>

**<a id="dockerfile-для-ml-service">Локальная разработка:</a>**

```bash
# Запуск с профилем разработки
./gradlew bootRun --args='--spring.profiles.active=dev'

# Или через IDE:
# Установите активный профиль: dev
```

**<a id="docker-compose-yml">Тестирование:</a>**
```bash
# Запуск unit тестов
./gradlew test

# Запуск с генерацией отчета покрытия
./gradlew jacocoTestReport

# Проверка стиля кода
./gradlew ktlintCheck
```

**<a id="init-db-sql">Производственная сборка:</a>**
```bash
# Очистка и сборка
./gradlew clean build

# Пропуск тестов (для быстрой сборки)
./gradlew build -x test

# Сборка с зависимостями
./gradlew bootJar
```

### <a id="production-настройки">Модели данных</a>

#### <a id="переменные-окружения-для-production">StudentEntity</a>
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

#### <a id="запуск-в-production">ThemeEntity</a>
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
