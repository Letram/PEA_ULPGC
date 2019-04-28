package com.carlosmartel.project3.data.repositories

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.carlosmartel.project3.data.dao.ProductQuery
import com.carlosmartel.project3.data.database.DatabaseManager
import com.carlosmartel.project3.data.entities.Product

/**
 * This class is used as an interface between the app and the db. It is in charge of making all the operations needed
 *
 * @property productQuery is the collection of actions that are currently supported by the db
 * @property allProducts are the products in the db
 * @property allProductsWithOrders are the products that have any order in the db
 *
 */

class ProductRepository (application: Application){
    private val productQuery: ProductQuery = DatabaseManager.getInstance(application)!!.productQuery()
    private var allProducts: LiveData<List<Product>> = productQuery.getAllProducts()
    private var allProductsWithOrders: LiveData<List<Int>> = productQuery.getAllProductsWithOrders()

    fun insert(product: Product, completion: (insertedId: Int) -> Unit){
        InsertProductAsync(productQuery, completion).execute(product)
    }

    class InsertProductAsync(
        private val productQuery: ProductQuery,
        val completion: (insertedId: Int) -> Unit
    ): AsyncTask<Product, Void, Void>() {
        private var insertedID: Int? = null
        override fun doInBackground(vararg params: Product?): Void? {
            insertedID = productQuery.insert(params[0]!!).toInt()
            return null
        }

        override fun onPostExecute(result: Void?) {
            insertedID?.let { completion(it) }
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