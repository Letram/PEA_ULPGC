package com.carlosmartel.project4.fragments.customer

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.carlosmartel.project4.data.entities.Customer
import com.carlosmartel.project4.data.json.backend.customerJson.CustomerAPIController
import com.carlosmartel.project4.data.json.backend.customerJson.JsonCustomerService
import com.carlosmartel.project4.data.json.backend.JsonData
import com.carlosmartel.project4.data.room.repositories.CustomerRepository
import org.json.JSONObject

class CustomerViewModel constructor(application: Application) : AndroidViewModel(application) {

    private var customerRepository: CustomerRepository = CustomerRepository(application)
    private var allCustomers: LiveData<List<Customer>>
    private var allCustomersWithOrders: LiveData<List<Int>>

    private var allCustomersJson: MutableLiveData<List<Customer>>
    private var customerApi: CustomerAPIController =
        CustomerAPIController(JsonCustomerService())

    init {
        allCustomers = customerRepository.getAllCustomers()
        allCustomersWithOrders = customerRepository.getAllCustomersWithOrders()
        allCustomersJson = MutableLiveData()
        refresh()
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
    fun refresh() {
        refreshCustomers()
    }

    private fun refreshCustomers() {
        customerApi.getCustomers(JsonData.GET_CUSTOMERS, null) { response ->
            val customers: MutableList<Customer> = ArrayList()
            if (response != null) {
                val fault = response.getInt("fault")
                if (fault == 0) {
                    val array = response.getJSONArray("data")
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        val customerAux = Customer(
                            address = obj.getString(JsonData.CUSTOMER_ADDRESS),
                            c_name = obj.getString(JsonData.CUSTOMER_NAME)
                        )
                        customerAux.u_id = obj.getString(JsonData.CUSTOMER_ID).toInt()
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

    fun insertJSON(name: String, address: String) {
        val jsonObject = JSONObject()
        jsonObject.put(JsonData.CUSTOMER_NAME, name)
        jsonObject.put(JsonData.CUSTOMER_ADDRESS, address)
        customerApi.insertCustomer(JsonData.INSERT_CUSTOMER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0)
                    refresh()
            }
        }
    }

    fun deleteJSON(customerID: Int) {
        val jsonObject = JSONObject()
        jsonObject.put(JsonData.CUSTOMER_ID, customerID)
        customerApi.deleteCustomer(JsonData.DELETE_CUSTOMER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0 && response.getBoolean("data"))
                    refresh()
            }
        }
    }

    fun updateJSON(customerID: Int, name: String, address: String) {
        val jsonObject = JSONObject()
        jsonObject.put(JsonData.CUSTOMER_ID, customerID)
        jsonObject.put(JsonData.CUSTOMER_NAME, name)
        jsonObject.put(JsonData.CUSTOMER_ADDRESS, address)
        customerApi.updateCustomer(JsonData.UPDATE_CUSTOMER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0 && response.getBoolean("data"))
                    refresh()
            }
        }
    }
}