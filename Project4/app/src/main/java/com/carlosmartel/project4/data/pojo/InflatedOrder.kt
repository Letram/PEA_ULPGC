package com.carlosmartel.project4.data.pojo

import android.arch.persistence.room.Embedded
import com.carlosmartel.project4.data.entities.Customer
import com.carlosmartel.project4.data.entities.Order
import com.carlosmartel.project4.data.entities.Product

class InflatedOrder {

    @Embedded
    var order: Order? = null

    @Embedded
    var customer: Customer? = null

    @Embedded
    var product: Product? = null
}