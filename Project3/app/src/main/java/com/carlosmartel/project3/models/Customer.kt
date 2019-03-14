package com.carlosmartel.project3.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity (indices = [Index(value = arrayOf("uid"))])
data class Customer (
    @PrimaryKey(autoGenerate = true) var uid: Int,
    var address: String,
    var name: String
)