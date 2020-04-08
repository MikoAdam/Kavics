package com.kavics.fragament

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.kavics.R
import com.kavics.model.Kavic
import kotlinx.android.synthetic.main.fragment_create.*

class KavicCreateFragment : DialogFragment(), DatePickerDialogFragment.DateListener {

    private lateinit var listener: KavicCreatedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = if (targetFragment != null) {
                targetFragment as KavicCreatedListener
            } else {
                activity as KavicCreatedListener
            }
        } catch (e: ClassCastException) {
            throw RuntimeException(e)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create, container, false)
        dialog?.setTitle(R.string.itemCreateKavic)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvKavicDueDate.setOnClickListener { showDatePickerDialog() }

        tvKavicDueDate.text = "  -  "

        btnCreateKavic.setOnClickListener {

            listener.onKavicCreated(
                Kavic(
                    title = etKavicTitle.text.toString(),
                    dueDate = tvKavicDueDate.text.toString(),
                    description = etKavicDescription.text.toString(),
                    labels = "",
                    done = false,
                    now = false
                )
            )

            dismiss()
        }

        btnCancelCreateKavic.setOnClickListener {
            dismiss()
        }

    }

    interface KavicCreatedListener {
        fun onKavicCreated(kavic: Kavic)
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialogFragment()
        datePicker.setTargetFragment(this, 0)
        fragmentManager?.let { datePicker.show(it, DatePickerDialogFragment.TAG) }
    }

    override fun onDateSelected(date: String) {
        tvKavicDueDate.text = date
    }
}