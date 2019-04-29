package com.carlosmartel.project4bis2.data.pojo

import android.arch.persistence.room.Embedded
import com.carlosmartel.project4bis2.data.entities.Customer
import com.carlosmartel.project4bis2.data.entities.Order
import com.carlosmartel.project4bis2.data.entities.Product

class InflatedOrder {

    @Embedded
    var order: Order? = null

    @Embedded
    var customer: Customer? = null

    @Embedded
    var product: Product? = null
}