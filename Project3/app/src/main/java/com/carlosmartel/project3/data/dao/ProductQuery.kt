package com.carlosmartel.project3.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.carlosmartel.project3.data.models.Product

@Dao
interface ProductQuery{
    @Query("SELECT * FROM Product WHERE productID = :productID")
    fun getProduct(productID: Int): Product

    @Query("SELECT * FROM Product")
    fun getAllProducts(): LiveData<List<Product>>

    @Insert
    fun insertAll(vararg products: Product)

    @Insert
    fun insert(product: Product)

    @Delete
    fun deleteProduct(product: Product)

    @Query("DELETE FROM Product")
    fun deleteAllProducts()

    @Update
    fun updateProduct(product: Product)
}