package com.kavics.adapter

import android.view.View
import com.kavics.model.OneTimeKavicItem

interface KavicItemClickListener {
    fun onItemClick(oneTimeKavicItem: OneTimeKavicItem)
    fun onItemLongClick(position: Int, view: View, oneTimeKavicItem: OneTimeKavicItem): Boolean
    fun checkBoxCheckedPopUp(oneTimeKavicItem: OneTimeKavicItem)
}