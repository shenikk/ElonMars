package com.example.elonmars.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.elonmars.data.model.TaskEntity

@Database(entities = [TaskEntity::class], version = 3, exportSchema = false)
abstract class TaskDataBase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        fun createDataBase(context: Context): TaskDataBase {
            return Room.databaseBuilder(
                context,
                TaskDataBase::class.java,
                TasksDbSchema.TasksTable.NAME
            )
                .fallbackToDestructiveMigrationFrom(1, 2)
                .build()
        }
    }
}