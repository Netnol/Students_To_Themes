package com.StudentsToThemes.spring_boot_kotlin_STT

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateStudentRequest(
    @field:NotBlank(message = "Name is Required")
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String,
    @field:Size(max = 2000, message = "CV text must not exceed 2000 characters")
    val cvText: String
)
