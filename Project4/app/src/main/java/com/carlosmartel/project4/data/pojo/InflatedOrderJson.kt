package com.carlosmartel.project4.data.pojo

import com.carlosmartel.project4.data.entities.Order

class InflatedOrderJson(
    private var productName: String,
    var order: Order,
    private var customerName: String,
    private var productPrice: Float
) {
}