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
import kotlinx.android.synthetic.main.activity_kavic_list.*
import kotlinx.android.synthetic.main.fragment_create_one_time_kavic.*
import kotlinx.android.synthetic.main.fragment_create_repeating_kavic.*
import kotlinx.coroutines.*
import java.util.*

class CreateKavicActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    CoroutineScope by MainScope() {

    private lateinit var deadlineDate: String
    private lateinit var startDate: String
    private lateinit var endDate: String
    private lateinit var database: KavicDatabase
    var deadlineDatePicking: Boolean = false
    var startDatePicking: Boolean = false
    var endDatePicking: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_kavic)

        setSupportActionBar(toolbar)

        val createOneTimeKavic = CreateOneTimeKavic()
        val createRepeatingKavic = CreateRepeatingKavic()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutCreateKavic, createOneTimeKavic)
            commit()
        }

        radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.radioButtonOneTimeKavic -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frameLayoutCreateKavic, createOneTimeKavic)
                        commit()
                    }
                }
                R.id.radioButtonRepeatingKavic -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frameLayoutCreateKavic, createRepeatingKavic)
                        commit()
                    }
                }
            }
        }


        val kavicViewModel = KavicViewModel()
        database = KavicDatabase.getDatabase(applicationContext)


        btnSave.setOnClickListener {
            if (editTextTitle.text.toString() != "") {
                if (radioButtonRepeatingKavic.isChecked) {
                    if (this::startDate.isInitialized && this::endDate.isInitialized) {
                        addRepeatingKavicItem(
                            RepeatingKavicItem(
                                title = editTextTitle.text.toString(),
                                description = editTextDescription.text.toString(),
                                howManySeconds = getHowManySeconds(),
                                lastDate = deadlineDate,
                                startDate = startDate,
                                repeatDays = editTextNumber.text.toString().toInt()
                            )
                        )
                        Toast.makeText(this, "Kavic created", Toast.LENGTH_SHORT).show()

                        finish()
                    } else {
                        Toast.makeText(this, "you have to fill everything", Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {
                    if (this::deadlineDate.isInitialized) {
                        kavicViewModel.insertOneTimeKavic(
                            OneTimeKavicItem(
                                title = editTextTitle.text.toString(),
                                description = editTextDescription.text.toString(),
                                howManySeconds = getHowManySeconds(),
                                deadline = deadlineDate
                            )
                        )
                        Toast.makeText(this, "Kavic created", Toast.LENGTH_SHORT).show()

                        finish()
                    } else {
                        Toast.makeText(this, "you have to fill everything", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            } else {
                Toast.makeText(this, "you most fill the title", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            finish()
        }

    }

    private fun getHowManySeconds(): Int {
        return if (editTextLength.text.toString() == "")
            0
        else
            editTextLength.text.toString().toInt()
    }


    private fun addRepeatingKavicItem(repeatingKavicItem: RepeatingKavicItem) = launch {
        withContext(Dispatchers.IO) {
            database.repeatingKavicItemDao().insert(repeatingKavicItem)
        }
    }

    fun showDatePickerDialog() {

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

            when {
                deadlineDatePicking -> {
                    deadlineDate = "$year $monthAsString $dayAsString"
                    textViewChosenDeadline.text = deadlineDate
                    deadlineDatePicking = false
                }
                startDatePicking -> {
                    startDate = "$year $monthAsString $dayAsString"
                    textViewChosenStartDate.text = startDate
                    startDatePicking = false
                }
                endDatePicking -> {
                    endDate = "$year $monthAsString $dayAsString"
                    textViewChosenEndDate.text = endDate
                    startDatePicking = false
                }
            }

        } else {
            Toast.makeText(this, "You can't choose past date.", Toast.LENGTH_LONG).show()
        }

    }

}
