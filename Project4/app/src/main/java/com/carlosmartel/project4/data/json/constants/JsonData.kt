package com.carlosmartel.project4.data.json.constants

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

        //Order Queries
        const val GET_ORDERS = "?QueryOrders"
        const val INSERT_ORDER = "?InsertOrder"

    }
}


