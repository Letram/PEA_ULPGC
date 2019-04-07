package com.carlosmartel.project3

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.carlosmartel.project3.data.entities.Customer
import com.carlosmartel.project3.fragments.customer.CustomerViewModel

class AddEditCustomerActivity : AppCompatActivity() {

    private lateinit var customerNameEdit: EditText
    private lateinit var customerAddressEdit: EditText
    private lateinit var customersWithOrders: List<Int>
    private var prevCustomer: Customer? = null
    private lateinit var customerViewModel: CustomerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        customerViewModel = ViewModelProviders.of(this).get(CustomerViewModel::class.java)

        customerNameEdit = this.findViewById(R.id.name_edit)
        customerAddressEdit = this.findViewById(R.id.address_edit)

        //first time coming to this view (not recreating)
        if (intent.hasExtra(CustomData.EXTRA_NAME)) {
            title = intent.getStringExtra(CustomData.EXTRA_NAME)
            customerNameEdit.setText(intent.getStringExtra(CustomData.EXTRA_NAME))
            customerAddressEdit.setText(intent.getStringExtra(CustomData.EXTRA_ADDRESS))
            prevCustomer = intent.getParcelableExtra(CustomData.EXTRA_CUSTOMER)
            customerViewModel.getAllCustomersWithOrders().observe(this, Observer {
                if (it != null)
                    customersWithOrders = it
            })
        } else
            title = getString(R.string.add_customer_title)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //maybe here we can use the layout of the menu we want depending on whether we created this activity via fab or clicking on an existing customer

        //default add_customer_menu, we could change this to another one if something in the intent says so.
        if (prevCustomer != null) menuInflater.inflate(R.menu.edit_menu, menu)
        else menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.save -> {
                saveCustomer()
                true
            }
            R.id.delete -> {
                deleteCustomer()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun deleteCustomer() {
        if (customersWithOrders.contains(intent.getIntExtra(CustomData.EXTRA_ID, -1)))
            openDialog()
        else {
            val deleteCustomer = prevCustomer!!
            val dialog = AlertDialog.Builder(this@AddEditCustomerActivity)
            dialog.setTitle(R.string.dialog_customer_title)
            dialog.setMessage(R.string.dialog_customer_confirmation)
            dialog.setPositiveButton(R.string.dialog_delete) { _, _ ->
                ViewModelProviders.of(this).get(CustomerViewModel::class.java).delete(deleteCustomer)
                setResult(CustomData.DEL_CUSTOMER_REQ)
                finish()
            }
            dialog.setNegativeButton(R.string.dialog_cancel) { _, _ -> }
            dialog.show()
        }
    }

    private fun openDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.dialog_customer_title)
        dialog.setMessage(R.string.dialog_cant_delete_product)
        dialog.setPositiveButton(R.string.OK) { _, _ -> }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun saveCustomer() {
        val name: String = customerNameEdit.text.toString()
        val address: String = customerAddressEdit.text.toString()

        if (name.trim().isEmpty() || address.trim().isEmpty()) {
            Toast.makeText(this, R.string.save_customer_toast, Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent()
        data.putExtra(CustomData.EXTRA_NAME, name)
        data.putExtra(CustomData.EXTRA_ADDRESS, address)


        if (prevCustomer != null) {
            data.putExtra(CustomData.EXTRA_ID, prevCustomer?.u_id)
            data.putExtra(CustomData.EXTRA_CUSTOMER, prevCustomer)
        }
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if (outState != null) {
            outState.putString(CustomData.EXTRA_NAME, customerNameEdit.text.toString())
            outState.putString(CustomData.EXTRA_ADDRESS, customerAddressEdit.text.toString())
            outState.putParcelable(CustomData.EXTRA_CUSTOMER, prevCustomer)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            prevCustomer = savedInstanceState.getParcelable(CustomData.EXTRA_CUSTOMER)!!
            customerNameEdit.setText(savedInstanceState.getString(CustomData.EXTRA_NAME))
            customerAddressEdit.setText(savedInstanceState.getString(CustomData.EXTRA_ADDRESS))
        }
        super.onRestoreInstanceState(savedInstanceState)
    }
}
