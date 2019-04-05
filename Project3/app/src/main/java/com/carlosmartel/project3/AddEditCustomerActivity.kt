package com.carlosmartel.project3

import android.app.Activity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)

        customerNameEdit = this.findViewById(R.id.name_edit)
        customerAddressEdit = this.findViewById(R.id.address_edit)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //get customer c_name from intent when creating activity from clicking on a customer from the list
        //we are now just putting a default title when adding a customer using the fab

        if (intent.hasExtra(CustomData.EXTRA_NAME)) {
            title = intent.getStringExtra(CustomData.EXTRA_NAME)
            customerNameEdit.setText(intent.getStringExtra(CustomData.EXTRA_NAME))
            customerAddressEdit.setText(intent.getStringExtra(CustomData.EXTRA_ADDRESS))
        } else
            title = if (!intent.hasExtra(CustomData.EXTRA_ID)) getString(
                R.string.add_customer_title,
                "Add customer"
            ) else intent.getStringExtra(CustomData.EXTRA_NAME)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //maybe here we can use the layout of the menu we want depending on whether we created this activity via fab or clicking on an existing customer

        //default add_customer_menu, we could change this to another one if something in the intent says so.
        if (intent.hasExtra(CustomData.EXTRA_ID)) menuInflater.inflate(R.menu.edit_menu, menu)
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
        val deleteCustomer = Customer(
            intent.getIntExtra(CustomData.EXTRA_ID, -1),
            intent.getStringExtra(CustomData.EXTRA_ADDRESS),
            intent.getStringExtra(CustomData.EXTRA_NAME)
        )
        val dialog = AlertDialog.Builder(this@AddEditCustomerActivity)
        dialog.setTitle(R.string.dialog_title)
        dialog.setMessage(R.string.dialog_customer_confirmation)
        dialog.setPositiveButton(R.string.dialog_delete){ _, _ ->
            ViewModelProviders.of(this).get(CustomerViewModel::class.java).delete(deleteCustomer)
            setResult(CustomData.DEL_CUSTOMER_REQ)
            finish()
        }
        dialog.setNegativeButton(R.string.dialog_cancel){_,_ ->}
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


        if (intent.hasExtra(CustomData.EXTRA_ID) && intent.getIntExtra(CustomData.EXTRA_ID, -1) != -1)
            data.putExtra(CustomData.EXTRA_ID, intent.getIntExtra(CustomData.EXTRA_ID, -1))
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}
