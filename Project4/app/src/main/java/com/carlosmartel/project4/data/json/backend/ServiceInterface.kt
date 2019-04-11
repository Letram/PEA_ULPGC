package com.carlosmartel.project4.data.json.backend

import org.json.JSONObject

interface ServiceInterface {
    fun getCustomers(path: String, params: JSONObject?)
    fun insertCustomer(path: String, params: JSONObject)

    fun getOrders(path: String, params: JSONObject)
}