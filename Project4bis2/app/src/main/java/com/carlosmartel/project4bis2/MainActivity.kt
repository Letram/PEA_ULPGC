package com.carlosmartel.project4bis2

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
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
import com.carlosmartel.project4bis2.activities.AddEditCustomerActivity
import com.carlosmartel.project4bis2.activities.AddEditProductActivity
import com.carlosmartel.project4bis2.activities.addEditOrderActivity.AddEditOrderActivity
import com.carlosmartel.project4bis2.data.entities.Customer
import com.carlosmartel.project4bis2.data.entities.Order
import com.carlosmartel.project4bis2.data.entities.Product
import com.carlosmartel.project4bis2.data.pojo.InflatedOrderJson
import com.carlosmartel.project4bis2.data.webServices.WebData
import com.carlosmartel.project4bis2.fragments.MyFragmentPagerAdapter
import com.carlosmartel.project4bis2.fragments.customer.CustomerFragment
import com.carlosmartel.project4bis2.fragments.customer.CustomerViewModel
import com.carlosmartel.project4bis2.fragments.order.OrderFragment
import com.carlosmartel.project4bis2.fragments.order.OrderViewModel
import com.carlosmartel.project4bis2.fragments.product.ProductFragment
import com.carlosmartel.project4bis2.fragments.product.ProductViewModel
import com.carlosmartel.project4bis2.receivers.MyNetworkReceiver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.text.SimpleDateFormat
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
    private var receiver: MyNetworkReceiver? = null

    override fun updateInflatedOrder(inflatedOrder: InflatedOrderJson) {
        val intent = Intent(this, AddEditOrderActivity::class.java)
        val customerAux = Customer(c_name = inflatedOrder.customerName, address = "")
        customerAux.u_id = inflatedOrder.order.uid
        val productAux =
            Product(p_name = inflatedOrder.productName, price = inflatedOrder.productPrice, description = "")
        productAux.p_id = inflatedOrder.order.productID
        intent.putExtra(CustomData.EXTRA_PRODUCT, productAux)
        intent.putExtra(CustomData.EXTRA_CUSTOMER, customerAux)
        intent.putExtra(CustomData.EXTRA_ORDER, inflatedOrder.order)
        startActivityForResult(intent, CustomData.EDIT_ORDER_REQ)
    }

    override fun updateOrder(order: Order) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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
            ViewModelProviders.of(this).get(OrderViewModel::class.java)
                .deleteJSON(order.orderID) { operationCompleted ->
                    if (!operationCompleted) Toast.makeText(
                        this,
                        R.string.operation_not_completed,
                        Toast.LENGTH_SHORT
                    ).show()
                    else {
                        Snackbar.make(viewPager, R.string.order_deleted, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.snack_undo) {
                                ViewModelProviders.of(this).get(OrderViewModel::class.java)
                                    .insertJSON(
                                        undoOrder.code,
                                        SimpleDateFormat("yyyy-MM-dd").format(undoOrder.date),
                                        undoOrder.uid,
                                        undoOrder.productID,
                                        undoOrder.quantity.toInt()
                                    ) { insertedID ->
                                        if (insertedID == -1)
                                            Toast.makeText(
                                                this,
                                                R.string.operation_not_completed,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                    }
                            }
                            .show()
                    }
                }
        }
        dialog.setNegativeButton(R.string.dialog_cancel) { _, _ -> }
        dialog.show()
    }

    override fun updateProduct(product: Product) {
        val intent = Intent(this@MainActivity, AddEditProductActivity::class.java)
        intent.putExtra(CustomData.EXTRA_NAME, product.p_name)
        intent.putExtra(CustomData.EXTRA_DESCRIPTION, product.description)
        intent.putExtra(CustomData.EXTRA_PRICE, product.price)
        intent.putExtra(CustomData.EXTRA_ID, product.p_id)
        intent.putExtra(CustomData.EXTRA_PRODUCT, product)
        startActivityForResult(intent, CustomData.EDIT_PRODUCT_REQ)
    }

    override fun deleteProduct(product: Product) {
        val dialog = AlertDialog.Builder(this@MainActivity)
        dialog.setTitle(R.string.dialog_product_title)
        dialog.setMessage(R.string.dialog_product_confirmation)
        dialog.setPositiveButton(R.string.dialog_delete) { _, _ ->
            ViewModelProviders.of(this).get(ProductViewModel::class.java)
                .deleteJSON(product.p_id) { operationCompleted ->
                    if (!operationCompleted) Toast.makeText(
                        this,
                        R.string.operation_not_completed,
                        Toast.LENGTH_SHORT
                    ).show()
                    else {
                        Snackbar.make(viewPager, R.string.product_deleted, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.snack_undo) {
                                ViewModelProviders.of(this).get(ProductViewModel::class.java)
                                    .insertJSON(product.p_name, product.description, product.price) { insertedID ->
                                        if (insertedID == -1)
                                            Toast.makeText(
                                                this,
                                                R.string.operation_not_completed,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                    }
                            }
                            .show()
                    }
                }
        }
        dialog.setNegativeButton(R.string.dialog_cancel) { _, _ -> }
        dialog.show()
    }

    override fun deleteCustomer(customer: Customer) {
        val dialog = AlertDialog.Builder(this@MainActivity)
        dialog.setTitle(R.string.dialog_customer_title)
        dialog.setMessage(R.string.dialog_customer_confirmation)
        dialog.setPositiveButton(R.string.dialog_delete) { _, _ ->
            ViewModelProviders.of(this).get(CustomerViewModel::class.java)
                .deleteSOAP(customer.u_id) { operationCompleted ->
                    if (!operationCompleted) {
                        Toast.makeText(this, R.string.operation_not_completed, Toast.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(viewPager, R.string.customer_deleted, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.snack_undo) {
                                ViewModelProviders.of(this).get(CustomerViewModel::class.java)
                                    .insertSOAP(customer.c_name, customer.address) { insertedID ->
                                        if (insertedID == -1)
                                            Toast.makeText(
                                                this,
                                                R.string.operation_not_completed,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                    }
                            }
                            .show()
                    }
                }
        }
        dialog.setNegativeButton(R.string.dialog_cancel) { _, _ -> }
        dialog.show()
    }

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

        registerConnectionReceiver()

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

    private fun registerConnectionReceiver() {
        receiver = MyNetworkReceiver()
        receiver!!.setNetworkCallbackListener(object : MyNetworkReceiver.NetworkCallback {
            override fun onConnectionGained() {
                Toast.makeText(this@MainActivity, R.string.connected, Toast.LENGTH_SHORT).show()
                reload()
            }

            override fun onConnectionLost() {
                Toast.makeText(this@MainActivity, R.string.not_connected, Toast.LENGTH_SHORT).show()
            }

        })
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (receiver != null)
            unregisterReceiver(receiver)
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.reload -> {
                reload()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun reload() {
        ViewModelProviders.of(this).get(CustomerViewModel::class.java).refreshAll() {}
        ViewModelProviders.of(this).get(ProductViewModel::class.java).refresh() {}
        ViewModelProviders.of(this).get(OrderViewModel::class.java).refresh() {}
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

                ViewModelProviders.of(this).get(CustomerViewModel::class.java).insertSOAP(name, address) { insertedID ->
                    if (insertedID == -1) Toast.makeText(
                        this,
                        R.string.operation_not_completed,
                        Toast.LENGTH_SHORT
                    ).show()
                    else {
                        Snackbar.make(viewPager, R.string.customer_saved_snack, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.snack_undo) {
                                ViewModelProviders.of(this).get(CustomerViewModel::class.java)
                                    .deleteSOAP(insertedID) { operationCompleted ->
                                        if (!operationCompleted)
                                            Toast.makeText(
                                                this@MainActivity,
                                                R.string.operation_not_completed,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                    }
                            }
                            .show()
                    }
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
            ViewModelProviders.of(this).get(CustomerViewModel::class.java)
                .updateSOAP(uid, name, address) { operationCompleted ->
                    if (!operationCompleted) {
                        Toast.makeText(this, R.string.operation_not_completed, Toast.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(viewPager, R.string.customer_saved_snack, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.snack_undo) {
                                ViewModelProviders.of(this).get(CustomerViewModel::class.java)
                                    .updateSOAP(
                                        undoCustomer.u_id,
                                        undoCustomer.c_name,
                                        undoCustomer.address
                                    ) { operationCompleted ->
                                        if (!operationCompleted)
                                            Toast.makeText(
                                                this@MainActivity,
                                                R.string.operation_not_completed,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                    }
                            }
                            .addCallback(object : Snackbar.Callback() {
                                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                    if (event == DISMISS_EVENT_TIMEOUT) {
                                        ViewModelProviders.of(this@MainActivity).get(OrderViewModel::class.java)
                                            .refresh() {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    R.string.operation_not_completed,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                    super.onDismissed(transientBottomBar, event)
                                }
                            })
                            .show()
                    }
                }
        } else if (requestCode == CustomData.ADD_PRODUCT_REQ && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                val name: String = data.getStringExtra(CustomData.EXTRA_NAME)
                val description: String = data.getStringExtra(CustomData.EXTRA_DESCRIPTION)
                val price: Float = data.getFloatExtra(CustomData.EXTRA_PRICE, 0F)
                val newProduct = Product(description = description, p_name = name, price = price)
                ViewModelProviders.of(this).get(ProductViewModel::class.java)
                    .insertJSON(newProduct.p_name, newProduct.description, newProduct.price) { insertedID ->
                        if (insertedID == -1) {
                            Toast.makeText(this, R.string.operation_not_completed, Toast.LENGTH_SHORT).show()
                        } else {
                            Snackbar.make(viewPager, R.string.product_saved_toast, Snackbar.LENGTH_SHORT)
                                .setAction(R.string.snack_undo) {
                                    ViewModelProviders.of(this).get(ProductViewModel::class.java)
                                        .deleteJSON(insertedID) { operationCompleted ->
                                            if (!operationCompleted)
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    R.string.operation_not_completed,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                        }
                                }
                                .show()
                        }
                    }
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
            val productAux = Product(p_name = name, description = description, price = price)
            productAux.p_id = productID
            val undoProduct = data.getParcelableExtra<Product>(CustomData.EXTRA_PRODUCT)
            ViewModelProviders.of(this).get(ProductViewModel::class.java)
                .updateJSON(
                    productAux.p_id,
                    productAux.p_name,
                    productAux.description,
                    productAux.price
                ) { operationCompleted ->
                    if (!operationCompleted) {
                        Toast.makeText(this, R.string.operation_not_completed, Toast.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(viewPager, R.string.product_updated, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.snack_undo) {
                                ViewModelProviders.of(this).get(ProductViewModel::class.java)
                                    .updateJSON(
                                        undoProduct.p_id,
                                        undoProduct.p_name,
                                        undoProduct.description,
                                        undoProduct.price
                                    ) { operationCompleted ->
                                        if (!operationCompleted)
                                            Toast.makeText(
                                                this@MainActivity,
                                                R.string.operation_not_completed,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                    }
                            }
                            .addCallback(object : Snackbar.Callback() {
                                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                    if (event == DISMISS_EVENT_TIMEOUT)
                                        ViewModelProviders.of(this@MainActivity).get(OrderViewModel::class.java).refresh() {
                                            Toast.makeText(
                                                this@MainActivity,
                                                R.string.operation_not_completed,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    super.onDismissed(transientBottomBar, event)
                                }
                            })
                            .show()
                    }
                }

        } else if (requestCode == CustomData.ADD_ORDER_REQ && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val uid: Int = data.getIntExtra(CustomData.EXTRA_ORDER_UID, -1)
                val pid: Int = data.getIntExtra(CustomData.EXTRA_ORDER_PID, -1)
                val code: String = data.getStringExtra(CustomData.EXTRA_ORDER_CODE)
                val date: Date = data.getSerializableExtra(CustomData.EXTRA_ORDER_DATE) as Date
                val quantity: Int = data.getIntExtra(CustomData.EXTRA_ORDER_QTY, 0)

                ViewModelProviders.of(this).get(OrderViewModel::class.java)
                    .insertJSON(code, SimpleDateFormat("yyyy-MM-dd").format(date), uid, pid, quantity) { insertedID ->
                        if (insertedID == -1) Toast.makeText(
                            this,
                            R.string.operation_not_completed,
                            Toast.LENGTH_SHORT
                        ).show()
                        else {
                            Snackbar.make(viewPager, R.string.order_created, Snackbar.LENGTH_SHORT)
                                .setAction(R.string.snack_undo) {
                                    ViewModelProviders.of(this).get(OrderViewModel::class.java)
                                        .deleteJSON(insertedID) { operationCompleted ->
                                            if (!operationCompleted)
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    R.string.operation_not_completed,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                        }
                                }
                                .show()
                        }
                    }

            }
        } else if (requestCode == CustomData.EDIT_ORDER_REQ && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val uid: Int = data.getIntExtra(CustomData.EXTRA_ORDER_UID, -1)
                val pid: Int = data.getIntExtra(CustomData.EXTRA_ORDER_PID, -1)
                val oid: Int = data.getIntExtra(CustomData.EXTRA_ORDER_ID, -1)
                val code: String = data.getStringExtra(CustomData.EXTRA_ORDER_CODE)
                val date: Date = data.getSerializableExtra(CustomData.EXTRA_ORDER_DATE) as Date
                val qty: Int = data.getIntExtra(CustomData.EXTRA_ORDER_QTY, 0)

                val undoOrder: Order = data.getParcelableExtra(CustomData.EXTRA_ORDER)

                ViewModelProviders.of(this).get(OrderViewModel::class.java).updateJSON(
                    orderID = oid,
                    idCustomer = uid,
                    idProduct = pid,
                    code = code,
                    date = SimpleDateFormat("yyyy-MM-dd").format(date),
                    quantity = qty
                ) { completedOperation ->
                    if (!completedOperation) Toast.makeText(
                        this,
                        R.string.operation_not_completed,
                        Toast.LENGTH_SHORT
                    ).show()
                    else {
                        Snackbar.make(viewPager, R.string.order_updated, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.snack_undo) {
                                ViewModelProviders.of(this).get(OrderViewModel::class.java)
                                    .updateJSON(
                                        orderID = undoOrder.orderID,
                                        idCustomer = undoOrder.uid,
                                        idProduct = undoOrder.productID,
                                        code = undoOrder.code,
                                        date = SimpleDateFormat("yyyy-MM-dd").format(undoOrder.date),
                                        quantity = undoOrder.quantity.toInt()
                                    ) { operationCompleted ->
                                        if (!operationCompleted)
                                            Toast.makeText(
                                                this@MainActivity,
                                                R.string.operation_not_completed,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                    }
                            }
                            .show()
                    }
                }

            }
        } else if (requestCode == CustomData.EDIT_CUSTOMER_REQ && resultCode == CustomData.DEL_CUSTOMER_REQ) {

            val uid = data!!.getIntExtra(WebData.CUSTOMER_ID, -1)
            if (uid != -1)
                ViewModelProviders.of(this).get(CustomerViewModel::class.java).deleteSOAP(uid) { operationCompleted ->
                    if (!operationCompleted) Toast.makeText(
                        this,
                        R.string.operation_not_completed,
                        Toast.LENGTH_SHORT
                    ).show()
                    else Toast.makeText(this, R.string.customer_deleted, Toast.LENGTH_SHORT).show()
                }

        } else if (requestCode == CustomData.EDIT_PRODUCT_REQ && resultCode == CustomData.DEL_PRODUCT_REQ) {

            val pid = data!!.getIntExtra(WebData.PRODUCT_ID, -1)
            if (pid != -1)
                ViewModelProviders.of(this).get(ProductViewModel::class.java).deleteJSON(pid) { operationCompleted ->
                    if (!operationCompleted) Toast.makeText(
                        this,
                        R.string.operation_not_completed,
                        Toast.LENGTH_SHORT
                    ).show()
                    else Toast.makeText(this, R.string.product_deleted, Toast.LENGTH_SHORT).show()
                }

        } else if (requestCode == CustomData.EDIT_ORDER_REQ && resultCode == CustomData.DEL_ORDER_REQ) {

            val oid = data!!.getIntExtra(WebData.ORDER_ID, -1)
            if (oid != -1)
                ViewModelProviders.of(this).get(OrderViewModel::class.java).deleteJSON(oid) {
                    Toast.makeText(this, R.string.order_deleted, Toast.LENGTH_SHORT).show()
                }

        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.run {
            outState.putInt(CustomData.CURRENT_TAB, tabLayout.selectedTabPosition)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            tabLayout.getTabAt(savedInstanceState.getInt(CustomData.CURRENT_TAB, 0))?.select()
        }
        super.onRestoreInstanceState(savedInstanceState)
    }
}
