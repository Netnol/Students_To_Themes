package com.StudentsToThemes.spring_boot_kotlin_STT.service

import com.StudentsToThemes.spring_boot_kotlin_STT.CreateStudentRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.StudentResponseDto
import com.StudentsToThemes.spring_boot_kotlin_STT.StudentNotFoundException
import com.StudentsToThemes.spring_boot_kotlin_STT.UpdateStudentRequest
import com.StudentsToThemes.spring_boot_kotlin_STT.repository.StudentsRepository
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class StudentsService(
    private val studentsRepository: StudentsRepository
) {
    // Создаем логгер для этого класса
    private val log = LoggerFactory.getLogger(StudentsService::class.java)

    /**
     * Get students by name, hard skill, background, interests and time in week.
     * @param name the name to search for
     * @param hardSkill the hard skill to search for
     * @param background the background to search for
     * @param interests the interests to search for
     * @param timeInWeek the time in week to search for
     * @return a list of students
     */
    fun getStudents(name: String?, hardSkill: String?, background: String?
                    , interests: String?, timeInWeek: String?
    ): List<StudentResponseDto> {
        log.debug("Searching students with filters - name: {}, hardSkill: {}, background: {}, interests: {}, timeInWeek: {}",
            name, hardSkill, background, interests, timeInWeek)

        // Build dynamic query
        val spec = StudentSpecifications.createSearchSpecification(
            name = name,
            hardSkill = hardSkill,
            background = background,
            interests = interests,
            timeInWeek = timeInWeek
        )

        return studentsRepository.findAll(spec).map { it.toResponseDto() }
    }

    /**
     * Get student by id.
     * @param id the id of the student to get
     * @return the student with the given id
     * @throws StudentNotFoundException if the student with the given id does not exist
     */
    fun getStudentById(id: UUID): StudentResponseDto {
        log.debug("Getting student by id: {}", id)

        return studentsRepository
            .findById(id)
            .map {
                log.debug("Student found: {} with id: {}", it.name, it.id)
                it.toResponseDto()
            }
            .orElseThrow {
                log.error("Student not found with id: {}", id)
                throw StudentNotFoundException(id)
            }
    }

    /**
     * Get students by ids.
     * @param ids the ids of the students to get
     * @return a list of students
     */
    fun getStudentsByIds(ids: List<UUID>): List<StudentResponseDto> {
        log.debug("Getting students by ids: {}", ids)

        val students = studentsRepository.findAllById(ids)
        val foundIds = students.map { it.id!! }
        val missingIds = ids - foundIds.toSet()

        if (missingIds.isNotEmpty()) {
            log.error("Students not found with ids: {}", missingIds)
            throw StudentNotFoundException(missingIds.first())
        }

        log.info("Found {} students with given filters", students.size)
        return students.map { it.toResponseDto() }
    }

    /**
     * Get active students.
     * @return a list of active students
     */
    fun getActiveStudents(): List<StudentResponseDto> {
        log.debug("Getting all active students")
        val students = studentsRepository
            .findAllByActiveTrue()
            .map { it.toResponseDto() }

        log.info("Found {} active students", students.size)
        return students
    }

    /**
     * Get unactive students.
     * @return a list of unactive students
     */
    fun getUnactiveStudents(): List<StudentResponseDto> {
        log.debug("Getting all unactive students")
        val students = studentsRepository
            .findAllByActiveFalse()
            .map { it.toResponseDto() }

        log.info("Found {} unactive students", students.size)
        return students
    }

    /**
     * Add a new student.
     * @param createRequest the request containing the student details
     * @return the created student
     */
    fun addStudent(createRequest: CreateStudentRequest): StudentResponseDto {
        log.info("Creating new student with name: {}", createRequest.name)

        val student = createRequest.toEntity()
        val savedStudent = studentsRepository.save(student)

        log.info("Successfully created student with id: {} and name: {}", savedStudent.id, savedStudent.name)
        return savedStudent.toResponseDto()
    }

    /**
     * Add new students.
     * @param createRequests list of the requests containing the student details
     * @return list of the created students
     */
    fun addStudents(createRequests: List<CreateStudentRequest>): List<StudentResponseDto> {
        log.info("Creating new students with names: {}", createRequests.map { it.name })

        val students = createRequests.map { it.toEntity() }
        val savedStudents = studentsRepository.saveAll(students)

        log.info("Successfully created {} students with ids {}", savedStudents.size, savedStudents.map { it.id })
        return savedStudents.map { it.toResponseDto() }
    }

    /**
     * Update an existing student.
     * @param id the id of the student to update
     * @param updateRequest the request containing the updated student details
     * @return the updated student
     */
    fun updateStudent(id: UUID, updateRequest: UpdateStudentRequest): StudentResponseDto {
        log.info("Updating student with id: {}", id)
        val existingStudent = studentsRepository
            .findById(id)
            .orElseThrow {
                log.error("Cannot update student - student not found with id: {}", id)
                throw StudentNotFoundException(id)
            }

        log.debug("Updating student fields - name: {}, hardSkill: {}, background: {}, interests: {}, timeInWeek: {}"
            , updateRequest.name, updateRequest.hardSkill, updateRequest.background
            , updateRequest.interests, updateRequest.timeInWeek)

        existingStudent.name = updateRequest.name
        existingStudent.hardSkill = updateRequest.hardSkill
        existingStudent.background = updateRequest.background
        existingStudent.interests = updateRequest.interests
        existingStudent.timeInWeek = updateRequest.timeInWeek
        existingStudent.updatedAt = Instant.now()

        val updatedStudent = studentsRepository.save(existingStudent)
        log.info("Successfully updated student with id: {}", id)
        return updatedStudent.toResponseDto()
    }

    /**
     * Delete a student by id.
     * @param id the id of the student to delete
     */
    fun deleteStudent(id: UUID) {
        log.warn("Deleting student with id: {}", id)

        if (!studentsRepository.existsById(id)) {
            log.error("Cannot delete student - student not found with id: {}", id)
            throw StudentNotFoundException(id)
        }

        studentsRepository.deleteById(id)
        log.info("Successfully deleted student with id: {}", id)
    }

    /**
     * Delete students by ids.
     * @param ids the ids of the students to delete
     */
    fun deleteStudents(ids: List<UUID>) {
        log.warn("Deleting {} students with ids: {}", ids.size, ids)
        studentsRepository.deleteAllById(ids)
        log.info("Successfully deleted {} students", ids.size)
    }

    /**
     * Delete all students.
     */
    fun deleteAllStudents() {
        log.warn("Deleting ALL students")
        studentsRepository.deleteAll()
        log.info("Successfully deleted all students")
    }

    /**
     * Delete unactive students.
     */
    fun deleteUnactiveStudents() {
        log.debug("Deleting unactive students")
        studentsRepository.deleteAllByActiveFalse()
        log.info("Successfully deleted unactive students")
    }
}