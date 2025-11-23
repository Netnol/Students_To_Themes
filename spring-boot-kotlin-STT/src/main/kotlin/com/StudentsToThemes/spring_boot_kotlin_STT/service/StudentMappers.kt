package com.StudentsToThemes.spring_boot_kotlin_STT.service

import com.StudentsToThemes.spring_boot_kotlin_STT.CreateStudentRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.StudentResponseDto
import com.StudentsToThemes.spring_boot_kotlin_STT.StudentEntity
import com.StudentsToThemes.spring_boot_kotlin_STT.StudentNotFoundException
import com.StudentsToThemes.spring_boot_kotlin_STT.UpdateStudentRequest
import java.time.Instant
import java.util.UUID

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
    specializationPriorities = this.specializationThemes
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
