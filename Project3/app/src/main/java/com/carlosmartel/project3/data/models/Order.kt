package com.carlosmartel.project3.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(
    indices = [
        Index(value = arrayOf("orderID")),
        Index(value = arrayOf("uid")),
        Index(value = arrayOf("productID"))
    ],
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = arrayOf("uid"),
            childColumns = arrayOf("uid"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = arrayOf("productID"),
            childColumns = arrayOf("productID"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Order(
    @PrimaryKey (autoGenerate = true) var orderID: Int,
    var uid: Int,
    var productID: Int,
    var code: String,
    var date: Date?,
    var quantity: Short
)