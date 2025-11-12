package com.StudentsToThemes.spring_boot_kotlin_STT.repository

import com.StudentsToThemes.spring_boot_kotlin_STT.StudentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface StudentsRepository: JpaRepository<StudentEntity, UUID> {
    /**
     * Find students by name containing the given string, ignoring case.
     * @param name the name to search for
     * @return a list of students
     */
    fun findByNameContainingIgnoreCase(name: String): List<StudentEntity>
    /**
     * Find students by name containing the given string and cv text containing the given string, ignoring case.
     * @param name the name to search for
     * @param cvText the cv text to search for
     * @return a list of students
     */
    fun findByNameContainingIgnoreCaseAndCvTextContainingIgnoreCase(name: String, cvText: String): List<StudentEntity>
    /**
     * Find students by cv text containing the given string, ignoring case.
     * @param cvText the cv text to search for
     * @return a list of students
     */
    fun findByCvTextContainingIgnoreCase(cvText: String): List<StudentEntity>
}