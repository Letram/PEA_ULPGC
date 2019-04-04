package com.carlosmartel.project3.AddEditOrderActivity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import com.carlosmartel.project3.CustomData
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.entities.Customer
import com.carlosmartel.project3.data.entities.Product
import com.carlosmartel.project3.selectCustomerActivity.SelectCustomerActivity
import java.text.DateFormat
import java.util.*


class AddEditOrderActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var datePickerText: TextView

    private lateinit var currentCustomer: Customer
    private lateinit var currentProduct: Product

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.time)

        datePickerText.text = currentDateString
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_order)
        datePickerText = findViewById(R.id.date_text)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CustomData.SELECT_CUSTOMER_REQ && resultCode == Activity.RESULT_OK){
            if(data != null){
                currentCustomer = data.getParcelableExtra(CustomData.EXTRA_CUSTOMER)
            }
        }
    }

    fun openDatepicker(view: View) {
        val datePicker = DatepickerDialog()
        datePicker.show(supportFragmentManager, "DatePicker")
    }

    fun openCustomerSelector(view:View){
        val intent = Intent(this, SelectCustomerActivity::class.java)
        startActivityForResult(intent, CustomData.SELECT_CUSTOMER_REQ)
    }
}
