package com.carlosmartel.project3.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.carlosmartel.project3.data.models.Order

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
}