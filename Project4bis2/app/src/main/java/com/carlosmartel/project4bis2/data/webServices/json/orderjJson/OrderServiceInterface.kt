package com.carlosmartel.project4bis2.data.webServices.json.orderjJson

import org.json.JSONObject

interface OrderServiceInterface {
    fun getOrders(path: String, params: JSONObject?, completionHandler: (response: JSONObject?) -> Unit)
    fun insertOrder(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
    fun deleteOrder(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
    fun updateOrder(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
}