package com.kavics.adapter

import android.view.View
import com.kavics.model.Kavic

interface KavicItemClickListener {
    fun onItemClick(kavic: Kavic)
    fun onItemLongClick(position: Int, view: View, kavic: Kavic): Boolean
}