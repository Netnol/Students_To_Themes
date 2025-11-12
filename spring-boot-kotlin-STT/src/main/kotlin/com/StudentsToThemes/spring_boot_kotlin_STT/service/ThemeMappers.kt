package com.StudentsToThemes.spring_boot_kotlin_STT.service

import com.StudentsToThemes.spring_boot_kotlin_STT.CreateThemeRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.ThemeResponseDto
import com.StudentsToThemes.spring_boot_kotlin_STT.ThemeEntity

//Need to change
fun ThemeEntity.toResponseDto() = ThemeResponseDto(
    id = this.id!!,
    name = this.name,
    description = this.description,
    author = this.author,
    priorityStudents = this.priorityStudents.map { it.toResponseDto() },
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun CreateThemeRequest.toEntity() = ThemeEntity(
    id = null,
    name = this.name,
    description = this.description,
    author = this.author
    // priorityStudents will maybe be set by service, I am not doing it now
)
