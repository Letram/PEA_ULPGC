package com.carlosmartel.project4bis2.data.webServices.soap

import com.carlosmartel.project4bis2.data.entities.Customer

interface CustomerServiceInterfaceSOAP {
    fun getCustomers(completionHandler: (response: List<Customer>?) -> Unit)
    fun insertCustomer(params: Customer, completionHandler: (response: Int?) -> Unit)
    fun updateCustomer(params: Customer, completionHandler: (response: Boolean?) -> Unit)
    fun deleteCustomer(params: Int, completionHandler: (response: Boolean?) -> Unit)
}