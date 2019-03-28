package com.carlosmartel.project3.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index(value = arrayOf("uid"))])
data class Customer(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    var address: String,
    var name: String
)