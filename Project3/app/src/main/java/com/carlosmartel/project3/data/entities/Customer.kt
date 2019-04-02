package com.carlosmartel.project3.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index(value = arrayOf("u_id"))])
data class Customer(
    @PrimaryKey(autoGenerate = true) var u_id: Int = 0,
    var address: String,
    var c_name: String
)