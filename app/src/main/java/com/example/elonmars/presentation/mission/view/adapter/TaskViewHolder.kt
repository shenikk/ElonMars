package com.example.elonmars.presentation.mission.view.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.R
import com.example.elonmars.presentation.mission.model.TaskItem

/**
 * ViewHolder для [com.example.elonmars.presentation.adapter.TaskAdapter]
 */
class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val taskTitle: TextView = view.findViewById(R.id.task)
    private val taskCheckBox: CheckBox = view.findViewById(R.id.task_checkbox)
    private val calendarImage: ImageView = view.findViewById(R.id.add_to_calendar)

    fun configureHolder(currentItem: TaskItem) {
        taskTitle.text = currentItem.title
        taskCheckBox.isChecked = currentItem.isCompleted
    }

    fun setOnCalendarImageClickListener(click: () -> Unit) {
        calendarImage.setOnClickListener {
            click()
        }
    }

    fun setOnTaskCheckBoxClickListener(click: () -> Unit) {
        taskCheckBox.setOnClickListener {
            click()
        }
    }
}
