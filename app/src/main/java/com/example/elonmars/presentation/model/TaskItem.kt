package com.example.elonmars.presentation.model

/**
 * Модель для описания данных о задаче за конкретный день.
 * Применяется на уровне презентации.
 */
class TaskItem(
        var title: String?,
        var isCompleted: Boolean,
        var dayOfMonth: Int,
        var month: Int
)
