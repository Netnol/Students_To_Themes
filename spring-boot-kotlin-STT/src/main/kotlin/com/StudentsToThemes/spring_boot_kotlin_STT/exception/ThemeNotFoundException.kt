package com.StudentsToThemes.spring_boot_kotlin_STT.exception

import java.util.UUID

class ThemeNotFoundException(
    private val id: UUID
) : RuntimeException(
    "Theme with id $id not found"
)