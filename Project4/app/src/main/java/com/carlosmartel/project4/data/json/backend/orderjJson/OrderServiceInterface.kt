package com.carlosmartel.project4.data.json.backend.orderjJson

import org.json.JSONObject

interface OrderServiceInterface {
    fun getOrders(path: String, params: JSONObject?, completionHandler: (response: JSONObject?) -> Unit)
    fun insertOrder(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
    fun deleteOrder(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
    fun updateOrder(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
}