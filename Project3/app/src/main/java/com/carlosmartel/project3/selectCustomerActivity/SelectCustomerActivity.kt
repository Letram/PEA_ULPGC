package com.carlosmartel.project3.selectCustomerActivity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.carlosmartel.project3.CustomData
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.entities.Customer
import com.carlosmartel.project3.fragments.customer.CustomerViewModel

class SelectCustomerActivity : AppCompatActivity() {

    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: SelectCustomerListAdapter

    private lateinit var customerSelected: Customer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_customer)
        recyclerView = findViewById(R.id.select_customer_rv)
        recyclerAdapter = SelectCustomerListAdapter()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
        customerViewModel = ViewModelProviders.of(this).get(CustomerViewModel::class.java)

        customerViewModel.getAllCustomers().observe(this, Observer {
            if(it != null){
                recyclerAdapter.setCustomers(it)
                recyclerAdapter.notifyDataSetChanged()
            }
        })

        recyclerAdapter.setOnCustomerClickListener(object: SelectCustomerListAdapter.OnCustomerClickListener{
            override fun onCustomerClick(customer: Customer) {
                customerSelected = customer
                recyclerAdapter.setCustomerSelected(customer)
            }

        })
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = resources.getString(R.string.select_customer)
        if(intent.hasExtra(CustomData.EXTRA_CUSTOMER)){
            customerSelected = intent.getParcelableExtra(CustomData.EXTRA_CUSTOMER)
            recyclerAdapter.setCustomerSelected(customerSelected)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.select_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.select -> {
                selectCustomer()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun selectCustomer() {
        val data = Intent()
        data.putExtra(CustomData.EXTRA_CUSTOMER, customerSelected)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}
