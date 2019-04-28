package com.carlosmartel.project3.data.repositories

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.carlosmartel.project3.data.dao.OrderQuery
import com.carlosmartel.project3.data.database.DatabaseManager
import com.carlosmartel.project3.data.entities.Order
import com.carlosmartel.project3.data.pojo.InflatedOrder

/**
 * This class is used as an interface between the app and the db. It is in charge of making all the operations needed
 *
 * @property orderQuery is the collection of actions that are currently supported by the db
 * @property allOrders are the orders in the db
 * @property allInflatedOrders are all the orders in the db plus all of its inflated data such as the customer data and product info
 *
 */
class OrderRepository(application: Application) {
    private val orderQuery: OrderQuery = DatabaseManager.getInstance(application)!!.orderQuery()
    private var allOrders: LiveData<List<Order>> = orderQuery.getAllOrders()
    private var allInflatedOrders: LiveData<List<InflatedOrder>> = orderQuery.getAllInflatedOrders()

    fun insert(order: Order, completion: (Int) -> Unit) {
        InsertOrderAsync(orderQuery, completion).execute(order)
    }

    class InsertOrderAsync(
        private val orderQuery: OrderQuery,
        val completion: (Int) -> Unit
    ) : AsyncTask<Order, Void, Void>() {
        private var insertedId: Int? = null
        override fun doInBackground(vararg params: Order?): Void? {
            insertedId = orderQuery.insert(params[0]!!).toInt()
            return null
        }

        override fun onPostExecute(result: Void?) {
            insertedId?.let { completion(it) }
        }
    }

    fun update(order: Order) {
        UpdateOrderAsync(orderQuery).execute(order)
    }

    class UpdateOrderAsync(private val orderQuery: OrderQuery) : AsyncTask<Order, Void, Void>() {
        override fun doInBackground(vararg params: Order?): Void? {
            orderQuery.updateOrder(params[0]!!)
            return null
        }
    }

    fun delete(order: Order) {
        DeleteOrderAsync(orderQuery).execute(order)
    }

    class DeleteOrderAsync(private val orderQuery: OrderQuery) : AsyncTask<Order, Void, Void>() {
        override fun doInBackground(vararg params: Order?): Void? {
            orderQuery.deleteOrder(params[0]!!)
            return null
        }
    }

    fun deleteAll() {
        DeleteAllOrdersAsync(orderQuery).execute()
    }

    class DeleteAllOrdersAsync(private val orderQuery: OrderQuery) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            orderQuery.deleteAllOrders()
            return null
        }

    }

    fun getAllOrders(): LiveData<List<Order>> {
        return allOrders
    }

    fun getAllInflatedOrders(): LiveData<List<InflatedOrder>> {
        return allInflatedOrders
    }
}