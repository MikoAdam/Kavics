package com.kavics.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "kavicsDatabase")
class KavicItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    @ColumnInfo(name = "kavicDeadline")
    override var deadline: String = "",
    var labels: String = "",
    var done: Boolean = false,
    var now: Boolean = false,
    var description: String = ""
) : Serializable, Item()