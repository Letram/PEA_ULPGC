package com.carlosmartel.project3.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.carlosmartel.project3.data.entities.Order
import com.carlosmartel.project3.data.pojo.InflatedOrder

@Dao
interface OrderQuery{
    @Query("SELECT * FROM `Order` WHERE orderID = :orderID")
    fun getOrder(orderID: Int): Order

    @Query("SELECT * FROM `Order` ORDER BY uid, productID, code")
    fun getAllOrders(): LiveData<List<Order>>

    @Insert
    fun insertAll(vararg orders: Order)

    @Insert
    fun insert(order: Order)

    @Delete
    fun deleteOrder(order: Order)

    @Query("DELETE FROM `Order`")
    fun deleteAllOrders()

    @Update
    fun updateOrder(order: Order)

    @Query("SELECT * FROM `Order`, Customer, Product WHERE Customer.u_id = `Order`.uid AND Product.p_id =  `Order`.productID ORDER BY u_id, p_id, code")
    fun getAllInflatedOrders(): LiveData<List<InflatedOrder>>
}