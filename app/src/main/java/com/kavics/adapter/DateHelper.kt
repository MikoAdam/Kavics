package com.kavics.adapter

import android.annotation.SuppressLint
import com.kavics.model.DeadlineItem
import com.kavics.model.Item
import com.kavics.model.OneTimeKavicItem
import com.kavics.model.RepeatingKavicItem
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateHelper {

    //adds the deadline items to a list of kavics
    fun getKavicListWithDeadlineItems(kavicItemList: List<Item>): List<Item> {

        if (!kavicItemList.isNullOrEmpty()) {

            val newKavicList = kavicItemList.toMutableList()
            newKavicList.removeAll { it is DeadlineItem }

            var kavicCounter = 0
            var howManyMinutesSum = 0

            while (kavicCounter < newKavicList.size) {

                if (newKavicList[kavicCounter] is OneTimeKavicItem) {

                    howManyMinutesSum += newKavicList[kavicCounter].howManyMinutes

                    if (newKavicList[kavicCounter] == newKavicList[newKavicList.size - 1]) {

                        val deadlineItem = DeadlineItem()
                        howManyMinutesSum = setHowManyMinutesSum(deadlineItem, howManyMinutesSum)
                        kavicCounter = setDeadlineValue(deadlineItem, newKavicList, kavicCounter)

                    } else if (newKavicList[kavicCounter].deadline != newKavicList[kavicCounter + 1].deadline) {

                        val deadlineItem = DeadlineItem()
                        howManyMinutesSum = setHowManyMinutesSum(deadlineItem, howManyMinutesSum)
                        kavicCounter = setDeadlineValue(deadlineItem, newKavicList, kavicCounter)
                    }
                }
                kavicCounter++
            }
            return newKavicList
        }

        return kavicItemList
    }

    private fun setDeadlineValue(
        deadlineItem: DeadlineItem,
        newKavicList: MutableList<Item>,
        kavicCounter: Int
    ): Int {
        var kavicCounter1 = kavicCounter
        deadlineItem.deadline = newKavicList[kavicCounter1].deadline
        newKavicList.add(kavicCounter1 + 1, deadlineItem)
        kavicCounter1++
        return kavicCounter1
    }

    private fun setHowManyMinutesSum(
        deadlineItem: DeadlineItem,
        howManyMinutesSum: Int
    ): Int {
        var howManyMinutesSum1 = howManyMinutesSum
        deadlineItem.howManyMinutes = howManyMinutesSum1
        howManyMinutesSum1 = 0
        return howManyMinutesSum1
    }

    @SuppressLint("SimpleDateFormat")
    fun getToday(): String {
        val calendar = Calendar.getInstance()
        val today = calendar.time

        val dateFormat: DateFormat = SimpleDateFormat("yyyy MM dd")

        return dateFormat.format(today)

    }

    fun getTomorrow(): String {
        val calendar = Calendar.getInstance()

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time

        val dateFormat: DateFormat = SimpleDateFormat("yyyy MM dd", Locale.getDefault())

        return dateFormat.format(tomorrow)
    }

    private fun dateStringToInt(date: String): Int {
        return date.replace(" ", "").toInt()
    }

    fun equalDate(repeatingKavicItem: RepeatingKavicItem): Boolean {

        val difference = dateStringToInt(getToday()) - dateStringToInt(repeatingKavicItem.lastDate)

        return if (getToday() <= repeatingKavicItem.lastDate)
            difference % repeatingKavicItem.repeatDays == 0
        else
            false

    }

}