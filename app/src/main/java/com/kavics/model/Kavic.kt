package com.kavics.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Kavics")
class Kavic(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "dueDate")
    var dueDate: String = "",

    @ColumnInfo(name = "labels")
    var labels: String = "",

    @ColumnInfo(name = "done")
    var done: Boolean = false,

    @ColumnInfo(name = "now")
    var now: Boolean = false,

    @ColumnInfo(name = "description")
    var description: String = ""
)