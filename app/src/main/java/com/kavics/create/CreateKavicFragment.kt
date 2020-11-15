package com.kavics.create

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kavics.R
import com.kavics.adapter.DateHelper
import com.kavics.model.OneTimeKavicItem
import com.kavics.model.RepeatingKavicItem
import com.kavics.viewmodel.KavicViewModel
import kotlinx.android.synthetic.main.fragment_create_kavic.*
import kotlinx.android.synthetic.main.fragment_create_one_time_kavic.*
import kotlinx.android.synthetic.main.fragment_create_repeating_kavic.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.util.*

class CreateKavicFragment : Fragment(R.layout.fragment_create_kavic),
    DatePickerDialog.OnDateSetListener,
    CoroutineScope by MainScope() {

    private lateinit var mainContext: Context
    private lateinit var deadlineDate: String
    private lateinit var startDate: String
    private lateinit var endDate: String
    private lateinit var kavicViewModel: KavicViewModel
    var deadlineDatePicking: Boolean = false
    var startDatePicking: Boolean = false
    var endDatePicking: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainContext = requireActivity().applicationContext

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createOneTimeKavic = CreateOneTimeKavic()
        val createRepeatingKavic = CreateRepeatingKavic()

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutCreateKavic, createOneTimeKavic)
            commit()
        }

        initializeRadioButtonListener(createOneTimeKavic, createRepeatingKavic)

        kavicViewModel = ViewModelProvider(this).get(KavicViewModel::class.java)

        initializeCreateButton()

        btnBack.setOnClickListener {
            //  finish()
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
/*                        Toast.makeText(this, "Kavic created", Toast.LENGTH_SHORT).show()
                        finish()*/
                    } else {
/*                        Toast.makeText(this, "You have to fill everything", Toast.LENGTH_SHORT)
                            .show()*/
                    }

                } else if (this::deadlineDate.isInitialized) {
                    createOneTimeKavic()
/*                    Toast.makeText(this, "Kavic created", Toast.LENGTH_SHORT).show()
                    finish()*/
                } else {
/*                    Toast.makeText(this, "You have to fill everything", Toast.LENGTH_SHORT)
                        .show()*/
                }

            }
        }
    }

    private fun isNumber(string: String): Boolean {
        try {
            Integer.parseInt(string)
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
                description = editTextDescription.text.toString(),
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
                description = editTextDescription.text.toString(),
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
                description = editTextDescription.text.toString(),
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
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.frameLayoutCreateKavic, createOneTimeKavic)
                        commit()
                    }
                }
                R.id.radioButtonRepeatingKavic -> {
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.frameLayoutCreateKavic, createRepeatingKavic)
                        commit()
                    }
                }
            }
        }
    }

    private fun addRepeatingKavicItem(repeatingKavicItem: RepeatingKavicItem) {

        kavicViewModel.insertRepeatingKavic(repeatingKavicItem)

    }

    fun showDatePickerDialog() {
        activity?.applicationContext?.let {
            DatePickerDialog(
                it,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
        }?.show()
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
            Toast.makeText(mainContext, "You can't choose past date.", Toast.LENGTH_LONG).show()
        }
    }


}