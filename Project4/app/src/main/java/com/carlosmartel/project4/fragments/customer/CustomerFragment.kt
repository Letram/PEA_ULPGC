package com.carlosmartel.project4.fragments.customer

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.project4.R
import com.carlosmartel.project4.data.entities.Customer
import com.carlosmartel.project4.data.webServices.json.customerJson.CustomerAPIController
import com.carlosmartel.project4.data.webServices.json.customerJson.JsonCustomerService
import com.carlosmartel.project4.data.pojo.InflatedOrderJson
import com.carlosmartel.project4.fragments.order.OrderViewModel

class CustomerFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var orderViewModel: OrderViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: CustomerListAdapter

    private lateinit var customersWithOrders: List<Int>
    private lateinit var orders: List<InflatedOrderJson>

    private lateinit var jsonCustomerAPI: CustomerAPIController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(com.carlosmartel.project4.R.layout.fragment_customer, container, false)

        recyclerView = v.findViewById(R.id.recycler_view)
        recyclerAdapter = CustomerListAdapter()
        jsonCustomerAPI =
            CustomerAPIController(JsonCustomerService())

        customerViewModel =
            ViewModelProviders.of(this.activity!!).get(CustomerViewModel(activity!!.application)::class.java)
        orderViewModel = ViewModelProviders.of(this.activity!!).get(OrderViewModel(activity!!.application)::class.java)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        customerViewModel.getAllCustomersJSON().observe(this, Observer {
            if (it != null) {
                recyclerAdapter.setCustomers(customers = it)
                recyclerAdapter.notifyDataSetChanged()
            }
        })

        orderViewModel.getAllInflatedOrdersJSON().observe(this, Observer {
            if (it != null) {
                orders = it
            }
        })

        recyclerAdapter.setOnItemClickListener(object : CustomerListAdapter.OnItemClickListener {
            override fun onItemClick(customer: Customer) {
                listener?.updateCustomer(customer)
            }

            //todo: check if the customer has orders related to him
            override fun onItemLongClick(customer: Customer) {
                for (infOrder in orders) {
                    if (infOrder.order.uid == customer.u_id) {
                        openDialog()
                        return
                    }
                }
                listener?.deleteCustomer(customer)
            }
        })
        return v
    }

    private fun openDialog() {
        val dialog = AlertDialog.Builder(activity!!)
        dialog.setTitle(R.string.dialog_customer_title)
        dialog.setMessage(R.string.dialog_cant_delete_customer)
        dialog.setPositiveButton(R.string.OK) { _, _ -> }
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnCustomerFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun updateCustomer(customer: Customer)
        fun deleteCustomer(customer: Customer)
    }

    companion object {
        @JvmStatic
        fun newInstance() = CustomerFragment()
    }
}