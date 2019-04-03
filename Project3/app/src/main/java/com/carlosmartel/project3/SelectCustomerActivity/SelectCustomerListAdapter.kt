package com.carlosmartel.project3.SelectCustomerActivity

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.entities.Customer
import kotlinx.android.synthetic.main.select_customer_item.view.*

class SelectCustomerListAdapter : RecyclerView.Adapter<SelectCustomerListAdapter.SelectCustomerViewHolder>() {

    private var customers: List<Customer> = ArrayList()

    class SelectCustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(customer: Customer) {
            itemView.customer_name.text = customer.c_name
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SelectCustomerViewHolder {
        val selectCustomerView = LayoutInflater.from(p0.context).inflate(R.layout.select_customer_item, p0, false)
        return SelectCustomerViewHolder(selectCustomerView)
    }

    override fun getItemCount(): Int {
        return customers.size
    }

    override fun onBindViewHolder(selectCustomerViewHolder: SelectCustomerViewHolder, position: Int) {
        selectCustomerViewHolder.bind(customer = customers[position])
    }

    fun setCustomers(customers: List<Customer>){
        this.customers = customers
        notifyDataSetChanged()
    }
}
