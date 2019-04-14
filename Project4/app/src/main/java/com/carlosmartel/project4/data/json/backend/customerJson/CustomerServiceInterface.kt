package com.carlosmartel.project4.data.json.backend.customerJson

import org.json.JSONObject

interface CustomerServiceInterface {
    fun getCustomers(path: String, params: JSONObject?, completionHandler: (response: JSONObject?) -> Unit)
    fun insertCustomer(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
    fun deleteCustomer(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
    fun updateCustomer(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
}