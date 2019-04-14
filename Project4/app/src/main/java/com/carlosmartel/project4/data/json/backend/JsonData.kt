package com.carlosmartel.project4.data.json.backend

class JsonData {
    companion object {
        const val URL: String = "http://appstip.iatext.ulpgc.es/ventas/server.php"

        //Customer Queries
        const val GET_CUSTOMERS = "?QueryCustomers"
        const val INSERT_CUSTOMER = "?InsertCustomer"
        const val DELETE_CUSTOMER = "?DeleteCustomer"
        const val UPDATE_CUSTOMER = "?UpdateCustomer"

        //Customer data
        const val CUSTOMER_NAME: String = "name"
        const val CUSTOMER_ADDRESS: String = "address"
        const val CUSTOMER_ID: String = "IDCustomer"

        //Product Queries
        const val GET_PRODUCTS = "?QueryProducts"
        const val INSERT_PRODUCT = "?InsertProduct"
        const val DELETE_PRODUCT = "?DeleteProduct"
        const val UPDATE_PRODUCT = "?UpdateProduct"

        //Product data
        const val PRODUCT_NAME: String = "name"
        const val PRODUCT_DESCRIPTION: String = "description"
        const val PRODUCT_PRICE: String = "price"
        const val PRODUCT_ID: String = "IDProduct"

        //Order Queries
        const val GET_ORDERS = "?QueryOrders"
        const val INSERT_ORDER = "?InsertOrder"

    }
}


