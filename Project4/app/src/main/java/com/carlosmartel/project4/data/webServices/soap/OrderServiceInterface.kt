package com.carlosmartel.project4.data.webServices.soap

import org.ksoap2.serialization.SoapObject

interface OrderServiceInterface {
    fun getOrders(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit)
    fun insertOrder(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit)
    fun deleteOrder(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit)
    fun updateOrder(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit)
}