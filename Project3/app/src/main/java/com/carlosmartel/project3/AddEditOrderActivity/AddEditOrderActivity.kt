package com.carlosmartel.project3.AddEditOrderActivity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import com.carlosmartel.project3.R
import com.carlosmartel.project3.selectCustomerActivity.SelectCustomerActivity
import java.text.DateFormat
import java.util.*


class AddEditOrderActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.time)

        datePickerText.text = currentDateString
    }

    private lateinit var datePickerText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_order)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        datePickerText = findViewById(R.id.date_text)
    }

    fun openDatepicker() {
        val datePicker = DatepickerDialog()
        datePicker.show(supportFragmentManager, "DatePicker")
    }

    fun openCustomerSelector(view:View){
        val intent = Intent(this, SelectCustomerActivity::class.java)
        startActivity(intent)
    }
}
