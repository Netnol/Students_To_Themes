package com.StudentsToThemes.spring_boot_kotlin_STT

import java.time.Instant
import java.util.UUID

data class StudentResponseDto (
    val id: UUID,
    val name: String,
    val cvText: String,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)