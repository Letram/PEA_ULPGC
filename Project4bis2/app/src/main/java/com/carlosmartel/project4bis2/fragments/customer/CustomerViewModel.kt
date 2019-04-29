package com.carlosmartel.project4bis2.fragments.customer

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.carlosmartel.project4bis2.data.entities.Customer
import com.carlosmartel.project4bis2.data.webServices.json.customerJson.CustomerAPIController
import com.carlosmartel.project4bis2.data.webServices.json.customerJson.JsonCustomerService
import com.carlosmartel.project4bis2.data.webServices.WebData
import com.carlosmartel.project4bis2.data.room.repositories.CustomerRepository
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
    private fun refresh() {
        refreshCustomers()
    }

    private fun refreshCustomers() {
        customerApi.getCustomers(WebData.GET_CUSTOMERS, null) { response ->
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
        customerApi.insertCustomer(WebData.INSERT_CUSTOMER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0){
                    completion(response.getInt("data"))
                    refresh()
                }
            }
        }
    }

    fun deleteJSON(customerID: Int, completion: () -> Unit) {
        val jsonObject = JSONObject()
        jsonObject.put(WebData.CUSTOMER_ID, customerID)
        customerApi.deleteCustomer(WebData.DELETE_CUSTOMER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0 && response.getBoolean("data")){
                    completion()
                    refresh()
                }
            }
        }
    }

    fun updateJSON(customerID: Int, name: String, address: String, completion: () -> Unit) {
        val jsonObject = JSONObject()
        jsonObject.put(WebData.CUSTOMER_ID, customerID)
        jsonObject.put(WebData.CUSTOMER_NAME, name)
        jsonObject.put(WebData.CUSTOMER_ADDRESS, address)
        customerApi.updateCustomer(WebData.UPDATE_CUSTOMER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0 && response.getBoolean("data")){
                    completion()
                    refresh()
                }
            }
        }
    }
}