package com.carlosmartel.project3.fragments.customer

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        recyclerView = v.findViewById(R.id.recycler_view)
        recyclerAdapter = CustomerListAdapter()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }


        customerViewModel = ViewModelProviders.of(this.activity!!)
            .get(CustomerViewModel(application = activity!!.application)::class.java)
        customerViewModel.getAllCustomers().observe(this, Observer {
            if (it != null) {
                recyclerAdapter.setCustomers(customers = it)
                recyclerAdapter.notifyDataSetChanged()
            }
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
                val undoCustomer = recyclerAdapter.getCustomerAt(viewHolder.adapterPosition)
                customerViewModel.delete(recyclerAdapter.getCustomerAt(viewHolder.adapterPosition))
                Snackbar.make(view!!, R.string.customer_deleted, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.snack_undo) { _ ->
                        customerViewModel.insert(undoCustomer)
                    }
                    .show()
            }
        }).attachToRecyclerView(recyclerView)

        recyclerAdapter.setOnItemClickListener(object : CustomerListAdapter.OnItemClickListener {
            override fun onItemClick(customer: Customer) {
                listener?.updateCustomer(customer)
            }

            override fun onItemLongClick(customer: Customer) {
                listener?.deleteCustomer(customer)
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

    interface OnFragmentInteractionListener {
        fun updateCustomer(customer: Customer)
        fun deleteCustomer(customer: Customer)
    }

    companion object {
        @JvmStatic
        fun newInstance() = CustomerFragment()
    }
}
