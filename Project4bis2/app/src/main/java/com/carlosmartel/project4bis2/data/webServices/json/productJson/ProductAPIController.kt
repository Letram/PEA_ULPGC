package com.carlosmartel.project4bis2.data.webServices.json.productJson

import org.json.JSONObject

class ProductAPIController constructor(private val productService: ProductServiceInterface) :
    ProductServiceInterface {

    override fun getProducts(path: String, params: JSONObject?, completionHandler: (response: JSONObject?) -> Unit) {
        productService.getProducts(path, params, completionHandler)
    }

    override fun insertProduct(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        productService.insertProduct(path, params, completionHandler)
    }

    override fun deleteProduct(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        productService.deleteProduct(path, params, completionHandler)
    }

    override fun updateProduct(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        productService.updateProduct(path, params, completionHandler)
    }

}