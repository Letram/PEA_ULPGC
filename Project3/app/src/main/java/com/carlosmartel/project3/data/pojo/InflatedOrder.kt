package com.carlosmartel.project3.data.pojo

import android.arch.persistence.room.Embedded
import com.carlosmartel.project3.data.entities.Customer
import com.carlosmartel.project3.data.entities.Order
import com.carlosmartel.project3.data.entities.Product

/**
 * This class is used to inflate an order with all of its data coming from the db
 *
 * @property order is the order itself
 * @property customer is the customer who made the order with all of its data
 * @property product is the product ordered with all of its data.
 */
class InflatedOrder {

    @Embedded
    var order: Order? = null

    @Embedded
    var customer: Customer? = null

    @Embedded
    var product: Product? = null
}