package com.StudentsToThemes.spring_boot_kotlin_STT.service

import com.StudentsToThemes.spring_boot_kotlin_STT.entity.StudentEntity
import com.StudentsToThemes.spring_boot_kotlin_STT.entity.ThemeEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.util.UUID

@Service
class MLSortingService {

    private val log = LoggerFactory.getLogger(MLSortingService::class.java)
    private val restTemplate = RestTemplate()

    @Value("\${ml.service.url:http://localhost:8000}")
    private lateinit var mlServiceUrl: String

    data class MLStudentRequest(
        val id: String,
        val name: String,
        val hardSkill: String,
        val background: String,
        val interests: String,
        val timeInWeek: String = ""
    )

    data class MLThemeRequest(
        val id: String,
        val name: String,
        val description: String,
        val author: String,
        val specializations: List<String>
    )

    data class MLSortRequest(
        val students: List<MLStudentRequest>,
        val theme: MLThemeRequest,
        val targetSpecialization: String
    )

    data class MLSortResponse(
        val sortedStudentIds: List<String>
    )

    /**
     * Отсортировать студентов специализации с помощью ML
     * Sort students by ML
     * @param students list of students
     * @param theme theme
     * @param targetSpecialization target specialization
     * @return sorted list of students
     */
    fun sortSpecializationStudents(
        students: List<StudentEntity>,
        theme: ThemeEntity,
        targetSpecialization: String
    ): List<StudentEntity> {
        log.info("ML sorting for specialization: {} in theme: {}", targetSpecialization, theme.id)

        if (students.size <= 1) {
            log.info("Skipping ML sort - too few students: {}", students.size)
            return students
        }

        try {
            val mlStudents = students.map { student ->
                MLStudentRequest(
                    id = student.id.toString(),
                    name = student.name,
                    hardSkill = student.hardSkill,
                    background = student.background,
                    interests = student.interests,
                    timeInWeek = student.timeInWeek ?: ""
                )
            }

            val mlTheme = MLThemeRequest(
                id = theme.id.toString(),
                name = theme.name,
                description = theme.description,
                author = theme.author,
                specializations = theme.specializations
            )

            val request = MLSortRequest(
                students = mlStudents,
                theme = mlTheme,
                targetSpecialization = targetSpecialization
            )

            val headers = HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
            }

            val entity = HttpEntity(request, headers)
            val response = restTemplate.postForObject(
                "$mlServiceUrl/sort-specialization",
                entity,
                MLSortResponse::class.java
            )

            val sortedIds = response?.sortedStudentIds?.map { UUID.fromString(it) } ?: emptyList()

            // Sort students according to ML result
            val studentMap = students.associateBy { it.id!! }
            return sortedIds.mapNotNull { studentMap[it] }

        } catch (e: Exception) {
            log.error("ML sorting failed: {}", e.message)
            // In case of error, return the original order
            return students
        }
    }

    /**
     * Check if ML service is available
     * @return true if service is available, false otherwise
     */
    fun isServiceAvailable(): Boolean {
        return try {
            restTemplate.getForObject("$mlServiceUrl/health", String::class.java)
            true
        } catch (e: Exception) {
            log.warn("ML service unavailable: {}", e.message)
            false
        }
    }
}