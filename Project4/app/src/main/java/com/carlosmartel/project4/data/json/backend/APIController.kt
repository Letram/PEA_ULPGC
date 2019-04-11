package com.carlosmartel.project4.data.json.backend

import org.json.JSONObject

class APIController constructor(serviceInjection: ServiceInterface): ServiceInterface {
    private val service: ServiceInterface = serviceInjection

    override fun getOrders(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        service.getOrders(path, params, completionHandler)
    }

    override fun insertCustomer(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        service.insertCustomer(path, params, completionHandler)
    }

    override fun getCustomers(path: String, params: JSONObject?, completionHandler: (response: JSONObject?) -> Unit) {
        service.getCustomers(path, params, completionHandler)
    }
}