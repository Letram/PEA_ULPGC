package com.carlosmartel.project3.AddEditOrderActivity

import android.app.Activity
import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import com.carlosmartel.project3.CustomData
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.entities.Customer
import com.carlosmartel.project3.data.entities.Order
import com.carlosmartel.project3.data.entities.Product
import com.carlosmartel.project3.fragments.order.OrderViewModel
import com.carlosmartel.project3.selectCustomerActivity.SelectCustomerActivity
import com.carlosmartel.project3.selectProductActivity.SelectProductActivity
import kotlinx.android.synthetic.main.activity_add_edit_order.*
import java.text.DateFormat
import java.util.*


class AddEditOrderActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var datePickerText: TextView
    private lateinit var customerNameText: TextView
    private lateinit var productNameText: TextView
    private lateinit var productQuantityText: TextView
    private lateinit var productPriceText: TextView
    private lateinit var orderCodeText: TextView

    private var currentCustomer: Customer? = null
    private var currentProduct: Product? = null
    private var currentOrder: Order? = null

    private var quantity: Int = 0
    private var price: Float = 0F
    private lateinit var date: Date

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val currentDateString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.time)

        datePickerText.text = currentDateString
        date = c.time
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_order)

        datePickerText = findViewById(R.id.date_text)
        customerNameText = findViewById(R.id.product_item_name)
        productNameText = findViewById(R.id.product_name)
        productQuantityText = findViewById(R.id.qty_text)
        productPriceText = findViewById(R.id.price_text)
        orderCodeText = findViewById(R.id.code_text)

        productQuantityText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(currentProduct != null){
                    quantity = if(s.isNullOrEmpty()) 0
                    else s.toString().toInt()
                    setPriceWithQuantity()
                }
            }

        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(intent.hasExtra(CustomData.EXTRA_ORDER)){
            currentProduct = intent.getParcelableExtra(CustomData.EXTRA_PRODUCT)
            currentCustomer = intent.getParcelableExtra(CustomData.EXTRA_CUSTOMER)
            currentOrder = intent.getParcelableExtra(CustomData.EXTRA_ORDER)

            title = currentOrder?.code
            datePickerText.text = DateFormat.getDateInstance(DateFormat.MEDIUM).format(currentOrder?.date)
            date = currentOrder?.date!!
            orderCodeText.text = currentOrder?.code
            productNameText.text = currentProduct?.p_name
            customerNameText.text = currentCustomer?.c_name
            productQuantityText.text = currentOrder?.quantity.toString()
            quantity = currentOrder?.quantity!!.toInt()
            setPriceWithQuantity()

        }else title = getString(R.string.add_order_title)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(intent.hasExtra(CustomData.EXTRA_ORDER)) menuInflater.inflate(R.menu.edit_menu, menu)
        else menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.save -> {
                saveOrder()
                true
            }

            R.id.delete -> {
                deleteOrder()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun deleteOrder() {
        val deleteOrder = intent.getParcelableExtra<Order>(CustomData.EXTRA_ORDER)

        val dialog = AlertDialog.Builder(this@AddEditOrderActivity)
        dialog.setTitle(R.string.dialog_order_title)
        dialog.setMessage(R.string.dialog_order_confirmation)
        dialog.setPositiveButton(R.string.dialog_delete){ _, _ ->
            ViewModelProviders.of(this).get(OrderViewModel::class.java).delete(deleteOrder)
            setResult(CustomData.DEL_ORDER_REQ)
            finish()
        }
        dialog.setNegativeButton(R.string.dialog_cancel){_,_ ->}
        dialog.show()
    }

    private fun saveOrder() {
        val uid: Int = currentCustomer?.u_id!!
        val pid: Int = currentProduct?.p_id!!
        val code: String = orderCodeText.text.toString()

        val data = Intent()
        data.putExtra(CustomData.EXTRA_ORDER_UID, uid)
        data.putExtra(CustomData.EXTRA_ORDER_PID, pid)
        data.putExtra(CustomData.EXTRA_ORDER_QTY, quantity)
        data.putExtra(CustomData.EXTRA_ORDER_DATE, date)
        data.putExtra(CustomData.EXTRA_ORDER_CODE, code)

        if(intent.hasExtra(CustomData.EXTRA_ORDER))
            data.putExtra(CustomData.EXTRA_ORDER_ID, currentOrder?.orderID)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private fun setPriceWithQuantity() {
        price = quantity * currentProduct?.price!!
        productPriceText.text = price.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CustomData.SELECT_CUSTOMER_REQ && resultCode == Activity.RESULT_OK){
            if(data != null){
                currentCustomer = data.getParcelableExtra(CustomData.EXTRA_CUSTOMER)
                customerNameText.text = currentCustomer?.c_name
            }
        }else if(requestCode == CustomData.SELECT_PRODUCT_REQ && resultCode == Activity.RESULT_OK){
            if(data != null){
                currentProduct = data.getParcelableExtra(CustomData.EXTRA_PRODUCT)
                productNameText.text = currentProduct?.p_name
                setPriceWithQuantity()
            }
        }
    }

    fun openDatepicker(view: View) {
        val datePicker = DatepickerDialog()
        datePicker.show(supportFragmentManager, "DatePicker")
    }

    fun openCustomerSelector(view:View){
        val intent = Intent(this, SelectCustomerActivity::class.java)
        if(currentCustomer != null)
            intent.putExtra(CustomData.EXTRA_CUSTOMER, currentCustomer!!)
        startActivityForResult(intent, CustomData.SELECT_CUSTOMER_REQ)
    }

    fun openProductSelector(view: View){
        val intent = Intent(this, SelectProductActivity::class.java)
        if(currentProduct != null)
            intent.putExtra(CustomData.EXTRA_PRODUCT, currentProduct!!)
        startActivityForResult(intent, CustomData.SELECT_PRODUCT_REQ)
    }
}
