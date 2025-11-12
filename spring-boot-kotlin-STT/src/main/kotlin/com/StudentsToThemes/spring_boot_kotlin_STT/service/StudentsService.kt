package com.StudentsToThemes.spring_boot_kotlin_STT.service

import com.StudentsToThemes.spring_boot_kotlin_STT.CreateStudentRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.StudentResponseDto
import com.StudentsToThemes.spring_boot_kotlin_STT.StudentNotFoundException
import com.StudentsToThemes.spring_boot_kotlin_STT.UpdateStudentRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.repository.StudentsRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class StudentsService(
    private val studentsRepository: StudentsRepository
) {
    /**
     * Get students by name and cv text.
     * @param name the name to search for
     * @param cvText the cv text to search for
     * @return a list of students
     */
    fun getStudents(name: String?, cvText: String?): List<StudentResponseDto> {
        return if (name != null) {
            // name and cvText
            if (cvText != null) {
                studentsRepository
                    .findByNameContainingIgnoreCaseAndCvTextContainingIgnoreCase(name, cvText)
                    .map { it.toResponseDto() }
            } else {
                // name only
                studentsRepository
                    .findByNameContainingIgnoreCase(name)
                    .map { it.toResponseDto() }
            }
        } else {
            if (cvText != null) {
                // cvText only
                studentsRepository
                    .findByCvTextContainingIgnoreCase(cvText)
                    .map { it.toResponseDto() }
            } else {
                // no filters
                studentsRepository
                    .findAll()
                    .map { it.toResponseDto() }
            }
        }
    }

    /**
     * Get student by id.
     * @param id the id of the student to get
     * @return the student with the given id
     * @throws StudentNotFoundException if the student with the given id does not exist
     */
    fun getStudentById(id: UUID): StudentResponseDto {
        return studentsRepository
            .findById(id)
            .map { it.toResponseDto() }
            .orElseThrow { StudentNotFoundException(id) }
    }

    /**
     * Add a new student.
     * @param createRequest the request containing the student details
     * @return the created student
     */
    fun addStudent(createRequest: CreateStudentRequest): StudentResponseDto {
        return studentsRepository
            .save(
                createRequest.toEntity()
            )
            .toResponseDto()
    }

    /**
     * Update an existing student.
     * @param id the id of the student to update
     * @param updateRequest the request containing the updated student details
     * @return the updated student
     */
    fun updateStudent(id: UUID, updateRequest: UpdateStudentRequest): StudentResponseDto {
        val existingStudent = studentsRepository
            .findById(id)
            .orElseThrow { StudentNotFoundException(id) }

        existingStudent.cvText = updateRequest.cvText

        return studentsRepository.save(existingStudent).toResponseDto()
    }

    /**
     * Delete a student by id.
     * @param id the id of the student to delete
     */
    fun deleteStudent(id: UUID) {
        if (!studentsRepository.existsById(id)) {
            throw StudentNotFoundException(id)
        }
        studentsRepository.deleteById(id)
    }
}