import sys
import os

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from main import CSVStudentTopicMatcher


def test_skill_extraction():
    """Тест извлечения навыков из текста"""
    matcher = CSVStudentTopicMatcher()

    test_cases = [
        ("Знаю Python и Docker", ["python", "docker"]),
        ("Работал с React и JavaScript", ["javascript"]),  
        ("Работал с Java и Spring", ["java"]),  
        ("Знаю Java и JavaScript", ["java", "javascript"]),  
        ("Опыт в машинном обучении и нейросетях", ["ml"]),
        ("SQL и базы данных", ["sql"]),
        ("", []),
        ("Знаю Python, Django и Flask", ["python"]),
        ("Опыт с Docker и Kubernetes", ["docker", "kubernetes"]),
        ("Занимаюсь NLP и машинным обучением", ["ml", "nlp"]),
    ]

    for input_text, expected in test_cases:
        result = matcher._extract_skills(input_text)
        result_sorted = sorted(result)
        expected_sorted = sorted(expected)
        
        assert result_sorted == expected_sorted, (
            f"Ошибка для текста: '{input_text}'\n"
            f"Получено: {result}\n"
            f"Ожидалось: {expected}"
        )

    print("✅ Все тесты извлечения навыков пройдены!")


if __name__ == "__main__":
    test_skill_extraction()
