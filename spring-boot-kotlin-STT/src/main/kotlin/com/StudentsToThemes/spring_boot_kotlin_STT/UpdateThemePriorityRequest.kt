package com.StudentsToThemes.spring_boot_kotlin_STT

import java.util.UUID

data class UpdateThemePriorityRequest(
    val studentIds: List<UUID> // New order of students
)
