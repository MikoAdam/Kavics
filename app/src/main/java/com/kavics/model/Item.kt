package com.kavics.model

import java.io.Serializable

abstract class Item(
    open var deadline: String = ""
) : Serializable