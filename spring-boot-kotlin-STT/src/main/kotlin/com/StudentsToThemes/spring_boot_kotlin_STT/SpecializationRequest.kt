package com.StudentsToThemes.spring_boot_kotlin_STT

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SpecializationRequest(
    @field:NotBlank(message = "Specialization name is required")
    @field:Size(max = 100, message = "Specialization name must not exceed 100 characters")
    @field:Pattern(
        regexp = "^[a-zA-Z0-9\\s\\-]+$",
        message = "Specialization name can only contain letters, numbers, spaces and hyphens"
    )
    val name: String
)
