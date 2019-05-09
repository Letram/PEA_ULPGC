package com.carlosmartel.project4bis2

import com.carlosmartel.project4bis2.data.entities.Customer
import com.carlosmartel.project4bis2.data.pojo.InflatedOrderJson

class SelectorData {
    companion object {
        var customers: List<Customer> = ArrayList()
        var products: List<Customer> = ArrayList()
        var orders: List<Customer> = ArrayList()
        var inflatedOrders: List<InflatedOrderJson> = ArrayList()
    }
}