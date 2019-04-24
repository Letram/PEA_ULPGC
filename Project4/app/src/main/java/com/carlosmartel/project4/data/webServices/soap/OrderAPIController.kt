package com.carlosmartel.project4.data.webServices.soap

import org.ksoap2.serialization.SoapObject

class OrderAPIController constructor(private val orderService: OrderServiceInterface) : OrderServiceInterface {
    override fun getOrders(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit) {
        orderService.getOrders(path, params, completionHandler)
    }

    override fun insertOrder(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit) {
        orderService.insertOrder(path, params, completionHandler)
    }

    override fun deleteOrder(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit) {
        orderService.deleteOrder(path, params, completionHandler)
    }

    override fun updateOrder(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit) {
        orderService.updateOrder(path, params, completionHandler)
    }

}