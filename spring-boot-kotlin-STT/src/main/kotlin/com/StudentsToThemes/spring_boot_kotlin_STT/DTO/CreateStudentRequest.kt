package com.StudentsToThemes.spring_boot_kotlin_STT.DTO

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateStudentRequest(
    @field:NotBlank(message = "Name is Required")
    @field:Size(max = 100, message = "Name must not exceed 100 characters")
    val name: String,
    @field:NotBlank(message = "Hard skill is Required")
    @field:Size(max = 100, message = "Hard skill must not exceed 100 characters")
    val hardSkill: String,
    @field:NotBlank(message = "Background is Required")
    @field:Size(max = 2000, message = "Background text must not exceed 2000 characters")
    val background: String,
    @field:NotBlank(message = "Interest is Required")
    @field:Size(max = 2000, message = "Interests text must not exceed 2000 characters")
    val interests: String,
    @field:Size(max = 100, message = "Time in week must not exceed 100 characters")
    val timeInWeek: String?,
)
