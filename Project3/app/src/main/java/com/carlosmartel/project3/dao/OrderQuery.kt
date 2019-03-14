package com.carlosmartel.project3.dao

import android.arch.persistence.room.*
import com.carlosmartel.project3.models.Order

@Dao
interface OrderQuery{
    @Query("SELECT * FROM `Order` WHERE orderID = :orderID")
    fun getOrder(orderID: Int): Order

    @Query("SELECT * FROM `Order` ORDER BY uid, productID, code")
    fun getAllOrders(): List<Order>

    @Insert
    fun insertAll(vararg products: Order)

    @Delete
    fun deleteOrder(product: Order)

    @Query("DELETE FROM `Order`")
    fun deleteAllOrders()

    @Update
    fun updateOrder(product: Order)
}