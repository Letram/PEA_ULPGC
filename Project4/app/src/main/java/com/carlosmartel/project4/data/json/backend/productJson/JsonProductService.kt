package com.carlosmartel.project4.data.json.backend.productJson

import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.carlosmartel.project4.data.json.backend.BackendVolley
import com.carlosmartel.project4.data.json.backend.customerJson.JsonCustomerService
import com.carlosmartel.project4.data.json.backend.JsonData
import org.json.JSONObject

class JsonProductService : ProductServiceInterface {

    val TAG = JsonCustomerService::class.java.simpleName

    override fun getProducts(path: String, params: JSONObject?, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjReq = JsonObjectRequest(
            Request.Method.GET, JsonData.URL + path, null,
            Response.Listener<JSONObject> { response ->
                completionHandler(response)
            },
            Response.ErrorListener {
                completionHandler(null)
            })
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun insertProduct(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjReq = JsonObjectRequest(
            Request.Method.POST,
            JsonData.URL + path,
            params,
            Response.Listener<JSONObject> { response ->
                Log.d(TAG, "/post request OK! Response: $response")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                completionHandler(null)
            }
        )
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun deleteProduct(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjReq = JsonObjectRequest(
            Request.Method.POST,
            JsonData.URL + path,
            params,
            Response.Listener<JSONObject> { response ->
                Log.d(TAG, "/delete request OK! Response: $response")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                VolleyLog.e(TAG, "/delete request fail! Error: ${error.message}")
                completionHandler(null)
            }
        )
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun updateProduct(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjReq = JsonObjectRequest(
            Request.Method.PUT,
            JsonData.URL + path,
            params,
            Response.Listener<JSONObject> { response ->
                Log.d(TAG, "/put request OK! Response: $response")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                VolleyLog.e(TAG, "/put request fail! Error: ${error.message}")
                completionHandler(null)
            }
        )
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

}