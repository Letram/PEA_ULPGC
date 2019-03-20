package com.carlosmartel.project3.fragments.customer

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.carlosmartel.project3.R


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
        val v = inflater.inflate(R.layout.fragment_customer, container, false)
        // Inflate the layout for this fragment
        recyclerView = v.findViewById(R.id.recycler_view)
        recyclerAdapter = CustomerListAdapter()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }


        customerViewModel = ViewModelProviders.of(this.activity!!).get(CustomerViewModel(application = activity!!.application)::class.java)
        customerViewModel.getAllCustomers().observe(this, Observer {
            Toast.makeText(context, "onChanged", Toast.LENGTH_SHORT).show()
            if (it != null) {
                recyclerAdapter.setCustomers(customers = it)
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
