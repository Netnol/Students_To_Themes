package com.StudentsToThemes.spring_boot_kotlin_STT.DTO

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

    val specializations: List<@NotBlank @Size(max = 100) String> = emptyList(),

    val priorityStudents: List<UUID> = emptyList()
) {
    init {
        validateSpecializations()
    }

    private fun validateSpecializations() {
        val duplicates = specializations
            .map { it.lowercase() }
            .groupBy { it }
            .filter { it.value.size > 1 }

        if (duplicates.isNotEmpty()) {
            throw IllegalArgumentException("Duplicate specializations found: ${duplicates.keys.joinToString()}")
        }

        specializations.forEach { specialization ->
            when {
                specialization.isBlank() ->
                    throw IllegalArgumentException("Specialization name cannot be blank")

                specialization.length > 100 ->
                    throw IllegalArgumentException("Specialization name '$specialization' cannot exceed 100 characters")

                !specialization.matches("^[a-zA-Z0-9\\s\\-]+$".toRegex()) ->
                    throw IllegalArgumentException("Specialization name '$specialization' can only contain letters, numbers, spaces and hyphens")
            }
        }
    }
}
