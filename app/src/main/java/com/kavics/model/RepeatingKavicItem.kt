package com.kavics.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "repeatingKavicsDatabase")
data class RepeatingKavicItem(

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var repeatDays: Int = 0,
    var startDate: String = "",
    var lastDate: String = ""

) : Serializable