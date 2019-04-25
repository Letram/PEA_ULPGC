package com.carlosmartel.project3.data.repositories

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.carlosmartel.project3.data.dao.CustomerQuery
import com.carlosmartel.project3.data.database.DatabaseManager
import com.carlosmartel.project3.data.entities.Customer
import com.carlosmartel.project3.fragments.customer.CustomerViewModel

class CustomerRepository (application: Application){
    private val customerQuery: CustomerQuery = DatabaseManager.getInstance(application)!!.customerQuery()
    private var allCustomers: LiveData<List<Customer>> = customerQuery.getAllCustomers()
    private var allCustomersWithOrders: LiveData<List<Int>> = customerQuery.getAllCustomersWithOrders()

    fun insert(customer: Customer){
        InsertCustomerAsync(customerQuery).execute(customer)
    }

    class InsertCustomerAsync(private val customerQuery: CustomerQuery): AsyncTask<Customer, Void, Void>() {
        override fun doInBackground(vararg params: Customer?): Void? {
            val userID = customerQuery.insert(params[0]!!)
            CustomerViewModel.lastCustomerID = userID
            return null
        }
    }

    fun update(customer: Customer){
        UpdateCustomerAsync(customerQuery).execute(customer)
    }

    class UpdateCustomerAsync(private val customerQuery: CustomerQuery): AsyncTask<Customer, Void, Void>() {
        override fun doInBackground(vararg params: Customer?): Void? {
            customerQuery.updateCustomer(params[0]!!)
            return null
        }
    }

    fun delete(customer: Customer){
        DeleteCustomerAsync(customerQuery).execute(customer)
    }

    class DeleteCustomerAsync(private val customerQuery: CustomerQuery): AsyncTask<Customer, Void, Void>() {
        override fun doInBackground(vararg params: Customer?): Void? {
            customerQuery.deleteCustomer(params[0]!!)
            return null
        }
    }

    fun deleteAll(){
        DeleteAllCustomersAsync(customerQuery).execute()
    }

    class DeleteAllCustomersAsync(private val customerQuery: CustomerQuery): AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            customerQuery.deleteAllCustomers()
            return null
        }

    }

    fun getAllCustomers(): LiveData<List<Customer>> {
        return allCustomers
    }

    fun getAllCustomersWithOrders(): LiveData<List<Int>> {
        return allCustomersWithOrders
    }
}