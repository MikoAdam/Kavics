package com.kavics.create

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kavics.R
import com.kavics.adapter.DateHelper
import com.kavics.model.OneTimeKavicItem
import com.kavics.model.RepeatingKavicItem
import com.kavics.viewmodel.KavicViewModel
import kotlinx.android.synthetic.main.activity_create_kavic.*
import kotlinx.android.synthetic.main.activity_kavic_list.*
import kotlinx.android.synthetic.main.fragment_create_one_time_kavic.*
import kotlinx.android.synthetic.main.fragment_create_repeating_kavic.*
import kotlinx.coroutines.*
import java.lang.Integer.parseInt
import java.util.*

class CreateKavicActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    CoroutineScope by MainScope() {

    private lateinit var deadlineDate: String
    private lateinit var startDate: String
    private lateinit var endDate: String
    private lateinit var kavicViewModel: KavicViewModel
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

        initializeRadioButtonListener(createOneTimeKavic, createRepeatingKavic)

        kavicViewModel = ViewModelProvider(this).get(KavicViewModel::class.java)

        initializeCreateButton()

        btnBack.setOnClickListener {
            finish()
        }

    }

    private fun initializeCreateButton() {
        btnSave.setOnClickListener {

            if (editTextTitle.text.isNullOrBlank()) {
                editTextTitle.requestFocus()
                editTextTitle.error = "Please type a title"
            } else {
                if (radioButtonRepeatingKavic.isChecked) {
                    if (this::startDate.isInitialized && this::endDate.isInitialized && isNumber(
                            editTextHowManyDays.text.toString()
                        )
                    ) {
                        createRepeatingKavic()
                        Toast.makeText(this, "Kavic created", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "You have to fill everything", Toast.LENGTH_SHORT)
                            .show()
                    }

                } else if (this::deadlineDate.isInitialized) {
                    createOneTimeKavic()
                    Toast.makeText(this, "Kavic created", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "You have to fill everything", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
    }

    private fun isNumber(string: String): Boolean {
        try {
            parseInt(string)
        } catch (e: NumberFormatException) {
            editTextHowManyDays.requestFocus()
            editTextHowManyDays.error = "Please type frequency"
            return false
        }
        return true
    }

    private fun createOneTimeKavic() {
        kavicViewModel.insertOneTimeKavic(
            OneTimeKavicItem(
                title = editTextTitle.text.toString(),
                deadline = deadlineDate,
                beforeDeadline = checkBoxBeforeDeadline.isChecked,
                howManyMinutes = getHowManyMinutes()
            )
        )
    }

    private fun getHowManyMinutes(): Int {
        return if (editTextHowManyMinutes.text.toString().toIntOrNull() == null) {
            0
        } else {
            editTextHowManyMinutes.text.toString().toInt()
        }
    }

    private fun createRepeatingKavic() {
        val dateHelper = DateHelper()
        if (startDate == dateHelper.getToday())
            addIfRepeatingKavicStartsToday()

        addRepeatingKavicItem(
            RepeatingKavicItem(
                title = editTextTitle.text.toString(),
                startDate = startDate,
                lastDate = endDate,
                repeatDays = editTextHowManyDays.text.toString().toInt(),
                howManyMinutes = getHowManyMinutes()
            )
        )
    }

    private fun addIfRepeatingKavicStartsToday() {
        val dateHelper = DateHelper()
        kavicViewModel.insertOneTimeKavic(
            OneTimeKavicItem(
                title = editTextTitle.text.toString(),
                deadline = dateHelper.getToday(),
                beforeDeadline = true,
                howManyMinutes = getHowManyMinutes()
            )
        )
    }

    private fun initializeRadioButtonListener(
        createOneTimeKavic: CreateOneTimeKavic,
        createRepeatingKavic: CreateRepeatingKavic
    ) {
        radioGroup.check(R.id.radioButtonOneTimeKavic)
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
    }

    private fun addRepeatingKavicItem(repeatingKavicItem: RepeatingKavicItem) = launch {
        withContext(Dispatchers.IO) {
            kavicViewModel.insertRepeatingKavic(repeatingKavicItem)
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

        var monthAsString = "${month + 1}"
        if (monthAsString.length == 1) {
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