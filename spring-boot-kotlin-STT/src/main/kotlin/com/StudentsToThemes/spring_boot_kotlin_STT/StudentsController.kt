package com.StudentsToThemes.spring_boot_kotlin_STT

import com.StudentsToThemes.spring_boot_kotlin_STT.service.StudentsService
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
@RequestMapping("/students")
class StudentsController(
    private val studentsService: StudentsService
) {
    private val log = LoggerFactory.getLogger(StudentsController::class.java)

    /**
     * Get students by name and cv text.
     * @param name the optional name to search for
     * @return a list of students
     */
    @GetMapping
    fun loadStudents(
        @RequestParam("name", required = false) name: String?,
        @RequestParam("hardSkill", required = false) hardSkill: String?,
        @RequestParam("background", required = false) background: String?,
        @RequestParam("interests", required = false) interests: String?,
        @RequestParam("timeInWeek", required = false) timeInWeek: String?
    ): List<StudentResponseDto> {
        log.debug("GET /students called with params: name={}, hardSkill={}, background={}, interests={}, timeInWeek={}",
            name, hardSkill, background, interests, timeInWeek)

        return studentsService.getStudents(name, hardSkill, background, interests, timeInWeek)
    }

    /**
     * Get student by id.
     * @param id the id of the student to get
     * @return the student with the given id
     */
    @GetMapping("/{id}")
    fun getStudentById(
        @PathVariable("id") id: UUID
    ): StudentResponseDto {
        log.debug("GET /students/{} called", id)
        return studentsService.getStudentById(id)
    }

    /**
     * Get students by ids.
     * @param ids the ids of the students to get
     * @return a list of students
     */
    @GetMapping("/by-ids")
    fun getStudentsByIds(
        @RequestBody ids: List<UUID>
    ): List<StudentResponseDto> {
        log.debug("GET /students/by-ids called with ids: {}", ids)
        return studentsService.getStudentsByIds(ids)
    }

    /**
     * Get active students.
     * @return a list of active students
     */
    @GetMapping("/active")
    fun getActiveStudents(): List<StudentResponseDto> {
        log.debug("GET /students/active called")
        return studentsService.getActiveStudents()
    }

    /**
     * Get unactive students.
     * @return a list of unactive students
     */
    @GetMapping("/unactive")
    fun getUnactiveStudents(): List<StudentResponseDto> {
        log.debug("GET /students/unactive called")
        return studentsService.getUnactiveStudents()
    }

    /**
     * Add a new student.
     * @param createRequest the request containing the student details
     * @return the created student
     */
    @PostMapping
    fun addStudent(
        @RequestBody createRequest: CreateStudentRequest
    ): StudentResponseDto {
        log.debug("POST /students called for student: {}", createRequest.name)
        return studentsService.addStudent(createRequest)
    }

    /**
     * Add new students.
     * @param createRequest list of the requests containing the student details
     * @return list of the created students
     */
    @PostMapping("/by-ids")
    fun addStudents(
        @RequestBody createRequests: List<CreateStudentRequest>
    ): List<StudentResponseDto> {
        log.debug("POST /students/by-ids called for students: {}", createRequests.map { it.name })
        return studentsService.addStudents(createRequests)
    }

    /**
     * Update an existing student.
     * @param id the id of the student to update
     * @param updateRequest the request containing the updated student details
     * @return the updated student
     */
    @PutMapping("/{id}")
    fun updateStudent(
        @PathVariable("id") id: UUID,
        @RequestBody updateRequest: UpdateStudentRequest
    ): StudentResponseDto {
        log.debug("PUT /students/{} called for student: {}", id, updateRequest.name)
        return studentsService.updateStudent(id, updateRequest)
    }

    /**
     * Change the activity of a student.
     * @param id the id of the student to change the activity
     * @param updateRequest the request containing the updated student details
     * @return the updated student
     */
    @PutMapping("/{id}/change-activity")
    fun changeStudentActivity(
        @PathVariable("id") id: UUID,
        @RequestBody active: Boolean
    ): StudentResponseDto {
        log.debug("PUT /students/{} called for student: {}", id, active)
        return studentsService.changeStudentActivity(id, active)
    }

    /**
     * Change the activity of students.
     * @param ids the ids of the students to change the activity
     * @param active the new activity status
     */
    @PutMapping("/{id}/change-activities")
    fun changeStudentActivities(
        @RequestBody ids: List<UUID>,
        @RequestBody active: Boolean
    ) {
        log.debug("PUT /students/{} called for students: {}", ids, active)
        studentsService.changeStudentActivities(ids, active)
    }

    /**
     * Delete a student by id.
     * @param id the id of the student to delete
     */
    @DeleteMapping("/{id}")
    fun deleteStudent(
        @PathVariable("id") id: UUID
    ) {
        log.debug("DELETE /students/{} called", id)
        studentsService.deleteStudent(id)
    }

    /**
     * Delete students by ids.
     * @param ids the ids of the students to delete
     */
    @DeleteMapping("/by-ids")
    fun deleteStudents(
        @RequestBody ids: List<UUID>
    ) {
        log.debug("DELETE /students/by-ids called for students: {}", ids)
        studentsService.deleteStudents(ids)
    }

    /**
     * Delete all students.
     */
    @DeleteMapping("/all")
    fun deleteAllStudents() {
        log.debug("DELETE /students/all called")
        studentsService.deleteAllStudents()
    }

    /**
     * Delete unactive students.
     */
    @DeleteMapping("/unactive")
    fun deleteUnactiveStudents() {
        log.debug("DELETE /students/unactive called")
        studentsService.deleteUnactiveStudents()
    }
}