package com.StudentsToThemes.spring_boot_kotlin_STT.service

import com.StudentsToThemes.spring_boot_kotlin_STT.DTO.CreateThemeRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.entity.StudentEntity
import com.StudentsToThemes.spring_boot_kotlin_STT.exception.StudentNotFoundException
import com.StudentsToThemes.spring_boot_kotlin_STT.DTO.StudentWithPriorityDto
import com.StudentsToThemes.spring_boot_kotlin_STT.exception.ThemeNotFoundException
import com.StudentsToThemes.spring_boot_kotlin_STT.DTO.ThemeResponseDto
import com.StudentsToThemes.spring_boot_kotlin_STT.entity.ThemeSpecializationStudent
import com.StudentsToThemes.spring_boot_kotlin_STT.queriesBuilder.ThemeSpecifications
import com.StudentsToThemes.spring_boot_kotlin_STT.DTO.ThemeWithPriorityDto
import com.StudentsToThemes.spring_boot_kotlin_STT.DTO.UpdateThemePriorityRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.DTO.UpdateThemeRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.repository.StudentsRepository
import com.StudentsToThemes.spring_boot_kotlin_STT.repository.ThemeSpecializationStudentRepository
import com.StudentsToThemes.spring_boot_kotlin_STT.repository.ThemesRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ThemesService(
    private val themesRepository: ThemesRepository,
    private val studentsRepository: StudentsRepository,
    private val themeSpecializationStudentRepository: ThemeSpecializationStudentRepository
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

        try {
            // Validation of the request is performed in the init block of CreateThemeRequest

            val theme = createRequest.toEntity()

            // If priorityStudents are given, add them to the theme
            if (createRequest.priorityStudents.isNotEmpty()) {
                val students = studentsRepository.findAllById(createRequest.priorityStudents)
                val foundIds = students.map { it.id!! }
                val missingIds = createRequest.priorityStudents - foundIds.toSet()

                if (missingIds.isNotEmpty()) {
                    throw StudentNotFoundException(missingIds.first())
                }

                theme.priorityStudents.addAll(students)
            }

            val savedTheme = themesRepository.save(theme)
            log.info("Successfully created theme with id: {}", savedTheme.id)

            return savedTheme.toResponseDto()
        } catch (e: IllegalArgumentException) {
            log.error("Validation error while creating theme: {}", e.message)
            throw e // Rethrow for controller to handle
        }
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

        try {
            // Validation is already in init block of UpdateThemeRequest

            val theme = themesRepository.findById(themeId)
                .orElseThrow { ThemeNotFoundException(themeId) }

            theme.name = updateRequest.name
            theme.description = updateRequest.description
            theme.author = updateRequest.author

            // Update specializations
            theme.updateSpecializations(updateRequest.specializations)

            val updatedTheme = themesRepository.save(theme)
            log.info("Successfully updated theme: {}", themeId)

            return updatedTheme.toResponseDto()
        } catch (e: IllegalArgumentException) {
            log.error("Validation error while updating theme: {}", e.message)
            throw e
        }
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

    /**
     * Update the students for a specialization in a theme.
     * @param themeId the id of the theme to update the students for
     * @param specializationName the name of the specialization to update the students for
     * @param studentIds the ids of the students to update
     * @return the updated theme
     */
    fun updateSpecializationStudents(themeId: UUID, specializationName: String, studentIds: List<UUID>): ThemeResponseDto {
        log.info("Updating students for specialization {} in theme {}", specializationName, themeId)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        // Validation of existence of specialization
        val exactSpecializationName = theme.getExactSpecializationName(specializationName)
            ?: throw IllegalArgumentException("Specialization '$specializationName' not found in theme")

        // Validation of students
        val students = studentsRepository.findAllById(studentIds)
        val foundIds = students.map { it.id!! }
        val missingIds = studentIds - foundIds.toSet()

        if (missingIds.isNotEmpty()) {
            throw StudentNotFoundException(missingIds.first())
        }

        // Checking students duplicates
        val duplicateStudents = studentIds.groupBy { it }.filter { it.value.size > 1 }
        if (duplicateStudents.isNotEmpty()) {
            throw IllegalArgumentException("Duplicate student IDs found: ${duplicateStudents.keys}")
        }

        // Deleting old records of this specialisation
        themeSpecializationStudentRepository.deleteByThemeIdAndSpecializationName(themeId, exactSpecializationName)

        // Creating new records
        students.forEachIndexed { index, student ->
            val specializationStudent = ThemeSpecializationStudent(
                theme = theme,
                specializationName = exactSpecializationName,
                student = student,
                priorityOrder = index
            )
            themeSpecializationStudentRepository.save(specializationStudent)
        }

        log.info("Successfully updated students for specialization {} in theme {}", specializationName, themeId)

        // Reloading theme with new data
        return themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }
            .toResponseDto()
    }

    /**
     * Copy the students from the theme to a specialization.
     * @param themeId the id of the theme to copy the students from
     * @param specializationName the name of the specialization to copy the students to
     * @return the updated theme
     */
    fun copyThemeStudentsToSpecialization(themeId: UUID, specializationName: String): ThemeResponseDto {
        log.info("Copying theme students to specialization {} in theme {}", specializationName, themeId)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        if (!theme.specializations.contains(specializationName)) {
            throw IllegalArgumentException("Specialization $specializationName not found in theme")
        }

        // Копируем основной список студентов в специализацию
        return updateSpecializationStudents(themeId, specializationName,
            theme.priorityStudents.map { it.id!! })
    }

    /**
     * Get the students for a specialization in a theme.
     * @param themeId the id of the theme to get the students for
     * @param specializationName the name of the specialization to get the students for
     * @param limit the maximum number of students to return
     * @return a list of students in the specialization
     */
    fun getSpecializationStudents(themeId: UUID, specializationName: String, limit: Int? = null): List<StudentWithPriorityDto> {
        log.debug("Getting students for specialization {} in theme {} with limit: {}",
            specializationName, themeId, limit)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        if (!theme.specializations.contains(specializationName)) {
            throw IllegalArgumentException("Specialization $specializationName not found in theme")
        }

        val specializationStudents = themeSpecializationStudentRepository
            .findByThemeIdAndSpecializationName(themeId, specializationName)
            .sortedBy { it.priorityOrder }

        val studentsWithPriority = specializationStudents.map { entity ->
            StudentWithPriorityDto(
                studentId = entity.student.id!!,
                studentName = entity.student.name,
                priority = entity.priorityOrder,
                hardSkill = entity.student.hardSkill,
                background = entity.student.background
            )
        }

        return if (limit != null) {
            studentsWithPriority.take(limit)
        } else {
            studentsWithPriority
        }
    }

    /**
     * Add a student to a specialization in a theme.
     * @param themeId the id of the theme to add the student to
     * @param specializationName the name of the specialization to add the student to
     * @param studentId the id of the student to add
     * @return the updated theme
     */
    fun addStudentToSpecialization(themeId: UUID, specializationName: String, studentId: UUID): ThemeResponseDto {
        log.info("Adding student {} to specialization {} in theme {}", studentId, specializationName, themeId)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        if (!theme.specializations.contains(specializationName)) {
            throw IllegalArgumentException("Specialization $specializationName not found in theme")
        }

        val student = studentsRepository.findById(studentId)
            .orElseThrow { StudentNotFoundException(studentId) }

        // Check if the student is already added
        val existing = themeSpecializationStudentRepository
            .findByThemeIdAndSpecializationNameAndStudentId(themeId, specializationName, studentId)

        if (existing.isPresent) {
            log.info("Student {} is already in specialization {} of theme {}", studentId, specializationName, themeId)
            return theme.toResponseDto()
        }

        // Defining next priority
        val nextPriority = themeSpecializationStudentRepository
            .findMaxPriorityOrderByThemeAndSpecialization(themeId, specializationName)
            ?.plus(1) ?: 0

        val specializationStudent = ThemeSpecializationStudent(
            theme = theme,
            specializationName = specializationName,
            student = student,
            priorityOrder = nextPriority
        )

        themeSpecializationStudentRepository.save(specializationStudent)
        log.info("Successfully added student to specialization")

        return theme.toResponseDto()
    }

    /**
     * Remove a student from a specialization in a theme.
     * @param themeId the id of the theme to remove the student from
     * @param specializationName the name of the specialization to remove the student from
     * @param studentId the id of the student to remove
     * @return the updated theme
     */
    fun removeStudentFromSpecialization(themeId: UUID, specializationName: String, studentId: UUID): ThemeResponseDto {
        log.info("Removing student {} from specialization {} in theme {}", studentId, specializationName, themeId)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        if (!theme.specializations.contains(specializationName)) {
            throw IllegalArgumentException("Specialization $specializationName not found in theme")
        }

        val existing = themeSpecializationStudentRepository
            .findByThemeIdAndSpecializationNameAndStudentId(themeId, specializationName, studentId)

        existing.ifPresent { themeSpecializationStudentRepository.delete(it) }
        log.info("Successfully removed student from specialization")

        return theme.toResponseDto()
    }

    /**
     * Get the specializations for a student.
     * @param studentId the id of the student to get the specializations for
     * @return a map of specializations to themes
     */
    fun getStudentSpecializations(studentId: UUID): Map<String, Map<UUID, Int>> {
        log.debug("Getting all specializations for student: {}", studentId)

        val student = studentsRepository.findById(studentId)
            .orElseThrow { StudentNotFoundException(studentId) }

        return student.specializationThemes
            .groupBy { it.specializationName }
            .mapValues { (_, specializations) ->
                specializations.associate { specialization ->
                    specialization.theme.id!! to specialization.priorityOrder
                }
            }
    }

    /**
     * Add a specialization to a theme.
     * @param themeId the id of the theme to add the specialization to
     * @param specialization the name of the specialization to add
     * @return the updated theme
     */
    fun addSpecializationToTheme(themeId: UUID, specialization: String): ThemeResponseDto {
        log.info("Adding specialization {} to theme {}", specialization, themeId)

        // Pre-Validation
        if (specialization.isBlank()) {
            throw IllegalArgumentException("Specialization name cannot be blank")
        }

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        // Checking if the specialization already exists
        if (theme.hasSpecialization(specialization)) {
            throw IllegalArgumentException("Specialization '$specialization' already exists in theme")
        }

        theme.addSpecialization(specialization)

        val updatedTheme = themesRepository.save(theme)
        log.info("Successfully added specialization {} to theme {}", specialization, themeId)

        return updatedTheme.toResponseDto()
    }

    /**
     * Remove a specialization from a theme.
     * @param themeId the id of the theme to remove the specialization from
     * @param specialization the name of the specialization to remove
     * @return the updated theme
     */
    fun removeSpecializationFromTheme(themeId: UUID, specialization: String): ThemeResponseDto {
        log.info("Removing specialization {} from theme {}", specialization, themeId)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        // Checking existence of specialisation
        if (!theme.hasSpecialization(specialization)) {
            throw IllegalArgumentException("Specialization '$specialization' not found in theme")
        }

        theme.removeSpecialization(specialization)

        val updatedTheme = themesRepository.save(theme)
        log.info("Successfully removed specialization {} from theme {}", specialization, themeId)

        return updatedTheme.toResponseDto()
    }

    /**
     * Update the specializations for a theme.
     * @param themeId the id of the theme to update the specializations for
     * @param specializations the list of specializations to update
     * @return the updated theme
     */
    fun updateThemeSpecializations(themeId: UUID, specializations: List<String>): ThemeResponseDto {
        log.info("Updating specializations for theme {}: {}", themeId, specializations)

        val theme = themesRepository.findById(themeId)
            .orElseThrow { ThemeNotFoundException(themeId) }

        theme.updateSpecializations(specializations)

        val updatedTheme = themesRepository.save(theme)
        log.info("Successfully updated specializations for theme {}", themeId)

        return updatedTheme.toResponseDto()
    }
}