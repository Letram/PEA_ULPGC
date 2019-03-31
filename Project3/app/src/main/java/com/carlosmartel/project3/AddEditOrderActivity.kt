package com.carlosmartel.project3

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class AddEditOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_order)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
