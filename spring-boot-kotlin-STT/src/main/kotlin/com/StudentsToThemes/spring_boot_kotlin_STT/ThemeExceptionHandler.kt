package com.StudentsToThemes.spring_boot_kotlin_STT

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ThemeExceptionHandler {
    /**
     * Handle theme not found exception.
     * @param ex the ThemeNotFoundException to handle
     * @return a map containing the error code and message
     */
    private val log = LoggerFactory.getLogger(ThemeExceptionHandler::class.java)

    @ExceptionHandler(ThemeNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun onThemeNotFoundException(ex: ThemeNotFoundException): Map<String, String?> {
        log.warn("Theme not found: {}", ex.message)
        return mapOf(
            "errorCode" to "THEME_NOT_FOUND",
            "message" to ex.message
        )
    }
}