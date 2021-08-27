package com.example.elonmars.data.database

import androidx.room.*
import com.example.elonmars.data.model.TaskEntity

@Dao
interface TaskDao {

    @Insert
    fun saveTask(taskEntity: TaskEntity)

    @Query("SELECT * FROM ${TasksDbSchema.TasksTable.NAME} WHERE ${TasksDbSchema.TasksTable.Cols.DAY_OF_MONTH} = :day AND ${TasksDbSchema.TasksTable.Cols.MONTH} = :month")
    fun getTasksByDate(day: String, month: String): List<TaskEntity>

    @Update
    fun updateTaskStatus(taskItem: TaskEntity)

    @Delete
    fun deleteTask(taskEntity: TaskEntity)
}