package com.StudentsToThemes.spring_boot_kotlin_STT

import java.time.Instant
import java.util.UUID

//Need to change
data class ThemeResponseDto(
    val id: UUID,
    val name: String,
    val description: String,
    val author: String,
    val priorityStudents: List<StudentResponseDto>,
    val studentPriorities: Map<UUID, Int>, // studentId -> priority
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)
