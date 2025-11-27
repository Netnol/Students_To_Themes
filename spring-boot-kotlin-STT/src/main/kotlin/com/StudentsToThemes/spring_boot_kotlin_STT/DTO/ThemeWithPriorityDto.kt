package com.StudentsToThemes.spring_boot_kotlin_STT.DTO

import java.util.UUID

data class ThemeWithPriorityDto(
    val themeId: UUID,
    val themeName: String,
    val priority: Int, // Order of theme in list of priorities
    val description: String,
    val author: String
)