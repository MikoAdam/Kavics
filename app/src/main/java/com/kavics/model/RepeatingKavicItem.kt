package com.kavics.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "repeatingKavicsDatabase")
class RepeatingKavicItem(

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String = "",
    var repeatDays: Int = 0,
    var startDate: String = "",
    var lastDate: String = "",
    var howManyMinutes: Int = 0

) : Serializable