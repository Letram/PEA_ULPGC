package com.carlosmartel.project4bis2.data.room.repositories

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.carlosmartel.project4bis2.data.room.dao.CustomerQuery
import com.carlosmartel.project4bis2.data.room.database.DatabaseManager
import com.carlosmartel.project4bis2.data.entities.Customer

class CustomerRepository (application: Application){
    private val customerQuery: CustomerQuery = DatabaseManager.getInstance(application)!!.customerQuery()
    private var allCustomers: LiveData<List<Customer>> = customerQuery.getAllCustomers()
    private var allCustomersWithOrders: LiveData<List<Int>> = customerQuery.getAllCustomersWithOrders()

    fun insert(customer: Customer){
        InsertCustomerAsync(customerQuery).execute(customer)
    }

    class InsertCustomerAsync(private val customerQuery: CustomerQuery): AsyncTask<Customer, Void, Void>() {
        override fun doInBackground(vararg params: Customer?): Void? {
            customerQuery.insert(params[0]!!)
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