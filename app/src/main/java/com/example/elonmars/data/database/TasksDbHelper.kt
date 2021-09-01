package com.example.elonmars.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import androidx.annotation.Nullable

/** База данных для хранения [TaskItem] */
class TasksDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "Tasks.db"
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(
            "CREATE TABLE " + TasksDbSchema.TasksTable.NAME + " (" +
                    BaseColumns._ID + " integer PRIMARY KEY autoincrement," +
                    TasksDbSchema.TasksTable.Cols.DAY_OF_MONTH + " text," +
                    TasksDbSchema.TasksTable.Cols.MONTH + " text," +
                    TasksDbSchema.TasksTable.Cols.TITLE + " text," +
                    TasksDbSchema.TasksTable.Cols.STATUS + " integer," +
                    BaseColumns._COUNT + " integer" + ")"
        )
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            sqLiteDatabase.execSQL(
                "ALTER TABLE " + TasksDbSchema.TasksTable.NAME + " ADD " +
                        BaseColumns._COUNT + " integer"
            )
            val contentValues = ContentValues()
            contentValues.put(BaseColumns._COUNT, 1)
            sqLiteDatabase.update(TasksDbSchema.TasksTable.NAME, contentValues, null, null)
        }
    }
}
