package com.example.elonmars.presentation

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.presentation.adapter.TaskAdapter

class MyItemTouchHelper(private val taskAdapter: TaskAdapter) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
), ItemTouchHelperAdapter {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onItemDismiss(viewHolder.adapterPosition)
    }


    // Методы из [ItemTouchHelperAdapter]

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
//        TODO("Not yet implemented")
    }

    override fun onItemDismiss(position: Int) {
        taskAdapter.dataSet.removeAt(position)
        taskAdapter.notifyDataSetChanged()
    }
}