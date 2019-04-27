package com.carlosmartel.project3.fragments.order

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.entities.Order
import com.carlosmartel.project3.data.pojo.InflatedOrder

class OrderFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: OrderListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order, container, false)

        recyclerView = view.findViewById(R.id.order_recycler_view)
        recyclerAdapter = OrderListAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
        orderViewModel = ViewModelProviders.of(this.activity!!).get(OrderViewModel(application = activity!!.application)::class.java)
        orderViewModel.getAllInflatedOrders().observe(this, Observer {
            if(it != null){
                recyclerAdapter.setInflatedOrders(it)
                recyclerAdapter.notifyDataSetChanged()
            }
        })

        orderViewModel.getAllOrders().observe(this, Observer {
            if (it != null){
                recyclerAdapter.setOrders(it)
                recyclerAdapter.notifyDataSetChanged()
            }
        })

        recyclerAdapter.setOnInflatedItemClickListener(object: OrderListAdapter.OnInflatedItemClickListener{
            override fun onItemClick(inflatedOrder: InflatedOrder) {
                listener?.updateInflatedOrder(inflatedOrder)
            }

            override fun onItemLongClick(inflatedOrder: InflatedOrder) {
                listener?.deleteOrder(inflatedOrder.order!!)
            }

        })
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnOrderFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun updateOrder(order: Order)
        fun deleteOrder(order: Order)
        fun updateInflatedOrder(inflatedOrder: InflatedOrder)
    }

    companion object {
        @JvmStatic
        fun newInstance() = OrderFragment()
    }
}
