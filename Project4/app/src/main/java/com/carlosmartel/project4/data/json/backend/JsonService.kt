package com.carlosmartel.project4.data.json.backend

import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.carlosmartel.project4.data.json.constants.JsonData
import org.json.JSONObject

class JsonService : ServiceInterface {

    val TAG = JsonService::class.java.simpleName

    override fun insertCustomer(path: String, params: JSONObject) {
        val jsonObjReq: JsonObjectRequest = JsonObjectRequest(Request.Method.POST, JsonData.URL + path, params)
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun getCustomers(path: String, params: JSONObject?) {
        val jsonObjReq = JsonObjectRequest(Request.Method.GET, JsonData.URL + path, params)
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun getOrders(path: String, params: JSONObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}