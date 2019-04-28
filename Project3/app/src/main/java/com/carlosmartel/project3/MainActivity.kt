package com.carlosmartel.project3

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.carlosmartel.project3.activities.AddEditCustomerActivity
import com.carlosmartel.project3.activities.AddEditProductActivity
import com.carlosmartel.project3.activities.addEditOrderActivity.AddEditOrderActivity
import com.carlosmartel.project3.data.entities.Customer
import com.carlosmartel.project3.data.entities.Order
import com.carlosmartel.project3.data.entities.Product
import com.carlosmartel.project3.data.pojo.InflatedOrder
import com.carlosmartel.project3.fragments.MyFragmentPagerAdapter
import com.carlosmartel.project3.fragments.customer.CustomerFragment
import com.carlosmartel.project3.fragments.customer.CustomerViewModel
import com.carlosmartel.project3.fragments.order.OrderFragment
import com.carlosmartel.project3.fragments.order.OrderViewModel
import com.carlosmartel.project3.fragments.product.ProductFragment
import com.carlosmartel.project3.fragments.product.ProductViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*

class MainActivity :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    OrderFragment.OnFragmentInteractionListener,
    ProductFragment.OnFragmentInteractionListener,
    CustomerFragment.OnFragmentInteractionListener {

    private lateinit var viewPager: ViewPager
    private lateinit var mAdapter: MyFragmentPagerAdapter
    private lateinit var tabLayout: TabLayout


    /**
     * Opens an activity to update an existing order.
     *
     * @param inflatedOrder order to be updated.
     */
    override fun updateInflatedOrder(inflatedOrder: InflatedOrder) {
        val intent = Intent(this, AddEditOrderActivity::class.java)
        intent.putExtra(CustomData.EXTRA_PRODUCT, inflatedOrder.product!!)
        intent.putExtra(CustomData.EXTRA_CUSTOMER, inflatedOrder.customer!!)
        intent.putExtra(CustomData.EXTRA_ORDER, inflatedOrder.order!!)
        startActivityForResult(intent, CustomData.EDIT_ORDER_REQ)
    }

    /**
     * Not used
     */
    override fun updateOrder(order: Order) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Deletes an order from the db if the positive button of a dialog is pressed. An undo action is provided.
     *
     * @param order product to be deleted.
     */
    override fun deleteOrder(order: Order) {
        val undoOrder = Order(
            productID = order.productID,
            orderID = order.orderID,
            uid = order.uid,
            quantity = order.quantity,
            date = order.date,
            code = order.code
        )
        val dialog = AlertDialog.Builder(this@MainActivity)
        dialog.setTitle(R.string.dialog_order_title)
        dialog.setMessage(R.string.dialog_order_confirmation)
        dialog.setPositiveButton(R.string.dialog_delete) { _, _ ->
            ViewModelProviders.of(this).get(OrderViewModel::class.java).delete(order)
            Snackbar.make(viewPager, R.string.order_deleted, Snackbar.LENGTH_SHORT)
                .setAction(R.string.snack_undo) {
                    ViewModelProviders.of(this).get(OrderViewModel::class.java).insert(undoOrder) {}
                }
                .show()
        }
        dialog.setNegativeButton(R.string.dialog_cancel) { _, _ -> }
        dialog.show()
    }

    /**
     * Opens an activity to update an existing product.
     *
     * @param product order to be updated.
     */
    override fun updateProduct(product: Product) {
        val intent = Intent(this@MainActivity, AddEditProductActivity::class.java)
        intent.putExtra(CustomData.EXTRA_NAME, product.p_name)
        intent.putExtra(CustomData.EXTRA_DESCRIPTION, product.description)
        intent.putExtra(CustomData.EXTRA_PRICE, product.price)
        intent.putExtra(CustomData.EXTRA_ID, product.p_id)
        intent.putExtra(CustomData.EXTRA_PRODUCT, product)
        startActivityForResult(intent, CustomData.EDIT_PRODUCT_REQ)
    }

    /**
     * Deletes a product from the db if the positive button of a dialog is pressed. An undo action is provided.
     *
     * @param product product to be deleted.
     */
    override fun deleteProduct(product: Product) {
        val undoProduct = Product(
            p_id = product.p_id,
            p_name = product.p_name,
            description = product.description,
            price = product.price
        )
        val dialog = AlertDialog.Builder(this@MainActivity)
        dialog.setTitle(R.string.dialog_product_title)
        dialog.setMessage(R.string.dialog_product_confirmation)
        dialog.setPositiveButton(R.string.dialog_delete) { _, _ ->
            ViewModelProviders.of(this).get(ProductViewModel::class.java).delete(product)
            Snackbar.make(viewPager, R.string.product_deleted, Snackbar.LENGTH_SHORT)
                .setAction(R.string.snack_undo) {
                    ViewModelProviders.of(this).get(ProductViewModel::class.java).insert(undoProduct) {}
                }
                .show()
        }
        dialog.setNegativeButton(R.string.dialog_cancel) { _, _ -> }
        dialog.show()
    }

    /**
     * Deletes a customer from the db if the positive button of a dialog is pressed. An undo action is provided.
     *
     * @param customer customer to be deleted.
     */
    override fun deleteCustomer(customer: Customer) {
        val undoCustomer = Customer(customer.u_id, customer.address, customer.c_name)
        val dialog = AlertDialog.Builder(this@MainActivity)
        dialog.setTitle(R.string.dialog_customer_title)
        dialog.setMessage(R.string.dialog_customer_confirmation)
        dialog.setPositiveButton(R.string.dialog_delete) { _, _ ->
            ViewModelProviders.of(this).get(CustomerViewModel::class.java).delete(customer)
            Snackbar.make(viewPager, R.string.customer_deleted, Snackbar.LENGTH_SHORT)
                .setAction(R.string.snack_undo) {
                    ViewModelProviders.of(this).get(CustomerViewModel::class.java).insert(undoCustomer) {}
                }
                .show()
        }
        dialog.setNegativeButton(R.string.dialog_cancel) { _, _ -> }
        dialog.show()
    }

    /**
     * Opens an activity to update an existing customer.
     *
     * @param customer customer to be updated.
     */
    override fun updateCustomer(customer: Customer) {
        val intent = Intent(this@MainActivity, AddEditCustomerActivity::class.java)
        intent.putExtra(CustomData.EXTRA_NAME, customer.c_name)
        intent.putExtra(CustomData.EXTRA_ADDRESS, customer.address)
        intent.putExtra(CustomData.EXTRA_ID, customer.u_id)
        intent.putExtra(CustomData.EXTRA_CUSTOMER, customer)
        startActivityForResult(intent, CustomData.EDIT_CUSTOMER_REQ)
    }

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
                    startActivityForResult(intent, CustomData.ADD_ORDER_REQ)
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
                val newCustomer = Customer(address = address, c_name = name)
                ViewModelProviders.of(this).get(CustomerViewModel::class.java).insert(newCustomer){ insertedId: Int ->
                    newCustomer.u_id = insertedId
                    Snackbar.make(viewPager, R.string.customer_saved_snack, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.snack_undo) {
                            ViewModelProviders.of(this).get(CustomerViewModel::class.java).delete(newCustomer)
                        }
                        .show()
                }
            }

        } else if (requestCode == CustomData.EDIT_CUSTOMER_REQ && resultCode == Activity.RESULT_OK) {

            val uid: Int = data!!.getIntExtra(CustomData.EXTRA_ID, -1)
            if (uid == -1) {
                Toast.makeText(this, "Customer could not be updated", Toast.LENGTH_SHORT).show()
                return
            }
            val name = data.getStringExtra(CustomData.EXTRA_NAME)
            val address = data.getStringExtra(CustomData.EXTRA_ADDRESS)
            val undoCustomer = data.getParcelableExtra<Customer>(CustomData.EXTRA_CUSTOMER)
            val customerAux = Customer(address = address, c_name = name)
            customerAux.u_id = uid
            ViewModelProviders.of(this).get(CustomerViewModel::class.java).update(customerAux)
            Snackbar.make(viewPager, R.string.customer_updated, Snackbar.LENGTH_SHORT)
                .setAction(R.string.snack_undo) {
                    ViewModelProviders.of(this).get(CustomerViewModel::class.java).update(undoCustomer)
                }
                .show()

        } else if (requestCode == CustomData.ADD_PRODUCT_REQ && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                val name: String = data.getStringExtra(CustomData.EXTRA_NAME)
                val description: String = data.getStringExtra(CustomData.EXTRA_DESCRIPTION)
                val price: Float = data.getFloatExtra(CustomData.EXTRA_PRICE, 0F)
                val newProduct = Product(description = description, p_name = name, price = price)
                ViewModelProviders.of(this).get(ProductViewModel::class.java).insert(newProduct){ insertedId: Int ->
                    newProduct.p_id = insertedId
                    ViewModelProviders.of(this).get(ProductViewModel::class.java).update(newProduct)
                    Snackbar.make(viewPager, R.string.product_saved_toast, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.snack_undo) {
                            ViewModelProviders.of(this).get(ProductViewModel::class.java).delete(newProduct)
                        }
                        .show()
                }
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
            val undoProduct = data.getParcelableExtra<Product>(CustomData.EXTRA_PRODUCT)
            val productAux = Product(p_name = name, description = description, price = price)
            productAux.p_id = productID
            ViewModelProviders.of(this).get(ProductViewModel::class.java).update(productAux)
            Snackbar.make(viewPager, R.string.product_updated, Snackbar.LENGTH_SHORT)
                .setAction(R.string.snack_undo) {
                    ViewModelProviders.of(this).get(ProductViewModel::class.java).update(undoProduct)
                }
                .show()

        } else if (requestCode == CustomData.ADD_ORDER_REQ && resultCode == Activity.RESULT_OK){
            if(data != null){
                val uid: Int = data.getIntExtra(CustomData.EXTRA_ORDER_UID, -1)
                val pid: Int = data.getIntExtra(CustomData.EXTRA_ORDER_PID, -1)
                val code: String = data.getStringExtra(CustomData.EXTRA_ORDER_CODE)
                val date: Date = data.getSerializableExtra(CustomData.EXTRA_ORDER_DATE) as Date
                val quantity: Int = data.getIntExtra(CustomData.EXTRA_ORDER_QTY, 0)

                val newOrder = Order(code = code, uid = uid, productID = pid, date = date, quantity = quantity.toShort())

                ViewModelProviders.of(this).get(OrderViewModel::class.java).insert(newOrder){ insertedId: Int ->
                    newOrder.orderID = insertedId
                    Snackbar.make(viewPager, R.string.order_created, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.snack_undo) {
                            ViewModelProviders.of(this).get(OrderViewModel::class.java).delete(newOrder)
                        }
                        .show()
                }

            }
        } else if (requestCode == CustomData.EDIT_ORDER_REQ && resultCode == Activity.RESULT_OK){
            if(data != null){
                val uid: Int = data.getIntExtra(CustomData.EXTRA_ORDER_UID, -1)
                val pid: Int = data.getIntExtra(CustomData.EXTRA_ORDER_PID, -1)
                val oid: Int = data.getIntExtra(CustomData.EXTRA_ORDER_ID, -1)
                val code: String = data.getStringExtra(CustomData.EXTRA_ORDER_CODE)
                val date: Date = data.getSerializableExtra(CustomData.EXTRA_ORDER_DATE) as Date
                val qty: Int = data.getIntExtra(CustomData.EXTRA_ORDER_QTY, 0)

                val updateOrder = Order(orderID = oid, uid = uid, productID = pid, date = date, code = code, quantity = qty.toShort())

                val undoOrder: Order = data.getParcelableExtra(CustomData.EXTRA_ORDER)

                ViewModelProviders.of(this).get(OrderViewModel::class.java).update(updateOrder)
                Snackbar.make(viewPager, R.string.order_updated, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.snack_undo) {
                        ViewModelProviders.of(this).get(OrderViewModel::class.java).update(undoOrder)
                    }
                    .show()
            }
        } else if (requestCode == CustomData.EDIT_CUSTOMER_REQ && resultCode == CustomData.DEL_CUSTOMER_REQ) {

            Toast.makeText(this, R.string.customer_deleted, Toast.LENGTH_SHORT).show()

        } else if (requestCode == CustomData.EDIT_PRODUCT_REQ && resultCode == CustomData.DEL_PRODUCT_REQ) {

            Toast.makeText(this, R.string.product_deleted, Toast.LENGTH_SHORT).show()

        } else if(requestCode == CustomData.EDIT_ORDER_REQ && resultCode == CustomData.DEL_ORDER_REQ){

            Toast.makeText(this, R.string.order_deleted, Toast.LENGTH_SHORT).show()

        }
    }
}
