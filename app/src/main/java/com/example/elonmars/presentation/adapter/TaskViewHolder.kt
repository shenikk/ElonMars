package com.example.elonmars.presentation.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.R

/**
 * ViewHolder для [com.example.elonmars.presentation.adapter.TaskAdapter]
 */
class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var taskTitle: TextView = view.findViewById(R.id.task)
    var taskCheckBox: CheckBox = view.findViewById(R.id.task_checkbox)
}