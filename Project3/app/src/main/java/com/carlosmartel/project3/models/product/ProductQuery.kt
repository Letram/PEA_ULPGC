package com.carlosmartel.project3.models.product

import android.arch.persistence.room.*

@Dao
interface ProductQuery{
    @Query("SELECT * FROM Product WHERE productID = :productID")
    fun getProduct(productID: Int): Product

    @Query("SELECT * FROM Product")
    fun getAllProducts(): List<Product>

    @Insert
    fun insertAll(vararg products: Product)

    @Delete
    fun deleteProduct(product: Product)

    @Query("DELETE FROM Product")
    fun deleteAllProducts()

    @Update
    fun updateProduct(product: Product)
}