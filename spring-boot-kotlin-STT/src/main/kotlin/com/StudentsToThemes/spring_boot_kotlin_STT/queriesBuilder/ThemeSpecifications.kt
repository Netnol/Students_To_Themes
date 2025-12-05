package com.StudentsToThemes.spring_boot_kotlin_STT.queriesBuilder

import com.StudentsToThemes.spring_boot_kotlin_STT.entity.ThemeEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

class ThemeSpecifications {
    companion object {

        /**
         * Create a specification for searching themes based on name, description, and author.
         * @param name the name to search for
         * @param description the description to search for
         * @param author the author to search for
         * @return the specification for searching themes
         */
        fun createSearchSpecification(
            name: String?,
            description: String?,
            author: String?
        ): Specification<ThemeEntity> {
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

                description?.takeIf { it.isNotBlank() }?.let {
                    predicates.add(
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root["description"]),
                            "%${it.lowercase()}%"
                        )
                    )
                }

                author?.takeIf { it.isNotBlank() }?.let {
                    predicates.add(
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root["author"]),
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