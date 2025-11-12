package com.StudentsToThemes.spring_boot_kotlin_STT

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class CreateThemeRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    @field:Size(max = 5000, message = "Description must not exceed 5000 characters")
    val description: String,
    @field:NotBlank(message = "Author is required")
    val author: String,
    val priorityStudents: List<UUID> = emptyList() // Only student IDs, maybe will remove, if not needed
)
