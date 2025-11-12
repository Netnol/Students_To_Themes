package com.StudentsToThemes.spring_boot_kotlin_STT

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class StudentsExceptionHandler {
    /**
     * Handle student not found exception.
     * @param ex the StudentNotFoundException to handle
     * @return a map containing the error code and message
     */
    @ExceptionHandler(StudentNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun onStudentNotFoundException(ex: StudentNotFoundException) = mapOf(
        "errorCode" to "STUDENT_NOT_FOUND",
        "message" to ex.message
    )
}