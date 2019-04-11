package com.carlosmartel.project4.fragments.customer

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
import com.carlosmartel.project4.data.json.backend.APIController
import com.carlosmartel.project4.data.json.backend.JsonService
import com.carlosmartel.project4.data.json.constants.JsonData
import org.json.JSONObject

class CustomerFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    //private lateinit var customerViewModel: CustomerViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: CustomerListAdapter
    private lateinit var customersWithOrders: List<Int>

    private lateinit var jsonAPI: APIController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(com.carlosmartel.project4.R.layout.fragment_customer, container, false)

        recyclerView = v.findViewById(R.id.recycler_view)
        recyclerAdapter = CustomerListAdapter()
        jsonAPI = APIController(JsonService())

        //customerViewModel = ViewModelProviders.of(this.activity!!).get(CustomerViewModel(application = activity!!.application)::class.java)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

/*
        customerViewModel.getAllCustomers().observe(this, Observer {
            if (it != null) {
                recyclerAdapter.setCustomers(customers = it)
                recyclerAdapter.notifyDataSetChanged()
            }
        })
        customerViewModel.getAllCustomersWithOrders().observe(this, Observer {
            if(it != null){
                customersWithOrders = it
            }
        })
*/
        recyclerAdapter.setOnItemClickListener(object : CustomerListAdapter.OnItemClickListener {
            override fun onItemClick(customer: Customer) {
                listener?.updateCustomer(customer)
            }

            override fun onItemLongClick(customer: Customer) {
                if (customersWithOrders.contains(customer.u_id)) {
                    openDialog()
                } else
                    listener?.deleteCustomer(customer)
            }
        })

        jsonAPI.getCustomers(JsonData.GET_CUSTOMERS, null) { response ->
            val customers: MutableList<Customer> = ArrayList()

            if (response != null) {
                val fault = response.getInt("fault")
                if (fault == 0) {
                    val array = response.getJSONArray("data")
                    for (i in 0..array.length()) {
                        val obj = array.getJSONObject(i)
                        val customerAux = Customer(
                            address = obj.getString(JsonData.CUSTOMER_ADDRESS),
                            c_name = obj.getString(JsonData.CUSTOMER_NAME)
                        )
                        customerAux.u_id = obj.getString(JsonData.CUSTOMER_ID).toInt()
                        customers.add(customerAux)
                    }
                    recyclerAdapter.setCustomers(customers)
                    recyclerAdapter.notifyDataSetChanged()
                }
            }
        }

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
