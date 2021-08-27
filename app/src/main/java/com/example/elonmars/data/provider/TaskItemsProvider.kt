package com.example.elonmars.data.provider

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.elonmars.data.database.TasksDbHelper
import com.example.elonmars.data.database.TasksDbSchema
import com.example.elonmars.presentation.model.TaskItem
import java.util.*

/** Провайдер данных из базы данных [TasksDbHelper] */
class TaskItemsProvider(private val tasksDbHelper: TasksDbHelper) : ITaskItemsProvider {

    override fun saveTask(taskItem: TaskItem) {
        val db: SQLiteDatabase = tasksDbHelper.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(TasksDbSchema.TasksTable.Cols.TITLE, taskItem.title)
        contentValues.put(TasksDbSchema.TasksTable.Cols.STATUS, 0) // по умолчанию все задачи сохраняются со статусом isCompleted = false

        contentValues.put(TasksDbSchema.TasksTable.Cols.DAY_OF_MONTH, taskItem.dayOfMonth)
        contentValues.put(TasksDbSchema.TasksTable.Cols.MONTH, taskItem.month)
        contentValues.put(BaseColumns._COUNT, 1)

        db.insert(TasksDbSchema.TasksTable.NAME, null, contentValues)
    }

    override fun getTasksByDate(date: Calendar): ArrayList<TaskItem> {
        val db: SQLiteDatabase = tasksDbHelper.readableDatabase

        val projection = arrayOf(
            TasksDbSchema.TasksTable.Cols.TITLE,
            TasksDbSchema.TasksTable.Cols.STATUS
        )

        // условия запроса
        val selection = TasksDbSchema.TasksTable.Cols.DAY_OF_MONTH + " = ? AND " + TasksDbSchema.TasksTable.Cols.MONTH + " = ? "
        val selectionArgs = arrayOf(date.get(Calendar.DAY_OF_MONTH).toString(), date.get(Calendar.MONTH).toString())

        val cursor = db.query(
            TasksDbSchema.TasksTable.NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        return readFromCursor(cursor, date)
    }

    override fun updateTaskStatus(taskItem: TaskItem) {
        // передается значение, которое сейчас сохранено в TaskItem
        val taskStatus = if (taskItem.isCompleted) {
            0
        } else {
            1
        }

        val db: SQLiteDatabase = tasksDbHelper.writableDatabase

        val selection =
            TasksDbSchema.TasksTable.Cols.DAY_OF_MONTH + " = ? AND " + TasksDbSchema.TasksTable.Cols.MONTH + " = ? AND " + TasksDbSchema.TasksTable.Cols.TITLE + " = ? "
        val selectionArgs = arrayOf(
            taskItem.dayOfMonth.toString(),
            taskItem.month.toString(),
            taskItem.title.toString()
        )

        val contentValues = ContentValues()
        contentValues.put(TasksDbSchema.TasksTable.Cols.STATUS, taskStatus)

        db.update(
            TasksDbSchema.TasksTable.NAME,
            contentValues,
            selection,
            selectionArgs
        )
    }

    override fun deleteTask(taskItem: TaskItem) {
        val db: SQLiteDatabase = tasksDbHelper.writableDatabase
        val selection =
            TasksDbSchema.TasksTable.Cols.DAY_OF_MONTH + " = ? AND " + TasksDbSchema.TasksTable.Cols.MONTH + " = ? AND " + TasksDbSchema.TasksTable.Cols.TITLE + " = ? "
        val selectionArgs = arrayOf(
            taskItem.dayOfMonth.toString(),
            taskItem.month.toString(),
            taskItem.title.toString()
        )

        db.delete(
            TasksDbSchema.TasksTable.NAME,
            selection,
            selectionArgs
        )
    }

    private fun readFromCursor(cursor: Cursor, date: Calendar): ArrayList<TaskItem> {
        val tasksList = arrayListOf<TaskItem>()
        try {
            while (cursor.moveToNext()) {
                val title = cursor.getString(cursor.getColumnIndex(TasksDbSchema.TasksTable.Cols.TITLE))
                val status = cursor.getInt(cursor.getColumnIndex(TasksDbSchema.TasksTable.Cols.STATUS))
                var statusForTask = false

                if (status == 1) {
                    statusForTask = true
                }
                tasksList.add(TaskItem(title, statusForTask, date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH)))
            }
        } finally {
            cursor.close()
        }
        return tasksList
    }
}