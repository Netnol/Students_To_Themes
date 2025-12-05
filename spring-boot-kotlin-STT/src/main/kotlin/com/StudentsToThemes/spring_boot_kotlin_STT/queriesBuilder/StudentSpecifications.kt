package com.StudentsToThemes.spring_boot_kotlin_STT.queriesBuilder

import com.StudentsToThemes.spring_boot_kotlin_STT.entity.StudentEntity
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.Predicate

class StudentSpecifications {
    companion object {
        /**
         * Create a dynamic search specification for students.
         * @param name the name to search for
         * @param hardSkill the hard skill to search for
         * @param background the background to search for
         * @param interests the interests to search for
         * @param timeInWeek the time in week to search for
         * @return a Specification object
         */
        fun createSearchSpecification(
            name: String?,
            hardSkill: String?,
            background: String?,
            interests: String?,
            timeInWeek: String?
        ): Specification<StudentEntity> {
            return Specification { root, query, criteriaBuilder ->
                val predicates = mutableListOf<Predicate>()

                // For each field, add a LIKE condition if the value is not null/empty
                name?.takeIf { it.isNotBlank() }?.let {
                    predicates.add(
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root["name"]),
                            "%${it.lowercase()}%"
                        )
                    )
                }

                hardSkill?.takeIf { it.isNotBlank() }?.let {
                    predicates.add(
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root["hardSkill"]),
                            "%${it.lowercase()}%"
                        )
                    )
                }

                background?.takeIf { it.isNotBlank() }?.let {
                    predicates.add(
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root["background"]),
                            "%${it.lowercase()}%"
                        )
                    )
                }

                interests?.takeIf { it.isNotBlank() }?.let {
                    predicates.add(
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root["interests"]),
                            "%${it.lowercase()}%"
                        )
                    )
                }

                timeInWeek?.takeIf { it.isNotBlank() }?.let {
                    predicates.add(
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root["timeInWeek"]),
                            "%${it.lowercase()}%"
                        )
                    )
                }

                // If there are conditions, combine them with OR, otherwise return all records
                if (predicates.isNotEmpty()) {
                    criteriaBuilder.or(*predicates.toTypedArray())
                } else {
                    criteriaBuilder.conjunction() // return all records
                }
            }
        }
    }
}