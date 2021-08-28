package com.example.elonmars.presentation.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.elonmars.R
import com.example.elonmars.data.database.TasksDbHelper
import com.example.elonmars.data.provider.SchedulersProvider
import com.example.elonmars.data.provider.TaskItemsProvider
import com.example.elonmars.data.repository.TasksRepository
import com.example.elonmars.domain.interactors.TaskInteractor
import com.example.elonmars.presentation.adapter.TaskAdapter
import com.example.elonmars.presentation.model.TaskItem
import com.example.elonmars.presentation.viewmodel.MarsMissionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.util.*

/** Экран с задачами, добавляемые пользователем */
class MarsMissionFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var floatingButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var noTaskText: TextView
    private lateinit var chosenTaskDate: Calendar

    private var dataSet: ArrayList<TaskItem> = arrayListOf()
    private var viewModel: MarsMissionViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mars_mission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createViewModel()
        observeLiveData()
        setChosenDate()
        viewModel?.getTaskItemFromDataBase(chosenTaskDate)

        calendarView = view.findViewById<CalendarView>(R.id.calendar).apply {
            this.setOnDateChangeListener { _, _, month, dayOfMonth ->
                setChosenDate(dayOfMonth, month)
                showTasksForChosenDate()
            }
        }

        floatingButton = view.findViewById<FloatingActionButton>(R.id.floating_button).apply {
            this.setOnClickListener {
                setUpDialog(this)
            }
        }

        noTaskText = view.findViewById(R.id.no_task_text)

        recyclerView = view.findViewById(R.id.task_recycler)
        setUpAdapter(dataSet)
    }

    private fun createViewModel() {
        val taskDbHelper = TasksDbHelper(this.context)
        val taskItemsProvider = TaskItemsProvider(taskDbHelper)
        val taskRepository = TasksRepository(taskItemsProvider)

        val tasksInteractor = TaskInteractor(taskRepository)
        val schedulersProvider = SchedulersProvider()

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MarsMissionViewModel(tasksInteractor, schedulersProvider) as T
            }
        }).get(MarsMissionViewModel::class.java)
    }

    private fun observeLiveData() {
        viewModel?.let {
            it.getTaskItemLiveData().observe(viewLifecycleOwner, { list ->
                if (list != null) {
                    showData(list)
                }
            })
        }
    }

    private fun showData(list: ArrayList<TaskItem>) {
        setUpAdapter(list)
        updateText(noTaskText, list)
    }

    private fun updateText(noTaskText: TextView, dataSet: ArrayList<TaskItem>) {
        if (dataSet.isEmpty()) {
            noTaskText.visibility = View.VISIBLE
        } else {
            noTaskText.visibility = View.GONE
        }
    }

    private fun setUpAdapter(dataSet: ArrayList<TaskItem>) {
        taskAdapter = TaskAdapter(dataSet) { holder, taskItem ->
            holder.taskCheckBox.setOnClickListener {
                viewModel?.updateTaskStatus(taskItem)
            }
            holder.itemView.setOnLongClickListener {
                setUpAlertDialog(taskItem)
                true
            }
        }
        recyclerView.adapter = taskAdapter
    }

    private fun setUpAlertDialog(taskItem: TaskItem) {
        AlertDialog.Builder(this.context)
            .setTitle(getString(R.string.alert_dialog_title))
            .setMessage(getString(R.string.alert_dialog_summary))
            .setPositiveButton(android.R.string.ok
            ) { _, _ ->
                viewModel?.deleteTaskItemFromDataBase(taskItem)
                // FIXME подумать, как можно улучшить этот момент (убрать мерцание текста при удалении айтема)
                viewModel?.getTaskItemFromDataBase(chosenTaskDate)
                taskAdapter.dataSet.remove(taskItem)
                taskAdapter.notifyDataSetChanged()
                updateText(noTaskText, dataSet)
            }
            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(android.R.string.cancel, null)
            .show()
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
                    val task = TaskItem(editText?.text.toString(),false, chosenTaskDate.get(Calendar.DAY_OF_MONTH),
                        chosenTaskDate.get(Calendar.MONTH))
                    viewModel?.addTaskItemToDataBase(task)

                    taskAdapter.dataSet.add(task)
                    taskAdapter.notifyItemInserted(taskAdapter.itemCount)
                    showTasksForChosenDate()
                    bottomSheetDialog.dismiss()
                } else {
                    bottomSheetDialog.dismiss()
                }
            }
        }
    }

    private fun showTasksForChosenDate() {
        viewModel?.getTaskItemFromDataBase(chosenTaskDate)
    }

    private fun setChosenDate(dayOfMonth: Int? = null, month: Int? = null) {
        chosenTaskDate = Calendar.getInstance()
        if (dayOfMonth != null && month != null) {
            chosenTaskDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            chosenTaskDate.set(Calendar.MONTH, month)
        }
    }
}