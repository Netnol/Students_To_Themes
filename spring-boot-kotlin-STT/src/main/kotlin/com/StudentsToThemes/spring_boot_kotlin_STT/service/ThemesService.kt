package com.StudentsToThemes.spring_boot_kotlin_STT.service

import com.StudentsToThemes.spring_boot_kotlin_STT.CreateThemeRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.StudentEntity
import com.StudentsToThemes.spring_boot_kotlin_STT.StudentNotFoundException
import com.StudentsToThemes.spring_boot_kotlin_STT.StudentWithPriorityDto
import com.StudentsToThemes.spring_boot_kotlin_STT.ThemeNotFoundException
import com.StudentsToThemes.spring_boot_kotlin_STT.ThemeResponseDto
import com.StudentsToThemes.spring_boot_kotlin_STT.ThemeSpecifications
import com.StudentsToThemes.spring_boot_kotlin_STT.ThemeWithPriorityDto
import com.StudentsToThemes.spring_boot_kotlin_STT.UpdateThemePriorityRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.UpdateThemeRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.repository.StudentsRepository
import com.StudentsToThemes.spring_boot_kotlin_STT.repository.ThemesRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ThemesService(
    private val themesRepository: ThemesRepository,
    private val studentsRepository: StudentsRepository
) {
    private val log = LoggerFactory.getLogger(ThemesService::class.java)

    /**
     * Get all themes.
     * @return a list of themes
     */
    fun getThemes(name: String?, description: String?, author: String?): List<ThemeResponseDto> {
        log.debug("searching themes with filters - name: {}, description: {}, author: {}"
            , name, description, author)

        //Build dynamic query
        val spec = ThemeSpecifications.createSearchSpecification(
            name = name,
            description = description,
            author = author
        )
        return themesRepository.findAll(spec).map { it.toResponseDto() }
    }

    /**
     * Get theme by id.
     * @param id the id of the theme to get
     * @return the theme
     */
    fun getThemeById(id: UUID): ThemeResponseDto {
        log.info("Getting theme by id: {}", id)
        return themesRepository.findById(id)
            .map { it.toResponseDto() }
            .orElseThrow { ThemeNotFoundException(id) }
    }

    /**
     * Create a new theme.
     * @param createRequest the request containing the theme details
     * @return the created theme
     */
    fun createTheme(createRequest: CreateThemeRequest): ThemeResponseDto {
        log.info("Creating new theme: {}", createRequest.name)

        val theme = createRequest.toEntity()

        // If priorityStudents are provided, add them to the theme
        if (createRequest.priorityStudents.isNotEmpty()) {
            val students = studentsRepository.findAllById(createRequest.priorityStudents)
            theme.priorityStudents.addAll(students)
        }

        val savedTheme = themesRepository.save(theme)
        log.info("Successfully created theme with id: {}", savedTheme.id)

        return savedTheme.toResponseDto()
    }

    /**
     * Update the priority of students in a theme.
     * @param themeId the id of the theme to update
     * @param updateRequest the request containing the updated student priorities
     * @return the updated theme
     */
    fun updateThemePriority(themeId: UUID, updateRequest: UpdateThemePriorityRequest): ThemeResponseDto {
        log.info("Updating priorities for theme: {}", themeId)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        val students = studentsRepository.findAllById(updateRequest.studentIds)

        // Clear and add students in the order specified in the request
        theme.priorityStudents.clear()
        theme.priorityStudents.addAll(students)

        val updatedTheme = themesRepository.save(theme)
        log.info("Successfully updated priorities for theme: {}", themeId)

        return updatedTheme.toResponseDto()
    }

    /**
     * Update an existing theme.
     * @param id the id of the theme to update
     * @param updateRequest the request containing the updated theme details
     * @return the updated theme
     */
    fun updateTheme(themeId: UUID, updateRequest: UpdateThemeRequest): ThemeResponseDto {
        log.info("Updating theme: {}", themeId)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        theme.name = updateRequest.name
        theme.description = updateRequest.description
        theme.author = updateRequest.author

        val updatedTheme = themesRepository.save(theme)
        log.info("Successfully updated theme: {}", themeId)

        return updatedTheme.toResponseDto()
    }

    /**
     * Delete a theme by id.
     * @param themeId the id of the theme to delete
     * @return the deleted theme
     */
    fun deleteTheme(themeId: UUID): ThemeResponseDto {
        log.info("Deleting theme: {}", themeId)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        themesRepository.delete(theme)
        log.info("Successfully deleted theme: {}", themeId)

        return theme.toResponseDto()
    }

    /**
     * Add a student to a theme.
     * @param themeId the id of the theme to add the student to
     * @param studentId the id of the student to add
     * @return the updated theme
     */
    fun addStudentToTheme(themeId: UUID, studentId: UUID): ThemeResponseDto {
        log.info("Adding student {} to theme {}", studentId, themeId)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        val student = studentsRepository.findById(studentId)
            .orElseThrow { StudentNotFoundException(studentId) }

        if (theme.priorityStudents.contains(student)) {
            log.info("Student {} is already in theme {}", studentId, themeId)
            return theme.toResponseDto()
        }
        // Add student to the end of the list (lowest priority)
        theme.priorityStudents.add(student)

        val updatedTheme = themesRepository.save(theme)
        log.info("Successfully added student to theme")

        return updatedTheme.toResponseDto()
    }

    /**
     * Add multiple students to a theme.
     * @param themeId the id of the theme to add the students to
     * @param studentIds the ids of the students to add
     * @return the updated theme
     */
    fun addStudentsToTheme(themeId: UUID, studentIds: List<UUID>): ThemeResponseDto {
        log.info("Adding students {} to theme {}", studentIds, themeId)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        val students = studentsRepository.findAllById(studentIds)
        val foundIds = students.map { it.id!! }
        val missingIds = studentIds - foundIds.toSet()

        if (missingIds.isNotEmpty()) {
            log.error("Students not found with ids: {}", missingIds)
            throw StudentNotFoundException(missingIds.first())
        }

        log.info("Found {} students with given filters", students.size)

        // Create a list of students to add
        val studentsToAdd = mutableListOf<StudentEntity>()

        // Check if each student is already in the theme and add them to the list if not
        students.forEach { student ->
            if (theme.priorityStudents.contains(student)) {
                log.info("Student {} is already in theme {}", student.id, themeId)
            } else {
                studentsToAdd.add(student)
            }
        }

        theme.priorityStudents.addAll(studentsToAdd)

        val updatedTheme = themesRepository.save(theme)
        log.info("Successfully added {} students to theme", studentsToAdd.size)

        return updatedTheme.toResponseDto()
    }

    /**
     * Change the activity of students in a theme.
     * @param themeId the id of the theme to change the activity of the students
     * @param active the new activity status
     * @return the updated theme
     */
    fun changeStudentsActivityInTheme(themeId: UUID, active: Boolean): ThemeResponseDto {
        log.info("Changing activity of students in theme {}", themeId)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        theme.priorityStudents.forEach { it.active = active }

        val updatedTheme = themesRepository.save(theme)
        log.info("Successfully changed activity of students in theme {}", themeId)

        return updatedTheme.toResponseDto()
    }

    /**
     * Remove a student from a theme.
     * @param themeId the id of the theme to remove the student from
     * @param studentId the id of the student to remove
     * @return the updated theme
     */
    fun removeStudentFromTheme(themeId: UUID, studentId: UUID): ThemeResponseDto {
        log.info("Removing student {} from theme {}", studentId, themeId)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        theme.priorityStudents.removeIf { it.id == studentId }

        val updatedTheme = themesRepository.save(theme)
        log.info("Successfully removed student from theme")

        return updatedTheme.toResponseDto()
    }

    /**
     * Remove multiple students from a theme.
     * @param themeId the id of the theme to remove the students from
     * @param studentIds the ids of the students to remove
     * @return the updated theme
     */
    fun removeStudentsFromTheme(themeId: UUID, studentIds: List<UUID>): ThemeResponseDto {
        log.info("Removing students {} from theme {}", studentIds, themeId)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        theme.priorityStudents.removeIf { it.id in studentIds }

        val updatedTheme = themesRepository.save(theme)
        log.info("Successfully removed students from theme")

        return updatedTheme.toResponseDto()
    }

    /**
     * Get the students in a theme.
     * @param themeId the id of the theme to get the students from
     * @param limit the maximum number of students to return
     * @return a list of students in the theme
     */
    fun getThemeStudents(themeId: UUID, limit: Int? = null): List<StudentWithPriorityDto> {
        log.debug("Getting students for theme {} with limit: {}", themeId, limit)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        val studentsWithPriority = theme.priorityStudents.mapIndexed { index, student ->
            StudentWithPriorityDto(
                studentId = student.id!!,
                studentName = student.name,
                priority = index,
                hardSkill = student.hardSkill,
                background = student.background
            )
        }

        return if (limit != null) {
            studentsWithPriority.take(limit)
        } else {
            studentsWithPriority
        }
    }

    /**
     * Get the themes for a student.
     * @param studentId the id of the student to get the themes for
     * @return a list of themes for the student
     */
    fun getStudentThemes(studentId: UUID): List<ThemeWithPriorityDto> {
        log.debug("Getting themes for student: {}", studentId)

        val student = studentsRepository.findById(studentId)
            .orElseThrow { StudentNotFoundException(studentId) }

        return student.themes.map { theme ->
            val priority = theme.priorityStudents.indexOfFirst { it.id == studentId }
            ThemeWithPriorityDto(
                themeId = theme.id!!,
                themeName = theme.name,
                priority = if (priority >= 0) priority else -1,
                description = theme.description,
                author = theme.author
            )
        }.filter { it.priority >= 0 } // Only themes where the student is actually present
    }
}