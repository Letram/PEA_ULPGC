package com.carlosmartel.project4bis2.fragments.customer

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.carlosmartel.project4bis2.SelectorData
import com.carlosmartel.project4bis2.data.entities.Customer
import com.carlosmartel.project4bis2.data.room.repositories.CustomerRepository
import com.carlosmartel.project4bis2.data.webServices.WebData
import com.carlosmartel.project4bis2.data.webServices.json.customerJson.CustomerAPIController
import com.carlosmartel.project4bis2.data.webServices.json.customerJson.JsonCustomerService
import com.carlosmartel.project4bis2.data.webServices.soap.CustomerAPIControllerSOAP
import com.carlosmartel.project4bis2.data.webServices.soap.SOAPCustomerService
import org.json.JSONObject

class CustomerViewModel constructor(application: Application) : AndroidViewModel(application) {

    private var customerRepository: CustomerRepository = CustomerRepository(application)
    private var allCustomers: LiveData<List<Customer>>
    private var allCustomersWithOrders: LiveData<List<Int>>

    private var allCustomersJson: MutableLiveData<List<Customer>>
    private var customerApiJSON: CustomerAPIController = CustomerAPIController(JsonCustomerService())

    private var allCustomersSOAP: MutableLiveData<List<Customer>>
    private var customerApiSOAP: CustomerAPIControllerSOAP = CustomerAPIControllerSOAP(SOAPCustomerService())

    init {
        allCustomers = customerRepository.getAllCustomers()
        allCustomersWithOrders = customerRepository.getAllCustomersWithOrders()
        allCustomersJson = MutableLiveData()
        allCustomersSOAP = MutableLiveData()
        if (WebData.connected) {
            refreshAll {}
            refreshJSON()
            refreshSOAP()
        }
    }

    fun refreshAll(completion: () -> Unit) {
        if(!WebData.connected){
            completion()
            return
        }
        refreshJSON()
        refreshSOAP()
    }

    fun insert(customer: Customer) {
        customerRepository.insert(customer)
    }

    fun update(customer: Customer) {
        customerRepository.update(customer)
    }

    fun delete(customer: Customer) {
        customerRepository.delete(customer)
    }

    fun deleteAll() {
        customerRepository.deleteAll()
    }

    fun getAllCustomers(): LiveData<List<Customer>> {
        return allCustomers
    }

    fun getAllCustomersWithOrders(): LiveData<List<Int>> {
        return allCustomersWithOrders
    }

    //JSON
    private fun refreshJSON() {
        refreshCustomersJSON()
    }

    private fun refreshCustomersJSON() {
        customerApiJSON.getCustomers(WebData.GET_CUSTOMERS, null) { response ->
            val customers: MutableList<Customer> = ArrayList()
            if (response != null) {
                val fault = response.getInt("fault")
                if (fault == 0) {
                    val array = response.getJSONArray("data")
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        val customerAux = Customer(
                            address = obj.getString(WebData.CUSTOMER_ADDRESS),
                            c_name = obj.getString(WebData.CUSTOMER_NAME)
                        )
                        customerAux.u_id = obj.getString(WebData.CUSTOMER_ID).toInt()
                        customers.add(customerAux)
                    }
                    allCustomersJson.value = customers
                }
            }
        }
    }

    fun getAllCustomersJSON(): MutableLiveData<List<Customer>> {
        return allCustomersJson
    }

    fun insertJSON(name: String, address: String, completion: (Int) -> Unit) {
        val jsonObject = JSONObject()
        jsonObject.put(WebData.CUSTOMER_NAME, name)
        jsonObject.put(WebData.CUSTOMER_ADDRESS, address)
        customerApiJSON.insertCustomer(WebData.INSERT_CUSTOMER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0) {
                    completion(response.getInt("data"))
                    refreshAll {}
                }
            }
        }
    }

    fun deleteJSON(customerID: Int, completion: () -> Unit) {
        val jsonObject = JSONObject()
        jsonObject.put(WebData.CUSTOMER_ID, customerID)
        customerApiJSON.deleteCustomer(WebData.DELETE_CUSTOMER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0 && response.getBoolean("data")) {
                    completion()
                    refreshAll {}
                }
            }
        }
    }

    fun updateJSON(customerID: Int, name: String, address: String, completion: () -> Unit) {
        val jsonObject = JSONObject()
        jsonObject.put(WebData.CUSTOMER_ID, customerID)
        jsonObject.put(WebData.CUSTOMER_NAME, name)
        jsonObject.put(WebData.CUSTOMER_ADDRESS, address)
        customerApiJSON.updateCustomer(WebData.UPDATE_CUSTOMER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0 && response.getBoolean("data")) {
                    completion()
                    refreshAll {}
                }
            }
        }
    }

    //SOAP
    private fun refreshSOAP() {
        refreshCustomersSOAP()
    }

    private fun refreshCustomersSOAP() {
        customerApiSOAP.getCustomers {
            allCustomersSOAP.value = it
            if (it != null)
                SelectorData.customers = it
        }
    }

    fun getAllCustomersSOAP(): MutableLiveData<List<Customer>> {
        return allCustomersSOAP
    }

    fun insertSOAP(name: String, address: String, completion: (Int) -> Unit) {
        if (!WebData.connected) {
            completion(-1)
            return
        }
        val customerToInsert = Customer(c_name = name, address = address)
        customerApiSOAP.insertCustomer(customerToInsert) { response ->
            response?.let {
                completion(it)
                refreshAll {}
            }
        }
    }

    fun updateSOAP(uid: Int, name: String?, address: String?, completion: (Boolean) -> Unit) {
        if (!WebData.connected) {
            completion(false)
            return
        }
        val customerToUpdate = Customer(c_name = name!!, address = address!!)
        customerToUpdate.u_id = uid
        customerApiSOAP.updateCustomer(customerToUpdate) { response ->
            response?.let {
                completion(it)
                refreshAll {}
            }
        }
    }

    fun deleteSOAP(uid: Int, completion: (Boolean) -> Unit) {
        if (!WebData.connected) {
            completion(false)
            return
        }
        customerApiSOAP.deleteCustomer(uid) { response ->
            response?.let {
                completion(it)
                refreshAll {}
            }
        }
    }
}