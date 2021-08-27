package com.example.elonmars.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.elonmars.data.database.TasksDbSchema

@Entity(
    tableName = TasksDbSchema.TasksTable.NAME
)
class TaskEntity(
    @PrimaryKey(autoGenerate = true) val _id : Int, // last so that we don't have to pass an ID value or named arguments
    @ColumnInfo(name = TasksDbSchema.TasksTable.Cols.DAY_OF_MONTH) var dayOfMonth: Int,
    @ColumnInfo(name = TasksDbSchema.TasksTable.Cols.MONTH) val month: Int,
    @ColumnInfo(name = TasksDbSchema.TasksTable.Cols.TITLE) val title: String?,
    @ColumnInfo(name = TasksDbSchema.TasksTable.Cols.STATUS) val isCompleted: Boolean
)