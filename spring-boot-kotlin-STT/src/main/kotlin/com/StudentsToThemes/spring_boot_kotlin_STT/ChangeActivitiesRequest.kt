package com.StudentsToThemes.spring_boot_kotlin_STT

import java.util.UUID

data class ChangeActivitiesRequest(
    val ids: List<UUID>,
    val active: Boolean
)
