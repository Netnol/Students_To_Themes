package com.StudentsToThemes.spring_boot_kotlin_STT.repository

import com.StudentsToThemes.spring_boot_kotlin_STT.entity.ThemeEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
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
    @Query("SELECT t FROM ThemeEntity t WHERE t.id IN :ids")
    fun findAllByIdWithStudents(@Param("ids") ids: List<UUID>): List<ThemeEntity>
}