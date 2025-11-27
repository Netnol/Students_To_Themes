package com.StudentsToThemes.spring_boot_kotlin_STT.exception

import java.util.UUID

class StudentNotFoundException(
    private val id: UUID
) : RuntimeException(
    "Student with id $id not found"
)