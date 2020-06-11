package com.kavics.adapter

import android.view.View
import com.kavics.model.KavicItem

interface KavicItemClickListener {
    fun onItemClick(kavicItem: KavicItem)
    fun onItemLongClick(position: Int, view: View, kavicItem: KavicItem): Boolean
    fun checkBoxChecked(kavicItem: KavicItem)
}