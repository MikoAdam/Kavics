package com.kavics.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "kavicsDatabase")
class OneTimeKavicItem(

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String = "",
    var description: String = "",
    @ColumnInfo(name = "kavicDeadline")
    override var deadline: String = "",
    var done: Boolean = false,
    var isArchive: Boolean = false

) : Serializable, Item()