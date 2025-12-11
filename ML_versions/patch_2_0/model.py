import pandas as pd
import numpy as np
from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity
import re
import json
from google.colab import files
from typing import List, Dict, Tuple, Optional, Any
from functools import lru_cache
import warnings
warnings.filterwarnings('ignore')

class CSVStudentTopicMatcher:
    """Класс для сопоставления студентов и тем проектов на основе семантического анализа"""
    
    def __init__(self, model_name='sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2'):
        self.model = SentenceTransformer(model_name)

        self._student_embeddings_cache = {}
        
        # Словарь для нормализации специализаций с приоритетом по точному совпадению
        self.specialization_mapping = {
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
        
        # Создаем обратный индекс для быстрого поиска специализаций
        self._spec_reverse_index = {}
        for main_spec, variants in self.specialization_mapping.items():
            for variant in variants:
                self._spec_reverse_index[variant.lower()] = main_spec
    
    @lru_cache(maxsize=1000)
    def _normalize_specialization(self, specialization: str) -> str:
        if pd.isna(specialization):
            return 'Other'
        
        spec_str = str(specialization).strip()
        if not spec_str or spec_str.lower() in ['nan', 'none', '']:
            return 'Other'
        
        spec_lower = spec_str.lower()
        
        # Проверяем точное совпадение в обратном индексе
        if spec_lower in self._spec_reverse_index:
            return self._spec_reverse_index[spec_lower]
        
        # Проверяем частичное совпадение
        for variant, main_spec in self._spec_reverse_index.items():
            if variant in spec_lower or spec_lower in variant:
                return main_spec
        
        # Маппинг русских названий на английские
        ru_en_map = {
            'машинное обучение': 'Machine Learning',
            'анализ данных': 'Data Science',
            'обработка текста': 'NLP',
            'компьютерное зрение': 'Computer Vision',
            'инженерия данных': 'Data Engineering',
            'бэкенд': 'Backend',
            'фронтенд': 'Frontend',
            'мобильная разработка': 'Android',
            'девопс': 'DevOps',
            'тестирование': 'QA',
            'дизайн': 'UI/UX',
            'геймдев': 'GameDev',
            'биоинформатика': 'Биоинформатика',
            'кибербезопасность': 'Cybersecurity',
            'робототехника': 'Robotics',
            'аналитика': 'Product Analytics'
        }
        
        for ru, en in ru_en_map.items():
            if ru in spec_lower:
                return en
        
        return 'Other'
    
    def _extract_skills(self, experience: str) -> List[str]:
        if pd.isna(experience):
            return []
        
        experience_str = str(experience)
        skills = set()  # Используем set для избежания дубликатов
        
        # Оптимизированный словарь навыков с ключевыми словами
        skill_keywords = {
            'python': ['python', 'pytorch', 'tensorflow', 'keras', 'pandas', 'numpy', 'scikit-learn', 'sklearn'],
            'java': ['java', 'spring', 'hibernate'],
            'kotlin': ['kotlin'],
            'sql': ['sql', 'postgresql', 'mysql', 'postgres', 'database'],
            'javascript': ['javascript', 'js', 'react', 'vue', 'angular', 'typescript', 'node.js', 'nodejs', 'node'],
            'docker': ['docker', 'container'],
            'kubernetes': ['kubernetes', 'k8s'],
            'ml': ['machine learning', 'ml', 'ai', 'нейросети', 'машинное обучение', 'deep learning'],
            'nlp': ['nlp', 'natural language', 'текст', 'linguistics'],
            'cv': ['computer vision', 'cv', 'image', 'vision', 'opencv', 'компьютерное зрение'],
            'data': ['data science', 'data analysis', 'data engineering', 'анализ данных', 'big data'],
            'web': ['web', 'frontend', 'backend', 'api', 'веб', 'website', 'fullstack'],
            'mobile': ['mobile', 'android', 'ios', 'мобильный', 'react native', 'flutter'],
            'devops': ['devops', 'ci/cd', 'cloud', 'aws', 'azure', 'gcp', 'github actions'],
            'qa': ['qa', 'testing', 'test', 'quality', 'тестирование', 'selenium', 'automation']
        }
        
        experience_lower = experience_str.lower()
        
        # Быстрый поиск навыков по ключевым словам
        for skill, keywords in skill_keywords.items():
            if any(keyword in experience_lower for keyword in keywords):
                skills.add(skill)
        
        return list(skills)
    
    def _normalize_hours(self, hours) -> float:
        try:
            # Преобразуем в число, извлекая цифры из строки
            if isinstance(hours, str):
                numbers = re.findall(r'\d+', hours)
                hours = int(numbers[0]) if numbers else 0
            else:
                hours = int(float(hours))
            
            # Бинаризация времени
            if hours <= 10:
                return 0.3
            elif hours <= 15:
                return 0.6
            elif hours <= 20:
                return 0.8
            else:
                return 1.0
        except (ValueError, TypeError):
            return 0.5  # Значение по умолчанию при ошибке
    
    def _create_topic_text(self, topic_data: pd.Series) -> str:
        text_parts = []
        relevant_columns = ['title', 'description', 'required_specializations', 'tasks', 'goals']
        
        for col in relevant_columns:
            if col in topic_data and pd.notna(topic_data[col]):
                text_parts.append(str(topic_data[col]))
        
        return " ".join(text_parts)
    
    def _extract_keywords(self, topic_data: pd.Series) -> List[str]:
        text = self._create_topic_text(topic_data)
        # Извлекаем слова длиной от 3 символов, включая русские и английские
        words = re.findall(r'\b[a-zA-Zа-яА-ЯёЁ]{3,}\b', text.lower())
        return list(set(words))  # Убираем дубликаты
    
    def calculate_semantic_similarity(self, student_texts: List[str], topic_text: str) -> np.ndarray:
        # Используем кэш для избежания повторных вычислений эмбеддингов
        cache_key = hash(tuple(student_texts))
        
        if cache_key not in self._student_embeddings_cache:
            # Пакетное вычисление эмбеддингов для всех студентов
            student_embeddings = self.model.encode(student_texts, show_progress_bar=False)
            self._student_embeddings_cache[cache_key] = student_embeddings
        else:
            student_embeddings = self._student_embeddings_cache[cache_key]
        
        # Вычисляем эмбеддинг для темы (один раз)
        topic_embedding = self.model.encode([topic_text])
        
        # Вычисляем косинусное сходство
        similarities = cosine_similarity(student_embeddings, topic_embedding)
        return similarities.flatten()
    
    @lru_cache(maxsize=128)
    def calculate_specialization_match(self, student_spec: str, required_spec: str) -> float:
        if student_spec == required_spec:
            return 1.0
        
        # Граф связанных специализаций
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
        
        # Проверяем двунаправленные связи
        if student_spec in related_specs and required_spec in related_specs[student_spec]:
            return 0.7
        
        if required_spec in related_specs and student_spec in related_specs[required_spec]:
            return 0.7
        
        return 0.0
    
    def calculate_skill_match(self, student_skills: List[str], topic_keywords: List[str]) -> float:
        if not student_skills:
            return 0.0
        
        # Преобразуем в множества для быстрого поиска
        student_skills_set = set(student_skills)
        topic_keywords_set = set(topic_keywords)
        
        # Ищем пересечения навыков и ключевых слов
        matches = len([skill for skill in student_skills_set
                      if any(skill in kw or kw in skill for kw in topic_keywords_set)])
        
        return matches / len(student_skills_set)
    
    def calculate_comprehensive_score(self, semantic_similarity: float, spec_match: float,
                                     skill_match: float, hours_score: float,
                                     weights: Optional[Dict] = None) -> float:
        if weights is None:
            # Веса можно настраивать в зависимости от приоритетов
            weights = {'semantic': 0.4, 'specialization': 0.3, 'skills': 0.2, 'hours': 0.1}
        
        # Взвешенная сумма всех метрик
        score = (semantic_similarity * weights['semantic'] +
                spec_match * weights['specialization'] +
                skill_match * weights['skills'] +
                hours_score * weights['hours'])
        
        return min(1.0, score)  # Ограничиваем максимальным значением 1.0
    
    def preprocess_student_data(self, student_data: pd.DataFrame) -> Tuple[pd.DataFrame, List[str], List[str]]:
        df = student_data.copy()
        
        # Определяем имена столбцов автоматически
        col_names = df.columns.tolist()
        
        # Поиск ID студента
        id_col = next((col for col in col_names if 'id' in col.lower()), 
                     next((col for col in col_names if 'номер' in col.lower()), col_names[0]))
        
        # Удаляем строки без ID
        df = df.dropna(subset=[id_col])
        
        # Конвертируем ID в числовой формат
        df['student_id_int'] = df[id_col].apply(self._safe_convert_to_int)
        
        # Поиск нужных столбцов по ключевым словам
        spec_col = next((col for col in col_names if 'специал' in col.lower() or 'роль' in col.lower()),
                       next((col for col in col_names if 'special' in col.lower() or 'role' in col.lower()), 
                           col_names[1] if len(col_names) > 1 else id_col))
        
        exp_col = next((col for col in col_names if 'опыт' in col.lower() or 'experience' in col.lower()),
                      col_names[2] if len(col_names) > 2 else spec_col)
        
        int_col = next((col for col in col_names if 'интерес' in col.lower() or 'interest' in col.lower()),
                      col_names[3] if len(col_names) > 3 else exp_col)
        
        hours_col = next((col for col in col_names if any(word in col.lower() for word in ['врем', 'час', 'time', 'готовы', 'тратить'])),
                        col_names[4] if len(col_names) > 4 else int_col)
        
        # Применяем нормализацию и извлечение признаков
        df['specialization_clean'] = df[spec_col].apply(self._normalize_specialization)
        df['skills'] = df[exp_col].apply(self._extract_skills)
        df['hours_normalized'] = df[hours_col].apply(self._normalize_hours)
        
        # Создаем список текстовых представлений для семантического анализа
        student_texts = []
        for _, row in df.iterrows():
            # Собираем все релевантные текстовые поля
            text_parts = []
            for col in [spec_col, exp_col, int_col]:
                if col in row and pd.notna(row[col]):
                    text_parts.append(str(row[col]))
            student_texts.append(" ".join(text_parts))
        
        return df, student_texts, [id_col, spec_col, exp_col, int_col, hours_col]
    
    def _safe_convert_to_int(self, value: Any) -> int:
        try:
            if pd.isna(value):
                return 0
            
            value_str = str(value).strip()
            numbers = re.findall(r'\d+', value_str)
            
            if numbers:
                return int(numbers[0])
            
            # Пробуем преобразовать как число с плавающей точкой
            return int(float(value))
        except (ValueError, TypeError):
            return 0
    
    def preprocess_topic_data(self, topic_data: pd.Series) -> Dict[str, Any]:
        processed_topic = topic_data.to_dict()
        
        # Определяем, какой столбец содержит специализации
        spec_col = None
        for col in ['required_specializations', 'Желаемая командная роль', 'роль', 'специализация']:
            if col in processed_topic:
                spec_col = col
                break
        
        if spec_col and pd.notna(processed_topic[spec_col]):
            # Разделяем специализации по запятым или другим разделителям
            spec_text = str(processed_topic[spec_col])
            # Разделяем по запятым, точкам с запятой или переводам строк
            required_specs = []
            for part in re.split(r'[,;\n]', spec_text):
                spec_clean = part.strip()
                if spec_clean:
                    required_specs.append(spec_clean)
            processed_topic['required_specializations_list'] = required_specs
        else:
            processed_topic['required_specializations_list'] = []
        
        # Определяем столбец с описанием темы
        desc_col = None
        for col in ['description', 'Выберите тематику', 'тематика', 'описание']:
            if col in processed_topic:
                desc_col = col
                break
        
        if desc_col:
            # Создаем полное текстовое описание
            text_parts = []
            if desc_col in processed_topic and pd.notna(processed_topic[desc_col]):
                text_parts.append(str(processed_topic[desc_col]))
            
            # Добавляем другие релевантные колонки
            for col in ['title', 'задача', 'goal', 'tasks', 'goals']:
                if col in processed_topic and pd.notna(processed_topic[col]):
                    text_parts.append(str(processed_topic[col]))
            
            processed_topic['full_description'] = " ".join(text_parts)
            processed_topic['keywords'] = self._extract_keywords(pd.Series(processed_topic))
        else:
            processed_topic['full_description'] = ""
            processed_topic['keywords'] = []
        
        return processed_topic
    
    def find_best_students_for_specialization(self, students_data: pd.DataFrame,
                                             topic_data: pd.Series,
                                             required_spec: str,
                                             top_k: int = 5) -> List[Dict]:
        # Предобработка данных
        students_df, student_texts, cols = self.preprocess_student_data(students_data)
        id_col, spec_col, exp_col, int_col, hours_col = cols
        
        processed_topic = self.preprocess_topic_data(topic_data)
        
        # Вычисление семантической схожести
        semantic_scores = self.calculate_semantic_similarity(
            student_texts,
            processed_topic['full_description']
        )
        
        results = []
        
        # Оценка каждого студента
        for i, (_, student) in enumerate(students_df.iterrows()):
            # Соответствие специализации
            spec_match = self.calculate_specialization_match(
                student['specialization_clean'],
                self._normalize_specialization(required_spec)
            )
            
            # Соответствие навыков
            skill_match = self.calculate_skill_match(
                student['skills'],
                processed_topic['keywords']
            )
            
            # Комплексная оценка
            comprehensive_score = self.calculate_comprehensive_score(
                semantic_similarity=semantic_scores[i],
                spec_match=spec_match,
                skill_match=skill_match,
                hours_score=student['hours_normalized']
            )
            
            # Преобразуем student_id в целое число
            raw_student_id = student[id_col]
            try:
                # Преобразуем в целое число, если это возможно
                if isinstance(raw_student_id, (int, np.integer)):
                    student_id_int = int(raw_student_id)
                elif isinstance(raw_student_id, (float, np.floating)):
                    student_id_int = int(raw_student_id)
                else:
                    # Для строк пытаемся извлечь число
                    numbers = re.findall(r'\d+', str(raw_student_id))
                    student_id_int = int(numbers[0]) if numbers else int(student['student_id_int'])
            except (ValueError, TypeError, IndexError):
                # Если не удается преобразовать, используем внутренний ID
                student_id_int = int(student['student_id_int'])
            
            # Сохраняем результат
            result = {
                'student_id': student_id_int, 
                'student_specialization': student[spec_col],
                'normalized_specialization': student['specialization_clean'],
                'required_specialization': required_spec,
                'comprehensive_score': float(comprehensive_score),
                'semantic_similarity': float(semantic_scores[i]),
                'specialization_match': float(spec_match),
                'skills_match': float(skill_match),
                'available_hours': float(student['hours_normalized']),
                'interest': str(student[int_col]) if int_col in student else '',
                'experience': str(student[exp_col]) if exp_col in student else ''
            }
            results.append(result)
        
        # Сортировка по комплексной оценке
        results.sort(key=lambda x: x['comprehensive_score'], reverse=True)
        return results[:top_k]
    
    def create_topic_specialization_map(self, students_data: pd.DataFrame,
                                       topics_data: pd.DataFrame,
                                       top_k_per_spec: int = 5) -> Dict:
        topic_specialization_map = {}
        
        # Обрабатываем каждую тему
        for topic_idx, topic in topics_data.iterrows():
            # Извлекаем или генерируем ID темы
            topic_id = topic.get('topic_id', 
                               topic.get('Id', 
                                       topic.get('id', f'topic_{topic_idx + 1}')))
            topic_id_int = self._safe_convert_to_int(topic_id)
            
            # Определяем название темы
            title_col = None
            for col in ['title', 'Выберите тематику', 'тематика', 'название']:
                if col in topic and pd.notna(topic[col]):
                    title_col = col
                    break
            
            topic_title = topic[title_col] if title_col else f'Тема {topic_idx + 1}'
            
            # Определяем требуемые специализации
            processed_topic = self.preprocess_topic_data(topic)
            required_specs = processed_topic['required_specializations_list']
            
            candidates_by_specialization = {}
            
            # Ищем лучших кандидатов для каждой требуемой специализации
            for required_spec in required_specs:
                if required_spec:  # Пропускаем пустые специализации
                    best_students = self.find_best_students_for_specialization(
                        students_data, topic, required_spec, top_k=top_k_per_spec
                    )
                    
                    # Сохраняем только ID студентов
                    student_ids = [student['student_id'] for student in best_students]
                    candidates_by_specialization[required_spec] = student_ids
            
            # Сохраняем результаты для этой темы
            topic_specialization_map[topic_id_int] = {
                'title': str(topic_title),
                'candidates_by_specialization': candidates_by_specialization
            }
        
        return topic_specialization_map
    
    def get_formatted_output(self, topic_specialization_map: Dict) -> Dict:
        formatted_output = {}
        
        for topic_id, topic_info in topic_specialization_map.items():
            project_key = f"Проект{topic_id}"
            formatted_output[project_key] = topic_info['candidates_by_specialization']
        
        return formatted_output
    
    def save_results_to_json(self, topic_specialization_map: Dict,
                            filename: str = "topic_candidate_map.json") -> Dict:
        formatted_data = self.get_formatted_output(topic_specialization_map)
        
        with open(filename, 'w', encoding='utf-8') as f:
            json.dump(formatted_data, f, ensure_ascii=False, indent=2)
        
        print(f"Результаты сохранены в файл: {filename}")
        return formatted_data

# Создаем экземпляр модели
print("Инициализация модели...")
matcher = CSVStudentTopicMatcher()
print("Модель загружена и готова к работе!")