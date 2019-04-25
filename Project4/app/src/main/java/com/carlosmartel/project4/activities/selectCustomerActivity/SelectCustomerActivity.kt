package com.carlosmartel.project4.activities.selectCustomerActivity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.carlosmartel.project4.CustomData
import com.carlosmartel.project4.R
import com.carlosmartel.project4.data.entities.Customer
import com.carlosmartel.project4.fragments.customer.CustomerViewModel

class SelectCustomerActivity : AppCompatActivity() {

    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: SelectCustomerListAdapter

    private var customerSelected: Customer? = null

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

        customerViewModel.getAllCustomersJSON().observe(this, Observer {
            if (it != null) {
                if (it.isEmpty()) {
                    openDialog()
                } else {
                    recyclerAdapter.setCustomers(it)
                    recyclerAdapter.notifyDataSetChanged()
                }
            }
        })

        recyclerAdapter.setOnCustomerClickListener(object :
            SelectCustomerListAdapter.OnCustomerClickListener {
            override fun onCustomerClick(customer: Customer) {
                customerSelected = customer
                recyclerAdapter.setCustomerSelected(customer)
            }

        })
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = resources.getString(R.string.select_customer)
        if (intent.hasExtra(CustomData.EXTRA_CUSTOMER)) {
            customerSelected = intent.getParcelableExtra(CustomData.EXTRA_CUSTOMER)
            recyclerAdapter.setCustomerSelected(customerSelected!!)
        }
    }

    private fun openDialog(){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.select_customer)
        dialog.setMessage(R.string.dialog_select_info)
        dialog.setPositiveButton(R.string.OK){ _, _ -> finish()}
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.select_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.select -> {
                selectCustomer()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun selectCustomer() {
        if(customerSelected == null){
            Toast.makeText(this, R.string.select_customer_pls, Toast.LENGTH_SHORT).show()
            return
        }
        val data = Intent()
        data.putExtra(CustomData.EXTRA_CUSTOMER, customerSelected!!)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}
