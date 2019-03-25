package com.carlosmartel.project3.fragments.customer

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.carlosmartel.project3.data.models.Customer
import com.carlosmartel.project3.data.repositories.CustomerRepository

class CustomerViewModel constructor(application: Application) : AndroidViewModel(application) {
    private var customerRepository: CustomerRepository = CustomerRepository(application)
    private var allCustomers: LiveData<List<Customer>>


    init {
        allCustomers = customerRepository.getAllCustomers()
    }
    fun insert(customer: Customer){
        customerRepository.insert(customer)
    }

    fun update(customer: Customer){
        customerRepository.update(customer)
    }

    fun delete(customer: Customer){
        customerRepository.delete(customer)
    }

    fun deleteAll(){
        customerRepository.deleteAll()
    }

    fun getAllCustomers(): LiveData<List<Customer>> {
        return allCustomers
    }
}