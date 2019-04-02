package com.carlosmartel.project3.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index(value = arrayOf("p_id"))])
data class Product(
    @PrimaryKey(autoGenerate = true) var p_id: Int = 0,
    var p_name: String,
    var price: Float,
    var description: String
)