package com.StudentsToThemes.spring_boot_kotlin_STT

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID

@Entity
@Table(
    name = "students"
)
class StudentEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    @Column(name = "name")
    var name: String = "",
    @Column(name = "hard_skill")
    var hardSkill: String = "",
    @Column(name = "background")
    var background: String = "",
    @Column(name = "interests")
    var interests: String = "",
    @Column(name = "time_in_week")
    var timeInWeek: String? = null,
    @Column(name = "active")
    var active: Boolean = true,
    @CreationTimestamp
    var createdAt: Instant = Instant.now(),
    @UpdateTimestamp
    var updatedAt: Instant = Instant.now()
){
    // Feedback for student theme search
    @ManyToMany(mappedBy = "priorityStudents")
    val themes: MutableList<ThemeEntity> = mutableListOf()
}