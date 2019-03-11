package com.carlosmartel.project3.models.order

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import com.carlosmartel.project3.models.customer.Customer
import com.carlosmartel.project3.models.product.Product
import java.util.*

@Entity(foreignKeys = [
    ForeignKey(
        entity = Customer::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("ownerId"),
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = Product::class,
        parentColumns = arrayOf("productID"),
        childColumns = arrayOf("productID"),
        onDelete = ForeignKey.CASCADE
    )]
)

data class Order(
    @PrimaryKey var orderID: Int,
    var uid: Int,
    var productID: Int,
    var code: String,
    var date: Date,
    var quantity: Int
)