package com.kavics.adapter

class TimeHelper {

    fun convertFromMins(howManyMinutes: Int): String {

        val hour = howManyMinutes / 60
        val minute = howManyMinutes % 60

        val h: String = addSingleDigitNumberZero(hour)
        val m: String = addSingleDigitNumberZero(minute)

        return "$h:$m"
    }

    private fun addSingleDigitNumberZero(number: Int): String {
        return if (number < 10)
            "0$number"
        else
            "$number"
    }

}