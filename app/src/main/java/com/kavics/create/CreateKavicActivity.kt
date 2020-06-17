package com.kavics.create

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kavics.R
import com.kavics.adapter.DateHelper
import com.kavics.database.KavicDatabase
import com.kavics.model.OneTimeKavicItem
import com.kavics.model.RepeatingKavicItem
import com.kavics.viewmodel.KavicViewModel
import kotlinx.android.synthetic.main.activity_create_kavic.*
import kotlinx.coroutines.*
import java.util.*

class CreateKavicActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    CoroutineScope by MainScope() {

    private lateinit var deadlineDate: String
    private lateinit var startDate: String
    private lateinit var database: KavicDatabase
    private var deadlineDatePicking: Boolean = false
    private var startDatePicking: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_kavic)

        val kavicViewModel = KavicViewModel()
        database = KavicDatabase.getDatabase(applicationContext)


        btnDeadline.setOnClickListener {
            deadlineDatePicking = true
            showDatePickerDialog()
        }

        btnStartDate.setOnClickListener {
            startDatePicking = true
            showDatePickerDialog()
        }

        btnCreateKavic.setOnClickListener {

            if (editTextKavicTitle.text.toString() != "" && this::deadlineDate.isInitialized) {
                if (checkBoxDaily.isChecked) {

                    addRepeatingKavicItem(
                        RepeatingKavicItem(
                            title = editTextKavicTitle.text.toString(),
                            repeatDays = editTextNumberOfRepeatingDays.text.toString().toInt(),
                            lastDate = deadlineDate,
                            startDate = startDate,
                            howManyDays = editTextHowManyDays.text.toString().toInt()
                        )
                    )

                    finish()
                } else {
                    kavicViewModel.insertOneTimeKavic(
                        OneTimeKavicItem(
                            title = editTextKavicTitle.text.toString(),
                            deadline = deadlineDate
                        )
                    )

                    finish()
                }

            } else {
                Toast.makeText(this, "you most fill the title", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelCreateKavic.setOnClickListener {
            finish()
        }

        checkBoxDaily.setOnClickListener {
            editTextNumberOfRepeatingDays.isClickable = checkBoxDaily.isChecked
        }
    }

    private fun addRepeatingKavicItem(repeatingKavicItem: RepeatingKavicItem) = launch {
        withContext(Dispatchers.IO) {
            database.repeatingKavicItemDao().insert(repeatingKavicItem)
        }
    }

    private fun showDatePickerDialog() {

        val datePickerDialog = DatePickerDialog(
            this,
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val deadlineHelper = DateHelper()

        var monthAsString = month.toString()
        if (month.toString().length == 1) {
            monthAsString = "0${month + 1}"
        }

        var dayAsString = dayOfMonth.toString()
        if (dayOfMonth.toString().length == 1) {
            dayAsString = "0$dayOfMonth"
        }

        if (deadlineHelper.getToday() <= "$year $monthAsString $dayAsString") {

            textViewSelectedDate.text =
                getString(R.string.date_yyyy_MM_dd, year.toString(), monthAsString, dayAsString)

            if (deadlineDatePicking) {
                deadlineDate = "$year $monthAsString $dayAsString"
                deadlineDatePicking = false
            }
            if (startDatePicking) {
                startDate = "$year $monthAsString $dayAsString"
                startDatePicking = false
            }

        } else {
            Toast.makeText(this, "You can't choose past date.", Toast.LENGTH_LONG).show()
        }

    }

}
