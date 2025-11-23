package com.StudentsToThemes.spring_boot_kotlin_STT

import java.time.Instant
import java.util.UUID

data class ThemeResponseDto(
    val id: UUID,
    val name: String,
    val description: String,
    val author: String,
    val specializations: List<String>,
    val priorityStudents: List<StudentResponseDto>,
    val studentPriorities: Map<UUID, Int>, // studentId -> priority в основной теме
    val specializationStudents: Map<String, List<StudentWithPriorityDto>>, // Specialisation -> list of students with priorities
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)
