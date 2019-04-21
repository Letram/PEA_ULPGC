package com.carlosmartel.project4.fragments.order

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.carlosmartel.project4.data.entities.Order
import com.carlosmartel.project4.data.json.backend.JsonData
import com.carlosmartel.project4.data.json.backend.orderjJson.JsonOrderService
import com.carlosmartel.project4.data.json.backend.orderjJson.OrderAPIController
import com.carlosmartel.project4.data.pojo.InflatedOrder
import com.carlosmartel.project4.data.pojo.InflatedOrderJson
import com.carlosmartel.project4.data.room.repositories.OrderRepository
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class OrderViewModel (application: Application) : AndroidViewModel(application){
    private var orderRepository: OrderRepository = OrderRepository(application)
    private var allOrders: LiveData<List<Order>>
    private var allInflatedOrder: LiveData<List<InflatedOrder>>

    private var allInflatedOrdersJson: MutableLiveData<List<InflatedOrderJson>>
    private var orderApi: OrderAPIController =
        OrderAPIController(JsonOrderService())
    

    init {
        allOrders = orderRepository.getAllOrders()
        allInflatedOrder = orderRepository.getAllInflatedOrders()
        allInflatedOrdersJson = MutableLiveData()
        refresh()
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

    //JSON
    private fun refresh() {
        refreshOrders()
    }

    private fun refreshOrders() {
        orderApi.getOrders(JsonData.GET_ORDERS, null) { response ->
            val orders: MutableList<InflatedOrderJson> = ArrayList()
            if (response != null) {
                val fault = response.getInt("fault")
                if (fault == 0) {
                    val array = response.getJSONArray("data")
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        if (obj.isNull(JsonData.CUSTOMER_ID) || obj.isNull(JsonData.PRODUCT_ID))
                            continue
                        val orderAux = Order(
                            uid = obj.getInt(JsonData.CUSTOMER_ID),
                            productID = obj.getInt(JsonData.PRODUCT_ID),
                            code = obj.getString(JsonData.ORDER_CODE),
                            date = SimpleDateFormat("yyyy-MM-dd").parse(obj.getString(JsonData.ORDER_DATE)),
                            quantity = obj.getInt(JsonData.ORDER_QTY).toShort()
                        )
                        orderAux.orderID = obj.getInt(JsonData.ORDER_ID)
                        val infOrder = InflatedOrderJson(
                            customerName = obj.getString(JsonData.ORDER_CUSTOMER_NAME),
                            productName = obj.getString(JsonData.ORDER_PRODUCT_NAME),
                            productPrice = obj.getDouble(JsonData.PRODUCT_PRICE).toFloat(),
                            order = orderAux
                        )
                        orders.add(infOrder)
                        println("SOMETHING WAS ADDED TO ORDER LIST")
                    }
                    allInflatedOrdersJson.value = orders
                }
            }
        }
    }

    fun getAllInflatedOrdersJSON(): MutableLiveData<List<InflatedOrderJson>> {
        return allInflatedOrdersJson
    }

    fun insertJSON(name: String, address: String) {
        val jsonObject = JSONObject()
        jsonObject.put(JsonData.CUSTOMER_NAME, name)
        jsonObject.put(JsonData.CUSTOMER_ADDRESS, address)
        orderApi.insertOrder(JsonData.INSERT_CUSTOMER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0)
                    refresh()
            }
        }
    }

    fun deleteJSON(orderID: Int) {
        val jsonObject = JSONObject()
        jsonObject.put(JsonData.CUSTOMER_ID, orderID)
        orderApi.deleteOrder(JsonData.DELETE_CUSTOMER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0 && response.getBoolean("data"))
                    refresh()
            }
        }
    }

    fun updateJSON(orderID: Int, name: String, address: String) {
        val jsonObject = JSONObject()
        jsonObject.put(JsonData.CUSTOMER_ID, orderID)
        jsonObject.put(JsonData.CUSTOMER_NAME, name)
        jsonObject.put(JsonData.CUSTOMER_ADDRESS, address)
        orderApi.updateOrder(JsonData.UPDATE_CUSTOMER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0 && response.getBoolean("data"))
                    refresh()
            }
        }
    }
}