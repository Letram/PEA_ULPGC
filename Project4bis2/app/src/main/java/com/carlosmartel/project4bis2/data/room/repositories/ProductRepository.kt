package com.carlosmartel.project4bis2.data.room.repositories

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.carlosmartel.project4bis2.data.room.dao.ProductQuery
import com.carlosmartel.project4bis2.data.room.database.DatabaseManager
import com.carlosmartel.project4bis2.data.entities.Product

class ProductRepository (application: Application){
    private val productQuery: ProductQuery = DatabaseManager.getInstance(application)!!.productQuery()
    private var allProducts: LiveData<List<Product>> = productQuery.getAllProducts()
    private var allProductsWithOrders: LiveData<List<Int>> = productQuery.getAllProductsWithOrders()

    fun insert(product: Product){
        InsertProductAsync(productQuery).execute(product)
    }

    class InsertProductAsync(private val productQuery: ProductQuery): AsyncTask<Product, Void, Void>() {
        override fun doInBackground(vararg params: Product?): Void? {
            productQuery.insert(params[0]!!)
            return null
        }
    }

    fun update(product: Product){
        UpdateProductAsync(productQuery).execute(product)
    }

    class UpdateProductAsync(private val productQuery: ProductQuery): AsyncTask<Product, Void, Void>() {
        override fun doInBackground(vararg params: Product?): Void? {
            productQuery.updateProduct(params[0]!!)
            return null
        }
    }

    fun delete(product: Product){
        DeleteProductAsync(productQuery).execute(product)
    }

    class DeleteProductAsync(private val productQuery: ProductQuery): AsyncTask<Product, Void, Void>() {
        override fun doInBackground(vararg params: Product?): Void? {
            productQuery.deleteProduct(params[0]!!)
            return null
        }
    }

    fun deleteAll(){
        DeleteAllProductsAsync(productQuery).execute()
    }

    class DeleteAllProductsAsync(private val productQuery: ProductQuery): AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            productQuery.deleteAllProducts()
            return null
        }

    }

    fun getAllProducts(): LiveData<List<Product>> {
        return allProducts
    }

    fun getAlProductsWithOrders(): LiveData<List<Int>> {
        return allProductsWithOrders
    }
}