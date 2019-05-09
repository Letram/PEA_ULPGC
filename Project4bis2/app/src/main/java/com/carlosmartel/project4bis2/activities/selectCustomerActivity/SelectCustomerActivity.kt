package com.carlosmartel.project4bis2.activities.selectCustomerActivity

import android.app.Activity
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
import com.carlosmartel.project4bis2.CustomData
import com.carlosmartel.project4bis2.R
import com.carlosmartel.project4bis2.SelectorData
import com.carlosmartel.project4bis2.data.entities.Customer
import com.carlosmartel.project4bis2.data.entities.Order
import com.carlosmartel.project4bis2.data.entities.Product
import com.carlosmartel.project4bis2.fragments.customer.CustomerViewModel

class SelectCustomerActivity : AppCompatActivity() {

    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: SelectCustomerListAdapter

    private var customerSelected: Customer? = null
    private var order: Order? = null
    private var orderProduct: Product? = null
    private var prevOrder: Order? = null
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

        //todo  redo this. Use of viewmodel or not?
        recyclerAdapter.setCustomers(SelectorData.customers)
        if (SelectorData.customers.isEmpty()) openDialog()
        /*
        customerViewModel.getAllCustomersSOAP().observe(this, Observer {
            if (it != null) {
                if (it.isEmpty()) {
                    openDialog()
                } else {
                    recyclerAdapter.setCustomers(it)
                    recyclerAdapter.notifyDataSetChanged()
                }
            }
        })
*/
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
        order = intent.getParcelableExtra(CustomData.EXTRA_ORDER)
        prevOrder = intent.getParcelableExtra(CustomData.EXTRA_ORDER_PREV)
        orderProduct = intent.getParcelableExtra(CustomData.EXTRA_PRODUCT)
    }

    private fun openDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.select_customer)
        dialog.setMessage(R.string.dialog_select_info)
        dialog.setPositiveButton(R.string.OK) { _, _ -> finish() }
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

    override fun onSupportNavigateUp(): Boolean {
        val data = Intent()
        if (customerSelected != null)
            data.putExtra(CustomData.EXTRA_CUSTOMER, customerSelected!!)
        data.putExtra(CustomData.EXTRA_PRODUCT, orderProduct)
        data.putExtra(CustomData.EXTRA_ORDER, order)
        data.putExtra(CustomData.EXTRA_ORDER_PREV, prevOrder)
        setResult(CustomData.BACK_PRESSED, data)
        finish()
        return true
    }

    /**
     * Finishes the activity and passes the information to the parent to add the customer into the order.
     */
    private fun selectCustomer() {
        if (customerSelected == null) {
            Toast.makeText(this, R.string.select_customer_pls, Toast.LENGTH_SHORT).show()
            return
        }
        val data = Intent()
        data.putExtra(CustomData.EXTRA_CUSTOMER, customerSelected!!)
        data.putExtra(CustomData.EXTRA_PRODUCT, orderProduct)
        data.putExtra(CustomData.EXTRA_ORDER, order)
        data.putExtra(CustomData.EXTRA_ORDER_PREV, prevOrder)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putParcelable(CustomData.EXTRA_CUSTOMER, customerSelected)
            putParcelable(CustomData.EXTRA_PRODUCT, orderProduct)
            putParcelable(CustomData.EXTRA_ORDER, order)
            putParcelable(CustomData.EXTRA_ORDER_PREV, prevOrder)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            customerSelected = savedInstanceState.getParcelable(CustomData.EXTRA_CUSTOMER)
            if (customerSelected != null) {
                recyclerAdapter.setCustomerSelected(customerSelected!!)
            }
            orderProduct = savedInstanceState.getParcelable(CustomData.EXTRA_PRODUCT)
            order = savedInstanceState.getParcelable(CustomData.EXTRA_ORDER)
            prevOrder = savedInstanceState.getParcelable(CustomData.EXTRA_ORDER_PREV)
        }
        super.onRestoreInstanceState(savedInstanceState)
    }
}
