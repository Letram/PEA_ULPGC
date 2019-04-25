package com.carlosmartel.project4.data.webServices.soap

import org.ksoap2.serialization.SoapObject

class CustomerAPIController constructor(customerServiceInjecttion: CustomerServiceInterface): CustomerServiceInterface{

    private val customerService = customerServiceInjecttion

    override fun getCustomers(path: String, completionHandler: (response: SoapObject?) -> Unit) {
        customerService.getCustomers(path, completionHandler)
    }

    override fun insertCustomer(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteCustomer(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCustomer(path: String, params: SoapObject, completionHandler: (response: SoapObject?) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}