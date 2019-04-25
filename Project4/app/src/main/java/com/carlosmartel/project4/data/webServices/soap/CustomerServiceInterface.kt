package com.carlosmartel.project4.data.webServices.soap

import org.ksoap2.serialization.SoapObject

interface CustomerServiceInterface {
    fun getCustomers(path: String, completionHandler: (response: SoapObject?) -> Unit)
    fun insertCustomer(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit)
    fun deleteCustomer(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit)
    fun updateCustomer(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit)
}