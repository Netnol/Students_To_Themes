package com.StudentsToThemes.spring_boot_kotlin_STT.DTO

import java.time.Instant
import java.util.UUID

data class StudentResponseDto(
    val id: UUID,
    val name: String,
    val hardSkill: String,
    val background: String,
    val interests: String,
    val timeInWeek: String?,
    val themePriorities: Map<UUID, Int>, // themeId -> priority в основной теме
    val specializationPriorities: Map<String, Map<UUID, Int>>, // specialization -> (themeId -> priority)
    val createdAt: Instant,
    val updatedAt: Instant
)