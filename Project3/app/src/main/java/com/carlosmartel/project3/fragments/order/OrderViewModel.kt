package com.carlosmartel.project3.fragments.order

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.carlosmartel.project3.data.models.Order
import com.carlosmartel.project3.data.repositories.OrderRepository

class OrderViewModel (application: Application) : AndroidViewModel(application){
    private var orderRepository: OrderRepository = OrderRepository(application)
    private var allOrders: LiveData<List<Order>>

    init {
        allOrders = orderRepository.getAllOrders()
    }

    fun insert(order: Order) {
        orderRepository.insert(order)
    }

    fun update(order: Order) {
        orderRepository.update(order)
    }

    fun delete(order: Order) {
        orderRepository.delete(order)
    }

    fun deleteAll() {
        orderRepository.deleteAll()
    }

    fun getAllOrders(): LiveData<List<Order>> {
        return allOrders
    }
}