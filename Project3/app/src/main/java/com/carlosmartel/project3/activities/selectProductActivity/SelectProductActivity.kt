package com.carlosmartel.project3.activities.selectProductActivity

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
import com.carlosmartel.project3.CustomData
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.entities.Customer
import com.carlosmartel.project3.data.entities.Order
import com.carlosmartel.project3.data.entities.Product
import com.carlosmartel.project3.fragments.product.ProductViewModel

class SelectProductActivity : AppCompatActivity() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: SelectProductListAdapter

    private var productSelected: Product? = null
    private var order: Order?= null
    private var orderCustomer: Customer?= null
    private var prevOrder: Order?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_product)
        recyclerView = findViewById(R.id.select_product_rv)
        recyclerAdapter = SelectProductListAdapter()
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        productViewModel.getAllProducts().observe(this, Observer {
            if (it != null) {
                if (it.isEmpty()) openDialog()
                else {
                    recyclerAdapter.setProducts(it)
                    recyclerAdapter.notifyDataSetChanged()
                }
            }
        })

        recyclerAdapter.setOnProductClickListener(object :
            SelectProductListAdapter.OnProductClickListener {
            override fun onProductClick(product: Product) {
                productSelected = product
                recyclerAdapter.setProductSelected(product)
            }
        })
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = resources.getString(R.string.select_product)
        if (intent.hasExtra(CustomData.EXTRA_PRODUCT)) {
            productSelected = intent.getParcelableExtra(CustomData.EXTRA_PRODUCT)
            recyclerAdapter.setProductSelected(productSelected!!)
        }
        order = intent.getParcelableExtra(CustomData.EXTRA_ORDER)
        prevOrder = intent.getParcelableExtra(CustomData.EXTRA_ORDER_PREV)
        orderCustomer = intent.getParcelableExtra(CustomData.EXTRA_CUSTOMER)
    }

    private fun openDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.select_product)
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
                selectProduct()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Finishes the activity and passes the information to the parent so it can add the product to the order.
     */
    private fun selectProduct() {
        if(productSelected == null){
            Toast.makeText(this, R.string.select_product_pls, Toast.LENGTH_SHORT).show()
            return
        }
        val data = Intent()
        data.putExtra(CustomData.EXTRA_PRODUCT, productSelected!!)
        data.putExtra(CustomData.EXTRA_CUSTOMER, orderCustomer)
        data.putExtra(CustomData.EXTRA_ORDER, order)
        data.putExtra(CustomData.EXTRA_ORDER_PREV, prevOrder)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putParcelable(CustomData.EXTRA_PRODUCT, productSelected)
            putParcelable(CustomData.EXTRA_CUSTOMER, orderCustomer)
            putParcelable(CustomData.EXTRA_ORDER, order)
            putParcelable(CustomData.EXTRA_ORDER_PREV, prevOrder)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            productSelected = savedInstanceState.getParcelable(CustomData.EXTRA_PRODUCT)
            if(productSelected != null){
                recyclerAdapter.setProductSelected(productSelected!!)
            }
            orderCustomer = savedInstanceState.getParcelable(CustomData.EXTRA_CUSTOMER)
            order = savedInstanceState.getParcelable(CustomData.EXTRA_ORDER)
            prevOrder = savedInstanceState.getParcelable(CustomData.EXTRA_ORDER_PREV)
        }
        super.onRestoreInstanceState(savedInstanceState)
    }
}
