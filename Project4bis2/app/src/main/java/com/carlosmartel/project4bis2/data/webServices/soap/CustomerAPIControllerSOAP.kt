package com.carlosmartel.project4bis2.data.webServices.soap

import org.ksoap2.serialization.SoapObject

class CustomerAPIControllerSOAP constructor(customerServiceInjecttion: CustomerServiceInterfaceSOAP): CustomerServiceInterfaceSOAP{

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