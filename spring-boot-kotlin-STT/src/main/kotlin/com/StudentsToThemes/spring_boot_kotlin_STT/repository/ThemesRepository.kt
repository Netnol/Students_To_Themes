package com.StudentsToThemes.spring_boot_kotlin_STT.repository

import com.StudentsToThemes.spring_boot_kotlin_STT.ThemeEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ThemesRepository: JpaRepository<ThemeEntity, UUID> {

}