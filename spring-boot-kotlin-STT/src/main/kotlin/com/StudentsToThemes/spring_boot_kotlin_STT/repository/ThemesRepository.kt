package com.StudentsToThemes.spring_boot_kotlin_STT.repository

import com.StudentsToThemes.spring_boot_kotlin_STT.entity.ThemeEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional
import java.util.UUID

interface ThemesRepository: JpaRepository<ThemeEntity, UUID>, JpaSpecificationExecutor<ThemeEntity> {

    @EntityGraph(attributePaths = ["priorityStudents", "specializationStudents", "specializationStudents.student"])
    override fun findAll(): List<ThemeEntity>

    @EntityGraph(attributePaths = ["priorityStudents", "specializationStudents", "specializationStudents.student"])
    override fun findById(id: UUID): Optional<ThemeEntity>

    @EntityGraph(attributePaths = ["priorityStudents", "specializationStudents", "specializationStudents.student"])
    @Query("SELECT t FROM ThemeEntity t WHERE t.id = :id")
    fun findByIdWithAllData(@Param("id") id: UUID): Optional<ThemeEntity>

    @EntityGraph(attributePaths = ["priorityStudents", "specializationStudents", "specializationStudents.student"])
    @Query("SELECT t FROM ThemeEntity t WHERE t.id IN :ids")
    fun findAllByIdWithStudents(@Param("ids") ids: List<UUID>): List<ThemeEntity>

    /**
     * Check if theme has specialization
     */
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM ThemeEntity t JOIN t.specializations s " +
            "WHERE t.id = :themeId AND s = :specializationName")
    fun hasSpecialization(
        @Param("themeId") themeId: UUID,
        @Param("specializationName") specializationName: String
    ): Boolean

    /**
     * Add a specialization to ml_sorted_specializations table
     */
    @Modifying
    @Query(
        value = """
        INSERT INTO theme_ml_sorted_specializations (theme_id, specialization_name) 
        VALUES (:themeId, :specializationName) 
        ON CONFLICT DO NOTHING
        """,
        nativeQuery = true
    )
    fun addMlSortedSpecialization(
        @Param("themeId") themeId: UUID,
        @Param("specializationName") specializationName: String
    )

    /**
     * Clear ml sorted specializations for a theme
     */
    @Modifying
    @Query(
        value = "DELETE FROM theme_ml_sorted_specializations WHERE theme_id = :themeId",
        nativeQuery = true
    )
    fun clearMlSortedSpecializations(@Param("themeId") themeId: UUID)
}