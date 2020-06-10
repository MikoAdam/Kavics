package com.kavics.create

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kavics.R
import com.kavics.model.KavicItem
import com.kavics.viewmodel.KavicViewModel
import kotlinx.android.synthetic.main.activity_create_kavic.*
import java.util.*

class CreateKavicActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var deadlineDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_kavic)

        val kavicViewModel = KavicViewModel()

        btnDeadline.setOnClickListener {
            showDatePickerDialog()
        }

        btnCreateKavic.setOnClickListener {

            if (editTextKavicTitle.text.toString() != "" && this::deadlineDate.isInitialized) {
                kavicViewModel.insert(
                    KavicItem(
                        title = editTextKavicTitle.text.toString(),
                        deadline = deadlineDate,
                        labels = "",
                        done = false,
                        now = false
                    )
                )

                finish()
            } else {
                Toast.makeText(this, "you most fill the title", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelCreateKavic.setOnClickListener {

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

        var monthAsString = month.toString()
        if (month.toString().length == 1) {
            monthAsString = "0${month + 1}"
        }

        var dayAsString = dayOfMonth.toString()
        if (dayOfMonth.toString().length == 1) {
            dayAsString = "0$dayOfMonth"
        }

        textViewSelectedDate.text =
            getString(R.string.date_yyyy_MM_dd, year.toString(), monthAsString, dayAsString)


        deadlineDate = "$year $monthAsString $dayAsString"
    }

}