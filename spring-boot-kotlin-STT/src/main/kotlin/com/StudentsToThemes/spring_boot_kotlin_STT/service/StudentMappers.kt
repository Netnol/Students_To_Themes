package com.StudentsToThemes.spring_boot_kotlin_STT.service

import com.StudentsToThemes.spring_boot_kotlin_STT.DTO.CreateStudentRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.DTO.StudentResponseDto
import com.StudentsToThemes.spring_boot_kotlin_STT.entity.StudentEntity

fun StudentEntity.toResponseDto() = StudentResponseDto(
    id = this.id!!,
    name = this.name,
    hardSkill = this.hardSkill,
    background = this.background,
    interests = this.interests,
    timeInWeek = this.timeInWeek,
    themePriorities = this.themes.associate { theme ->
        theme.id!! to theme.priorityStudents.indexOfFirst { it.id == this.id }
    }.filter { it.value >= 0 },
    specializationPriorities = this.getSafeSpecializationThemes()
        .groupBy { it.specializationName }
        .mapValues { (_, specializations) ->
            specializations.associate { specialization ->
                specialization.theme.id!! to specialization.priorityOrder
            }
        },
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun CreateStudentRequest.toEntity() = StudentEntity(
    id = null,
    name = this.name,
    hardSkill = this.hardSkill,
    background = this.background,
    interests = this.interests,
    timeInWeek = this.timeInWeek
)
