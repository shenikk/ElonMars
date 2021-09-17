package com.example.elonmars.presentation.mission.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.R
import com.example.elonmars.presentation.mission.model.TaskItem

/**
 * Адаптер для отображения списка задач.
 *
 * @param dataSet список элементов [TaskItem].
 * @param onItemBind лямбда с полезной нагрузкой, которая выполняется при биндинга холдера.
 */
class TaskAdapter(
    var dataSet: ArrayList<TaskItem>,
    private val onItemBind: (TaskViewHolder, TaskItem) -> Unit
) : RecyclerView.Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = dataSet[position]

        holder.configureHolder(currentItem)
        onItemBind(holder, currentItem)
    }

    override fun getItemCount(): Int = dataSet.size
}
