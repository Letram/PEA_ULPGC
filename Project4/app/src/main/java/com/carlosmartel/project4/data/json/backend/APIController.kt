package com.carlosmartel.project4.data.json.backend

import org.json.JSONObject

class APIController constructor(customerServiceInjection: CustomerServiceInterface) : CustomerServiceInterface {

    private val customerService: CustomerServiceInterface = customerServiceInjection

    override fun insertCustomer(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        customerService.insertCustomer(path, params, completionHandler)
    }

    override fun getCustomers(path: String, params: JSONObject?, completionHandler: (response: JSONObject?) -> Unit) {
        customerService.getCustomers(path, params, completionHandler)
    }

    override fun deleteCustomer(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        customerService.deleteCustomer(path, params, completionHandler)
    }

    override fun updateCustomer(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        customerService.updateCustomer(path, params, completionHandler)
    }
}