"""Важное замечание: данный загрузчик предназначен только для jupyter notebook,
 не для VS Code, поэтому для просмотра полной реализации патча 1.0 посмотрите 
 здешний .ipynb"""


import pandas as pd
import numpy as np
from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.metrics import precision_score, recall_score, f1_score, accuracy_score
import matplotlib.pyplot as plt
import seaborn as sns
import re
import chardet
from google.colab import files
import io

#Автоматически определяет кодировку и разделитель файла
def detect_file_params(file_content):

    encoding_result = chardet.detect(file_content)
    encoding = encoding_result['encoding']
    confidence = encoding_result['confidence']


    sample_size = min(10000, len(file_content))
    sample = file_content[:sample_size]

    try:
        decoded_sample = sample.decode(encoding)
    except:

        decoded_sample = sample.decode('utf-8', errors='ignore')

    separators = [',', ';', '\t', '|']
    separator_scores = {}

    for sep in separators:
        first_line = decoded_sample.split('\n')[0] if '\n' in decoded_sample else decoded_sample
        sep_count = first_line.count(sep)
        separator_scores[sep] = sep_count

    best_separator = max(separator_scores, key=separator_scores.get)


    return encoding, best_separator

#загрузка
def load_csv_automatic(file_content, file_type):
    try:
        encoding, separator = detect_file_params(file_content)

        df = pd.read_csv(
            io.BytesIO(file_content),
            sep=separator,
            encoding=encoding,
            on_bad_lines='skip'
        )
        return df

    except Exception as e:

        encodings = ['utf-8', 'cp1251', 'windows-1251', 'iso-8859-1', 'koi8-r', 'mac_cyrillic']
        separators = [',', ';', '\t', '|']

        for encoding in encodings:
            for separator in separators:
                try:
                    df = pd.read_csv(
                        io.BytesIO(file_content),
                        sep=separator,
                        encoding=encoding,
                        on_bad_lines='skip'
                    )
                    if len(df) > 0 and len(df.columns) > 1:
                        print(f" {file_type} загружен с {encoding}, разделитель '{separator}'")
                        return df
                except:
                    continue

        print(f" Не удалось загрузить {file_type} ни с одной комбинацией")
        return None

def validate_dataframes(students_df, topics_df):
    issues = []


    if students_df is not None:
        required_student_cols = ['id', 'Специализация', 'Опыт в проектах', 'Интересы', 'Сколько времени в неделю(ч)']
        missing_student_cols = [col for col in required_student_cols if col not in students_df.columns]
        if missing_student_cols:
            issues.append(f"Отсутствуют колонки студентов: {missing_student_cols}")

    if topics_df is not None:
        required_topic_cols = ['topic_id', 'title', 'description', 'num_people', 'required_specializations']
        missing_topic_cols = [col for col in required_topic_cols if col not in topics_df.columns]
        if missing_topic_cols:
            issues.append(f"Отсутствуют колонки тем: {missing_topic_cols}")

    return issues



print(" Загрузите файл students.csv")
uploaded_students = files.upload()

print(" Загрузите файл topics.csv")
uploaded_topics = files.upload()


students_filename = list(uploaded_students.keys())[0] if uploaded_students else None
topics_filename = list(uploaded_topics.keys())[0] if uploaded_topics else None


students_df = None
topics_df = None

if students_filename:
    students_df = load_csv_automatic(uploaded_students[students_filename], "students.csv")

if topics_filename:
    topics_df = load_csv_automatic(uploaded_topics[topics_filename], "topics.csv")


print("\n ФИНАЛЬНЫЙ РЕЗУЛЬТАТ ЗАГРУЗКИ:")
print(f" Студентов: {len(students_df)}")
print(f" Тем: {len(topics_df)}")

print("\n Структура students:")
print(students_df.info())
print("\nПример данных:")
print(students_df.head(3))

print("\n Структура topics:")
print(topics_df.info())
print("\nПример данных:")
print(topics_df.head(3))


print("\n ПРОВЕРКА ДАННЫХ:")
print(f"Специализации студентов: {students_df['Специализация'].unique()[:10]}")  # первые 10
print(f"Требуемые специализации в темах: {topics_df['required_specializations'].unique()[:10]}")