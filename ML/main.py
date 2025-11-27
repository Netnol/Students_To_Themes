import pandas as pd
import numpy as np
from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity
import re
import json
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Dict, Any
import uvicorn

app = FastAPI(title="Student-Themes ML Matching Service")


class StudentRequest(BaseModel):
    id: str
    name: str
    hardSkill: str
    background: str
    interests: str
    timeInWeek: str = ""


class ThemeRequest(BaseModel):
    id: str
    name: str
    description: str
    author: str
    specializations: List[str]


class SortRequest(BaseModel):
    students: List[StudentRequest]
    theme: ThemeRequest
    targetSpecialization: str


class SortResponse(BaseModel):
    sortedStudentIds: List[str]


class CSVStudentTopicMatcher:
    def __init__(self, model_name='sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2'):
        print("Загрузка модели...")
        self.model = SentenceTransformer(model_name)
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
        print("Модель загружена")

    def _normalize_specialization(self, specialization):
        if pd.isna(specialization):
            return 'Other'

        spec_str = str(specialization).strip()
        if not spec_str or spec_str.lower() in ['nan', 'none', '']:
            return 'Other'

        spec_lower = spec_str.lower()

        for main_spec, variants in self.specialization_mapping.items():
            for variant in variants:
                if variant.lower() == spec_lower:
                    return main_spec

        for main_spec, variants in self.specialization_mapping.items():
            for variant in variants:
                if variant.lower() in spec_lower:
                    return main_spec

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

    def _extract_skills(self, experience):
        if pd.isna(experience):
            return []

        experience_str = str(experience)
        skills = []
        skill_keywords = {
            'python': ['python', 'pytorch', 'tensorflow', 'keras', 'pandas', 'numpy', 'scikit', 'scikit-learn'],
            'java': ['java', 'spring', 'hibernate'],
            'kotlin': ['kotlin'],
            'sql': ['sql', 'postgresql', 'mysql', 'database', 'база данных'],
            'javascript': ['javascript', 'js', 'react', 'vue', 'angular', 'typescript', 'node.js', 'node'],
            'docker': ['docker', 'container'],
            'kubernetes': ['kubernetes', 'k8s'],
            'ml': ['machine learning', 'ml', 'ai', 'нейросети', 'машинное обучение', 'deep learning'],
            'nlp': ['nlp', 'natural language', 'текст', 'linguistics', 'обработка естественного языка'],
            'cv': ['computer vision', 'cv', 'image', 'vision', 'opencv', 'компьютерное зрение'],
            'data': ['data science', 'data analysis', 'data engineering', 'анализ данных', 'big data'],
            'web': ['web', 'frontend', 'backend', 'api', 'веб', 'website'],
            'mobile': ['mobile', 'android', 'ios', 'мобильный', 'react native', 'flutter'],
            'devops': ['devops', 'ci/cd', 'cloud', 'aws', 'azure', 'gcp', 'github actions'],
            'qa': ['qa', 'testing', 'test', 'quality', 'тестирование', 'selenium']
        }

        experience_lower = experience_str.lower()
        for skill, keywords in skill_keywords.items():
            for keyword in keywords:
                if keyword in experience_lower:
                    skills.append(skill)
                    break

        return list(set(skills))

    def _normalize_hours(self, hours):
        try:
            hours = int(hours)
            if hours <= 10:
                return 0.3
            elif hours <= 15:
                return 0.6
            elif hours <= 20:
                return 0.8
            else:
                return 1.0
        except:
            return 0.5

    def _create_topic_text(self, theme_data):
        """Создает текст темы для эмбеддингов"""
        text_parts = []

        # Используем поля из ThemeRequest
        if 'name' in theme_data:
            text_parts.append(str(theme_data['name']))
        if 'description' in theme_data:
            text_parts.append(str(theme_data['description']))
        if 'specializations' in theme_data:
            text_parts.extend([str(spec) for spec in theme_data['specializations']])

        return " ".join(text_parts)

    def _extract_keywords(self, theme_data):
        text = self._create_topic_text(theme_data)
        words = re.findall(r'\b[a-zA-Zа-яА-Я]{3,}\b', text.lower())
        return list(set(words))

    def calculate_semantic_similarity(self, student_texts, topic_text):
        student_embeddings = self.model.encode(student_texts)
        topic_embedding = self.model.encode([topic_text])
        similarities = cosine_similarity(student_embeddings, topic_embedding)
        return similarities.flatten()

    def calculate_specialization_match(self, student_spec, required_spec):
        if student_spec == required_spec:
            return 1.0

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

        if student_spec in related_specs and required_spec in related_specs[student_spec]:
            return 0.7

        if required_spec in related_specs and student_spec in related_specs[required_spec]:
            return 0.7

        return 0.0

    def calculate_skill_match(self, student_skills, topic_keywords):
        if not student_skills:
            return 0.0
        matches = 0
        for skill in student_skills:
            if any(skill in keyword or keyword in skill for keyword in topic_keywords):
                matches += 1
        return matches / len(student_skills)

    def calculate_comprehensive_score(self, semantic_similarity, spec_match, skill_match, hours_score, weights=None):
        if weights is None:
            weights = {'semantic': 0.4, 'specialization': 0.3, 'skills': 0.2, 'hours': 0.1}
        score = (semantic_similarity * weights['semantic'] +
                 spec_match * weights['specialization'] +
                 skill_match * weights['skills'] +
                 hours_score * weights['hours'])
        return score

    def preprocess_student_data(self, students_data):
        """Обрабатывает список студентов для ML"""
        df = pd.DataFrame([student.dict() for student in students_data])

        df['specialization_clean'] = df['hardSkill'].apply(self._normalize_specialization)
        df['skills'] = df['background'].apply(self._extract_skills)
        df['hours_normalized'] = df['timeInWeek'].apply(self._normalize_hours)

        return df

    def preprocess_theme_data(self, theme_data):
        """Обрабатывает данные темы для ML"""
        processed_theme = theme_data.copy()
        processed_theme['full_description'] = self._create_topic_text(theme_data)
        processed_theme['keywords'] = self._extract_keywords(theme_data)
        return processed_theme

    def sort_students_for_specialization(self, students_data, theme_data, target_specialization):
        """Основной метод сортировки студентов для специализации"""
        students_df = self.preprocess_student_data(students_data)
        processed_theme = self.preprocess_theme_data(theme_data.dict())

        # Создаем тексты студентов для семантического сравнения
        student_texts = []
        for _, student in students_df.iterrows():
            student_text = f"{student['hardSkill']} {student['background']} {student['interests']}"
            student_texts.append(student_text)

        # Вычисляем семантическое сходство
        semantic_scores = self.calculate_semantic_similarity(student_texts, processed_theme['full_description'])

        results = []
        for i, (_, student) in enumerate(students_df.iterrows()):
            # Совпадение специализации
            spec_match = self.calculate_specialization_match(
                student['specialization_clean'],
                target_specialization
            )

            # Совпадение навыков
            skill_match = self.calculate_skill_match(
                student['skills'],
                processed_theme['keywords']
            )

            # Комплексная оценка
            comprehensive_score = self.calculate_comprehensive_score(
                semantic_similarity=semantic_scores[i],
                spec_match=spec_match,
                skill_match=skill_match,
                hours_score=student['hours_normalized']
            )

            results.append({
                'student_id': student['id'],
                'comprehensive_score': float(comprehensive_score),
                'semantic_similarity': float(semantic_scores[i]),
                'specialization_match': float(spec_match),
                'skills_match': float(skill_match)
            })

        # Сортируем по убыванию комплексной оценки
        results.sort(key=lambda x: x['comprehensive_score'], reverse=True)
        return [result['student_id'] for result in results]


# Создаем экземпляр матчера
matcher = CSVStudentTopicMatcher()


@app.post("/sort-specialization", response_model=SortResponse)
async def sort_specialization(request: SortRequest):
    try:
        print(f"Received request for specialization: {request.targetSpecialization}")
        print(f"Students count: {len(request.students)}")
        print(f"Theme: {request.theme.name}")

        sorted_ids = matcher.sort_students_for_specialization(
            request.students,
            request.theme,
            request.targetSpecialization
        )

        print(f"Sorted student IDs: {sorted_ids}")
        return SortResponse(sortedStudentIds=sorted_ids)

    except Exception as e:
        print(f"Error in sort_specialization: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Sorting error: {str(e)}")


@app.get("/health")
async def health_check():
    return {"status": "healthy", "service": "Student-Themes ML Matching"}


@app.get("/")
async def root():
    return {"message": "Student-Themes ML Matching Service is running"}


if __name__ == "__main__":
    print("Запуск ML сервиса на http://localhost:8000")
    uvicorn.run(app, host="0.0.0.0", port=8000)