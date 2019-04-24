package com.carlosmartel.project4.data.pojo

import com.carlosmartel.project4.data.entities.Order

class InflatedOrderJson(
    internal var productName: String,
    var order: Order,
    internal var customerName: String,
    internal var productPrice: Float
) {
}