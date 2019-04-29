package com.carlosmartel.project4bis2.data.webServices

import android.content.Context
import android.net.ConnectivityManager

class WebData {
    companion object {
        const val URL: String = "http://appstip.iatext.ulpgc.es/ventas/server.php?"
        const val URL_SOAP: String = "http://appstip.iatext.ulpgc.es/ventas/server.php"
        const val URN: String =  "urn://ulpgc.masterii.moviles"

        //Customer Queries
        const val GET_CUSTOMERS = "QueryCustomers"
        const val INSERT_CUSTOMER = "InsertCustomer"
        const val DELETE_CUSTOMER = "DeleteCustomer"
        const val UPDATE_CUSTOMER = "UpdateCustomer"

        //Customer data
        const val CUSTOMER_NAME = "name"
        const val CUSTOMER_ADDRESS = "address"
        const val CUSTOMER_ID = "IDCustomer"

        //Product Queries
        const val GET_PRODUCTS = "QueryProducts"
        const val INSERT_PRODUCT = "InsertProduct"
        const val DELETE_PRODUCT = "DeleteProduct"
        const val UPDATE_PRODUCT = "UpdateProduct"

        //Product data
        const val PRODUCT_NAME = "name"
        const val PRODUCT_DESCRIPTION = "description"
        const val PRODUCT_PRICE = "price"
        const val PRODUCT_ID = "IDProduct"

        //Order Queries
        const val GET_ORDERS = "QueryOrders"
        const val INSERT_ORDER = "InsertOrder"
        const val DELETE_ORDER = "DeleteOrder"
        const val UPDATE_ORDER = "UpdateOrder"

        //Order data
        const val ORDER_CODE = "code"
        const val ORDER_QTY = "quantity"
        const val ORDER_DATE = "date"
        const val ORDER_ID = "IDOrder"
        const val ORDER_CUSTOMER_NAME = "customerName"
        const val ORDER_PRODUCT_NAME = "productName"
    }

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}


