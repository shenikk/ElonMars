package com.example.elonmars.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.R
import com.example.elonmars.presentation.MyItemTouchHelper
import com.example.elonmars.presentation.adapter.TaskAdapter
import com.example.elonmars.presentation.model.TaskItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

/** Экран с задачами, добавляемые пользователем */
class MarsMissionFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var floatingButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private var dataSet: ArrayList<TaskItem> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mars_mission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView = view.findViewById<CalendarView>(R.id.calendar).apply {
            this.setOnDateChangeListener { _, _, _, _ ->
                Toast.makeText(context, "Add a task for this day!", Toast.LENGTH_SHORT).show()
            }
        }

        floatingButton = view.findViewById<FloatingActionButton>(R.id.floating_button).apply {
            this.setOnClickListener {
                setUpDialog(this)
            }
        }

        recyclerView = view.findViewById(R.id.task_recycler)

        taskAdapter = TaskAdapter(dataSet)
        recyclerView.adapter = taskAdapter
        addItemTouchHelper(recyclerView, taskAdapter)
    }

    private fun setUpDialog(view: View) {
        val bottomSheetDialog = BottomSheetDialog(view.context, R.style.BottomSheetDialogTheme)

        bottomSheetDialog.setContentView(LayoutInflater.from(context).inflate(R.layout.bottom_sheet, null))
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.dismissWithAnimation = true
        bottomSheetDialog.show()

        initDialogViews(bottomSheetDialog)
    }

    private fun initDialogViews(bottomSheetDialog: BottomSheetDialog) {
        val editText = bottomSheetDialog.findViewById<TextInputEditText>(R.id.edit_text)

        bottomSheetDialog.findViewById<Button>(R.id.save_button)?.apply {
            setOnClickListener {
                if (!editText?.text.isNullOrEmpty()) {
                    dataSet.add(TaskItem(editText?.text.toString(),false))
                    taskAdapter.notifyDataSetChanged()
                    bottomSheetDialog.dismiss()
                } else {
                    bottomSheetDialog.dismiss()
                }
            }
        }
    }

    private fun addItemTouchHelper(recyclerView: RecyclerView, taskAdapter: TaskAdapter) {
        val itemTouchHelperCallback = MyItemTouchHelper(taskAdapter)
        val touchHelper = ItemTouchHelper(itemTouchHelperCallback)
        touchHelper.attachToRecyclerView(recyclerView)
    }
}