package com.carlosmartel.project3.selectCustomerActivity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.entities.Customer
import com.carlosmartel.project3.fragments.customer.CustomerViewModel
import java.util.*

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
                println(customerSelected.c_name)
            }

        })
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


}
