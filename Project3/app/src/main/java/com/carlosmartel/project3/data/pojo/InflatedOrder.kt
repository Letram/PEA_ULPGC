package com.carlosmartel.project3.data.pojo

import android.arch.persistence.room.Embedded
import com.carlosmartel.project3.data.entities.Customer
import com.carlosmartel.project3.data.entities.Order
import com.carlosmartel.project3.data.entities.Product

class InflatedOrder {

    @Embedded
    var order: Order? = null

    @Embedded
    var customer: Customer? = null

    @Embedded
    var product: Product? = null
}