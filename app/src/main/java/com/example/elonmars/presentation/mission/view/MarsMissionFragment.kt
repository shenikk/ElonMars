package com.example.elonmars.presentation.mission.view

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.elonmars.MyApplication
import com.example.elonmars.R
import com.example.elonmars.databinding.FragmentMarsMissionBinding
import com.example.elonmars.di.activity.DaggerActivityComponent
import com.example.elonmars.presentation.extensions.logError
import com.example.elonmars.presentation.extensions.showSnackbar
import com.example.elonmars.presentation.mission.viewmodel.MarsMissionViewModel
import com.example.elonmars.presentation.mission.model.TaskItem
import com.example.elonmars.presentation.mission.utils.InputTextWatcher
import com.example.elonmars.presentation.mission.view.adapter.TaskAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.collections.ArrayList

/** Экран с задачами, добавляемые пользователем */
class MarsMissionFragment : Fragment() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var chosenTaskDate: Calendar

    private var dataSet: List<TaskItem> = arrayListOf()
    private var viewModel: MarsMissionViewModel? = null
    private var _binding: FragmentMarsMissionBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val REQUEST_CODE_CALENDAR_PERMISSION = 122
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsMissionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        provideDependencies(view.context)
        observeLiveData()
        setChosenDate()
        initUI()
        viewModel?.getTaskItemFromDataBase(chosenTaskDate)
        setUpAdapter(dataSet)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        binding.calendar.apply {
            this.setOnDateChangeListener { _, _, month, dayOfMonth ->
                setChosenDate(dayOfMonth, month)
                showTasksForChosenDate()
            }
        }

        binding.floatingButton.apply {
            this.setOnClickListener {
                setUpDialog(this.context)
            }
        }
    }

    private fun provideDependencies(context: Context) {
        val appComponent = MyApplication.getAppComponent(context)
        val activityComponent = DaggerActivityComponent.builder()
            .appComponent(appComponent)
            .build()

        viewModel = ViewModelProvider(this, activityComponent.getViewModelFactory()).get(
            MarsMissionViewModel::class.java
        )
    }

    private fun observeLiveData() {
        viewModel?.let {
            it.getTaskItemLiveData().observe(viewLifecycleOwner, { list ->
                if (list != null) {
                    showData(list)
                }
            })

            it.getErrorLiveData().observe(viewLifecycleOwner, { error ->
                showError(error)
            })
        }
    }

    private fun showData(list: List<TaskItem>) {
        setUpAdapter(list)
        updateText(binding.noTaskText, list)
    }

    private fun showError(throwable: Throwable) {
        logError("showError called with error = $throwable")
        showSnackbar(R.string.fragment_error_message)
    }

    private fun updateText(noTaskText: TextView, dataSet: List<TaskItem>) {
        if (dataSet.isEmpty()) {
            noTaskText.visibility = View.VISIBLE
        } else {
            noTaskText.visibility = View.GONE
        }
    }

    private fun setUpAdapter(dataSet: List<TaskItem>) {
        taskAdapter = TaskAdapter(ArrayList(dataSet)) { holder, taskItem ->
            holder.itemView.setOnLongClickListener {
                setUpAlertDialog(taskItem, it.context)
                true
            }
            holder.setOnCalendarImageClickListener {
                checkPermissionToInsertToCalendar(holder.itemView.context, taskItem.title)
            }
            holder.setOnTaskCheckBoxClickListener {
                viewModel?.updateTaskStatus(taskItem)
            }
        }
        binding.taskRecycler.adapter = taskAdapter
    }

    private fun checkPermissionToInsertToCalendar(context: Context, title: String?) {
        if (ContextCompat.checkSelfPermission(
                context.applicationContext,
                Manifest.permission.READ_CALENDAR
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR),
                REQUEST_CODE_CALENDAR_PERMISSION
            )
        } else {
            insertEventToCalnedar(title)
        }
    }

    private fun insertEventToCalnedar(title: String?) {
        val intent = Intent(Intent.ACTION_INSERT)
        intent.data = CalendarContract.Events.CONTENT_URI
        intent.putExtra(CalendarContract.Events.TITLE, title)
        intent.putExtra(CalendarContract.Events.DESCRIPTION, getString(R.string.task_description))
        intent.putExtra(CalendarContract.Events.ALL_DAY, true)
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, chosenTaskDate.timeInMillis)
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, chosenTaskDate.timeInMillis)
        startActivity(intent)
    }

    private fun setUpAlertDialog(taskItem: TaskItem, context: Context) {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.alert_dialog_title))
            .setMessage(getString(R.string.alert_dialog_summary))
            .setPositiveButton(android.R.string.ok) { _, _ ->
                viewModel?.deleteTaskItemFromDataBase(taskItem)
                viewModel?.getTaskItemFromDataBase(chosenTaskDate)
                taskAdapter.dataSet.remove(taskItem)
                taskAdapter.notifyDataSetChanged()
                updateText(binding.noTaskText, dataSet)
            }
            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun setUpDialog(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme).also {
            it.setContentView(
                LayoutInflater.from(context).inflate(R.layout.bottom_sheet, null)
            )
            it.setCancelable(true)
            it.dismissWithAnimation = true
            it.show()
        }

        setUpBottomSheetDialog(bottomSheetDialog)
    }

    private fun setUpBottomSheetDialog(bottomSheetDialog: BottomSheetDialog) {
        val editText = bottomSheetDialog.findViewById<TextInputEditText>(R.id.edit_text)
        val textInputLayout = bottomSheetDialog.findViewById<TextInputLayout>(R.id.input_layout)
        textInputLayout?.editText?.addTextChangedListener(InputTextWatcher {
            hideError(textInputLayout)
        })

        bottomSheetDialog.findViewById<Button>(R.id.save_button)?.apply {
            setOnClickListener {
                if (!editText?.text.isNullOrEmpty()) {
                    val task = TaskItem(
                        editText?.text.toString(), false,
                        chosenTaskDate.get(Calendar.DAY_OF_MONTH),
                        chosenTaskDate.get(Calendar.MONTH)
                    )
                    viewModel?.addTaskItemToDataBase(task)

                    taskAdapter.dataSet.add(task)
                    taskAdapter.notifyItemInserted(taskAdapter.itemCount)
                    showTasksForChosenDate()
                    bottomSheetDialog.dismiss()
                } else {
                    textInputLayout?.let { textInputLayout ->
                        validateInput(editText?.text.toString(), textInputLayout)
                    }
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

    private fun validateInput(input: String, inputView: TextInputLayout): Boolean {
        return if (input.isEmpty()) {
            showError(inputView)
            false
        } else {
            hideError(inputView)
            true
        }
    }

    private fun hideError(inputView: TextInputLayout) {
        inputView.error = null
    }

    private fun showError(inputView: TextInputLayout) {
        inputView.error = inputView.context.getString(R.string.error_message)
    }
}
