package com.StudentsToThemes.spring_boot_kotlin_STT

import com.StudentsToThemes.spring_boot_kotlin_STT.service.ThemesService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/themes")
class ThemesController(
    private val themesService: ThemesService
) {
    private val log = LoggerFactory.getLogger(ThemesController::class.java)

    /**
     * Get all themes.
     * @return a list of themes
     */
    @GetMapping
    fun getThemes(
        @RequestParam("name", required = false) name: String?,
        @RequestParam("description", required = false) description: String?,
        @RequestParam("author", required = false) author: String?
    ): List<ThemeResponseDto> {
        log.debug("GET /themes called with params: name={}, description={}, author={}",
            name, description, author)
        return themesService.getThemes(name, description, author)
    }

    /**
     * Get a theme by id.
     * @param id the id of the theme to get
     * @return the theme
     */
    @GetMapping("/{id}")
    fun getThemeById(@PathVariable id: UUID): ThemeResponseDto {
        log.debug("GET /themes/{}", id)
        return themesService.getThemeById(id)
    }

    /**
     * Create a new theme.
     * @param createRequest the request containing the theme details
     * @return the created theme
     */
    @PostMapping
    fun createTheme(@RequestBody createRequest: CreateThemeRequest): ThemeResponseDto {
        log.debug("POST /themes")
        return themesService.createTheme(createRequest)
    }

    /**
     * Update the priority of students in a theme.
     * @param themeId the id of the theme to update
     * @param updateRequest the request containing the updated student priorities
     * @return the updated theme
     */
    @PutMapping("/{themeId}/priority")
    fun updateThemePriority(
        @PathVariable themeId: UUID,
        @RequestBody updateRequest: UpdateThemePriorityRequest
    ): ThemeResponseDto {
        log.debug("PUT /themes/{}/priority", themeId)
        return themesService.updateThemePriority(themeId, updateRequest)
    }

    /**
     * Update an existing theme.
     * @param themeId the id of the theme to update
     * @param updateRequest the request containing the updated theme details
     * @return the updated theme
     */
    @PutMapping("/{themeId}")
    fun updateTheme(
        @PathVariable themeId: UUID,
        @RequestBody updateRequest: UpdateThemeRequest
    ): ThemeResponseDto {
        log.debug("PUT /themes/{}", themeId)
        return themesService.updateTheme(themeId, updateRequest)
    }

    /**
     * Delete a theme by id.
     * @param themeId the id of the theme to delete
     * @return the deleted theme
     */
    @DeleteMapping("/{themeId}")
    fun deleteTheme(@PathVariable themeId: UUID): ThemeResponseDto {
        log.debug("DELETE /themes/{}", themeId)
        return themesService.deleteTheme(themeId)
    }

    /**
     * Add a student to a theme.
     * @param themeId the id of the theme to add the student to
     * @param studentId the id of the student to add
     * @return the updated theme
     */
    @PostMapping("/{themeId}/students/{studentId}")
    fun addStudentToTheme(
        @PathVariable themeId: UUID,
        @PathVariable studentId: UUID
    ): ThemeResponseDto {
        log.debug("POST /themes/{}/students/{}", themeId, studentId)
        return themesService.addStudentToTheme(themeId, studentId)
    }

    /**
     * Add multiple students to a theme.
     * @param themeId the id of the theme to add the students to
     * @param studentIds the ids of the students to add
     * @return the updated theme
     */
    @PostMapping("/{themeId}/students")
    fun addStudentsToTheme(
        @PathVariable themeId: UUID,
        @RequestBody studentIds: List<UUID>
    ): ThemeResponseDto {
        log.debug("POST /themes/{}/students", themeId)
        return themesService.addStudentsToTheme(themeId, studentIds)
    }

    /**
     * Change the activity of students in a theme.
     * @param themeId the id of the theme to change the activity of the students
     * @param request the request containing the new activity status
     * @return the updated theme
     */
    @PutMapping("/{themeId}/students/active")
    fun changeStudentsActivityInTheme(
        @PathVariable themeId: UUID,
        @RequestBody request: ActiveRequest
    ): ThemeResponseDto {
        log.debug("PUT /themes/{}/students/active with active: {}", themeId, request.active)
        return themesService.changeStudentsActivityInTheme(themeId, request.active)
    }

    /**
     * Remove a student from a theme.
     * @param themeId the id of the theme to remove the student from
     * @param studentId the id of the student to remove
     * @return the updated theme
     */
    @DeleteMapping("/{themeId}/students/{studentId}")
    fun removeStudentFromTheme(
        @PathVariable themeId: UUID,
        @PathVariable studentId: UUID
    ): ThemeResponseDto {
        log.debug("DELETE /themes/{}/students/{}", themeId, studentId)
        return themesService.removeStudentFromTheme(themeId, studentId)
    }

    /**
     * Remove multiple students from a theme.
     * @param themeId the id of the theme to remove the students from
     * @param studentIds the ids of the students to remove
     * @return the updated theme
     */
    @DeleteMapping("/{themeId}/students")
    fun removeStudentsFromTheme(
        @PathVariable themeId: UUID,
        @RequestBody studentIds: List<UUID>
    ): ThemeResponseDto {
        log.debug("DELETE /themes/{}/students", themeId)
        return themesService.removeStudentsFromTheme(themeId, studentIds)
    }

    /**
     * Get the students in a theme.
     * @param themeId the id of the theme to get the students from
     * @param limit the maximum number of students to return
     * @return a list of students in the theme
     */
    @GetMapping("/{themeId}/students")
    fun getThemeStudents(
        @PathVariable themeId: UUID,
        @RequestParam(required = false) limit: Int?
    ): List<StudentWithPriorityDto> {
        log.debug("GET /themes/{}/students?limit={}", themeId, limit)
        return themesService.getThemeStudents(themeId, limit)
    }

    /**
     * Get the themes for a student.
     * @param studentId the id of the student to get the themes for
     * @return a list of themes for the student
     */
    @GetMapping("/students/{studentId}/themes")
    fun getStudentThemes(@PathVariable studentId: UUID): List<ThemeWithPriorityDto> {
        log.debug("GET /students/{}/themes", studentId)
        return themesService.getStudentThemes(studentId)
    }

    /**
     * Update the students in a specialization of a theme.
     * @param themeId the id of the theme to update the students in
     * @param specializationName the name of the specialization to update the students in
     * @param studentIds the ids of the students to update
     * @return the updated theme
     */
    @PutMapping("/{themeId}/specializations/{specializationName}/students")
    fun updateSpecializationStudents(
        @PathVariable themeId: UUID,
        @PathVariable specializationName: String,
        @RequestBody studentIds: List<UUID>
    ): ThemeResponseDto {
        log.debug("PUT /themes/{}/specializations/{}/students", themeId, specializationName)
        return themesService.updateSpecializationStudents(themeId, specializationName, studentIds)
    }

    /**
     * Copy the students from the theme to a specialization.
     * @param themeId the id of the theme to copy the students from
     * @param specializationName the name of the specialization to copy the students to
     * @return the updated theme
     */
    @PostMapping("/{themeId}/specializations/{specializationName}/copy-from-theme")
    fun copyThemeStudentsToSpecialization(
        @PathVariable themeId: UUID,
        @PathVariable specializationName: String
    ): ThemeResponseDto {
        log.debug("POST /themes/{}/specializations/{}/copy-from-theme", themeId, specializationName)
        return themesService.copyThemeStudentsToSpecialization(themeId, specializationName)
    }

    /**
     * Get the students in a specialization of a theme.
     * @param themeId the id of the theme to get the students from
     * @param specializationName the name of the specialization to get the students from
     * @param limit the maximum number of students to return
     * @return a list of students in the specialization
     */
    @GetMapping("/{themeId}/specializations/{specializationName}/students")
    fun getSpecializationStudents(
        @PathVariable themeId: UUID,
        @PathVariable specializationName: String,
        @RequestParam(required = false) limit: Int?
    ): List<StudentWithPriorityDto> {
        log.debug("GET /themes/{}/specializations/{}/students?limit={}", themeId, specializationName, limit)
        return themesService.getSpecializationStudents(themeId, specializationName, limit)
    }

    /**
     * Add a student to a specialization in a theme.
     * @param themeId the id of the theme to add the student to
     * @param specializationName the name of the specialization to add the student to
     * @param studentId the id of the student to add
     * @return the updated theme
     */
    @PostMapping("/{themeId}/specializations/{specializationName}/students/{studentId}")
    fun addStudentToSpecialization(
        @PathVariable themeId: UUID,
        @PathVariable specializationName: String,
        @PathVariable studentId: UUID
    ): ThemeResponseDto {
        log.debug("POST /themes/{}/specializations/{}/students/{}", themeId, specializationName, studentId)
        return themesService.addStudentToSpecialization(themeId, specializationName, studentId)
    }

    /**
     * Remove a student from a specialization in a theme.
     * @param themeId the id of the theme to remove the student from
     * @param specializationName the name of the specialization to remove the student from
     * @param studentId the id of the student to remove
     * @return the updated theme
     */
    @DeleteMapping("/{themeId}/specializations/{specializationName}/students/{studentId}")
    fun removeStudentFromSpecialization(
        @PathVariable themeId: UUID,
        @PathVariable specializationName: String,
        @PathVariable studentId: UUID
    ): ThemeResponseDto {
        log.debug("DELETE /themes/{}/specializations/{}/students/{}", themeId, specializationName, studentId)
        return themesService.removeStudentFromSpecialization(themeId, specializationName, studentId)
    }

    /**
     * Get the specializations for a student.
     * @param studentId the id of the student to get the specializations for
     * @return a map of specializations to themes
     */
    @GetMapping("/students/{studentId}/specializations")
    fun getStudentSpecializations(@PathVariable studentId: UUID): Map<String, Map<UUID, Int>> {
        log.debug("GET /students/{}/specializations", studentId)
        return themesService.getStudentSpecializations(studentId)
    }

    /**
     * Add a specialization to a theme.
     * @param themeId the id of the theme to add the specialization to
     * @param request the request containing the name of the specialization to add
     * @return the updated theme
     */
    @PostMapping("/{themeId}/specializations")
    fun addSpecialization(
        @PathVariable themeId: UUID,
        @RequestBody @Valid request: SpecializationRequest
    ): ThemeResponseDto {
        log.debug("POST /themes/{}/specializations with specialization: {}", themeId, request.name)
        return themesService.addSpecializationToTheme(themeId, request.name)
    }

    /**
     * Remove a specialization from a theme.
     * @param themeId the id of the theme to remove the specialization from
     * @param specializationName the name of the specialization to remove
     * @return the updated theme
     */
    @DeleteMapping("/{themeId}/specializations/{specializationName}")
    fun removeSpecialization(
        @PathVariable themeId: UUID,
        @PathVariable specializationName: String
    ): ThemeResponseDto {
        log.debug("DELETE /themes/{}/specializations/{}", themeId, specializationName)
        return themesService.removeSpecializationFromTheme(themeId, specializationName)
    }

    /**
     * Update the specializations in a theme.
     * @param themeId the id of the theme to update the specializations in
     * @param specializations the list of specializations to update
     * @return the updated theme
     */
    @PutMapping("/{themeId}/specializations")
    fun updateSpecializations(
        @PathVariable themeId: UUID,
        @RequestBody specializations: List<String>
    ): ThemeResponseDto {
        log.debug("PUT /themes/{}/specializations with: {}", themeId, specializations)
        return themesService.updateThemeSpecializations(themeId, specializations)
    }
}