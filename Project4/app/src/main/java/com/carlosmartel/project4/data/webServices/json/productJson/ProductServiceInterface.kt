package com.carlosmartel.project4.data.webServices.json.productJson

import org.json.JSONObject

interface ProductServiceInterface {
    fun getProducts(path: String, params: JSONObject?, completionHandler: (response: JSONObject?) -> Unit)
    fun insertProduct(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
    fun deleteProduct(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
    fun updateProduct(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
}