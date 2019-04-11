package com.carlosmartel.project4.fragments.customer

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.carlosmartel.project4.data.entities.Customer
import com.carlosmartel.project4.data.room.repositories.CustomerRepository

class CustomerViewModel constructor(application: Application) : AndroidViewModel(application) {

    private var customerRepository: CustomerRepository = CustomerRepository(application)
    private var allCustomers: LiveData<List<Customer>>
    private var allCustomersWithOrders: LiveData<List<Int>>

    init {
        allCustomers = customerRepository.getAllCustomers()
        allCustomersWithOrders = customerRepository.getAllCustomersWithOrders()
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
}