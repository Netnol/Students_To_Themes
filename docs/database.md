## <a id="база-данных">🗄 База данных</a>

- [Полная схема данных](#полная-схема-данных)
  - [Таблица: students](#таблица-students)
  - [Таблица: themes](#таблица-themes)
  - [Таблица: theme_specializations](#таблица-theme_specializations)
  - [Таблица: theme_student_priority](#таблица-theme_student_priority)
  - [Таблица: theme_specialization_students](#таблица-theme_specialization_students)
  - [Таблица: theme_ml_sorted_specializations](#таблица-theme_ml_sorted_specializations)
- [Индексы для оптимизации](#индексы-для-оптимизации)
  - [Индексы для theme_specialization_students](#индексы-для-theme_specialization_students)
  - [Индексы для theme_student_priority](#индексы-для-theme_student_priority)
  - [Индексы для theme_specializations](#индексы-для-theme_specializations)
  - [Дополнительные индексы для поиска](#дополнительные-индексы-для-поиска)
- [Миграции](#миграции)
  - [Структура миграций](#структура-миграций)
  - [Пример миграции](#пример-миграции)

### <a id="полная-схема-данных">Полная схема данных</a>

#### <a id="таблица-students">Таблица: students</a>
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

#### <a id="таблица-themes">Таблица: themes</a>
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

#### <a id="таблица-theme_specializations">Таблица: theme_specializations</a>
```sql
CREATE TABLE theme_specializations (
    theme_id UUID NOT NULL REFERENCES themes(id) ON DELETE CASCADE,
    specialization_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (theme_id, specialization_name)
);
```

#### <a id="таблица-theme_student_priority">Таблица: theme_student_priority</a>
```sql
CREATE TABLE theme_student_priority (
    theme_id UUID NOT NULL REFERENCES themes(id) ON DELETE CASCADE,
    student_id UUID NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    priority_order INTEGER NOT NULL,
    PRIMARY KEY (theme_id, student_id)
);
```

#### <a id="таблица-theme_specialization_students">Таблица: theme_specialization_students</a>

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

#### <a id="таблица-theme_ml_sorted_specializations">Таблица: theme_ml_sorted_specializations</a>
```sql
CREATE TABLE theme_ml_sorted_specializations (
    theme_id UUID NOT NULL REFERENCES themes(id) ON DELETE CASCADE,
    specialization_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (theme_id, specialization_name)
);
```

### <a id="индексы-для-оптимизации">Индексы для оптимизации</a>

<a id="индексы-для-theme_specialization_students">Индексы для theme_specialization_students</a>

```sql

CREATE INDEX idx_theme_specialization_students_theme_spec ON theme_specialization_students(theme_id, specialization_name);
CREATE INDEX idx_theme_specialization_students_student ON theme_specialization_students(student_id);
CREATE INDEX idx_theme_specialization_students_priority ON theme_specialization_students(priority_order);
```
<a id="индексы-для-theme_student_priority">Индексы для theme_student_priority</a>

```sql
CREATE INDEX idx_theme_student_priority_theme ON theme_student_priority(theme_id);
CREATE INDEX idx_theme_student_priority_student ON theme_student_priority(student_id);
```
<a id="индексы-для-theme_specializations">Индексы для theme_specializations</a>
```sql
CREATE INDEX idx_theme_specializations_theme ON theme_specializations(theme_id);
```
<a id="дополнительные-индексы-для-поиска">Дополнительные индексы для поиска</a>

```sql
CREATE INDEX idx_students_name ON students(name);
CREATE INDEX idx_students_active ON students(active);
CREATE INDEX idx_themes_name ON themes(name);
CREATE INDEX idx_themes_author ON themes(author);
```

### <a id="миграции">Миграции</a>
<a id="структура-миграций">Структура миграций</a>

Система использует Flyway для управления миграциями в production. Миграции находятся в `src/main/resources/db/migration/`.

**<a id="пример-миграции">Пример миграции</a>**
```sql
-- V2__Add_performance_indexes.sql
CREATE INDEX idx_theme_specialization_students_theme_spec ON theme_specialization_students(theme_id, specialization_name);
CREATE INDEX idx_theme_specialization_students_student ON theme_specialization_students(student_id);
CREATE INDEX idx_theme_specialization_students_priority ON theme_specialization_students(priority_order);
CREATE INDEX idx_theme_student_priority_theme ON theme_student_priority(theme_id);
CREATE INDEX idx_theme_student_priority_student ON theme_student_priority(student_id);
CREATE INDEX idx_theme_specializations_theme ON theme_specializations(theme_id);
```