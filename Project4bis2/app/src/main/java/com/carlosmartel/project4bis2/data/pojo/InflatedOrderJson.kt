package com.carlosmartel.project4bis2.data.pojo

import com.carlosmartel.project4bis2.data.entities.Order

class InflatedOrderJson(
    internal var productName: String,
    var order: Order,
    internal var customerName: String,
    internal var productPrice: Float
) {
}