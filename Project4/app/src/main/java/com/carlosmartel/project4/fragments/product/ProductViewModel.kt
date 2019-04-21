package com.carlosmartel.project4.fragments.product

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.carlosmartel.project4.data.entities.Product
import com.carlosmartel.project4.data.json.backend.productJson.JsonProductService
import com.carlosmartel.project4.data.json.backend.productJson.ProductAPIController
import com.carlosmartel.project4.data.json.backend.JsonData
import com.carlosmartel.project4.data.room.repositories.ProductRepository
import org.json.JSONObject

class ProductViewModel constructor(application: Application) : AndroidViewModel(application) {

    private var productRepository: ProductRepository = ProductRepository(application)
    private var allProducts: LiveData<List<Product>>
    private var allProductsWithOrders: LiveData<List<Int>>

    private var allProductsJson: MutableLiveData<List<Product>>
    private var productAPI: ProductAPIController =
        ProductAPIController(JsonProductService())
    init {
        allProducts = productRepository.getAllProducts()
        allProductsWithOrders = productRepository.getAlProductsWithOrders()
        allProductsJson = MutableLiveData()
        refresh()
    }

    fun insert(product: Product) {
        productRepository.insert(product)
    }

    fun update(product: Product) {
        productRepository.update(product)
    }

    fun delete(product: Product) {
        productRepository.delete(product)
    }

    fun deleteAll() {
        productRepository.deleteAll()
    }

    fun getAllProducts(): LiveData<List<Product>> {
        return allProducts
    }

    fun getAllProductsWithOrders(): LiveData<List<Int>> {
        return allProductsWithOrders
    }

    //JSON
    private fun refresh() {
        refreshProducts()
    }

    private fun refreshProducts() {
        productAPI.getProducts(JsonData.GET_PRODUCTS, null) { response ->
            val products: MutableList<Product> = ArrayList()
            if (response != null) {
                val fault = response.getInt("fault")
                if (fault == 0) {
                    val array = response.getJSONArray("data")
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        val productAux = Product(
                            p_name = obj.getString(JsonData.PRODUCT_NAME),
                            description = obj.getString(JsonData.PRODUCT_DESCRIPTION),
                            price = obj.getDouble(JsonData.PRODUCT_PRICE).toFloat()
                        )
                        productAux.p_id = obj.getString(JsonData.PRODUCT_ID).toInt()
                        products.add(productAux)
                    }
                    allProductsJson.value = products
                }
            }
        }
    }

    fun getAllProductsJSON(): MutableLiveData<List<Product>> {
        return allProductsJson
    }

    fun insertJSON(name: String, description: String, price: Float) {
        val jsonObject = JSONObject()
        jsonObject.put(JsonData.PRODUCT_NAME, name)
        jsonObject.put(JsonData.PRODUCT_DESCRIPTION, description)
        jsonObject.put(JsonData.PRODUCT_PRICE, price)
        productAPI.insertProduct(JsonData.INSERT_PRODUCT, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0)
                    refresh()
            }
        }
    }

    fun deleteJSON(productID: Int) {
        val jsonObject = JSONObject()
        jsonObject.put(JsonData.PRODUCT_ID, productID)
        productAPI.deleteProduct(JsonData.DELETE_PRODUCT, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0 && response.getBoolean("data"))
                    refresh()
            }
        }
    }

    fun updateJSON(productID: Int, name: String, description: String, price: Float) {
        val jsonObject = JSONObject()
        jsonObject.put(JsonData.PRODUCT_ID, productID)
        jsonObject.put(JsonData.PRODUCT_NAME, name)
        jsonObject.put(JsonData.PRODUCT_DESCRIPTION, description)
        jsonObject.put(JsonData.PRODUCT_PRICE, price)
        productAPI.updateProduct(JsonData.UPDATE_PRODUCT, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0 && response.getBoolean("data"))
                    refresh()
            }
        }
    }
}