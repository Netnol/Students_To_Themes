package com.StudentsToThemes.spring_boot_kotlin_STT

import com.StudentsToThemes.spring_boot_kotlin_STT.service.ThemesService
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
     * @param active the new activity status
     * @return the updated theme
     */
    @PutMapping("/{themeId}/students/active")
    fun changeStudentsActivityInTheme(
        @PathVariable themeId: UUID,
        @RequestBody active: Boolean
    ): ThemeResponseDto {
        log.debug("PUT /themes/{}/students/active", themeId)
        return themesService.changeStudentsActivityInTheme(themeId, active)
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
}