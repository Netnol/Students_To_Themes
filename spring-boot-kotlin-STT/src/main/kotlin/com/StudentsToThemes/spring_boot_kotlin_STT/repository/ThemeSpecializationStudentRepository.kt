package com.StudentsToThemes.spring_boot_kotlin_STT.repository

import com.StudentsToThemes.spring_boot_kotlin_STT.entity.ThemeSpecializationStudent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional
import java.util.UUID

interface ThemeSpecializationStudentRepository : JpaRepository<ThemeSpecializationStudent, UUID> {

    /**
     * Find all students in a specialization in a theme.
     * @param themeId the id of the theme to search in
     * @param specializationName the name of the specialization to search in
     * @return a list of students in the specialization in the theme
     */
    fun findByThemeIdAndSpecializationName(themeId: UUID, specializationName: String): List<ThemeSpecializationStudent>

    /**
     * Find a student in a specialization in a theme.
     * @param themeId the id of the theme to search in
     * @param specializationName the name of the specialization to search in
     * @param studentId the id of the student to search for
     * @return an optional containing the student if found, or an empty optional if not found
     */
    fun findByThemeIdAndSpecializationNameAndStudentId(
        themeId: UUID,
        specializationName: String,
        studentId: UUID
    ): Optional<ThemeSpecializationStudent>

    /**
     * Delete a student from a specialization in a theme.
     * @param themeId the id of the theme to delete the student from
     * @param specializationName the name of the specialization to delete the student from
     * @param studentId the id of the student to delete
     * @return the number of rows deleted
     */
    @Modifying
    @Query("DELETE FROM ThemeSpecializationStudent tss WHERE tss.theme.id = :themeId AND tss.specializationName = :specializationName AND tss.student.id = :studentId")
    fun deleteByThemeIdAndSpecializationNameAndStudentId(
        @Param("themeId") themeId: UUID,
        @Param("specializationName") specializationName: String,
        @Param("studentId") studentId: UUID
    ): Int

    /**
     * Find student by id.
     * @param studentId the id of the student to find
     * @return a list of students with the given id
     */
    fun findByStudentId(studentId: UUID): List<ThemeSpecializationStudent>

    /**
     * Find the maximum priority order of students in a specialization in a theme.
     * @param themeId the id of the theme to search in
     * @param specializationName the name of the specialization to search in
     * @return the maximum priority order
     */
    @Query("SELECT MAX(tss.priorityOrder) FROM ThemeSpecializationStudent tss WHERE tss.theme.id = :themeId AND tss.specializationName = :specializationName")
    fun findMaxPriorityOrderByThemeAndSpecialization(
        @Param("themeId") themeId: UUID,
        @Param("specializationName") specializationName: String
    ): Int?

    /**
     * Delete all students from a specific specialization in a theme
     * @param themeId the id of the theme
     * @param specializationName the name of the specialization
     */
    @Modifying
    @Query("DELETE FROM ThemeSpecializationStudent tss WHERE tss.theme.id = :themeId AND tss.specializationName = :specializationName")
    fun deleteByThemeIdAndSpecializationName(
        @Param("themeId") themeId: UUID,
        @Param("specializationName") specializationName: String
    )

    @Query("SELECT DISTINCT tss.specializationName FROM ThemeSpecializationStudent tss " +
            "WHERE tss.theme.id = :themeId")
    fun findDistinctSpecializationNamesByThemeId(@Param("themeId") themeId: UUID): List<String>
}