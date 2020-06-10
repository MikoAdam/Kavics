package com.kavics.adapter

import com.kavics.model.DeadlineItem
import com.kavics.model.Item
import com.kavics.model.KavicItem
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class deadlineHelper {

    fun getListWithDates(kavicItemList: List<Item>): List<Item> {
        if (!kavicItemList.isNullOrEmpty()) {

            val newKavicList = kavicItemList.toMutableList()
            newKavicList.removeAll { it is DeadlineItem }

            var i = 0

            while (i < newKavicList.size) {
                if (newKavicList[i] is KavicItem) {
                    if (newKavicList[i] == newKavicList[newKavicList.size - 1]) {

                        val deadlineItem = DeadlineItem()
                        deadlineItem.deadline = newKavicList[i].deadline
                        newKavicList.add(i + 1, deadlineItem)
                        i++

                    } else if (newKavicList[i].deadline != newKavicList[i + 1].deadline) {

                        val deadlineItem = DeadlineItem()
                        deadlineItem.deadline = newKavicList[i].deadline
                        newKavicList.add(i + 1, deadlineItem)
                        i++
                    }
                }
                i++
            }
            return newKavicList
        }

        return kavicItemList
    }

    fun getToday(): String {
        val calendar = Calendar.getInstance()
        val today = calendar.time

        val dateFormat: DateFormat = SimpleDateFormat("yyyy MM dd", Locale.getDefault())

        return dateFormat.format(today)

    }

    fun getTomorrow(): String {
        val calendar = Calendar.getInstance()

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time

        val dateFormat: DateFormat = SimpleDateFormat("yyyy MM dd", Locale.getDefault())

        return dateFormat.format(tomorrow)
    }

}