package com.carlosmartel.project3.fragments.customer

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.carlosmartel.project3.AddEditCustomerActivity
import com.carlosmartel.project3.CustomData
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.models.Customer


class CustomerFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: CustomerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(com.carlosmartel.project3.R.layout.fragment_customer, container, false)
        // Inflate the layout for this fragment
        recyclerView = v.findViewById(R.id.recycler_view)
        recyclerAdapter = CustomerListAdapter()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }


        customerViewModel = ViewModelProviders.of(this.activity!!).get(CustomerViewModel(application = activity!!.application)::class.java)
        customerViewModel.getAllCustomers().observe(this, Observer {
            if (it != null) {
                recyclerAdapter.setCustomers(customers = it)
            }
            println("Triggered")
        })

        //In order to delete a customer we just have to swipe left or right
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                customerViewModel.delete(recyclerAdapter.getCustomerAt(viewHolder.adapterPosition))
                Toast.makeText(context, R.string.customer_deleted, Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerView)

        recyclerAdapter.setOnItemClickListener(object : CustomerListAdapter.OnItemClickListener {
            override fun onItemClick(customer: Customer) {
                val intent = Intent(activity, AddEditCustomerActivity::class.java)
                intent.putExtra(CustomData.EXTRA_NAME, customer.name)
                intent.putExtra(CustomData.EXTRA_ADDRESS, customer.address)
                intent.putExtra(CustomData.EXTRA_ID, customer.uid)
                startActivityForResult(intent, CustomData.EDIT_CUSTOMER_REQ)
            }
        })
        return v
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

    interface OnFragmentInteractionListener {}

    companion object {
        @JvmStatic
        fun newInstance() = CustomerFragment()
    }
}
