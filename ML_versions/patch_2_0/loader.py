def detect_environment():
    try:
        # Проверяем, находимся ли мы в Google Colab
        import google.colab
        return "colab"
    except:
        pass
    
    try:
        # Проверяем, находимся ли мы в Jupyter
        from IPython import get_ipython
        if get_ipython() is not None:
            return "jupyter"
    except:
        pass
    
    return "vscode"  # По умолчанию предполагаем VS Code

def upload_file_colab():
    from google.colab import files
    print("Пожалуйста, загрузите файл...")
    uploaded = files.upload()
    
    if uploaded:
        filename = list(uploaded.keys())[0]
        print(f"Файл '{filename}' успешно загружен!")
        return filename
    return None

def ask_for_file_path(filename_hint=""):
    default_paths = [
        filename_hint,
        f"./{filename_hint}",
        f"../{filename_hint}",
        f"data/{filename_hint}",
        f"../data/{filename_hint}",
    ]
    
    print(f"Файл '{filename_hint}' не найден по умолчанию.")
    print("Возможные варианты:")
    for i, path in enumerate(default_paths, 1):
        if path and path != filename_hint:
            print(f"  {i}. {path}")
    
    print(f"\nВведите путь к файлу (или оставьте пустым для '{filename_hint}'):")
    user_path = input().strip()
    
    if not user_path:
        return filename_hint
    return user_path

# Определяем среду
environment = detect_environment()
print(f"Определена среда: {environment.upper()}")

# ============================================
# ЗАГРУЗКА ДАННЫХ СТУДЕНТОВ
# ============================================

print("\n" + "="*50)
print("ЗАГРУЗКА ДАННЫХ СТУДЕНТОВ")
print("="*50)

students_loaded = False
student_file_options = [
    'Students_LISA.xlsx',
    'Students_LISA.xlsx',
    'students.xlsx',
    'students.xlsx',
    'Students.xlsx',
    'students_data.xlsx',
    'LISA_students.xlsx'
]

# Пробуем найти файл автоматически
for filename in student_file_options:
    try:
        if environment == "colab":
            # В Colab пробуем загрузить из файловой системы
            students_df = pd.read_excel(filename)
            print(f"✓ Файл студентов найден: {filename}")
            print(f"  Загружено {len(students_df)} записей")
            print(f"  Столбцы: {students_df.columns.tolist()}")
            students_loaded = True
            break
        else:
            # В Jupyter/VS Code пробуем стандартные пути
            if os.path.exists(filename):
                students_df = pd.read_excel(filename)
                print(f"✓ Файл студентов найден: {filename}")
                print(f"  Загружено {len(students_df)} записей")
                print(f"  Столбцы: {students_df.columns.tolist()}")
                students_loaded = True
                break
    except Exception as e:
        continue

# Если файл не найден автоматически
if not students_loaded:
    print("\nФайл со студентами не найден автоматически.")
    
    if environment == "colab":
        print("Запускаю загрузку файла студентов...")
        student_filename = upload_file_colab()
        if student_filename:
            students_df = pd.read_excel(student_filename)
            students_loaded = True
    else:
        # Для Jupyter/VS Code запрашиваем путь
        student_filename = ask_for_file_path("Students_LISA.xlsx")
        try:
            students_df = pd.read_excel(student_filename)
            students_loaded = True
        except Exception as e:
            print(f"Ошибка загрузки: {e}")

if not students_loaded:
    raise FileNotFoundError("Не удалось загрузить данные студентов")

# ============================================
# ЗАГРУЗКА ДАННЫХ ТЕМ ПРОЕКТОВ
# ============================================

print("\n" + "="*50)
print("ЗАГРУЗКА ДАННЫХ ТЕМ ПРОЕКТОВ")
print("="*50)

topics_loaded = False
topic_file_options = [
    'topics_Lisa.xlsx',
    'topics_LISA.xlsx',
    'topics.xlsx',
    'topics.xls',
    'Topics.xlsx',
    'topics_data.xlsx',
    'Lisa_topics.xlsx'
]

# Пробуем найти файл автоматически
for filename in topic_file_options:
    try:
        if environment == "colab":
            # В Colab пробуем загрузить из файловой системы
            topics_df = pd.read_excel(filename)
            print(f"✓ Файл тем найден: {filename}")
            print(f"  Загружено {len(topics_df)} записей")
            print(f"  Столбцы: {topics_df.columns.tolist()}")
            topics_loaded = True
            break
        else:
            # В Jupyter/VS Code пробуем стандартные пути
            if os.path.exists(filename):
                topics_df = pd.read_excel(filename)
                print(f"✓ Файл тем найден: {filename}")
                print(f"  Загружено {len(topics_df)} записей")
                print(f"  Столбцы: {topics_df.columns.tolist()}")
                topics_loaded = True
                break
    except Exception as e:
        continue

