package com.carlosmartel.project3

class CustomData {
    companion object {

        //attributes
        const val EXTRA_NAME: String = "EXTRA_NAME"
        const val EXTRA_ADDRESS: String = "EXTRA_ADDRESS"
        const val EXTRA_ID: String = "EXTRA_ID"
        const val EXTRA_DESCRIPTION: String = "EXTRA_DESCRIPTION"
        const val EXTRA_PRICE:String = "EXTRA_PRICE"

        //requests
        const val ADD_CUSTOMER_REQ: Int = 1
        const val EDIT_CUSTOMER_REQ: Int = 2
        const val DEL_CUSTOMER_REQ: Int = 3
        const val ADD_PRODUCT_REQ: Int = 4
        const val EDIT_PRODUCT_REQ: Int = 5
        const val DEL_PRODUCT_REQ: Int = 6
    }
}