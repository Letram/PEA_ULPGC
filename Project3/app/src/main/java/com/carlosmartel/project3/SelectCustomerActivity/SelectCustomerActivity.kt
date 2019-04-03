package com.carlosmartel.project3.SelectCustomerActivity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.carlosmartel.project3.MainActivity
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.entities.Customer
import com.carlosmartel.project3.data.repositories.CustomerRepository
import com.carlosmartel.project3.fragments.customer.CustomerViewModel

class SelectCustomerActivity : AppCompatActivity() {

    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: SelectCustomerListAdapter

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
            if(it != null)
                recyclerAdapter.setCustomers(it)
        })
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


}
