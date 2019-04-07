package com.carlosmartel.project3.selectCustomerActivity

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.entities.Customer
import kotlinx.android.synthetic.main.select_customer_item.view.*

class SelectCustomerListAdapter : RecyclerView.Adapter<SelectCustomerListAdapter.SelectCustomerViewHolder>() {

    private var customers: List<Customer> = ArrayList()
    private lateinit var listener: OnCustomerClickListener
    private var customerSelected: Customer? = null

    class SelectCustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(customer: Customer, listener: OnCustomerClickListener) {
            itemView.customer_item_name.text = customer.c_name
            itemView.customer_item_address.text = customer.address
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onCustomerClick(customer)
                }
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SelectCustomerViewHolder {
        val selectCustomerView = LayoutInflater.from(p0.context).inflate(R.layout.select_customer_item, p0, false)
        return SelectCustomerViewHolder(selectCustomerView)
    }

    override fun getItemCount(): Int {
        return customers.size
    }

    fun setCustomerSelected(customer: Customer) {
        customerSelected = customer
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(selectCustomerViewHolder: SelectCustomerViewHolder, position: Int) {
        if (customerSelected?.u_id == customers[position].u_id) selectCustomerViewHolder.itemView.setBackgroundColor(Color.LTGRAY)
        else selectCustomerViewHolder.itemView.setBackgroundColor(Color.WHITE)
        selectCustomerViewHolder.bind(customer = customers[position], listener = listener)
    }

    fun setCustomers(customers: List<Customer>) {
        this.customers = customers
    }

    fun setOnCustomerClickListener(listener: OnCustomerClickListener) {
        this.listener = listener
    }

    interface OnCustomerClickListener {
        fun onCustomerClick(customer: Customer)
    }
}
