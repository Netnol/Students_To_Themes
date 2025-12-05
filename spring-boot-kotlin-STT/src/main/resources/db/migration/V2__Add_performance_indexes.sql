-- Indexes for improving performance

-- Fir fast search of students by theme and specialization
CREATE INDEX idx_theme_specialization_students_theme_spec ON theme_specialization_students(theme_id, specialization_name);

-- For fast search of themes by student
CREATE INDEX idx_theme_specialization_students_student ON theme_specialization_students(student_id);

-- For sorting by priority
CREATE INDEX idx_theme_specialization_students_priority ON theme_specialization_students(priority_order);

-- For fast search of students in the main theme
CREATE INDEX idx_theme_student_priority_theme ON theme_student_priority(theme_id);
CREATE INDEX idx_theme_student_priority_student ON theme_student_priority(student_id);

-- For fast search of specializations in the theme
CREATE INDEX idx_theme_specializations_theme ON theme_specializations(theme_id);