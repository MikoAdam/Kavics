package com.kavics.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kavics.R
import com.kavics.edit.EditKavicActivity
import com.kavics.model.OneTimeKavicItem
import com.kavics.viewmodel.KavicViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val KAVIC_ITEM = "KAVIC_ITEM"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val kavicViewModel = KavicViewModel()

        val kavic = intent.getSerializableExtra(EditKavicActivity.KAVIC_ITEM) as OneTimeKavicItem

        textViewTimer.text = kavic.howManySeconds.toString()


    }
}