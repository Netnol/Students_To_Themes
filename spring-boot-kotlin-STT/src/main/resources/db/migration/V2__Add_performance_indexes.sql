-- Индексы для улучшения производительности

-- Для быстрого поиска студентов по теме и специализации
CREATE INDEX idx_theme_specialization_students_theme_spec ON theme_specialization_students(theme_id, specialization_name);

-- Для быстрого поиска тем по студенту
CREATE INDEX idx_theme_specialization_students_student ON theme_specialization_students(student_id);

-- Для сортировки по приоритету
CREATE INDEX idx_theme_specialization_students_priority ON theme_specialization_students(priority_order);

-- Для быстрого поиска студентов основной темы
CREATE INDEX idx_theme_student_priority_theme ON theme_student_priority(theme_id);
CREATE INDEX idx_theme_student_priority_student ON theme_student_priority(student_id);

-- Для быстрого поиска специализаций темы
CREATE INDEX idx_theme_specializations_theme ON theme_specializations(theme_id);