package com.example.elonmars.presentation.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.R

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var taskTitle = view.findViewById<TextView>(R.id.task)
    var taskCheckBox = view.findViewById<CheckBox>(R.id.task_checkbox)
}