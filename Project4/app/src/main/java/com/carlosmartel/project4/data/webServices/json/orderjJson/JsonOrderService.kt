package com.carlosmartel.project4.data.webServices.json.orderjJson

import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.carlosmartel.project4.data.webServices.json.BackendVolley
import com.carlosmartel.project4.data.webServices.WebData
import org.json.JSONObject

class JsonOrderService : OrderServiceInterface {
    val TAG = JsonOrderService::class.java.simpleName

    override fun insertOrder(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjReq = JsonObjectRequest(
            Request.Method.POST,
            WebData.URL + path,
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

    override fun getOrders(path: String, params: JSONObject?, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjReq = JsonObjectRequest(
            Request.Method.GET, WebData.URL + path, null,
            Response.Listener<JSONObject> { response ->
                completionHandler(response)
            },
            Response.ErrorListener {
                completionHandler(null)
            })
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun deleteOrder(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        println(params)

        val jsonObjReq = JsonObjectRequest(
            Request.Method.POST,
            WebData.URL + path,
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

    override fun updateOrder(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjReq = JsonObjectRequest(
            Request.Method.PUT,
            WebData.URL + path,
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