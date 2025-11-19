package com.StudentsToThemes.spring_boot_kotlin_STT

import java.util.UUID

data class StudentWithPriorityDto(
    val studentId: UUID,
    val studentName: String,
    val priority: Int, // Position in the list of priorities
    val hardSkill: String,
    val background: String
)