# Если файл не найден автоматически
if not topics_loaded:
    print("\nФайл с темами не найден автоматически.")
    
    if environment == "colab":
        print("Запускаю загрузку файла тем...")
        topic_filename = upload_file_colab()
        if topic_filename:
            topics_df = pd.read_excel(topic_filename)
            topics_loaded = True
    else:
        # запрашиваем путь
        topic_filename = ask_for_file_path("topics_Lisa.xlsx")
        try:
            topics_df = pd.read_excel(topic_filename)
            topics_loaded = True
        except Exception as e:
            print(f"Ошибка загрузки: {e}")

if not topics_loaded:
    raise FileNotFoundError("Не удалось загрузить данные тем проектов")

# ============================================
# ПРЕДВАРИТЕЛЬНЫЙ ПРОСМОТР ДАННЫХ
# ============================================

print("\n" + "="*50)
print("ПРЕДВАРИТЕЛЬНЫЙ ПРОСМОТР ДАННЫХ")
print("="*50)

print("\n--- СТУДЕНТЫ (первые 3 записи) ---")
print(students_df.head(3))
print(f"\nВсего студентов: {len(students_df)}")

print("\n--- ТЕМЫ ПРОЕКТОВ (первые 3 записи) ---")
print(topics_df.head(3))
print(f"\nВсего тем: {len(topics_df)}")

# ============================================
# ВЫПОЛНЕНИЕ СОПОСТАВЛЕНИЯ
# ============================================

print("\n" + "="*50)
print("ВЫПОЛНЕНИЕ СОПОСТАВЛЕНИЯ")
print("="*50)

print("Это может занять некоторое время...")

try:
    topic_specialization_map = matcher.create_topic_specialization_map(
        students_df,
        topics_df,
        top_k_per_spec=5
    )
    
    print("✓ Сопоставление успешно завершено!")
    
    final_data = matcher.save_results_to_json(topic_specialization_map)
    
    # ============================================
    # ВЫВОД РЕЗУЛЬТАТОВ
    # ============================================
    
    print("\n" + "="*50)
    print("РЕЗУЛЬТАТЫ СОПОСТАВЛЕНИЯ")
    print("="*50)
    
    total_matches = 0
    for topic_id, topic_info in topic_specialization_map.items():
        candidates = topic_info['candidates_by_specialization']
        num_candidates = sum(len(ids) for ids in candidates.values())
        if num_candidates > 0:
            print(f"\nТема #{topic_id}: {topic_info['title'][:50]}...")
            total_matches += num_candidates
            
            for spec, student_ids in candidates.items():
                if student_ids:
                    print(f"  {spec}: {student_ids}")
    
    print(f"\nВсего найдено сопоставлений: {total_matches}")
    
    # ============================================
    # СОХРАНЕНИЕ И СКАЧИВАНИЕ РЕЗУЛЬТАТОВ
    # ============================================
    
    if environment == "colab":
        try:
            from google.colab import files
            files.download("topic_candidate_map.json")
            print("\n✓ Файл с результатами скачан на ваше устройство!")
        except:
            print("\n✓ Файл сохранен: topic_candidate_map.json")
    else:
        # Для Jupyter/VS Code предлагаем открыть файл
        print("\n✓ Результаты сохранены в файл: topic_candidate_map.json")
        print("Файл находится в текущей директории:")
        print(f"  {os.path.abspath('topic_candidate_map.json')}")
        
        # Проверяем существование файла
        if os.path.exists("topic_candidate_map.json"):
            file_size = os.path.getsize("topic_candidate_map.json")
            print(f"  Размер файла: {file_size:,} байт")
            
            # Показываем первые несколько строк для чекапа
            try:
                with open("topic_candidate_map.json", 'r', encoding='utf-8') as f:
                    first_lines = [next(f) for _ in range(5)]
                print("\nПервые строки файла:")
                print("".join(first_lines))
            except:
                pass
    
    # ============================================
    # ДОПОЛНИТЕЛЬНЫЕ ВОЗМОЖНОСТИ
    # ============================================
    
    print("\n" + "="*50)
    print("ДОПОЛНИТЕЛЬНЫЕ ВОЗМОЖНОСТИ")
    print("="*50)
    print("Доступные переменные:")
    print("1. students_df - данные студентов")
    print("2. topics_df - данные тем проектов")
    print("3. final_data - результаты в формате словаря")
    print("4. topic_specialization_map - полные результаты с деталями")
    print("\nПример использования:")
    print("  final_data['Проект1'] - получить кандидатов для проекта 1")
    
except Exception as e:
    print(f"\n✗ Ошибка при сопоставлении: {e}")
    print("Проверьте структуру данных и попробуйте снова.")