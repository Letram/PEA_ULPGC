package com.carlosmartel.project4.data.webServices.json.orderjJson

import org.json.JSONObject

class OrderAPIController constructor(orderServiceInjection: OrderServiceInterface) :
    OrderServiceInterface {

    private val orderService: OrderServiceInterface = orderServiceInjection

    override fun insertOrder(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        orderService.insertOrder(path, params, completionHandler)
    }

    override fun getOrders(path: String, params: JSONObject?, completionHandler: (response: JSONObject?) -> Unit) {
        orderService.getOrders(path, params, completionHandler)
    }

    override fun deleteOrder(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        orderService.deleteOrder(path, params, completionHandler)
    }

    override fun updateOrder(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        orderService.updateOrder(path, params, completionHandler)
    }
}