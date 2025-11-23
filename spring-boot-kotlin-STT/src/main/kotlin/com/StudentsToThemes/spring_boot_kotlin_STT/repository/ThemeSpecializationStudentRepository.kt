package com.StudentsToThemes.spring_boot_kotlin_STT.repository

import com.StudentsToThemes.spring_boot_kotlin_STT.ThemeSpecializationStudent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional
import java.util.UUID

interface ThemeSpecializationStudentRepository : JpaRepository<ThemeSpecializationStudent, UUID> {

    fun findByThemeIdAndSpecializationName(themeId: UUID, specializationName: String): List<ThemeSpecializationStudent>

    fun findByThemeIdAndSpecializationNameAndStudentId(
        themeId: UUID,
        specializationName: String,
        studentId: UUID
    ): Optional<ThemeSpecializationStudent>

    fun deleteByThemeIdAndSpecializationName(themeId: UUID, specializationName: String)

    fun findByStudentId(studentId: UUID): List<ThemeSpecializationStudent>

    @Query("SELECT MAX(tss.priorityOrder) FROM ThemeSpecializationStudent tss WHERE tss.theme.id = :themeId AND tss.specializationName = :specializationName")
    fun findMaxPriorityOrderByThemeAndSpecialization(
        @Param("themeId") themeId: UUID,
        @Param("specializationName") specializationName: String
    ): Int?
}