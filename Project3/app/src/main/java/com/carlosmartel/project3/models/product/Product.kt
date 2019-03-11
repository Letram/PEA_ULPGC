package com.carlosmartel.project3.models.product

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey var productID: Int,
    var name: String,
    var price: Float,
    var description: String
)