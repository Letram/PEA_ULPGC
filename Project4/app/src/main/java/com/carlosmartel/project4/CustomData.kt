package com.carlosmartel.project4

class CustomData {
    companion object {

        //attributes
        const val EXTRA_NAME: String = "EXTRA_NAME"
        const val EXTRA_ADDRESS: String = "EXTRA_ADDRESS"
        const val EXTRA_ID: String = "EXTRA_ID"
        const val EXTRA_DESCRIPTION: String = "EXTRA_DESCRIPTION"
        const val EXTRA_PRICE:String = "EXTRA_PRICE"
        const val EXTRA_CUSTOMER: String = "EXTRA_CUSTOMER"
        const val EXTRA_PRODUCT: String = "EXTRA_PRODUCT"
        const val EXTRA_ORDER: String = "EXTRA_ORDER"
        const val EXTRA_ORDER_UID: String = "EXTRA_ORDER_UID"
        const val EXTRA_ORDER_PID: String = "EXTRA_ORDER_PID"
        const val EXTRA_ORDER_QTY: String = "EXTRA_ORDER_QTY"
        const val EXTRA_ORDER_DATE: String  = "EXTRA_ORDER_DATE"
        const val EXTRA_ORDER_CODE: String = "EXTRA_ORDER_CODE"
        const val EXTRA_ORDER_ID: String = "EXTRA_ORDER_ID"
        const val EXTRA_ORDER_PREV: String = "EXTRA_ORDER_PREV"

        const val CURRENT_TAB: String = "CURRENT_TAB"

        //requests
        const val ADD_CUSTOMER_REQ: Int = 1
        const val EDIT_CUSTOMER_REQ: Int = 2
        const val DEL_CUSTOMER_REQ: Int = 3
        const val ADD_PRODUCT_REQ: Int = 4
        const val EDIT_PRODUCT_REQ: Int = 5
        const val DEL_PRODUCT_REQ: Int = 6
        const val SELECT_CUSTOMER_REQ: Int = 7
        const val SELECT_PRODUCT_REQ: Int = 8
        const val ADD_ORDER_REQ: Int = 9
        const val EDIT_ORDER_REQ: Int = 10
        const val DEL_ORDER_REQ: Int = 11

        const val BACK_PRESSED: Int = 99
    }
}