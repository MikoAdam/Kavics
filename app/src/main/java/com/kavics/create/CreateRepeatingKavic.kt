package com.kavics.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kavics.R
import kotlinx.android.synthetic.main.fragment_create_repeating_kavic.*

class CreateRepeatingKavic : Fragment(R.layout.fragment_create_repeating_kavic) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createActivity = activity as CreateKavicActivity

        btnChooseStartDate.setOnClickListener {
            createActivity.startDatePicking = true
            createActivity.showDatePickerDialog()
        }

        btnChooseEndDate.setOnClickListener {
            createActivity.endDatePicking = true
            createActivity.showDatePickerDialog()
        }
    }

}