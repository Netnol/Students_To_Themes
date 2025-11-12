package com.StudentsToThemes.spring_boot_kotlin_STT

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID
import kotlin.collections.mutableListOf

@Entity
@Table(
    name = "themes"
)
class ThemeEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    @Column(name = "name")
    var name: String = "",
    @Column(name = "description", columnDefinition = "TEXT")
    var description: String = "",
    @Column(name = "author")
    var author: String = "",
    // many-to-many relationship with StudentEntity
    @ManyToMany
    @JoinTable(
        name = "theme_student_priority",
        joinColumns = [JoinColumn(name = "theme_id")],
        inverseJoinColumns = [JoinColumn(name = "student_id")]
    )
    var priorityStudents: MutableList<StudentEntity> = mutableListOf(),
    @CreationTimestamp
    var createdAt: Instant = Instant.now(),
    @UpdateTimestamp
    var updatedAt: Instant = Instant.now()
){
}