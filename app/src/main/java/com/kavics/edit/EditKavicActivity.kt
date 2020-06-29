package com.kavics.edit

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kavics.R
import com.kavics.model.OneTimeKavicItem
import com.kavics.viewmodel.KavicViewModel
import kotlinx.android.synthetic.main.activity_kavic_edit.*
import kotlinx.android.synthetic.main.activity_kavic_list.*

class EditKavicActivity : AppCompatActivity() {

    companion object {
        const val KAVIC_ITEM = "KAVIC_ITEM"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kavic_edit)
        setSupportActionBar(toolbar)

        val kavicViewModel = ViewModelProvider(this).get(KavicViewModel::class.java)
        val kavic = intent.getSerializableExtra(KAVIC_ITEM) as OneTimeKavicItem

        editTextTextKavicTitle.setText(kavic.title)

        btnSaveEdit.setOnClickListener {

            if (editTextTextKavicTitle.text.toString() != "") {
                kavic.title = editTextTextKavicTitle.text.toString()
                kavicViewModel.updateOneTimeKavic(kavic)

                finish()

            } else {
                Toast.makeText(this, "You have to fill all", Toast.LENGTH_LONG).show()
            }

        }

    }
}