package com.carlosmartel.project3.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.carlosmartel.project3.data.entities.Product

@Dao
interface ProductQuery{
    @Query("SELECT * FROM Product WHERE p_id = :productID")
    fun getProduct(productID: Int): Product

    @Query("SELECT * FROM Product ORDER BY p_name")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT DISTINCT p_id FROM Product, `Order` WHERE Product.p_id = `Order`.productID")
    fun getAllProductsWithOrders(): LiveData<List<Int>>

    @Insert
    fun insertAll(vararg products: Product)

    @Insert
    fun insert(product: Product): Long

    @Delete
    fun deleteProduct(product: Product)

    @Query("DELETE FROM Product")
    fun deleteAllProducts()

    @Update
    fun updateProduct(product: Product)
}