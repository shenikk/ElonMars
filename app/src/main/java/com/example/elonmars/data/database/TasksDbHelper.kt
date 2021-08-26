package com.example.elonmars.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import androidx.annotation.Nullable

/** База данных для хранения [TaskItem] */
class TasksDbHelper(@Nullable private val context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Tasks.db"
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(
            "CREATE TABLE " + TasksDbSchema.TasksTable.NAME + " (" +
                    BaseColumns._ID + " integer PRIMARY KEY autoincrement," +
                    TasksDbSchema.TasksTable.Cols.DAY_OF_MONTH + " text," +
                    TasksDbSchema.TasksTable.Cols.MONTH + " text," +
                    TasksDbSchema.TasksTable.Cols.TITLE + " text," +
                    TasksDbSchema.TasksTable.Cols.STATUS + " integer" + ")"
        )
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}