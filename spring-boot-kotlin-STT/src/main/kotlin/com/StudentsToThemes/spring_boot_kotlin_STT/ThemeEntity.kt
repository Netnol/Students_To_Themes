package com.StudentsToThemes.spring_boot_kotlin_STT

import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.MapKeyColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderBy
import jakarta.persistence.OrderColumn
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
class ThemeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(name = "name")
    var name: String = "",

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String = "",

    @Column(name = "author")
    var author: String = "",

    // List of specializations
    @ElementCollection
    @CollectionTable(
        name = "theme_specializations",
        joinColumns = [JoinColumn(name = "theme_id")]
    )
    @Column(name = "specialization_name")
    var specializations: MutableList<String> = mutableListOf(),

    // Main List of students with priorities
    @ManyToMany
    @JoinTable(
        name = "theme_student_priority",
        joinColumns = [JoinColumn(name = "theme_id")],
        inverseJoinColumns = [JoinColumn(name = "student_id")]
    )
    @OrderColumn(name = "priority_order")
    var priorityStudents: MutableList<StudentEntity> = mutableListOf(),

    // Bond with student specialisations (cascade deletion)
    @OneToMany(mappedBy = "theme", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("priorityOrder ASC")
    var specializationStudents: MutableList<ThemeSpecializationStudent> = mutableListOf(),

    @CreationTimestamp
    var createdAt: Instant = Instant.now(),

    @UpdateTimestamp
    var updatedAt: Instant = Instant.now()
) {
    // Methods for managing specializations
    /**
     * Add a specialization to the theme.
     * @param specialization the name of the specialization to add
     */
    fun addSpecialization(specialization: String) {
        validateSpecializationName(specialization)

        if (specializations.any { it.equals(specialization, ignoreCase = true) }) {
            throw IllegalArgumentException("Specialization '$specialization' already exists in theme")
        }
        specializations.add(specialization)
    }

    /**
     * Remove a specialization from the theme.
     * @param specialization the name of the specialization to remove
     */
    fun removeSpecialization(specialization: String) {
        specializations.remove(specialization)
        // Deleting all students from these specialization
        specializationStudents.removeIf { it.specializationName == specialization }
    }

    /**
     * Update the list of specializations for the theme.
     * @param newSpecializations the new list of specializations
     * @throws IllegalArgumentException if there are duplicate specializations or if any specialization name is invalid
     */
    fun updateSpecializations(newSpecializations: List<String>) {
        // Check if there are duplicate specializations
        val duplicates = newSpecializations
            .groupBy { it.lowercase() }
            .filter { it.value.size > 1 }
            .keys

        if (duplicates.isNotEmpty()) {
            throw IllegalArgumentException("Duplicate specializations found: ${duplicates.joinToString()}")
        }

        // Validate every specialisation
        newSpecializations.forEach { validateSpecializationName(it) }

        // Deleting specialisations that weren't in the new list
        val toRemove = specializations - newSpecializations.toSet()
        specializations.removeAll(toRemove)

        // Deleting students from removed specializations
        specializationStudents.removeIf { it.specializationName in toRemove }

        // Adding new specializations
        newSpecializations.forEach { spec ->
            if (!specializations.any { it.equals(spec, ignoreCase = true) }) {
                specializations.add(spec)
            }
        }
    }

    /**
     * Validate the name of a specialization.
     * @param specialization the name of the specialization to validate
     * @throws IllegalArgumentException if the specialization name is invalid
     */
    private fun validateSpecializationName(specialization: String) {
        when {
            specialization.isBlank() ->
                throw IllegalArgumentException("Specialization name cannot be blank")

            specialization.length > 100 ->
                throw IllegalArgumentException("Specialization name cannot exceed 100 characters")

            !specialization.matches("^[a-zA-Z0-9\\s\\-]+$".toRegex()) ->
                throw IllegalArgumentException("Specialization name can only contain letters, numbers, spaces and hyphens")
        }
    }

    /**
     * Check if the theme has a specialization.
     * @param specialization the name of the specialization to check
     * @return true if the theme has the specialization, false otherwise
     */
    fun hasSpecialization(specialization: String): Boolean {
        return specializations.any { it.equals(specialization, ignoreCase = true) }
    }

    /**
     * Get the exact name of a specialization.
     * @param specialization the name of the specialization to get
     * @return the exact name of the specialization, or null if not found
     */
    fun getExactSpecializationName(specialization: String): String? {
        return specializations.find { it.equals(specialization, ignoreCase = true) }
    }
}