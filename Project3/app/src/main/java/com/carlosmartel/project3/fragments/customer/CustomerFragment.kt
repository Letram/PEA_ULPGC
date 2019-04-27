package com.carlosmartel.project3.fragments.customer

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
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.entities.Customer

class CustomerFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: CustomerListAdapter
    private lateinit var customersWithOrders: List<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_customer, container, false)

        recyclerView = v.findViewById(R.id.recycler_view)
        recyclerAdapter = CustomerListAdapter()
        customerViewModel = ViewModelProviders.of(this.activity!!)
            .get(CustomerViewModel(application = activity!!.application)::class.java)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }


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
/*
        //A SWIPE CANNOT BE UNDONE, THUS IS JUST DEACTIVATED

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
                if(customersWithOrders.contains(undoCustomer.u_id)){
                    openDialog()
                }else{
                    customerViewModel.delete(recyclerAdapter.getCustomerAt(viewHolder.adapterPosition))
                    Snackbar.make(view!!, R.string.customer_deleted, Snackbar.LENGTH_SHORT)
                        .setAction(R.string.snack_undo) {
                            customerViewModel.insert(undoCustomer)
                        }
                        .show()
                }
            }
        }).attachToRecyclerView(recyclerView)
*/
        recyclerAdapter.setOnItemClickListener(object : CustomerListAdapter.OnItemClickListener {
            override fun onItemClick(customer: Customer) {
                listener?.updateCustomer(customer)
            }

            override fun onItemLongClick(customer: Customer) {
                if(customersWithOrders.contains(customer.u_id)){
                    openDialog()
                }else
                    listener?.deleteCustomer(customer)
            }
        })
        return v
    }

    private fun openDialog(){
        val dialog = AlertDialog.Builder(activity!!)
        dialog.setTitle(R.string.dialog_customer_title)
        dialog.setMessage(R.string.dialog_cant_delete_customer)
        dialog.setPositiveButton(R.string.OK){ _, _ -> }
        dialog.setCancelable(false)
        dialog.show()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnCustomerFragmentInteractionListener")
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
