package com.carlosmartel.project3.data.repositories

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.carlosmartel.project3.data.dao.CustomerQuery
import com.carlosmartel.project3.data.database.DatabaseManager
import com.carlosmartel.project3.data.entities.Customer

/**
 * This class is used as an interface between the app and the db. It is in charge of making all the operations needed
 *
 * @property customerQuery is the collection of actions that are currently supported by the db
 * @property allCustomers are the customer in the db
 * @property allCustomersWithOrders are the customers that have any order in the db
 *
 */
class CustomerRepository(application: Application) {
    private val customerQuery: CustomerQuery = DatabaseManager.getInstance(application)!!.customerQuery()
    private var allCustomers: LiveData<List<Customer>> = customerQuery.getAllCustomers()
    private var allCustomersWithOrders: LiveData<List<Int>> = customerQuery.getAllCustomersWithOrders()

    fun insert(customer: Customer, completion: (Int) -> Unit) {
        InsertCustomerAsync(customerQuery, completion).execute(customer)
    }

    class InsertCustomerAsync(
        private val customerQuery: CustomerQuery,
        val completion: (Int) -> Unit
    ) : AsyncTask<Customer, Void, Void>() {
        private var insertedId: Int? = null
        override fun doInBackground(vararg params: Customer?): Void? {
            insertedId = customerQuery.insert(params[0]!!).toInt()
            return null
        }

        override fun onPostExecute(result: Void?) {
            insertedId?.let { completion(it) }
        }
    }

    fun update(customer: Customer) {
        UpdateCustomerAsync(customerQuery).execute(customer)
    }

    class UpdateCustomerAsync(private val customerQuery: CustomerQuery) : AsyncTask<Customer, Void, Void>() {
        override fun doInBackground(vararg params: Customer?): Void? {
            customerQuery.updateCustomer(params[0]!!)
            return null
        }
    }

    fun delete(customer: Customer) {
        DeleteCustomerAsync(customerQuery).execute(customer)
    }

    class DeleteCustomerAsync(private val customerQuery: CustomerQuery) : AsyncTask<Customer, Void, Void>() {
        override fun doInBackground(vararg params: Customer?): Void? {
            customerQuery.deleteCustomer(params[0]!!)
            return null
        }
    }

    fun deleteAll() {
        DeleteAllCustomersAsync(customerQuery).execute()
    }

    class DeleteAllCustomersAsync(private val customerQuery: CustomerQuery) : AsyncTask<Void, Void, Void>() {
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