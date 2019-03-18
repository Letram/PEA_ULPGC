package com.carlosmartel.project3.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity (indices = [Index(value = arrayOf("productID"))])

data class Product(
    @PrimaryKey (autoGenerate = true) var productID: Int,
    var name: String,
    var price: Float,
    var description: String
)