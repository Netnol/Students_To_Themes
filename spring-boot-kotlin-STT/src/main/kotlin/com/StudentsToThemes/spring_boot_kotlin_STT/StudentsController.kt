package com.StudentsToThemes.spring_boot_kotlin_STT

import com.StudentsToThemes.spring_boot_kotlin_STT.service.StudentsService
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
    /**
     * Get students by name and cv text.
     * @param name the optional name to search for
     * @param cvText the optional cv text to search for
     * @return a list of students
     */
    @GetMapping
    fun loadStudents(
        @RequestParam("name", required = false) name: String?,
        @RequestParam("cv_text", required = false) cvText: String?
    ): List<StudentResponseDto> {
        return studentsService.getStudents(name, cvText)
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
        return studentsService.getStudentById(id)
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
        return studentsService.addStudent(createRequest)
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
        return studentsService.updateStudent(id, updateRequest)
    }

    /**
     * Delete a student by id.
     * @param id the id of the student to delete
     */
    @DeleteMapping("/{id}")
    fun deleteStudent(
        @PathVariable("id") id: UUID
    ) {
        studentsService.deleteStudent(id)
    }
}