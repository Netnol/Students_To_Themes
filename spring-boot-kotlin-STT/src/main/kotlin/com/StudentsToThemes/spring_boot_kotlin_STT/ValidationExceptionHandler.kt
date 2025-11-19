package com.StudentsToThemes.spring_boot_kotlin_STT

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ValidationExceptionHandler {
    private val log = LoggerFactory.getLogger(ValidationExceptionHandler::class.java)

    /**
     * Handle validation failed exception.
     * @param e the MethodArgumentNotValidException to handle
     * @return a map containing the error code and message
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun onValidationFailed(e: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        log.warn("Validation failed: {}", e.bindingResult.fieldErrors)

        val map = mutableMapOf<String, Any>()
        e.bindingResult.fieldErrors.forEach { error ->
            map[error.field] = error.defaultMessage ?: "Validation failed"
        }

        return ResponseEntity
            .badRequest()
            .body(map)
    }
}