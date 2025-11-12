package com.StudentsToThemes.spring_boot_kotlin_STT.service

import com.StudentsToThemes.spring_boot_kotlin_STT.CreateStudentRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.StudentResponseDto
import com.StudentsToThemes.spring_boot_kotlin_STT.StudentEntity
import com.StudentsToThemes.spring_boot_kotlin_STT.UpdateStudentRequest
import java.time.Instant
import java.util.UUID

fun StudentEntity.toResponseDto() = StudentResponseDto(
    id = this.id!!,
    name = this.name,
    cvText = this.cvText,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun CreateStudentRequest.toEntity() = StudentEntity(
    id = null, // ID will be generated automatically
    name = this.name,
    cvText = this.cvText
)

