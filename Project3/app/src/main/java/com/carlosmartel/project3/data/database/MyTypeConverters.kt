package com.carlosmartel.project3.data.database

import android.arch.persistence.room.TypeConverter
import java.util.*

class MyTypeConverters{

    @TypeConverter
    fun fromTimestamp(value: Long?): Date?{
        //in javascript it would be something like '''value.let( (it) => {Date(it)} )''', the variable 'it' is default.
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}