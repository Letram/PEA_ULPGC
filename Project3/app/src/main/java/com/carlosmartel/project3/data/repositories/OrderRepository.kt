package com.carlosmartel.project3.data.repositories

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.carlosmartel.project3.data.dao.OrderQuery
import com.carlosmartel.project3.data.database.DatabaseManager
import com.carlosmartel.project3.data.models.Order

class OrderRepository (application: Application){
    private val orderQuery: OrderQuery = DatabaseManager.getInstance(application)!!.orderQuery()
    private var allOrders: LiveData<List<Order>> = orderQuery.getAllOrders()

    fun insert(order: Order){
        InsertOrderAsync(orderQuery).execute(order)
    }

    class InsertOrderAsync(private val orderQuery: OrderQuery): AsyncTask<Order, Void, Void>() {
        override fun doInBackground(vararg params: Order?): Void? {
            orderQuery.insert(params[0]!!)
            return null
        }
    }

    fun update(order: Order){
        UpdateOrderAsync(orderQuery).execute(order)
    }

    class UpdateOrderAsync(private val orderQuery: OrderQuery): AsyncTask<Order, Void, Void>() {
        override fun doInBackground(vararg params: Order?): Void? {
            orderQuery.updateOrder(params[0]!!)
            return null
        }
    }

    fun delete(order: Order){
        DeleteOrderAsync(orderQuery).execute(order)
    }

    class DeleteOrderAsync(private val orderQuery: OrderQuery): AsyncTask<Order, Void, Void>() {
        override fun doInBackground(vararg params: Order?): Void? {
            orderQuery.deleteOrder(params[0]!!)
            return null
        }
    }

    fun deleteAll(){
        DeleteAllOrdersAsync(orderQuery).execute()
    }

    class DeleteAllOrdersAsync(private val orderQuery: OrderQuery): AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            orderQuery.deleteAllOrders()
            return null
        }

    }

    fun getAllOrders(): LiveData<List<Order>> {
        return allOrders
    }
}