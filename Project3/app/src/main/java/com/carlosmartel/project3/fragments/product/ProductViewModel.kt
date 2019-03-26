package com.carlosmartel.project3.fragments.product

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.carlosmartel.project3.data.models.Product
import com.carlosmartel.project3.data.repositories.ProductRepository

class ProductViewModel constructor(application: Application): AndroidViewModel(application) {
    
    private var productRepository: ProductRepository = ProductRepository(application)
    private var allProducts: LiveData<List<Product>>
    
    init{
        allProducts = productRepository.getAllProducts()
    }

    fun insert(product: Product){
        productRepository.insert(product)
    }

    fun update(product: Product){
        productRepository.update(product)
    }

    fun delete(product: Product){
        productRepository.delete(product)
    }

    fun deleteAll(){
        productRepository.deleteAll()
    }

    fun getAllProducts(): LiveData<List<Product>> {
        return allProducts
    }

}
