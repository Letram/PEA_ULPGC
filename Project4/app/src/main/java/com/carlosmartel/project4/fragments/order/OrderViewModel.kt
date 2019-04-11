package com.carlosmartel.project4.fragments.order

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.carlosmartel.project4.data.entities.Order
import com.carlosmartel.project4.data.pojo.InflatedOrder
import com.carlosmartel.project4.data.room.repositories.OrderRepository

class OrderViewModel (application: Application) : AndroidViewModel(application){
    private var orderRepository: OrderRepository = OrderRepository(application)
    private var allOrders: LiveData<List<Order>>
    private var allInflatedOrder: LiveData<List<InflatedOrder>>
    init {
        allOrders = orderRepository.getAllOrders()
        allInflatedOrder = orderRepository.getAllInflatedOrders()
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

    fun getAllInflatedOrders(): LiveData<List<InflatedOrder>> {
        return allInflatedOrder
    }
}