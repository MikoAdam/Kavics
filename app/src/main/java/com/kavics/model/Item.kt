package com.kavics.model

import java.io.Serializable

abstract class Item(
    open var deadline: String = "",
    open var howManyMinutes: Int = 0
) : Serializable