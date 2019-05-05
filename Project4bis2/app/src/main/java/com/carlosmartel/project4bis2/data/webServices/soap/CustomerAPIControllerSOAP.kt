package com.carlosmartel.project4bis2.data.webServices.soap

import com.carlosmartel.project4bis2.data.entities.Customer

class CustomerAPIControllerSOAP constructor(customerServiceInjecttion: CustomerServiceInterfaceSOAP): CustomerServiceInterfaceSOAP{

    private val customerService = customerServiceInjecttion

    override fun getCustomers(completionHandler: (response: List<Customer>?) -> Unit) {
        customerService.getCustomers(completionHandler)
    }

    override fun insertCustomer(params: Customer, completionHandler: (response: Int?) -> Unit) {
        customerService.insertCustomer(params, completionHandler)
    }

    override fun updateCustomer(params: Customer, completionHandler: (response: Boolean?) -> Unit) {
        customerService.updateCustomer(params, completionHandler)
    }

    override fun deleteCustomer(params: Int, completionHandler: (response: Boolean?) -> Unit) {
        customerService.deleteCustomer(params, completionHandler)
    }

}