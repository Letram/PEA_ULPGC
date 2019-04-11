package com.carlosmartel.project3.fragments.product

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.carlosmartel.project3.data.entities.Product
import com.carlosmartel.project3.data.repositories.ProductRepository

class ProductViewModel constructor(application: Application) : AndroidViewModel(application) {

    private var productRepository: ProductRepository = ProductRepository(application)
    private var allProducts: LiveData<List<Product>>
    private var allProductsWithOrders: LiveData<List<Int>>

    init {
        allProducts = productRepository.getAllProducts()
        allProductsWithOrders = productRepository.getAlProductsWithOrders()
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
}