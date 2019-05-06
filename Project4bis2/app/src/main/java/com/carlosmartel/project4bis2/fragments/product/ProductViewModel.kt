package com.carlosmartel.project4bis2.fragments.product

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.carlosmartel.project4bis2.data.entities.Product
import com.carlosmartel.project4bis2.data.webServices.json.productJson.JsonProductService
import com.carlosmartel.project4bis2.data.webServices.json.productJson.ProductAPIController
import com.carlosmartel.project4bis2.data.webServices.WebData
import com.carlosmartel.project4bis2.data.room.repositories.ProductRepository
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
    fun refresh() {
        refreshProducts()
    }

    private fun refreshProducts() {
        productAPI.getProducts(WebData.GET_PRODUCTS, null) { response ->
            val products: MutableList<Product> = ArrayList()
            if (response != null) {
                val fault = response.getInt("fault")
                if (fault == 0) {
                    val array = response.getJSONArray("data")
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        val productAux = Product(
                            p_name = obj.getString(WebData.PRODUCT_NAME),
                            description = obj.getString(WebData.PRODUCT_DESCRIPTION),
                            price = obj.getDouble(WebData.PRODUCT_PRICE).toFloat()
                        )
                        productAux.p_id = obj.getString(WebData.PRODUCT_ID).toInt()
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

    fun insertJSON(
        name: String,
        description: String,
        price: Float,
        completion: (Int) -> Unit
    ) {
        val jsonObject = JSONObject()
        jsonObject.put(WebData.PRODUCT_NAME, name)
        jsonObject.put(WebData.PRODUCT_DESCRIPTION, description)
        jsonObject.put(WebData.PRODUCT_PRICE, price)
        productAPI.insertProduct(WebData.INSERT_PRODUCT, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0) {
                    completion(response.getInt("data"))
                    refresh()
                }
            }
        }
    }

    fun deleteJSON(productID: Int, completion: () -> Unit) {
        val jsonObject = JSONObject()
        jsonObject.put(WebData.PRODUCT_ID, productID)
        productAPI.deleteProduct(WebData.DELETE_PRODUCT, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0 && response.getBoolean("data")){
                    completion()
                    refresh()
                }
            }
        }
    }

    fun updateJSON(
        productID: Int,
        name: String,
        description: String,
        price: Float,
        completion: () -> Unit
    ) {
        val jsonObject = JSONObject()
        jsonObject.put(WebData.PRODUCT_ID, productID)
        jsonObject.put(WebData.PRODUCT_NAME, name)
        jsonObject.put(WebData.PRODUCT_DESCRIPTION, description)
        jsonObject.put(WebData.PRODUCT_PRICE, price)
        productAPI.updateProduct(WebData.UPDATE_PRODUCT, jsonObject) { response ->
            if (response != null) {
                if (response.getInt("fault") == 0 && response.getBoolean("data")) {
                    completion()
                    refresh()
                }
            }
        }
    }
}