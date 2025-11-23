package com.StudentsToThemes.spring_boot_kotlin_STT.repository

import com.StudentsToThemes.spring_boot_kotlin_STT.StudentEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import java.util.Optional
import java.util.UUID

interface StudentsRepository: JpaRepository<StudentEntity, UUID>, JpaSpecificationExecutor<StudentEntity> {
    @EntityGraph(attributePaths = ["themes", "specializationThemes", "specializationThemes.theme"])
    override fun findAll(): List<StudentEntity>

    @EntityGraph(attributePaths = ["themes", "specializationThemes", "specializationThemes.theme"])
    override fun findById(id: UUID): Optional<StudentEntity>

    /**
     * Find students by name containing the given string, ignoring case.
     * @param name the name to search for
     * @return a list of students
     */
    fun findByNameContainingIgnoreCase(name: String): List<StudentEntity>

    /**
     * Delete all students that are not active.
     */
    fun deleteAllByActiveFalse()

    /**
     * Find all students that are active.
     * @return a list of students
     */
    fun findAllByActiveTrue(): List<StudentEntity>

    /**
     * Find all students that are not active.
     * @return a list of students
     */
    fun findAllByActiveFalse(): List<StudentEntity>
}