package com.kavics.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "kavicsDatabase")
class OneTimeKavicItem(

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String = "",
    @ColumnInfo(name = "kavicDeadline")
    override var deadline: String = "",
    @ColumnInfo(name = "kavicHowManyMinutes")
    override var howManyMinutes: Int = 0,
    var beforeDeadline: Boolean = true,
    var done: Boolean = false,
    var isArchive: Boolean = false

) : Serializable, Item()