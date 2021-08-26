package com.example.elonmars.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.R
import com.example.elonmars.data.model.PhotoItem
import com.example.elonmars.presentation.model.TaskItem

/**
 * Адаптер для отображения списка задач.
 *
 * @param dataSet список элементов [TaskItem]
 */
class TaskAdapter(var dataSet: ArrayList<TaskItem>, private val onItemClicked: (TaskViewHolder, TaskItem) -> Unit) : RecyclerView.Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = dataSet[position]

        holder.taskTitle.text = currentItem.title
        holder.taskCheckBox.isChecked = currentItem.isCompleted
        onItemClicked(holder, currentItem)
    }

    override fun getItemCount(): Int = dataSet.size
}