package com.carlosmartel.project4bis2.fragments.order

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.carlosmartel.project4bis2.data.entities.Order
import com.carlosmartel.project4bis2.data.webServices.WebData
import com.carlosmartel.project4bis2.data.webServices.json.orderjJson.JsonOrderService
import com.carlosmartel.project4bis2.data.webServices.json.orderjJson.OrderAPIController
import com.carlosmartel.project4bis2.data.pojo.InflatedOrder
import com.carlosmartel.project4bis2.data.pojo.InflatedOrderJson
import com.carlosmartel.project4bis2.data.room.repositories.OrderRepository
import org.json.JSONObject
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class OrderViewModel(application: Application) : AndroidViewModel(application) {
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
    fun refresh() {
        refreshOrders()
    }

    private fun refreshOrders() {
        orderApi.getOrders(WebData.GET_ORDERS, null) { response ->
            val orders: MutableList<InflatedOrderJson> = ArrayList()
            if (response != null) {
                val fault = response.getInt("fault")
                if (fault == 0) {
                    val array = response.getJSONArray("data")
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        if (obj.isNull(WebData.CUSTOMER_ID) || obj.isNull(
                                WebData.PRODUCT_ID
                            )
                        )
                            continue
                        val orderAux = Order(
                            uid = obj.getInt(WebData.CUSTOMER_ID),
                            productID = obj.getInt(WebData.PRODUCT_ID),
                            code = obj.getString(WebData.ORDER_CODE),
                            date = SimpleDateFormat("yyyy-MM-dd").parse(obj.getString(WebData.ORDER_DATE)),
                            quantity = obj.getInt(WebData.ORDER_QTY).toShort()
                        )
                        orderAux.orderID = obj.getInt(WebData.ORDER_ID)
                        val infOrder = InflatedOrderJson(
                            customerName = obj.getString(WebData.ORDER_CUSTOMER_NAME),
                            productName = obj.getString(WebData.ORDER_PRODUCT_NAME),
                            productPrice = obj.getDouble(WebData.PRODUCT_PRICE).toFloat(),
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

    fun insertJSON(
        code: String,
        date: String,
        idCustomer: Int,
        idProduct: Int,
        quantity: Int,
        completion: (Int) -> Unit
    ) {
        val jsonObject = JSONObject()
        jsonObject.put(WebData.ORDER_CODE, code)
        jsonObject.put(WebData.ORDER_QTY, quantity)
        jsonObject.put(WebData.CUSTOMER_ID, idCustomer)
        jsonObject.put(WebData.PRODUCT_ID, idProduct)
        jsonObject.put(WebData.ORDER_DATE, date)
        orderApi.insertOrder(WebData.INSERT_ORDER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0) {
                    completion(response.getInt("data"))
                    refresh()
                }
            }
        }
    }

    fun deleteJSON(orderID: Int, completion: () -> Unit) {
        val jsonObject = JSONObject()
        jsonObject.put(WebData.ORDER_ID, orderID)
        orderApi.deleteOrder(WebData.DELETE_ORDER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0 && response.getBoolean("data")) {
                    completion()
                    refresh()
                }
            }
        }
    }

    fun updateJSON(
        orderID: Int,
        code: String,
        quantity: Int,
        idCustomer: Int,
        idProduct: Int,
        date: String,
        completion: () -> Unit
    ) {
        val jsonObject = JSONObject()
        jsonObject.put(WebData.ORDER_ID, orderID)
        jsonObject.put(WebData.ORDER_CODE, code)
        jsonObject.put(WebData.CUSTOMER_ID, idCustomer)
        jsonObject.put(WebData.PRODUCT_ID, idProduct)
        jsonObject.put(WebData.ORDER_DATE, date)
        jsonObject.put(WebData.ORDER_QTY, quantity)
        orderApi.updateOrder(WebData.UPDATE_ORDER, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0 && response.getBoolean("data")) {
                    completion()
                    refresh()
                }
            }
        }
    }
}