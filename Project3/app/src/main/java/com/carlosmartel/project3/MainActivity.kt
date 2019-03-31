package com.carlosmartel.project3

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.carlosmartel.project3.data.models.Customer
import com.carlosmartel.project3.data.models.Order
import com.carlosmartel.project3.data.models.Product
import com.carlosmartel.project3.fragments.MyFragmentPagerAdapter
import com.carlosmartel.project3.fragments.customer.CustomerFragment
import com.carlosmartel.project3.fragments.customer.CustomerViewModel
import com.carlosmartel.project3.fragments.order.OrderFragment
import com.carlosmartel.project3.fragments.product.ProductFragment
import com.carlosmartel.project3.fragments.product.ProductViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    OrderFragment.OnFragmentInteractionListener,
    ProductFragment.OnFragmentInteractionListener,
    CustomerFragment.OnFragmentInteractionListener {
    override fun updateOrder(order: Order) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteOrder(order: Order) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateProduct(product: Product) {
        val intent = Intent(this@MainActivity, AddEditProductActivity::class.java)
        intent.putExtra(CustomData.EXTRA_NAME, product.name)
        intent.putExtra(CustomData.EXTRA_DESCRIPTION, product.description)
        intent.putExtra(CustomData.EXTRA_PRICE, product.price)
        intent.putExtra(CustomData.EXTRA_ID, product.productID)
        startActivityForResult(intent, CustomData.EDIT_PRODUCT_REQ)
    }

    override fun deleteProduct(product: Product) {
        val undoProduct = Product(
            productID = product.productID,
            name = product.name,
            description = product.description,
            price = product.price
        )
        val dialog = AlertDialog.Builder(this@MainActivity)
        dialog.setTitle(R.string.dialog_title)
        dialog.setMessage(R.string.dialog_product_confirmation)
        dialog.setPositiveButton(R.string.dialog_delete) { _, _ ->
            ViewModelProviders.of(this).get(ProductViewModel::class.java).delete(product)
            Snackbar.make(viewPager, R.string.customer_deleted, Snackbar.LENGTH_SHORT)
                .setAction(R.string.snack_undo) {
                    ViewModelProviders.of(this).get(ProductViewModel::class.java).insert(undoProduct)
                }
                .show()
        }
        dialog.setNegativeButton(R.string.dialog_cancel) { _, _ -> }
        dialog.show()
    }

    override fun deleteCustomer(customer: Customer) {
        val undoCustomer = Customer(customer.uid, customer.address, customer.name)
        val dialog = AlertDialog.Builder(this@MainActivity)
        dialog.setTitle(R.string.dialog_title)
        dialog.setMessage(R.string.dialog_customer_confirmation)
        dialog.setPositiveButton(R.string.dialog_delete) { _, _ ->
            ViewModelProviders.of(this).get(CustomerViewModel::class.java).delete(customer)
            Snackbar.make(viewPager, R.string.customer_deleted, Snackbar.LENGTH_SHORT)
                .setAction(R.string.snack_undo) { _ ->
                    ViewModelProviders.of(this).get(CustomerViewModel::class.java).insert(undoCustomer)
                }
                .show()
        }
        dialog.setNegativeButton(R.string.dialog_cancel) { _, _ -> }
        dialog.show()
    }

    override fun updateCustomer(customer: Customer) {
        val intent = Intent(this@MainActivity, AddEditCustomerActivity::class.java)
        intent.putExtra(CustomData.EXTRA_NAME, customer.name)
        intent.putExtra(CustomData.EXTRA_ADDRESS, customer.address)
        intent.putExtra(CustomData.EXTRA_ID, customer.uid)
        startActivityForResult(intent, CustomData.EDIT_CUSTOMER_REQ)
    }

    private lateinit var viewPager: ViewPager
    private lateinit var mAdapter: MyFragmentPagerAdapter
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            when (viewPager.currentItem) {
                0 -> {
                    val intent = Intent(this@MainActivity, AddEditCustomerActivity::class.java)
                    startActivityForResult(intent, CustomData.ADD_CUSTOMER_REQ)
                }
                1 -> {
                    val intent = Intent(this@MainActivity, AddEditOrderActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    val intent = Intent(this@MainActivity, AddEditProductActivity::class.java)
                    startActivityForResult(intent, CustomData.ADD_PRODUCT_REQ)
                }
            }
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        mAdapter = MyFragmentPagerAdapter(supportFragmentManager)
        mAdapter.setTitles(
            arrayOf(
                getString(R.string.customer_tab),
                getString(R.string.order_tab),
                getString(R.string.product_tab)
            )
        )
        viewPager = findViewById(R.id.View_Pager)
        viewPager.adapter = mAdapter

        tabLayout = this.findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_customer -> {
                viewPager.currentItem = 0
            }
            R.id.nav_order -> {
                viewPager.currentItem = 1
            }
            R.id.nav_product -> {
                viewPager.currentItem = 2
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CustomData.ADD_CUSTOMER_REQ && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                val name: String = data.getStringExtra(CustomData.EXTRA_NAME)
                val address: String = data.getStringExtra(CustomData.EXTRA_ADDRESS)
                val newCustomer = Customer(address = address, name = name)
                ViewModelProviders.of(this).get(CustomerViewModel::class.java).insert(newCustomer)
                Toast.makeText(this, R.string.customer_saved_snack, Toast.LENGTH_SHORT).show()
            }

        } else if (requestCode == CustomData.EDIT_CUSTOMER_REQ && resultCode == Activity.RESULT_OK) {

            val uid: Int = data!!.getIntExtra(CustomData.EXTRA_ID, -1)
            if (uid == -1) {
                Toast.makeText(this, "Customer could not be updated", Toast.LENGTH_SHORT).show()
                return
            }
            val name = data.getStringExtra(CustomData.EXTRA_NAME)
            val address = data.getStringExtra(CustomData.EXTRA_ADDRESS)
            val customerAux = Customer(address = address, name = name)
            customerAux.uid = uid
            ViewModelProviders.of(this).get(CustomerViewModel::class.java).update(customerAux)
            Toast.makeText(this, "Customer updated", Toast.LENGTH_SHORT).show()

        } else if (requestCode == CustomData.ADD_PRODUCT_REQ && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                val name: String = data.getStringExtra(CustomData.EXTRA_NAME)
                val description: String = data.getStringExtra(CustomData.EXTRA_DESCRIPTION)
                val price: Float = data.getFloatExtra(CustomData.EXTRA_PRICE, 0F)
                val newProduct = Product(description = description, name = name, price = price)
                ViewModelProviders.of(this).get(ProductViewModel::class.java).insert(newProduct)
                Toast.makeText(this, R.string.product_saved_toast, Toast.LENGTH_SHORT).show()
            }

        } else if (requestCode == CustomData.EDIT_PRODUCT_REQ && resultCode == Activity.RESULT_OK) {

            val productID: Int = data!!.getIntExtra(CustomData.EXTRA_ID, -1)
            if (productID == -1) {
                Toast.makeText(this, "Product could not be updated", Toast.LENGTH_SHORT).show()
                return
            }
            val name = data.getStringExtra(CustomData.EXTRA_NAME)
            val description = data.getStringExtra(CustomData.EXTRA_DESCRIPTION)
            val price = data.getFloatExtra(CustomData.EXTRA_PRICE, -1F)
            val productAux = Product(name = name, description = description, price = price)
            productAux.productID = productID
            ViewModelProviders.of(this).get(ProductViewModel::class.java).update(productAux)
            Toast.makeText(this, "Customer updated", Toast.LENGTH_SHORT).show()

        } else if (requestCode == CustomData.EDIT_CUSTOMER_REQ && resultCode == CustomData.DEL_CUSTOMER_REQ) {

            Toast.makeText(this, R.string.customer_deleted, Toast.LENGTH_SHORT).show()

        } else if (requestCode == CustomData.EDIT_PRODUCT_REQ && resultCode == CustomData.DEL_PRODUCT_REQ) {

            Toast.makeText(this, R.string.product_deleted, Toast.LENGTH_SHORT).show()

        } else

            Toast.makeText(this, "fml", Toast.LENGTH_SHORT).show()
    }
}
