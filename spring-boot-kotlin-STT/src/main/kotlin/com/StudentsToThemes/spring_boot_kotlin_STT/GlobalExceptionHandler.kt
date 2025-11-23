package com.StudentsToThemes.spring_boot_kotlin_STT

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    /**
     * Handle validation exceptions.
     * @param ex the exception to handle
     * @return a response entity with the error details
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleValidationException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        log.warn("Validation error: {}", ex.message)

        val errorResponse = ErrorResponse(
            message = ex.message ?: "Validation failed",
            error = "VALIDATION_ERROR",
            status = HttpStatus.BAD_REQUEST.value(),
            timestamp = Instant.now()
        )

        return ResponseEntity.badRequest().body(errorResponse)
    }

    /**
     * Handle theme not found exceptions.
     * @param ex the exception to handle
     * @return a response entity with the error details
     */
    @ExceptionHandler(ThemeNotFoundException::class)
    fun handleThemeNotFoundException(ex: ThemeNotFoundException): ResponseEntity<ErrorResponse> {
        log.warn("Theme not found: {}", ex.message)

        val errorResponse = ErrorResponse(
            message = ex.message,
            error = "THEME_NOT_FOUND",
            status = HttpStatus.NOT_FOUND.value(),
            timestamp = Instant.now()
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    /**
     * Handle student not found exceptions.
     * @param ex the exception to handle
     * @return a response entity with the error details
     */
    @ExceptionHandler(StudentNotFoundException::class)
    fun handleStudentNotFoundException(ex: StudentNotFoundException): ResponseEntity<ErrorResponse> {
        log.warn("Student not found: {}", ex.message)

        val errorResponse = ErrorResponse(
            message = ex.message,
            error = "STUDENT_NOT_FOUND",
            status = HttpStatus.NOT_FOUND.value(),
            timestamp = Instant.now()
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    /**
     * Handle generic exceptions.
     * @param ex the exception to handle
     * @return a response entity with the error details
     */
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        log.error("Internal server error", ex)

        val errorResponse = ErrorResponse(
            message = "Internal server error",
            error = "INTERNAL_ERROR",
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            timestamp = Instant.now()
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}

data class ErrorResponse(
    val message: String?,
    val error: String,
    val status: Int,
    val timestamp: Instant
)