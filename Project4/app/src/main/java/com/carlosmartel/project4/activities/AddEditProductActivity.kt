package com.carlosmartel.project4.activities

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
import com.carlosmartel.project4.CustomData
import com.carlosmartel.project4.R
import com.carlosmartel.project4.data.entities.Product
import com.carlosmartel.project4.data.pojo.InflatedOrderJson
import com.carlosmartel.project4.fragments.order.OrderViewModel
import com.carlosmartel.project4.fragments.product.ProductViewModel

class AddEditProductActivity : AppCompatActivity() {

    private lateinit var productName: EditText
    private lateinit var productDescription: EditText
    private lateinit var productPrice: EditText
    private lateinit var productViewModel: ProductViewModel

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var orders: List<InflatedOrderJson>
    private lateinit var products: List<Product>


    private var prevProduct: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_product)

        productName = findViewById(R.id.product_name)
        productDescription = findViewById(R.id.product_description)
        productPrice = findViewById(R.id.product_price)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel(application)::class.java)

        productViewModel.getAllProductsJSON().observe(this, Observer {
            if (it != null)
                products = it
        })
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel(application)::class.java)
        orderViewModel.getAllInflatedOrdersJSON().observe(this, Observer {
            if (it != null) {
                orders = it
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(CustomData.EXTRA_NAME)) {
            title = intent.getStringExtra(CustomData.EXTRA_NAME)
            productName.setText(intent.getStringExtra(CustomData.EXTRA_NAME))
            productDescription.setText(intent.getStringExtra(CustomData.EXTRA_DESCRIPTION))
            productPrice.setText(intent.getFloatExtra(CustomData.EXTRA_PRICE, -1F).toString())
            prevProduct = intent.getParcelableExtra(CustomData.EXTRA_PRODUCT)
        } else
            title = getString(R.string.add_product_title)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        if (prevProduct != null) menuInflater.inflate(R.menu.edit_menu, menu)
        else menuInflater.inflate(R.menu.add_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.save -> {
                saveProduct()
                true
            }
            R.id.delete -> {
                deleteProduct()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        setResult(CustomData.BACK_PRESSED)
        finish()
        return true
    }

    private fun deleteProduct() {
        if (hasOrders(prevProduct))
            openDialog()
        else {
            val deleteProduct = prevProduct!!
            val dialog = AlertDialog.Builder(this@AddEditProductActivity)
            dialog.setTitle(R.string.dialog_product_title)
            dialog.setMessage(R.string.dialog_product_confirmation)
            dialog.setPositiveButton(R.string.dialog_delete) { _, _ ->
                ViewModelProviders.of(this).get(ProductViewModel::class.java).delete(deleteProduct)
                setResult(CustomData.DEL_PRODUCT_REQ)
                finish()
            }
            dialog.setNegativeButton(R.string.dialog_cancel) { _, _ -> }
            dialog.show()
        }
    }

    private fun hasOrders(prevProduct: Product?): Boolean {
        for (infOrder in orders) {
            if (infOrder.order.productID == prevProduct!!.p_id) {
                return true
            }
        }
        return false
    }

    private fun openDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.dialog_product_title)
        dialog.setMessage(R.string.dialog_cant_delete_product)
        dialog.setPositiveButton(R.string.OK) { _, _ -> }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun saveProduct() {
        val name = productName.text.toString()
        val description = productDescription.text.toString()
        val price = productPrice.text.toString()

        if (!valid(name, description, price)) {
            Toast.makeText(this, R.string.save_product_toast, Toast.LENGTH_SHORT).show()
            return
        }
        if (isRepeated(name)) {
            Toast.makeText(this, R.string.another_name, Toast.LENGTH_SHORT).show()
            return
        }
        val data = Intent()
        data.putExtra(CustomData.EXTRA_NAME, name)
        data.putExtra(CustomData.EXTRA_DESCRIPTION, description)
        data.putExtra(CustomData.EXTRA_PRICE, price.toFloat())

        if (prevProduct != null) {
            data.putExtra(CustomData.EXTRA_ID, intent.getIntExtra(CustomData.EXTRA_ID, -1))
            data.putExtra(CustomData.EXTRA_PRODUCT, intent.getParcelableExtra(CustomData.EXTRA_PRODUCT) as Product)
        }
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private fun isRepeated(name: String): Boolean {
        for (product in products) {
            if (product.p_name == name) {
                return true
            }
        }
        return false
    }

    private fun valid(name: String, description: String, price: String): Boolean {
        val res = name.trim().isEmpty() || description.trim().isEmpty() || price.trim().isEmpty()
        return !res
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putParcelable(CustomData.EXTRA_PRODUCT, prevProduct)
            putString(CustomData.EXTRA_NAME, productName.text.toString())
            putString(CustomData.EXTRA_DESCRIPTION, productDescription.text.toString())
            putFloat(CustomData.EXTRA_PRICE, productPrice.text.toString().toFloat())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            prevProduct = savedInstanceState.getParcelable(CustomData.EXTRA_PRODUCT)
            productName.setText(savedInstanceState.getString(CustomData.EXTRA_NAME))
            productDescription.setText(savedInstanceState.getString(CustomData.EXTRA_DESCRIPTION))
            productPrice.setText(savedInstanceState.getFloat(CustomData.EXTRA_PRICE).toString())
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

}
