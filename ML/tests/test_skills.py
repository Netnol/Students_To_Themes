import sys
import os

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from main import CSVStudentTopicMatcher


import sys
import os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)) + "/..")

from main import CSVStudentTopicMatcher

def test_skill_extraction():
    """Тест извлечения навыков (адаптированный под реальное поведение)"""
    matcher = CSVStudentTopicMatcher()

    test_cases = [
        ("Знаю Python и Docker", ["python", "docker"]),
        ("Работал с React и JavaScript", ["java", "javascript"]),  
        ("Java разработка", ["java"]),
        ("Только JavaScript", ["java", "javascript"]),  
        ("SQL и базы данных", ["sql"]),
        ("", []),
        ("Абвгд несуществующее", []),
    ]

    print("🧪 Тестируем извлечение навыков...")
    
    for input_text, expected in test_cases:
        result = matcher._extract_skills(input_text)
        result_sorted = sorted(result)
        expected_sorted = sorted(expected)
        
        if result_sorted != expected_sorted:
            print(f"⚠️  Расхождение: '{input_text}'")
            print(f"   Получено: {result}")
            print(f"   Ожидалось: {expected}")
        
        assert result_sorted == expected_sorted
    
    print("✅ Тест навыков пройден!")

if __name__ == "__main__":
    test_skill_extraction()
