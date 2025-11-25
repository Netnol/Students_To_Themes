package com.StudentsToThemes.spring_boot_kotlin_STT.service

import com.StudentsToThemes.spring_boot_kotlin_STT.CreateThemeRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.StudentWithPriorityDto
import com.StudentsToThemes.spring_boot_kotlin_STT.ThemeResponseDto
import com.StudentsToThemes.spring_boot_kotlin_STT.ThemeEntity

//Need to change
fun ThemeEntity.toResponseDto() = ThemeResponseDto(
    id = this.id!!,
    name = this.name,
    description = this.description,
    author = this.author,
    specializations = this.specializations,
    priorityStudents = this.priorityStudents.map { it.toResponseDto() },
    studentPriorities = this.priorityStudents.mapIndexed { index, student ->
        student.id!! to index
    }.toMap(),
    specializationStudents = this.specializationStudents
        .filter { it.student.id != null } // Безопасная фильтрация
        .groupBy { it.specializationName }
        .mapValues { (_, entities) ->
            entities.sortedBy { it.priorityOrder }
                .map { entity ->
                    StudentWithPriorityDto(
                        studentId = entity.student.id!!,
                        studentName = entity.student.name,
                        priority = entity.priorityOrder,
                        hardSkill = entity.student.hardSkill,
                        background = entity.student.background
                    )
                }
        },
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun CreateThemeRequest.toEntity() = ThemeEntity(
    id = null,
    name = this.name,
    description = this.description,
    author = this.author,
    specializations = this.specializations.toMutableList()
)
