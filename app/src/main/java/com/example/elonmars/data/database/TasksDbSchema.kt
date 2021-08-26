package com.example.elonmars.data.database

import android.provider.BaseColumns

/** Схема для базы данных */
class TasksDbSchema {
    object TasksTable {
        const val NAME = "tasks"

        object Cols : BaseColumns {
            const val TITLE = "title"
            const val DAY_OF_MONTH = "day_of_month"
            const val MONTH = "month"
            const val STATUS = "status"
        }
    }
}