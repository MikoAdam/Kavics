package com.kavics.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kavics.R
import kotlinx.android.synthetic.main.fragment_create_one_time_kavic.*

class CreateOneTimeKavic : Fragment(R.layout.fragment_create_one_time_kavic) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createActivity = activity as CreateKavicActivity

        btnChooseDeadline.setOnClickListener {
            createActivity.startDatePicking = false
            createActivity.endDatePicking = false
            createActivity.deadlineDatePicking = true
            createActivity.showDatePickerDialog()
        }

    }

}